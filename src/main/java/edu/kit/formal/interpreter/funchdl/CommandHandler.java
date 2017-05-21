package edu.kit.formal.interpreter.funchdl;

import edu.kit.formal.interpreter.Interpreter;
import edu.kit.formal.interpreter.VariableAssignment;
import edu.kit.formal.proofscriptparser.ast.CallStatement;
import edu.kit.formal.proofscriptparser.ast.Parameters;

/**
 * @author Alexander Weigl
 * @version 1 (20.05.17)
 */
public interface CommandHandler {
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
    void evaluate(Interpreter interpreter,
                  CallStatement call,
                  VariableAssignment params);
}
