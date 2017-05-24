package edu.kit.formal.commands;

import edu.kit.formal.interpreter.State;

import java.util.ArrayList;
import java.util.List;

/**
 * AbstractHandler for Chain of Respionsibility
 */
public abstract class CommandHandler {
    protected List<CommandHandler> nextList = new ArrayList<>();

    public void addNextCommandHandler(CommandHandler cmdHandler) {
        this.nextList.add(cmdHandler);
    }

    public void handle(String commandName) {
        for (int i = 0; i < nextList.size(); i++) {
            CommandHandler cmdHandler = nextList.get(i);
            cmdHandler.handle(commandName);
        }
    }

    protected abstract State processCommand();

}
