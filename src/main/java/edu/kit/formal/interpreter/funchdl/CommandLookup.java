package edu.kit.formal.interpreter.funchdl;

import edu.kit.formal.interpreter.Interpreter;
import edu.kit.formal.interpreter.VariableAssignment;
import edu.kit.formal.proofscriptparser.ast.CallStatement;

/**
 * @author Alexander Weigl
 * @version 1 (20.05.17)
 */
public interface CommandLookup {
    void callCommand(Interpreter i, CallStatement c, VariableAssignment p);
}
