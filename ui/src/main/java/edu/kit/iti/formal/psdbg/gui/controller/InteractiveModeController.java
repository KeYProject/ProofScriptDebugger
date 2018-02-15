package edu.kit.iti.formal.psdbg.gui.controller;

import com.google.common.eventbus.Subscribe;
import de.uka.ilkd.key.control.AbstractUserInterfaceControl;
import de.uka.ilkd.key.control.DefaultUserInterfaceControl;
import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.logic.Sequent;
import de.uka.ilkd.key.logic.SequentFormula;
import de.uka.ilkd.key.macros.scripts.EngineState;
import de.uka.ilkd.key.macros.scripts.RuleCommand;
import de.uka.ilkd.key.macros.scripts.ScriptException;
import de.uka.ilkd.key.pp.LogicPrinter;
import de.uka.ilkd.key.proof.Goal;
import de.uka.ilkd.key.proof.Node;
import de.uka.ilkd.key.proof.Proof;
import edu.kit.iti.formal.psdbg.LabelFactory;
import edu.kit.iti.formal.psdbg.RuleCommandHelper;
import edu.kit.iti.formal.psdbg.gui.controls.ScriptArea;
import edu.kit.iti.formal.psdbg.gui.controls.ScriptController;
import edu.kit.iti.formal.psdbg.gui.controls.Utils;
import edu.kit.iti.formal.psdbg.gui.model.InspectionModel;
import edu.kit.iti.formal.psdbg.interpreter.Evaluator;
import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.data.VariableAssignment;
import edu.kit.iti.formal.psdbg.interpreter.dbg.DebuggerFramework;
import edu.kit.iti.formal.psdbg.interpreter.dbg.PTreeNode;
import edu.kit.iti.formal.psdbg.interpreter.exceptions.ScriptCommandNotApplicableException;
import edu.kit.iti.formal.psdbg.parser.PrettyPrinter;
import edu.kit.iti.formal.psdbg.parser.ast.*;
import edu.kit.iti.formal.psdbg.parser.data.Value;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.key_project.util.collection.ImmutableList;
import recoder.util.Debug;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@RequiredArgsConstructor
public class InteractiveModeController {
    private static final Logger LOGGER = LogManager.getLogger(InteractiveModeController.class);

    private final Map<Node, Statements> cases = new HashMap<>();

    private final ScriptController scriptController;
    private BooleanProperty activated = new SimpleBooleanProperty();
    private ScriptArea scriptArea;

    private InspectionModel model;

    private DebuggerFramework<KeyData> debuggerFramework;
    private PTreeNode<KeyData> nodeAtInteractionStart;

    //needed for Undo-Operation
    private ArrayList<CallStatement> savepointsstatement;
    private ArrayList<Node> savepointslist;

    private Proof currentProof;
    private Services keYServices;

    private boolean moreThanOneMatch = false;

    private CasesStatement casesStatement;

    public void start(Proof currentProof, InspectionModel model) {
        Events.register(this);
        cases.clear();
        this.currentProof = currentProof;
        currentProof.getSubtreeGoals(currentProof.root()).forEach(goal -> {
            cases.put(goal.node(), new Statements());
        });
        this.scriptArea = scriptController.newScript();
        this.model = model;
        casesStatement = new CasesStatement();
        cases.forEach((k, v) -> {
            val gcs = new GuardedCaseStatement();
            val m = new MatchExpression();
            m.setPattern(new StringLiteral(
                    format(LabelFactory.getBranchingLabel(k))
            ));
            gcs.setGuard(m);
            gcs.setBody(v);
            casesStatement.getCases().add(gcs);
        });
        savepointslist = new ArrayList<>();
        savepointsstatement = new ArrayList<>();
        nodeAtInteractionStart = debuggerFramework.getStatePointer();

    }


