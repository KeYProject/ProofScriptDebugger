package edu.kit.formal.interpreter;

import edu.kit.formal.dbg.Debugger;
import edu.kit.formal.proofscriptparser.ast.Type;
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


        GoalNode parent = new GoalNode(null, "pa");
        parent.addVarDecl("a", Type.INT);
        parent.addVarDecl("b", Type.INT);
        VariableAssignment va = parent.getAssignments();
        va.setVarValue("a", Value.from(1));
        va.setVarValue("b", Value.from(1));
        GoalNode selected = new GoalNode(parent, "selg");
        mEval = new MatchEvaluator(va, selected, new Debugger.PseudoMatcher());

    }

}
