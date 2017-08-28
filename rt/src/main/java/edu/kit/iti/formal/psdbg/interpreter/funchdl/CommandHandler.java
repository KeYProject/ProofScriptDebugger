package edu.kit.iti.formal.psdbg.interpreter.funchdl;

import edu.kit.iti.formal.psdbg.interpreter.Interpreter;
import edu.kit.iti.formal.psdbg.interpreter.data.VariableAssignment;
import edu.kit.iti.formal.psdbg.parser.ast.CallStatement;

/**
 * @author Alexander Weigl
 * @version 1 (20.05.17)
 */
public interface CommandHandler<T> {
    /**
     * determines if this handler can handle the given command
     *
     * @param call
     * @return
     * @throws IllegalArgumentException
     */
    boolean handles(CallStatement call) throws IllegalArgumentException;

    /**
     * @param interpreter
     * @param call
     * @param params
     */
    void evaluate(Interpreter<T> interpreter,
                  CallStatement call,
                  VariableAssignment params);

    default boolean isAtomic() {
        return false;
    }

    /**
     * Return a html documentation message
     *
     * @param call
     * @return
     */
    default String getHelp(CallStatement call) {
        return "Help is not implemented for " + getClass().getCanonicalName();
    }

}
