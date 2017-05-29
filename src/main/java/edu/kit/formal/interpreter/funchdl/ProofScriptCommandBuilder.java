package edu.kit.formal.interpreter.funchdl;

import de.uka.ilkd.key.control.AbstractUserInterfaceControl;
import de.uka.ilkd.key.control.DefaultUserInterfaceControl;
import de.uka.ilkd.key.macros.scripts.EngineState;
import de.uka.ilkd.key.macros.scripts.ProofScriptCommand;
import de.uka.ilkd.key.proof.Goal;
import edu.kit.formal.interpreter.Interpreter;
import edu.kit.formal.interpreter.State;
import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.KeyData;
import edu.kit.formal.interpreter.data.VariableAssignment;
import edu.kit.formal.proofscriptparser.ast.CallStatement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.key_project.util.collection.ImmutableList;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexander Weigl
 * @version 1 (21.05.17)
 */
@RequiredArgsConstructor
public class ProofScriptCommandBuilder implements CommandHandler<KeyData> {
    @Getter
    private final Map<String, ProofScriptCommand> commands;

    public ProofScriptCommandBuilder() {
        this(new HashMap<>());
    }

    @Override
    public boolean handles(CallStatement call) {
        return commands.containsKey(call.getCommand());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void evaluate(Interpreter<KeyData> interpreter,
                         CallStatement call,
                         VariableAssignment params) {
        ProofScriptCommand c = commands.get(call.getCommand());
        State<KeyData> state = interpreter.getCurrentState();
        GoalNode<KeyData> expandedNode = state.getSelectedGoalNode();
        KeyData kd = expandedNode.getData();
        Map<String, String> map = new HashMap<>();
        params.asMap().forEach((k, v) -> map.put(k.getIdentifier(), v.toString()));
        System.out.println(map);
        try {
            EngineState estate = new EngineState(kd.getProof());
            estate.setGoal(kd.getNode());
            Object cc = c.evaluateArguments(estate, map);
            AbstractUserInterfaceControl uiControl = new DefaultUserInterfaceControl();
            c.execute(uiControl, cc, estate);

            ImmutableList<Goal> ngoals = kd.getProof().getSubtreeGoals(kd.getNode());
            state.getGoals().remove(expandedNode);
            for (Goal g : ngoals) {
                KeyData kdn = new KeyData(kd, g.node());
                state.getGoals().add(new GoalNode<>(expandedNode, kdn));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
