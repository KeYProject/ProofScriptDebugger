package edu.kit.formal.commands;

import edu.kit.formal.interpreter.State;
import edu.kit.formal.proofscriptparser.ast.ProofScript;

import java.util.HashMap;

/**
 * Created by sarah on 5/17/17.
 */
public class LocalScriptCommandHandler extends CommandHandler {
    HashMap<String, ProofScript> localScripts;
    String requestedCommand;

    public LocalScriptCommandHandler() {
        localScripts = new HashMap<>();
    }

    @Override
    public void handle(String commandName) {
        if (localScripts.containsKey(commandName)) {
            requestedCommand = commandName;
            processCommand();
        } else {
            super.handle(commandName);
        }
    }

    @Override
    protected State processCommand() {
        ProofScript command = localScripts.get(requestedCommand);
        return null;
        //execute

    }
}
