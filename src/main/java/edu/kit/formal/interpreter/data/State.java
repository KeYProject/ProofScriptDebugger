package edu.kit.formal.interpreter.data;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Object representing a state
 *
 * @author S.Grebing
 */

public class State<T> {
    /**
     * All goalnodes in this state
     */
    @Getter
    private List<GoalNode<T>> goals;


    /**
     * Currently selected GoalNode
     */
    @Getter
    @Setter
    private GoalNode<T> selectedGoalNode;

    @Getter
    private boolean errorState;

    public State(Collection<GoalNode<T>> goals, GoalNode selected) {
        this.goals = new ArrayList<>(goals);
        this.selectedGoalNode = selected;
        assert selected == null || goals.contains(selected);
    }

    public State(List<GoalNode<T>> goals, int n) {
        this(goals, goals.get(n));
    }

    public State(GoalNode<T> goal) {
        assert goal != null;
        goals = new ArrayList<>();
        goals.add(goal);
        setSelectedGoalNode(goal);
    }


    public List<GoalNode<T>> getGoals() {
        return goals;
    }

    public GoalNode<T> getSelectedGoalNode() {
        if (selectedGoalNode == null) {
            throw new IllegalStateException("no selected node");
        } else {
            if (getGoals().size() == 1) {
                selectedGoalNode = getGoals().get(0);
            }
            return selectedGoalNode;
        }
    }

    public void setSelectedGoalNode(GoalNode<T> gn) {
        this.selectedGoalNode = gn;
    }

    /**
     * TODO correct this method, atm does nothing helpful!
     *
     * @return
     */
    public State<T> copy() {
        List<GoalNode<T>> copiedGoals = new ArrayList<>();
        GoalNode<T> refToSelGoal = selectedGoalNode;
        return new State<T>(copiedGoals, refToSelGoal);
    }

    public String toString() {
        if (selectedGoalNode == null) {
            return "No Goal selected";
        } else {
            return selectedGoalNode.toString();
        }

    }

}

