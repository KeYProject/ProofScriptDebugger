package edu.kit.formal.interpreter;

import java.util.List;

/**
 * Object representing a state
 *
 * @author S.Grebing
 */
public class State extends AbstractState {


    /**
     * All goalnodes in this state
     */
    private List<GoalNode> goals;


    /**
     * Currently selected GoalNode
     */
    private GoalNode selectedGoalNode;

    public State(List<GoalNode> goals, GoalNode selected) {
        this.goals = goals;
        this.selectedGoalNode = selected;


    }

    @Override
    public boolean isErrorState() {
        return false;
    }

    @Override
    public List<GoalNode> getGoals() {
        return goals;
    }

    @Override
    public GoalNode getSelectedGoalNode() {
        if (selectedGoalNode == null) {
            return null;
        } else {
            return selectedGoalNode;
        }
    }

    public void setSelectedGoalNode(GoalNode selectedGoalNode) {
        this.selectedGoalNode = selectedGoalNode;
    }

}

