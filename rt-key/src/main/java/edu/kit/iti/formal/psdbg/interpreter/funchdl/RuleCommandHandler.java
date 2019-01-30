package edu.kit.iti.formal.psdbg.interpreter.funchdl;

import de.uka.ilkd.key.control.AbstractUserInterfaceControl;
import de.uka.ilkd.key.control.DefaultUserInterfaceControl;
import de.uka.ilkd.key.control.instantiation_model.TacletInstantiationModel;
import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.logic.*;
import de.uka.ilkd.key.macros.scripts.EngineState;
import de.uka.ilkd.key.macros.scripts.RuleCommand;
import de.uka.ilkd.key.macros.scripts.ScriptException;
import de.uka.ilkd.key.pp.LogicPrinter;
import de.uka.ilkd.key.pp.NotationInfo;
import de.uka.ilkd.key.pp.ProgramPrinter;
import de.uka.ilkd.key.proof.Goal;
import de.uka.ilkd.key.proof.Proof;
import de.uka.ilkd.key.proof.RuleAppIndex;
import de.uka.ilkd.key.proof.SVInstantiationException;
import de.uka.ilkd.key.proof.rulefilter.TacletFilter;
import de.uka.ilkd.key.rule.IfFormulaInstantiation;
import de.uka.ilkd.key.rule.Rule;
import de.uka.ilkd.key.rule.Taclet;
import de.uka.ilkd.key.rule.TacletApp;
import de.uka.ilkd.key.rule.inst.SVInstantiations;
import edu.kit.iti.formal.psdbg.RuleCommandHelper;
import edu.kit.iti.formal.psdbg.ValueInjector;
import edu.kit.iti.formal.psdbg.interpreter.IndistinctWindow;
import edu.kit.iti.formal.psdbg.interpreter.Interpreter;
import edu.kit.iti.formal.psdbg.interpreter.TacletAppSelectionDialogService;
import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.data.State;
import edu.kit.iti.formal.psdbg.interpreter.data.VariableAssignment;
import edu.kit.iti.formal.psdbg.interpreter.exceptions.ScriptCommandNotApplicableException;
import edu.kit.iti.formal.psdbg.interpreter.exceptions.VariableNotDeclaredException;
import edu.kit.iti.formal.psdbg.interpreter.exceptions.VariableNotDefinedException;
import edu.kit.iti.formal.psdbg.parser.Facade;
import edu.kit.iti.formal.psdbg.parser.ast.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.key_project.util.collection.ImmutableList;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

/**
 * @author Alexander Weigl
 * @version 1 (21.05.17)
 */
@RequiredArgsConstructor
public class RuleCommandHandler implements CommandHandler<KeyData> {
    public static final String[] MAGIC_PARAMETER_NAMES = new String[]{
            "on", "formula"
    };

    private static final Logger LOGGER = LogManager.getLogger(RuleCommandHandler.class);
    @Getter
    private final Map<String, Rule> rules;

    private Scanner scanner = new Scanner(System.in);

    @Getter
    @Setter
    private TacletAppSelectionDialogService tacletAppSelectionDialogService;

    public RuleCommandHandler() {
        this(new HashMap<>());
    }

