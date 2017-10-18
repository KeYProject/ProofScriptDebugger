package edu.kit.iti.formal.psdbg.interpreter.data;

import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;
import edu.kit.iti.formal.psdbg.parser.ast.CaseStatement;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Data
public class InterpreterExtendedState<T> {

    @Getter
    @Setter
    private Map<CaseStatement, List<GoalNode<T>>> mappingOfCaseToStates = new HashMap<>();

    /**
     * State before the statement;
     */
    @Getter
    @Setter
    private State<T> stateBeforeStmt;

    /**
     * State after execution of statement
     */
    @Getter
    @Setter
    private State<T> stateAfterStmt;

    /**
     * Statement
     */
    @Getter
    @Setter
    private ASTNode stmt;

    /**
     * Null if root
     */
    @Getter
    @Setter
    private InterpreterExtendedState<T> predecessor;


    public InterpreterExtendedState(InterpreterExtendedState<T> pred) {
        this.predecessor = pred;
    }

    public InterpreterExtendedState<T> copy() {
        InterpreterExtendedState<T> ext = new InterpreterExtendedState<>();
        if (this.predecessor != null) {
            ext.setPredecessor(this.predecessor.copy());
        } else {
            ext.predecessor = null;
        }
        ext.setStmt(stmt.copy());
        if (stateAfterStmt != null) {
            ext.setStateAfterStmt(this.stateAfterStmt.copy());
        } else {
            ext.setStateAfterStmt(null);
        }
        if (stateBeforeStmt != null) {
            ext.setStateBeforeStmt(this.stateBeforeStmt.copy());
        } else {
            ext.setStateBeforeStmt(null);
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
        StringBuilder sb = new StringBuilder("\n%%%%%%%%%%%%%%%%%%%%%\nExtended State ");
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
        sb.append("\n%%%%%%%%%%%%%%%%%%%%\n");
        return sb.toString();
//            this.stateBeforeStmt.toString()+"\n"+this.stateAfterStmt.toString();
    }
    //getuserSelected/
    //verfuegbar im case
    //map<case, listgoal>
    //regel anwendbar oder nicht
}
