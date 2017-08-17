package edu.kit.formal.psdb.interpreter.funchdl;

import edu.kit.formal.psdb.interpreter.Interpreter;

/**
 * @author Alexander Weigl
 * @version 1 (20.05.17)
 */
public interface CommandCall {
    void evaluate(Interpreter state);
}
