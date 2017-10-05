package edu.kit.iti.formal.psdbg.interpreter.funchdl;

import edu.kit.iti.formal.psdbg.interpreter.Interpreter;
import edu.kit.iti.formal.psdbg.interpreter.data.VariableAssignment;
import edu.kit.iti.formal.psdbg.interpreter.exceptions.NoCallHandlerException;
import edu.kit.iti.formal.psdbg.parser.ast.CallStatement;
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
        boolean mayBeEscapedMacro = callStatement.getCommand().contains("_");
        List<CommandHandler> foundHandlers = new ArrayList<>();
        CommandHandler found = null;
        for (CommandHandler b : builders) {
            if (b.handles(callStatement)) {
                foundHandlers.add(b);
                found = b;
            }
        }

        if (found == null && mayBeEscapedMacro) {
            //if a proof macro contains a "-" character, the proof script language does not support this.
            // Therefore we have to check for both versions
            //  if (mayBeEscapedMacro) {

            String command = callStatement.getCommand();
            callStatement.setCommand(command.replace("_", "-"));

            for (CommandHandler b : builders) {
                if (b.handles(callStatement)) {
                    foundHandlers.add(b);
                    found = b;
                }
            }
        }
        if (foundHandlers.size() >= 1) return foundHandlers.get(0);
            throw new NoCallHandlerException(callStatement);

    }

    @Override
    public String getHelp(CallStatement call) {
        return getBuilder(call).getHelp(call);
    }

}
