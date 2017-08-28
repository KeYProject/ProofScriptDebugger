package edu.kit.iti.formal.psdbg.interpreter;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import de.uka.ilkd.key.api.ScriptApi;
import de.uka.ilkd.key.api.VariableAssignments;
import de.uka.ilkd.key.proof.Goal;
import de.uka.ilkd.key.proof.Proof;
import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.data.State;
import edu.kit.iti.formal.psdbg.interpreter.data.VariableAssignment;
import edu.kit.iti.formal.psdbg.interpreter.funchdl.CommandLookup;
import edu.kit.iti.formal.psdbg.parser.ast.IsClosableCase;
import edu.kit.iti.formal.psdbg.parser.types.SimpleType;
import lombok.Getter;
import org.key_project.util.collection.ImmutableList;

import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (28.08.17)
 */
public class KeyInterpreter extends Interpreter<KeyData> {
    @Getter
    private static final BiMap<SimpleType, VariableAssignments.VarType> typeConversionBiMap =
            new ImmutableBiMap.Builder<SimpleType, VariableAssignments.VarType>()
                    .put(SimpleType.ANY, VariableAssignments.VarType.ANY)
                    .put(SimpleType.BOOL, VariableAssignments.VarType.BOOL)
                    //.put(SimpleType.TERM, VariableAssignments.VarType.FORMULA) //TODO: parametrisierte Terms
                    .put(SimpleType.INT, VariableAssignments.VarType.INT)
                    .put(SimpleType.STRING, VariableAssignments.VarType.OBJECT)
                    .put(SimpleType.INT_ARRAY, VariableAssignments.VarType.INT_ARRAY)
                    .put(SimpleType.SEQ, VariableAssignments.VarType.SEQ)
                    .build();
    @Getter
    private ScriptApi scriptApi;

    public KeyInterpreter(CommandLookup lookup) {
        super(lookup);
    }


    @Override
    public Object visit(IsClosableCase isClosableCase) {
        State<KeyData> currentStateToMatch = peekState();
        State<KeyData> currentStateToMatchCopy = peekState().copy(); //deepcopy
        GoalNode<KeyData> selectedGoalNode = currentStateToMatch.getSelectedGoalNode();
        GoalNode<KeyData> selectedGoalCopy = currentStateToMatch.getSelectedGoalNode().deepCopy(); //deepcopy

        enterScope(isClosableCase);
        executeBody(isClosableCase.getBody(), selectedGoalNode, new VariableAssignment(selectedGoalNode.getAssignments()));
        State<KeyData> stateafterIsClosable = peekState();
        List<GoalNode<KeyData>> goals = stateafterIsClosable.getGoals();
        boolean allClosed = true;
        for (GoalNode<KeyData> goal : goals) {
            KeyData data = (KeyData) goal.getData();
            if (!data.getNode().isClosed()) {
                allClosed = false;
                break;
            }
        }
        if (!allClosed) {
            System.out.println("IsClosable was not successful, rolling back proof");
            Proof currentKeYproof = selectedGoalNode.getData().getProof();
            ImmutableList<Goal> subtreeGoals = currentKeYproof.getSubtreeGoals(((KeyData) selectedGoalNode.getData()).getNode());
            currentKeYproof.pruneProof(selectedGoalCopy.getData().getNode());
            popState();
            pushState(currentStateToMatchCopy);
        }
        //check if state is closed
        exitScope(isClosableCase);
        return false;
    }
}
