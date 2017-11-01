package edu.kit.iti.formal.psdbg.interpreter.data;

import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;
import edu.kit.iti.formal.psdbg.parser.ast.CaseStatement;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Data
public class InterpreterExtendedState<T> {

    /**
     * Null if root
     */
    private final InterpreterExtendedState<T> predecessor;

    /**
     * If we are in a case statement, this map defines which
     */
    private Map<CaseStatement, List<GoalNode<T>>> mappingOfCaseToStates = new HashMap<>();

    /**
     * State before the statement;
     */
    private State<T> stateBeforeStmt;

    /**
     * State after execution of statement
     */
    private State<T> stateAfterStmt;

    /**
     * Statement
     */
    private ASTNode stmt;

    public InterpreterExtendedState<T> copy() {
        InterpreterExtendedState<T> ext = new InterpreterExtendedState<>(
                this.predecessor != null
                        ? this.predecessor.copy()
                        : null);

        ext.setStmt(stmt);

        if (stateAfterStmt != null) {
            ext.setStateAfterStmt(this.stateAfterStmt.copy());
        }

        if (stateBeforeStmt != null) {
            ext.setStateBeforeStmt(this.stateBeforeStmt.copy());
        }
        return ext;
    }


    /*
    public List<GoalNode<T>> getClosedNodes() {
        return super.getGoals().stream().filter(nodes -> nodes.isClosed()).collect(Collectors.toList());
    }

    public List<GoalNode<T>> getOpenNodes() {
        return super.getGoals().stream().filter(nodes -> !nodes.isClosed()).collect(Collectors.toList());
    }*/

    public List<GoalNode<T>> getActiveGoalsForCase(CaseStatement caseStmt) {
        return mappingOfCaseToStates.getOrDefault(caseStmt, Collections.emptyList());

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        //sb.append("\n%%%%%%%%%%%%%%%%%%%%%\n");
        sb.append("Extended State ");
        if (getStmt() != null) {
            sb.append(getStmt().getNodeName() + ": \n");
        }
        sb.append("State before Statement: \n");
        if (stateBeforeStmt != null) {
            stateBeforeStmt.getGoals().stream().map(GoalNode::getData).forEach(sb::append);
        } else {
            sb.append("Before is Empty");
        }
        sb.append("\nAfter Statement:\n");
        if (stateAfterStmt != null) {
            stateAfterStmt.getGoals().stream().map(GoalNode::getData).forEach(sb::append);
        } else {
            sb.append("After is empty");
        }
        if (getMappingOfCaseToStates().containsKey(stmt)) {
            sb.append("Case Stmt with");
            getMappingOfCaseToStates().get(stmt).forEach(tGoalNode -> {
                sb.append(tGoalNode.getData());
            });
        }

        //sb.append("\n%%%%%%%%%%%%%%%%%%%%\n");
        return sb.toString();
//            this.stateBeforeStmt.toString()+"\n"+this.stateAfterStmt.toString();
    }
    //getuserSelected/
    //verfuegbar im case
    //map<case, listgoal>
    //regel anwendbar oder nicht
}