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
                /*if (found == null) {
                    found = b;
                } else {
                    found = b; //CUTCommand
                    System.out.println(b.getClass());
                    System.out.println(found.getClass());
                    if (callStatement.getCommand().equals("cut")) {
                        System.out.println("Cut Case");
                    }
                    //throw new IllegalStateException("Call on line" + callStatement + " is ambigue.");
                }*/
            } else {
                if (mayBeEscapedMacro) {
                    String command = callStatement.getCommand();
                    callStatement.setCommand(command.replace("_", "-"));
                    if (b.handles(callStatement)) {
                        foundHandlers.add(b);
                        found = b;
                    }
                }
            }
        }

        if (foundHandlers.size() == 1) return foundHandlers.get(0);
        if (foundHandlers.size() > 1) {
            return foundHandlers.get(0);
        } else {
            throw new NoCallHandlerException(callStatement);
        }
    }

    @Override
    public String getHelp(CallStatement call) {
        return getBuilder(call).getHelp(call);
    }

}
