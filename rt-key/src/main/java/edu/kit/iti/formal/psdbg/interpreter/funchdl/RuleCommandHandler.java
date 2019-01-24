package edu.kit.iti.formal.psdbg.interpreter.funchdl;

import de.uka.ilkd.key.control.AbstractUserInterfaceControl;
import de.uka.ilkd.key.control.DefaultUserInterfaceControl;
import de.uka.ilkd.key.control.instantiation_model.TacletInstantiationModel;
import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.logic.PosInOccurrence;
import de.uka.ilkd.key.logic.PosInTerm;
import de.uka.ilkd.key.logic.SequentFormula;
import de.uka.ilkd.key.macros.scripts.EngineState;
import de.uka.ilkd.key.macros.scripts.RuleCommand;
import de.uka.ilkd.key.macros.scripts.ScriptException;
import de.uka.ilkd.key.proof.Goal;
import de.uka.ilkd.key.proof.Proof;
import de.uka.ilkd.key.proof.RuleAppIndex;
import de.uka.ilkd.key.proof.SVInstantiationException;
import de.uka.ilkd.key.proof.rulefilter.TacletFilter;
import de.uka.ilkd.key.rule.Rule;
import de.uka.ilkd.key.rule.Taclet;
import de.uka.ilkd.key.rule.TacletApp;
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
import edu.kit.iti.formal.psdbg.parser.ast.CallStatement;
import edu.kit.iti.formal.psdbg.parser.ast.Variable;
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

            if (e instanceof ScriptException.IndistinctFormula) {

                List<TacletApp> matchingapps = ((ScriptException.IndistinctFormula) e).getMatchingApps();
                ObservableList<String> obsMatchApps = FXCollections.observableArrayList();
                matchingapps.forEach(k -> obsMatchApps.add(k.toString()));
                //TODO: open window here
                IndistinctWindow indistinctWindow = new IndistinctWindow(obsMatchApps);
                tacletAppSelectionDialogService.setPane(indistinctWindow);
                tacletAppSelectionDialogService.showDialog();

                //IndistinctPrompt prompt = new IndistinctPrompt(matchingapps);
                //TacletApp chosen = prompt.getChosen();

                /*
                int inputindex = -1;

                while (true) {
                    System.out.println("Taclet " + matchingapps.get(0).taclet().displayName() + " is applicable on multiple formulas, " +
                            "please choose one of the following by entering a number from 0 to " + (matchingapps.size() - 1) + " :");
                    matchingapps.forEach(k -> System.out.println(k));
                    System.out.flush();
                    // User input
                    try {
                        inputindex = scanner.nextInt(); //System.in.read();

                    } catch (InputMismatchException ime) {
                        System.out.println("InputMismatchException thrown: edu/kit/iti/formal/psdbg/interpreter/funchdl/RuleCommandHandler.java:176");
                        continue;
                    }
                    if (0 <= inputindex && inputindex <= matchingapps.size() - 1) {
                        System.out.println("Acceptable inputindex = " + inputindex);
                        break;
                    } else {
                        System.out.println("No valid input");
                    }
                }
                */
                try {
                    tacletAppSelectionDialogService.latch.await();
                } catch (InterruptedException ie) {
                    System.out.println("latch await got interrupted");
                }
                TacletApp chosenApp = matchingapps.get(tacletAppSelectionDialogService.getUserIndexInput());

                TacletInstantiationModel tim = new TacletInstantiationModel(chosenApp, kd.getGoal().sequent(), kd.getGoal().getLocalNamespaces(), null, kd.getGoal());
                try {
                    TacletApp newTA = tim.createTacletApp();
                    newTA.execute(kd.getGoal(), kd.getProof().getServices());

                    int linenr = call.getStartPosition().getLineNumber();
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

    private class IndistinctPrompt {
        private final TacletApp chosen = null;

        IndistinctPrompt(List<TacletApp> matchingapps) {

            final Stage dialog = new Stage();
            dialog.setTitle("Select a Tacletapp");
            dialog.initStyle(StageStyle.UTILITY);
            dialog.initModality(Modality.WINDOW_MODAL);

            ListView listView = new ListView(FXCollections.observableArrayList(matchingapps));
            final TextField textField = new TextField();
            final Button submitButton = new Button("Submit");

            submitButton.setDefaultButton(true);
            submitButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    String userinput = textField.getText();

                    if (userinput.equals("")) {
                        System.out.println("Invalid input!");
                        return;
                    }

                    int index;
                    try {
                        index = Integer.parseInt(userinput);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input!");
                        return;
                    }

                    if (0 < index && index < matchingapps.size()) {
                        final TacletApp chosen = matchingapps.get(index);
                        dialog.close();
                    } else {
                        System.out.println("Invalid input!");
                        return;
                    }

                }
            });

            final VBox layout = new VBox(10);
            layout.setAlignment(Pos.CENTER_RIGHT);
            layout.setStyle("-fx-background-color: azure; -fx-padding: 10;");
            layout.getChildren().setAll(
                    listView,
                    textField,
                    submitButton
            );

            dialog.setScene(new Scene(layout));
            dialog.showAndWait();

        }

        private TacletApp getChosen() {
            return chosen;
        }
    }
}
