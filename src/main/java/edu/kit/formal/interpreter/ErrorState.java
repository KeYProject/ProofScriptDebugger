package edu.kit.formal.interpreter;

import java.util.List;

/**
 * State representing error states
 *
 * @author S.Grebing
 */
public class ErrorState extends AbstractState {
    @Override
    public boolean isErrorState() {
        return true;
    }

    @Override
    public List<GoalNode> getGoals() {
        return null;
    }

    @Override
    public GoalNode getSelectedGoalNode() {
        return null;
    }

    @Override
    public State copy() {
        return null;
    }
}
