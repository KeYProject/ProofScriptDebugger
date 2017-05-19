package edu.kit.formal.TestCommands;

import edu.kit.formal.interpreter.GoalNode;
import edu.kit.formal.interpreter.State;

/**
 * Created by sarah on 5/17/17.
 */
public class SplitCommand extends AbstractCommand {
    @Override
    public State execute(State s) {
        GoalNode g = s.getSelectedGoalNode();
        String seq1 = g.getSequent().concat("1");
        String seq2 = g.getSequent().concat("2");

        s.getGoals().remove(g);
        s.getGoals().add(new GoalNode(g, seq1));
        s.getGoals().add(new GoalNode(g, seq2));
        return s;
    }
}
