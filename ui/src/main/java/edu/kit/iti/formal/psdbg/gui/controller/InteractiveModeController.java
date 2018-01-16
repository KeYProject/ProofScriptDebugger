package edu.kit.iti.formal.psdbg.gui.controller;

import com.google.common.eventbus.Subscribe;
import de.uka.ilkd.key.control.AbstractUserInterfaceControl;
import de.uka.ilkd.key.control.DefaultUserInterfaceControl;
import de.uka.ilkd.key.logic.SequentFormula;
import de.uka.ilkd.key.macros.scripts.EngineState;
import de.uka.ilkd.key.macros.scripts.RuleCommand;
import de.uka.ilkd.key.macros.scripts.ScriptException;
import de.uka.ilkd.key.proof.Goal;
import de.uka.ilkd.key.proof.Node;
import de.uka.ilkd.key.proof.Proof;
import edu.kit.iti.formal.psdbg.LabelFactory;
import edu.kit.iti.formal.psdbg.gui.controls.ScriptArea;
import edu.kit.iti.formal.psdbg.gui.controls.ScriptController;
import edu.kit.iti.formal.psdbg.gui.model.InspectionModel;
import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.data.VariableAssignment;
import edu.kit.iti.formal.psdbg.interpreter.exceptions.ScriptCommandNotApplicableException;
import edu.kit.iti.formal.psdbg.parser.PrettyPrinter;
import edu.kit.iti.formal.psdbg.parser.ast.*;
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

    private BooleanProperty activated = new SimpleBooleanProperty();

    private final ScriptController scriptController;

    private ScriptArea scriptArea;

    private InspectionModel model;


    public void start(Proof currentProof, InspectionModel model) {
        Events.register(this);
        cases.clear();
        currentProof.getSubtreeGoals(currentProof.root()).forEach(goal -> {
            cases.put(goal.node(), new Statements());
        });
        this.scriptArea = scriptController.newScript();
        this.model = model;


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
        String tapName = tap.getApp().taclet().displayName();
        Goal g = tap.getCurrentGoal();

        SequentFormula seqForm = tap.getPio().sequentFormula();
        //transform term to parsable string representation
        String term = edu.kit.iti.formal.psdbg.termmatcher.Utils.toPrettyTerm(seqForm.formula());
        Parameters params = new Parameters();
        params.put(new Variable("formula"), new TermLiteral(term));
        VariableAssignment va = new VariableAssignment(null);
        CallStatement call = new CallStatement(tapName, params);


        // Insert into the right cases
        Node currentNode = g.node();
        cases.get(findRoot(currentNode)).add(call);

        // How to Play this on the Proof?
        // How to Build a new StatePointer? Is it still possible?
        //  or only at #stop()?

        String c = getCasesAsString();
        scriptArea.setText("" +
                "//Preview \n" + c);

        applyRule(call, g);
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
        CasesStatement c = new CasesStatement();
        cases.forEach((k, v) -> {
            val gcs = new GuardedCaseStatement();
            val m = new MatchExpression();
            m.setPattern(new StringLiteral(
                    LabelFactory.getBranchingLabel(k)
            ));
            gcs.setGuard(m);
            gcs.setBody(v);
            c.getCases().add(gcs);
        });

        PrettyPrinter pp = new PrettyPrinter();
        c.accept(pp);
        return pp.toString();
    }

    private void applyRule(CallStatement call, Goal g) {
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
        Map<String, String> map = new HashMap<>();
        // call.getParameters().forEach((k, v) -> map.put(k.getIdentifier(), v.getData().toString()));
        LOGGER.info("Execute {} with {}", call, map);
        try {
            KeyData kd = expandedNode.getData();
            map.put("#2", call.getCommand());
            EngineState estate = new EngineState(g.proof());
            estate.setGoal(g);
            RuleCommand.Parameters cc = c.evaluateArguments(estate, map); //reflection exception
            AbstractUserInterfaceControl uiControl = new DefaultUserInterfaceControl();
            c.execute(uiControl, cc, estate);

            ImmutableList<Goal> ngoals = g.proof().getSubtreeGoals(g.node());

            goals.remove(expandedNode);
            for (Goal newGoalNode : ngoals) {
                KeyData kdn = new KeyData(kd, newGoalNode.node());
                goals.add(new GoalNode<>(expandedNode, kdn, kdn.getNode().isClosed()));
            }
        } catch (Exception e) {
            if (e.getClass().equals(ScriptException.class)) {
                System.out.println("e.getMessage() = " + e.getMessage());
                throw new ScriptCommandNotApplicableException(e, c, map);

            } else {
                throw new RuntimeException(e);
            }
        }

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
}
