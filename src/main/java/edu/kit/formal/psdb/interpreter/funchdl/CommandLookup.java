package edu.kit.formal.psdb.interpreter.funchdl;

import edu.kit.formal.psdb.interpreter.Interpreter;
import edu.kit.formal.psdb.interpreter.data.VariableAssignment;
import edu.kit.formal.psdb.parser.ast.CallStatement;

/**
 * @author Alexander Weigl
 * @version 1 (20.05.17)
 */
public interface CommandLookup<T> {
    void callCommand(Interpreter<T> i, CallStatement c, VariableAssignment p);

    boolean isAtomic(CallStatement call);

    public CommandHandler getBuilder(CallStatement callStatement);

    String getHelp(CallStatement call);
}
