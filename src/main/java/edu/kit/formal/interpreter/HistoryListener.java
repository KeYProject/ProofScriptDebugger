package edu.kit.formal.interpreter;

import edu.kit.formal.proofscriptparser.DefaultASTVisitor;
import edu.kit.formal.proofscriptparser.ast.ASTNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (21.05.17)
 */
@RequiredArgsConstructor
public class HistoryListener extends DefaultASTVisitor<Void> {
    @Getter
    private final List<ASTNode> queueNode = new LinkedList<>();
    @Getter
    private final List<AbstractState> queueState = new LinkedList<>();

    private final Interpreter interpreter;

    @Override
    public Void defaultVisit(ASTNode node) {
        queueState.add(interpreter.getCurrentState());
        queueNode.add(node);
        return null;
    }


}
