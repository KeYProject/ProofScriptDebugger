package edu.kit.formal.TestCommands;

import edu.kit.formal.interpreter.State;

/**
 * TestCommand
 * Created by sarah on 5/17/17.
 */
public final class PrintCommand extends AbstractCommand {


    public State execute(State s) {

        System.out.println("Printing state " + s + " " + s.getSelectedGoalNode().toString());
        return s;
    }

}
