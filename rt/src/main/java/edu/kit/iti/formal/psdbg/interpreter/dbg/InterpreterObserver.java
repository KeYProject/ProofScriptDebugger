package edu.kit.iti.formal.psdbg.interpreter.dbg;

import edu.kit.iti.formal.psdbg.interpreter.Interpreter;
import edu.kit.iti.formal.psdbg.parser.Visitor;

/**
 * @author Alexander Weigl
 * @version 1 (27.10.17)
 */
public interface InterpreterObserver<T> {
    default void install(Interpreter<T> interpreter) {
        if (getInterpreter() != null) deinstall(interpreter);
        interpreter.getEntryListeners().add(0, getEntryListener());
        interpreter.getExitListeners().add(0, getExitListener());
        setInterpreter(interpreter);
    }

    Interpreter<T> getInterpreter();

    void setInterpreter(Interpreter<T> interpreter);

    default void deinstall(Interpreter<T> interpreter) {
        if (interpreter != null) {
            interpreter.getEntryListeners().remove(getEntryListener());
            interpreter.getExitListeners().remove(getExitListener());
        }
    }

    Visitor getExitListener();

    Visitor getEntryListener();
}
