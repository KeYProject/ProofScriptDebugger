package edu.kit.iti.formal.psdbg.interpreter.dbg;

import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.State;
import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;
import edu.kit.iti.formal.psdbg.parser.ast.CaseStatement;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.*;

/**
 * PTreeNode represents a node in the state graph.
 * <p>
 * This class is visualized by the debugger. Their interdepencies are given via the {@link StateGraphWrapper}.
 * </p>
 * A node contains a reference to the ASTNode and a reference to the corresponding interpreter state
 * if this state is already interpreted, null otherwise.
 */
@Data
@ToString
@RequiredArgsConstructor
public class PTreeNode<T> {
    private final ASTNode statement;

    private Map<CaseStatement, List<GoalNode<T>>> mappingOfCaseToStates = new HashMap<>();
    private State<T> stateBeforeStmt;
    private State<T> stateAfterStmt;
    private ASTNode[] context;
    private boolean root;

    public List<GoalNode<T>> getActiveGoalsForCase(CaseStatement caseStmt) {
        return mappingOfCaseToStates.getOrDefault(caseStmt, Collections.emptyList());
    }
}
