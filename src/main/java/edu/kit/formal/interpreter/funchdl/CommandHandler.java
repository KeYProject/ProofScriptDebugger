package edu.kit.formal.interpreter.funchdl;

import edu.kit.formal.interpreter.Interpreter;
import edu.kit.formal.interpreter.data.VariableAssignment;
import edu.kit.formal.proofscriptparser.ast.CallStatement;

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
}
