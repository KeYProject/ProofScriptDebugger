package edu.kit.formal.interpreter;

import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.Value;
import edu.kit.formal.interpreter.data.VariableAssignment;
import edu.kit.formal.interpreter.dbg.PseudoMatcher;
import edu.kit.formal.proofscriptparser.ast.Type;
import edu.kit.formal.proofscriptparser.ast.Variable;
import org.junit.Before;

/**
 * Created by sarah on 5/23/17.
 */

public class MatchEvaluatorTest {

    MatchEvaluator mEval;

    public MatchEvaluatorTest() {

    }

    @Before
    public void setup() {
        GoalNode<String> parent = new GoalNode<>(null, "pa", false);
        parent.declareVariable(new Variable("a"), Type.INT);
        parent.declareVariable(new Variable("b"), Type.INT);
        VariableAssignment va = parent.getAssignments();
        va.assign(new Variable("a"), Value.from(1));
        va.assign(new Variable("b"), Value.from(1));
        GoalNode selected = new GoalNode(parent, "selg", false);
        mEval = new MatchEvaluator(va, selected, new PseudoMatcher());
    }

}