    public static Set<String> findTaclets(Proof p, Goal g) {
        Services services = p.getServices();
        TacletFilter filter = new TacletFilter() {
            @Override
            protected boolean filter(Taclet taclet) {
                return true;
            }
        };
        RuleAppIndex index = g.ruleAppIndex();
        index.autoModeStopped();
        HashSet<String> set = new HashSet<>();
        for (SequentFormula sf : g.node().sequent().antecedent()) {
            ImmutableList<TacletApp> apps = index.getTacletAppAtAndBelow(filter,
                    new PosInOccurrence(sf, PosInTerm.getTopLevel(), true),
                    services);
            apps.forEach(t -> set.add(t.taclet().name().toString()));
        }

        try {
            for (SequentFormula sf : g.node().sequent().succedent()) {
                ImmutableList<TacletApp> apps = index.getTacletAppAtAndBelow(filter,
                        new PosInOccurrence(sf, PosInTerm.getTopLevel(), true),
                        services);
                apps.forEach(t -> set.add(t.taclet().name().toString()));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return set;
    }

    @Override
    public boolean handles(CallStatement call, KeyData data) throws IllegalArgumentException {
        if (rules.containsKey(call.getCommand())) return true;//static/rigid rules
        try {
            if (data != null) {
                Goal goal = data.getGoal();
                Set<String> rules = findTaclets(data.getProof(), goal);
                return rules.contains(call.getCommand());
            }
        } catch (NullPointerException npe) {
            System.out.println("npe = " + npe);
            return false;
        }
        return false;
    }

    @Override
    public void evaluate(Interpreter<KeyData> interpreter,
                         CallStatement call,
                         VariableAssignment params,
                         KeyData data) throws RuntimeException, ScriptCommandNotApplicableException {
        if (!rules.containsKey(call.getCommand())) {
            throw new IllegalStateException();
        }
        //FIXME duplicate of ProofScriptCommandBuilder
        RuleCommand c = new RuleCommand();
        State<KeyData> state = interpreter.getCurrentState();
        GoalNode<KeyData> expandedNode = state.getSelectedGoalNode();
        KeyData kd = expandedNode.getData();
        Map<String, Object> map = createParameters(expandedNode.getAssignments());

        params.asMap().forEach((k, v) -> map.put(k.getIdentifier(), v.getData()));
        LOGGER.info("Execute {} with {}", call, map);

        RuleCommand.Parameters cc;
        try {
            map.put("#2", call.getCommand());
            EngineState estate = new EngineState(kd.getProof());
            estate.setGoal(kd.getNode());
            cc = new RuleCommand.Parameters();
            ValueInjector valueInjector = ValueInjector.createDefault(kd.getNode());
            cc = valueInjector.inject(c, cc, map);
            AbstractUserInterfaceControl uiControl = new DefaultUserInterfaceControl();
            c.execute(uiControl, cc, estate);


        } catch (ScriptException e) {

            boolean exceptionsolved = false;

            //Exception thown when multiple TacletApp possible
            if (e instanceof ScriptException.IndistinctFormula) {

                List<TacletApp> matchingapps = ((ScriptException.IndistinctFormula) e).getMatchingApps();
                ObservableList<String> obsMatchApps = FXCollections.observableArrayList();
                int linenr = call.getStartPosition().getLineNumber();
                matchingapps.forEach(k -> obsMatchApps.add(tacletAppIntoString(k, kd.getGoal(), linenr)));
                //TODO: open window here
                IndistinctWindow indistinctWindow = new IndistinctWindow(obsMatchApps);
                tacletAppSelectionDialogService.setPane(indistinctWindow);
                tacletAppSelectionDialogService.showDialog();

                TacletApp chosenApp = matchingapps.get(tacletAppSelectionDialogService.getUserIndexInput());

                TacletInstantiationModel tim = new TacletInstantiationModel(chosenApp, kd.getGoal().sequent(), kd.getGoal().getLocalNamespaces(), null, kd.getGoal());
                try {
                    TacletApp newTA = tim.createTacletApp();
                    newTA.execute(kd.getGoal(), kd.getProof().getServices());

                    exceptionsolved = true;
                } catch (SVInstantiationException svie) {

                }
            }


            if (!exceptionsolved) {
                if (interpreter.isStrictMode()) {
                    throw new ScriptCommandNotApplicableException(e, c, map);
                } else {
                    //Utils necessary oder schmeißen
                    LOGGER.error("Command " + call.getCommand() + " is not applicable in line " + call.getRuleContext().getStart().getLine());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //refreshes Debuggermainmodel
        ImmutableList<Goal> ngoals = kd.getProof().getSubtreeGoals(kd.getNode());
        state.getGoals().remove(expandedNode);
        if (state.getSelectedGoalNode().equals(expandedNode)) {
            state.setSelectedGoalNode(null);
        }
        for (Goal g : ngoals) {
            KeyData kdn = new KeyData(kd, g.node());
            state.getGoals().add(new GoalNode<>(expandedNode, kdn, kdn.getNode().isClosed()));
        }
    }

    private Map<String, Object> createParameters(VariableAssignment assignments) {
        Map<String, Object> params = new HashMap<>();
        for (String s : MAGIC_PARAMETER_NAMES) {
            try {
                params.put(s, assignments.getValue(new Variable(Variable.MAGIC_PREFIX + s)));
            } catch (VariableNotDefinedException | VariableNotDeclaredException e) {

            }
        }
        return params;
    }

    private String tacletAppIntoString(TacletApp tacletApp, Goal g, int linenumber) {
        String tacletAppString = "";

        String tapName = tacletApp.taclet().name().toString();
        PosInOccurrence pio = tacletApp.posInOccurrence();
        SequentFormula seqForm = pio.sequentFormula();

        Services keYServices = g.proof().getServices();

        Sequent seq = g.sequent();
        String sfTerm = printParsableTerm(seqForm.formula(), keYServices);
        String onTerm = printParsableTerm(pio.subTerm(), keYServices);

        RuleCommand.Parameters params = new RuleCommand.Parameters();
        params.formula = seqForm.formula();
        params.rulename = tacletApp.taclet().name().toString();
        params.on = pio.subTerm();

        RuleCommandHelper rch = new RuleCommandHelper(g, params);
        int occ = rch.getOccurence(tacletApp);


        tacletAppString = String.format("%s formula=' %s ' on=' %s ' occ=%d;",
                tapName,
                sfTerm.trim(),
                onTerm.trim(),
                occ);

        // add missing instantiations
        if (!tacletApp.ifInstsComplete()) {
            SVInstantiations sv = tacletApp.instantiations();
            //TODO:
        }

        tacletAppString += String.format("(linenumber = %d)", linenumber);

        return tacletAppString;
    }

    public static String printParsableTerm(Term term, Services services) {

        NotationInfo ni = new NotationInfo();
        LogicPrinter p = new LogicPrinter(new ProgramPrinter(), ni, services);
        ni.refresh(services, false, false);
        String termString = "";
        try {
            p.printTerm(term);
        } catch (IOException ioe) {
            // t.toString();
        }
        termString = p.toString();
        return termString;
    }
}
