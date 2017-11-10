package edu.kit.iti.formal.psdbg.interpreter.funchdl;

import de.uka.ilkd.key.control.AbstractUserInterfaceControl;
import de.uka.ilkd.key.control.DefaultUserInterfaceControl;
import de.uka.ilkd.key.macros.scripts.EngineState;
import de.uka.ilkd.key.macros.scripts.RuleCommand;
import de.uka.ilkd.key.macros.scripts.ScriptException;
import de.uka.ilkd.key.proof.Goal;
import de.uka.ilkd.key.rule.Rule;
import edu.kit.iti.formal.psdbg.interpreter.Interpreter;
import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.data.State;
import edu.kit.iti.formal.psdbg.interpreter.data.VariableAssignment;
import edu.kit.iti.formal.psdbg.interpreter.exceptions.ScriptCommandNotApplicableException;
import edu.kit.iti.formal.psdbg.parser.ast.CallStatement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.key_project.util.collection.ImmutableList;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexander Weigl
 * @version 1 (21.05.17)
 */
@RequiredArgsConstructor
public class RuleCommandHandler implements CommandHandler<KeyData> {
    private static final Logger LOGGER = LogManager.getLogger(RuleCommandHandler.class);

    @Getter
    private final Map<String, Rule> rules;

    public RuleCommandHandler() {
        this(new HashMap<>());
    }

    @Override
    public boolean handles(CallStatement call) throws IllegalArgumentException {
        return rules.containsKey(call.getCommand());
    }

    @Override
    public void evaluate(Interpreter<KeyData> interpreter,
                         CallStatement call,
                         VariableAssignment params) throws IllegalStateException, RuntimeException, ScriptCommandNotApplicableException {
        if (!rules.containsKey(call.getCommand())) {
            throw new IllegalStateException();
        }
        //FIXME duplicate of ProofScriptCommandBuilder
        RuleCommand c = new RuleCommand();
        State<KeyData> state = interpreter.getCurrentState();
        GoalNode<KeyData> expandedNode = state.getSelectedGoalNode();
        KeyData kd = expandedNode.getData();
        Map<String, String> map = new HashMap<>();
        params.asMap().forEach((k, v) -> map.put(k.getIdentifier(), v.getData().toString()));
        LOGGER.info("Execute {} with {}", call, map);
        try {
            map.put("#2", call.getCommand());
            EngineState estate = new EngineState(kd.getProof());
            estate.setGoal(kd.getNode());
            RuleCommand.Parameters cc = c.evaluateArguments(estate, map); //reflection exception
            AbstractUserInterfaceControl uiControl = new DefaultUserInterfaceControl();
            c.execute(uiControl, cc, estate);

            ImmutableList<Goal> ngoals = kd.getProof().getSubtreeGoals(kd.getNode());
            state.getGoals().remove(expandedNode);
            if (state.getSelectedGoalNode().equals(expandedNode)) {
                state.setSelectedGoalNode(null);
            }
            for (Goal g : ngoals) {
                KeyData kdn = new KeyData(kd, g.node());
                state.getGoals().add(new GoalNode<>(expandedNode, kdn, kdn.getNode().isClosed()));
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

}
