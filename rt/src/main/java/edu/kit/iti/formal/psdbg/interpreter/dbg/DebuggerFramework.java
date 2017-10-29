package edu.kit.iti.formal.psdbg.interpreter.dbg;

import com.google.common.graph.MutableValueGraph;
import edu.kit.iti.formal.psdbg.interpreter.Interpreter;
import edu.kit.iti.formal.psdbg.interpreter.graphs.ControlFlowNode;
import edu.kit.iti.formal.psdbg.interpreter.graphs.ControlFlowTypes;
import edu.kit.iti.formal.psdbg.parser.ast.ProofScript;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * List of Pipeline:
 * <p>
 * <code>Interpreter -> StateWrapper -> ProofTreeManager</code>
 * <p>
 * Order of the Listeners:
 * <ol>
 * <li></li>
 * <li>{@link StateWrapper}: signals the PTreeNodes</li>
 * <li></li>
 * </ol>
 *
 * @author Alexander Weigl
 * @version 1 (27.10.17)
 */
public class DebuggerFramework<T> {
    private final Interpreter<T> interpreter;

    @Getter
    private final List<Consumer<PTreeNode<T>>> beforeExecutionListener = new LinkedList<>();

    @Getter
    private final List<Consumer<PTreeNode<T>>> afterExecutionListener = new LinkedList<>();

    @Getter
    private final List<Consumer<PTreeNode<T>>> currentStatePointerListener = new LinkedList<>();

    private final ProofTreeManager<T> ptreeManager;
    private final Thread interpreterThread;
    private final BlockListener<T> blocker;
    private final StateWrapper<T> stateWrapper;


    public DebuggerFramework(@Nonnull Interpreter<T> interpreter,
                             @Nonnull ProofScript main,
                             MutableValueGraph<ControlFlowNode, ControlFlowTypes> cfg) {
        this.interpreter = interpreter;
        blocker = new BlockListener<>(interpreter);
        stateWrapper = new StateWrapper<>(interpreter);
        ptreeManager = new ProofTreeManager<>(cfg);
        stateWrapper.setEmitNode(ptreeManager::receiveNode);
        interpreterThread = new Thread(() -> interpreter.interpret(main));
    }

    public List<Consumer<PTreeNode<T>>> getStatePointerListener() {
        return ptreeManager.getStatePointerListener();
    }

    @Nullable
    public PTreeNode<T> getStatePointer() {
        return ptreeManager.getStatePointer();
    }

    void setStatePointer(PTreeNode<T> statePointer) {
        ptreeManager.setStatePointer(statePointer);
    }

    public void start() {
        interpreterThread.start();
    }

    /**
     * stops the interpreter in the background on the next {@link edu.kit.iti.formal.psdbg.parser.ast.ASTNode}.
     * Does not stop the KeY framework in the background.
     */
    public void stop() {
        interpreter.getHardStop().set(true);
    }

    /**
     * Hard thread stop.
     */
    public void hardStop() {
        interpreterThread.stop();
    }

    public void execute(DebuggerCommand<T> cmd) throws DebuggerException {
        cmd.execute(this);
    }

    /* public boolean addBreakpoint(int line) {
         return blocker.getBreakpoints().add(line);
     }

     public boolean removeBreakpint(int line) {
         return blocker.getBreakpoints().remove(line);
     }
 */
    public void disableBlocker() {
        this.blocker.deinstall();
    }

    public PTreeNode<T> getCurrentStatePointer() {
        return ptreeManager.getStatePointer();
    }

    public void releaseUntil(Blocker.BlockPredicate predicate) {
        blocker.getPredicates().add(predicate);
        blocker.getMarkForDisable(predicate);
        blocker.unlock();
    }

    public Set<PTreeNode<T>> getStates() {
        return ptreeManager.getNodes();
    }
}
