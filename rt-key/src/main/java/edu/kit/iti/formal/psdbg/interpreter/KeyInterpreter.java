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
import edu.kit.iti.formal.psdbg.parser.ast.ClosesCase;
import edu.kit.iti.formal.psdbg.parser.ast.TryCase;
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
    public Object visit(ClosesCase closesCase) {
        State<KeyData> currentStateToMatch = peekState();
        State<KeyData> currentStateToMatchCopy = currentStateToMatch.copy(); //deepcopy
        GoalNode<KeyData> selectedGoalNode = currentStateToMatch.getSelectedGoalNode();
        GoalNode<KeyData> selectedGoalCopy = currentStateToMatch.getSelectedGoalNode().deepCopy(); //deepcopy
        enterScope(closesCase);
        //execute closesscript
        executeBody(closesCase.getClosesScript(), selectedGoalNode, new VariableAssignment(selectedGoalNode.getAssignments()));
        //check whether script closed proof
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
        //prune proof
        System.out.println("The closes script " + (allClosed ? "closed the proof.\n" : "did not close the proof.\n") + "Rolling Back proof now.");
        Proof currentKeYproof = selectedGoalNode.getData().getProof();
        ImmutableList<Goal> subtreeGoals = currentKeYproof.getSubtreeGoals(((KeyData) selectedGoalNode.getData()).getNode());
        currentKeYproof.pruneProof(selectedGoalCopy.getData().getNode());
        popState();
        pushState(currentStateToMatchCopy);
        exitScope(closesCase);
        return allClosed;
    }

    @Override
    public Object visit(TryCase TryCase) {
        State<KeyData> currentStateToMatch = peekState();
        State<KeyData> currentStateToMatchCopy = currentStateToMatch.copy(); //deepcopy
        GoalNode<KeyData> selectedGoalNode = currentStateToMatch.getSelectedGoalNode();
        GoalNode<KeyData> selectedGoalCopy = currentStateToMatch.getSelectedGoalNode().deepCopy(); //deepcopy

        enterScope(TryCase);
        executeBody(TryCase.getBody(), selectedGoalNode, new VariableAssignment(selectedGoalNode.getAssignments()));
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
        exitScope(TryCase);
        return allClosed;

        /*        //executeBody and if goal is closed afterwards return true
        //else prune proof and return false

        State<T> stateBeforeTry = peekState().copy();
        State<T> tState = executeBody(tryCase.getBody(), peekState().getSelectedGoalNode(), peekState().getSelectedGoalNode().getAssignments());

        boolean isClosed = tState.getGoals().stream().allMatch(tGoalNode -> tGoalNode.isClosed());


        if(!isClosed){

            exitScope(tryCase);
            return false;
        }else{

            exitScope(tryCase);
            return true;
        }
*/
    }
}
