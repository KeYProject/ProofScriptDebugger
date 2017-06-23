package edu.kit.formal.interpreter.funchdl;

import edu.kit.formal.interpreter.Interpreter;
import edu.kit.formal.interpreter.data.VariableAssignment;
import edu.kit.formal.proofscriptparser.ast.CallStatement;

/**
 * @author Alexander Weigl
 * @version 1 (20.05.17)
 */
public interface CommandLookup<T> {
    void callCommand(Interpreter<T> i, CallStatement c, VariableAssignment p);

    boolean isAtomic(CallStatement call);

    public CommandHandler getBuilder(CallStatement callStatement);
}
