package edu.kit.formal.interpreter;

import java.util.List;

/**
 * Represents a script state
 */
public abstract class AbstractState {
    /**
     * Returns whether the state is an ErrorState
     *
     * @return
     */
    public abstract boolean isErrorState();

    /**
     * Returns all GoalNodes in a state
     *
     * @return
     */
    public abstract List<GoalNode> getGoals();

    /**
     * Returns the selected GoalNode to which the next statement should be applied
     * Can be null if no selector was applied or state is an ErrorState
     *
     * @return GoalNode or null
     */
    public abstract GoalNode getSelectedGoalNode();


    public State copy() {
        // return new State();
        return null;
    }
}
