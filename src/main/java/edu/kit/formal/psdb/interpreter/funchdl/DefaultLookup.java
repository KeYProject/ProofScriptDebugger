package edu.kit.formal.psdb.interpreter.funchdl;

import edu.kit.formal.psdb.interpreter.Interpreter;
import edu.kit.formal.psdb.interpreter.data.VariableAssignment;
import edu.kit.formal.psdb.interpreter.exceptions.NoCallHandlerException;
import edu.kit.formal.psdb.parser.ast.CallStatement;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (20.05.17)
 */
public class DefaultLookup implements CommandLookup {
    @Getter
    private final List<CommandHandler> builders = new ArrayList<>(1024);

    public DefaultLookup() {
    }

    public DefaultLookup(CommandHandler... cmdh) {
        builders.addAll(Arrays.asList(cmdh));
    }

    public void callCommand(Interpreter interpreter,
                            CallStatement call,
                            VariableAssignment params) {
        CommandHandler b = getBuilder(call);
        b.evaluate(interpreter, call, params);
    }


    @Override
    public boolean isAtomic(CallStatement call) {
        try {
            CommandHandler cmdh = getBuilder(call);
            if (cmdh != null)
                return cmdh.isAtomic();
            return true;
        } catch (NoCallHandlerException nche) {
            return false;
        }
    }

    public CommandHandler getBuilder(CallStatement callStatement) {
        CommandHandler found = null;
        for (CommandHandler b : builders) {
            if (b.handles(callStatement)) {
                if (found == null) {
                    found = b;
                } else {
                    throw new IllegalStateException("Call on line"
                            + callStatement.getStartPosition().getLineNumber()
                            + " is ambigue.");
                }
            }
        }

        if (found != null) return found;
        throw new NoCallHandlerException(callStatement);
    }

}