    /**
     * Undo the application of the last rule
     */
    public void undo(javafx.event.ActionEvent actionEvent) {
        if (savepointslist.isEmpty()) {
            Debug.log("Kein vorheriger Zustand."); //TODO: events fire
            return;
        }

        val pruneNode = savepointslist.get(savepointslist.size() - 1);
        savepointslist.remove(pruneNode);
        ImmutableList<Goal> goalsbeforePrune = currentProof.getSubtreeGoals(pruneNode);

        currentProof.pruneProof(pruneNode);
        ImmutableList<Goal> goalsafterPrune = currentProof.getSubtreeGoals(pruneNode);

        ObservableList<GoalNode<KeyData>> goals = model.getGoals();
        List<GoalNode<KeyData>> prunedChildren = goals.stream()
                .filter(keyDataGoalNode -> goalsbeforePrune.contains(keyDataGoalNode.getData().getGoal()))
                .collect(Collectors.toList());

        KeyData kd = prunedChildren.get(0).getData();
        goals.removeAll(prunedChildren);
        GoalNode<KeyData> lastGoalNode = null;
        for (Goal newGoalNode : goalsafterPrune) {
            KeyData kdn = new KeyData(kd, newGoalNode.node());
            goals.add(
                    lastGoalNode = new GoalNode<>(prunedChildren.get(0).getParent().getParent(), kdn, kdn.getNode().isClosed()));
        }

        model.setSelectedGoalNodeToShow(lastGoalNode);

        val pruneStatement = savepointsstatement.get(savepointsstatement.size() - 1);
        cases.forEach((k, v) -> v.remove(pruneStatement));

        String c = getCasesAsString();
        scriptArea.setText("" +
                "//Preview \n" + c);
    }

    public void stop() {
        Events.unregister(this);
        String c = getCasesAsString();
        scriptController.getDockNode(scriptArea).undock();
        Events.fire(new Events.InsertAtTheEndOfMainScript(c));
    }

    @Subscribe
    public void handle(Events.TacletApplicationEvent tap) {

        LOGGER.debug("Handling {}", tap);
        moreThanOneMatch = false;
        String tapName = tap.getApp().taclet().name().toString();
        Goal g = tap.getCurrentGoal();

        SequentFormula seqForm = tap.getPio().sequentFormula();
        //transform term to parsable string representation
        Sequent seq = g.sequent();
        String sfTerm = Utils.printParsableTerm(seqForm.formula(), keYServices);
        String onTerm = Utils.printParsableTerm(tap.getPio().subTerm(), keYServices);


        RuleCommand.Parameters params = new RuleCommand.Parameters();
        params.formula = seqForm.formula();
        params.rulename = tap.getApp().taclet().name().toString();
        params.on = tap.getPio().subTerm();

        RuleCommandHelper rch = new RuleCommandHelper(g, params);
        int occ = rch.getOccurence(tap.getApp());

        Parameters callp = new Parameters();
        callp.put(new Variable("formula"), new TermLiteral(sfTerm));
        callp.put(new Variable("occ"), new IntegerLiteral(BigInteger.valueOf(occ)));
        callp.put(new Variable("on"), new TermLiteral(onTerm));

        VariableAssignment va = new VariableAssignment(null);
        CallStatement call = new CallStatement(tapName, callp);

        /*
        Iterator<ImmutableMapEntry<SchemaVariable, InstantiationEntry<?>>> iter = tap.getApp().instantiations().pairIterator();
        while (iter.hasNext()) {
            ImmutableMapEntry<SchemaVariable, InstantiationEntry<?>> entry = iter.next();
            String p = "inst_" + entry.key().proofToString();
            String v = edu.kit.iti.formal.psdbg.termmatcher.Utils.toPrettyTerm((Term) entry.value().getInstantiation());
            call.getParameters().put(new Variable(p), new TermLiteral(v));
        }*/

        try {

            applyRule(call, g);
            // Insert into the right cases
            // Node currentNode = g.node();
            // cases.get(findRoot(currentNode)).add(call);

            // How to Play this on the Proof?
            // How to Build a new StatePointer? Is it still possible?
            //  or only at #stop()?

            String c = getCasesAsString();
            scriptArea.setText("" +
                    "//Preview \n" + c);


        } catch (ScriptCommandNotApplicableException e) {
            StringBuilder sb = new StringBuilder("The script command ");
            sb.append(call.getCommand()).append(" was not applicable.");
            sb.append("\nSequent Formula: formula=").append(sfTerm);
            sb.append("\nOn Sub Term: on=").append(onTerm);

            Utils.showExceptionDialog("Proof Command was not applicable",
                    "Proof Command was not applicable.",
                    sb.toString(), e);
        }


    }


    private Node findRoot(Node cur) {
        while (cur != null) {
            if (cases.keySet().contains(cur))
                return cur;
            cur = cur.parent();
        }
        return null;
    }

    public String getCasesAsString() {
        PrettyPrinter pp = new PrettyPrinter();
        casesStatement.accept(pp);
        return pp.toString();
    }

