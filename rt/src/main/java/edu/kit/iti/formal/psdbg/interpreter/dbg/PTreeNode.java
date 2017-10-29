package edu.kit.iti.formal.psdbg.interpreter.dbg;

import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.State;
import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;
import edu.kit.iti.formal.psdbg.parser.ast.CaseStatement;
import edu.kit.iti.formal.psdbg.parser.ast.Parameters;
import lombok.*;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PTreeNode represents a node in the state graph.
 * <p>
 * This class is visualized by the debugger. Their interdepencies are given via the.
 * </p>
 * A node contains a reference to the ASTNode and a reference to the corresponding interpreter state
 * if this state is already interpreted, null otherwise.
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class PTreeNode<T> {
    private final ASTNode statement;

    private Map<CaseStatement, List<GoalNode<T>>> mappingOfCaseToStates = new HashMap<>();
    private State<T> stateBeforeStmt;
    private State<T> stateAfterStmt;
    private ASTNode[] context;

    private boolean atomic;

    @Nullable
    private PTreeNode<T> stepInto;
    @Nullable
    private PTreeNode<T> stepOver;
    @Nullable
    private PTreeNode<T> stepInvOver;
    @Nullable
    private PTreeNode<T> stepInvInto;
    @Nullable
    private PTreeNode<T> stepReturn;
    private Parameters goals;

    public void connectStepOver(PTreeNode<T> jumpOverTo) {
        setStepOver(jumpOverTo);
        jumpOverTo.setStepInvOver(this);
    }

    public void connectStepInto(PTreeNode<T> jumpIntoTo) {
        setStepInto(jumpIntoTo);
        jumpIntoTo.setStepInvInto(this);
    }

    public List<GoalNode<T>> getActiveGoalsForCase(CaseStatement caseStmt) {
        return mappingOfCaseToStates.getOrDefault(caseStmt, Collections.emptyList());
    }
}