package edu.kit.formal.TestCommands;

import edu.kit.formal.interpreter.State;

/**
 * Created by sarah on 5/17/17.
 */
public abstract class AbstractCommand {

    public abstract State execute(State s);
}
