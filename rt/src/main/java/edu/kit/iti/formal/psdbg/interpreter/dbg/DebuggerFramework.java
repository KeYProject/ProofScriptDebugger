package edu.kit.iti.formal.psdbg.interpreter.dbg;

import edu.kit.iti.formal.psdbg.interpreter.Interpreter;
import edu.kit.iti.formal.psdbg.parser.ast.ProofScript;
import javafx.event.EventHandler;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (27.10.17)
 */
public class DebuggerFramework<T> {
    private final Interpreter<T> interpreter;
    private Thread interpreterThread;
    private BlockListener<T> listener;
    private StateWrapper stateWrapper;

    private List<EventHandler<BeforeExecution>> beforeExecutionListener = new LinkedList<>();
    private List<EventHandler<AfterExecution>> beforeExecutionListener = new LinkedList<>();

    public DebuggerFramework(@Nonnull Interpreter<T> interpreter,
                             @Nonnull final ProofScript script) {
        this.interpreter = interpreter;
        listener = new BlockListener<>(interpreter);
        interpreterThread = new Thread(() -> interpreter.interpret(script));
    }

    public void start() {
        interpreterThread.start();
    }

    public void hardStop() {
        interpreter.getHardStop().set(true);
        //interpreterThread.stop();
    }

}
