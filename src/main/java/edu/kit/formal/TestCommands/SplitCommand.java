package edu.kit.formal.TestCommands;

import edu.kit.formal.interpreter.GoalNode;
import edu.kit.formal.interpreter.State;
import edu.kit.formal.interpreter.Value;
import edu.kit.formal.interpreter.VariableAssignment;
import edu.kit.formal.proofscriptparser.ast.Type;

import java.math.BigInteger;

/**
 * Created by sarah on 5/17/17.
 */
public class SplitCommand extends AbstractCommand {
    @Override
    public State execute(State s) {
        GoalNode g = s.getSelectedGoalNode();
        VariableAssignment assignments = g.getAssignments();
        String seq1 = g.getSequent().concat("1");
        String seq2 = g.getSequent().concat("2");

        s.getGoals().remove(g);
        s.getGoals().add(new GoalNode(g, seq1));

        GoalNode g2 = new GoalNode(g, seq2);
        g2.setVarValue("z", new Value(Type.INT, BigInteger.valueOf(10)));
        s.getGoals().add(g2);
        return s;
    }
}