    private void applyRule(CallStatement call, Goal g) throws ScriptCommandNotApplicableException {
        savepointslist.add(g.node());
        savepointsstatement.add(call);

        ObservableList<GoalNode<KeyData>> goals = model.getGoals();
        GoalNode<KeyData> expandedNode;
        List<GoalNode<KeyData>> collect = goals.stream().filter(keyDataGoalNode -> keyDataGoalNode.getData().getGoal().equals(g)).collect(Collectors.toList());

        if (collect.isEmpty() || collect.size() > 1) {
            throw new RuntimeException("Interactive Rule can not be applied, can not find goal in goal list");
        } else {
            expandedNode = collect.get(0);

        }
        RuleCommand c = new RuleCommand();
        // KeyData kd = g.getData();
        Evaluator eval = new Evaluator(expandedNode.getAssignments(), expandedNode);

        Map<String, String> map = new HashMap<>();
        call.getParameters().forEach((variable, expression) -> {

            Value exp = eval.eval(expression);
            map.put(variable.getIdentifier(), exp.getData().toString());
        });

//        call.getParameters().forEach((k, v) -> map.put(k.getIdentifier(),));
        LOGGER.info("Execute {} with {}", call, map);
        try {
            KeyData kd = expandedNode.getData();
            map.put("#2", call.getCommand());
            EngineState estate = new EngineState(g.proof());
            estate.setGoal(g);
            //System.out.println("on = " + map.get("on"));
            //System.out.println("formula = " + map.get("formula"));
            //System.out.println("occ = " + map.get("occ"));
            RuleCommand.Parameters cc = c.evaluateArguments(estate, map); //reflection exception

            AbstractUserInterfaceControl uiControl = new DefaultUserInterfaceControl();
            c.execute(uiControl, cc, estate);

            ImmutableList<Goal> ngoals = g.proof().getSubtreeGoals(expandedNode.getData().getNode());

            goals.remove(expandedNode);
            GoalNode<KeyData> last = null;


            if (ngoals.size() > 1) {

                cases.get(findRoot(ngoals.get(0).node())).add(call);
                CasesStatement inner = new CasesStatement();
                cases.get(findRoot(ngoals.get(0).node())).add(inner);

                for (Goal newGoalNode : ngoals) {
                    KeyData kdn = new KeyData(kd, newGoalNode.node());
                    goals.add(last = new GoalNode<>(expandedNode, kdn, kdn.getNode().isClosed()));
                    val caseForSubNode = new GuardedCaseStatement();
                    val m = new MatchExpression();
                    m.setPattern(new StringLiteral(
                            format(LabelFactory.getBranchingLabel(newGoalNode.node()))
                    ));
                    caseForSubNode.setGuard(m);
                    inner.getCases().add(caseForSubNode);
                    cases.put(last.getData().getNode(), caseForSubNode.getBody());
                }
            } else {
                if (ngoals.size() == 0) {
                    cases.get(findRoot(expandedNode.getData().getNode())).add(call);
                } else {
                    KeyData kdn = new KeyData(kd, ngoals.get(0).node());
                    goals.add(last = new GoalNode<>(expandedNode, kdn, kdn.getNode().isClosed()));
                    Node currentNode = last.getData().getNode();
                    cases.get(findRoot(currentNode)).add(call);
                }
            }
            if (last != null)
                model.setSelectedGoalNodeToShow(last);
        } catch (Exception e) {
            if (e.getClass().equals(ScriptException.class)) {
                System.out.println("e.getMessage() = " + e.getMessage());
                throw new ScriptCommandNotApplicableException(e, c, map);

            } else {
                throw new RuntimeException(e);
            }
        }

    }

    private String format(String branchingLabel) {
        // System.out.println("branchingLabel = " + branchingLabel);
        String newLabel = branchingLabel;
        if (branchingLabel.endsWith("$$")) {
            newLabel = branchingLabel.substring(0, branchingLabel.length() - 2);
            newLabel += ".*";
            //   System.out.println("newLabel = " + newLabel);
        }
        return newLabel;
    }

    public boolean isActivated() {
        return activated.get();
    }

    public void setActivated(boolean activated) {
        this.activated.set(activated);
    }

    public BooleanProperty activatedProperty() {
        return activated;
    }

    public void setKeYServices(Services keYServices) {
        this.keYServices = keYServices;
    }


}
