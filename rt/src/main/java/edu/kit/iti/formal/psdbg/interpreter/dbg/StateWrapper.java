package edu.kit.iti.formal.psdbg.interpreter.dbg;

import edu.kit.iti.formal.psdbg.interpreter.Interpreter;
import edu.kit.iti.formal.psdbg.interpreter.data.State;
import edu.kit.iti.formal.psdbg.parser.DefaultASTVisitor;
import edu.kit.iti.formal.psdbg.parser.Visitor;
import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;
import edu.kit.iti.formal.psdbg.parser.ast.ProofScript;
import edu.kit.iti.formal.psdbg.parser.ast.Statement;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Alexander Weigl
 * @version 1 (27.10.17)
 */
public class StateWrapper<T> implements InterpreterObserver<T> {
    private static final Logger LOGGER = LogManager.getLogger(StateWrapper.class);

    @Getter @Setter
    private Interpreter<T> interpreter;

    @Getter
    private Visitor<Void> entryListener = new EntryListener();

    @Getter
    private Visitor<Void> exitListener = new ExitListener();

    @Getter
    private List<ASTNode> contextStack = new ArrayList<>(100);

    @Setter @Getter
    private Consumer<PTreeNode<T>> emitNode = (n) -> {
    };

    @Nullable
    private PTreeNode<T> lastNode;


    public StateWrapper(Interpreter<T> interpreter) {
        install(interpreter);
    }

    public ASTNode[] getContextCopy() {
        return contextStack.toArray(new ASTNode[contextStack.size()]);
    }

    /**
     * @param node
     */
    public PTreeNode<T> createNode(ASTNode node) {
        LOGGER.info("Creating Root for State graph with statement {}@{}",
                node.getNodeName(), node.getStartPosition());
        lastNode = new PTreeNode<>(node);
        lastNode.setContext(getContextCopy());
        contextStack.add(node);
        State<T> currentInterpreterStateCopy = getInterpreterStateCopy();
        lastNode.setStateBeforeStmt(currentInterpreterStateCopy);

        //add node to state graph
        return lastNode;
    }

    public void createRoot(ProofScript node) {
        emitNode.accept(createNode(node));
    }

    public void createNormalNode(ASTNode node) {
        emitNode.accept(createNode(node));
    }


    public State<T> getInterpreterStateCopy() {
        return interpreter.getCurrentState().copy();
    }

    private void completeLastNode(@Nonnull ASTNode node) {
        assert lastNode != null;
        lastNode.setStateAfterStmt(getInterpreterStateCopy());
        if (node.equals(peekContext())) {
            popContext();
        }
    }

    private void popContext() {
        contextStack.remove(contextStack.size() - 1);
    }

    private ASTNode peekContext() {
        return contextStack.isEmpty()
                ? null
                : contextStack.get(contextStack.size() - 1);
    }

    private class EntryListener extends DefaultASTVisitor<Void> {
        boolean root = true;

        @Override
        public Void defaultVisit(ASTNode node) {
            if (node instanceof Statement)
                createNormalNode(node);
            return null;
        }

        @Override
        public Void visit(ProofScript proofScript) {
            if (root) {
                createRoot(proofScript);
                root = false;
            } else {
                defaultVisit(proofScript);
            }
            return null;
        }
    }

    private class ExitListener extends DefaultASTVisitor<Void> {
        @Override
        public Void defaultVisit(ASTNode node) {
            if (node instanceof Statement)
                completeLastNode(node);
            return null;
        }
    }
}
