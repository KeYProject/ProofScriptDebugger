package edu.kit.formal.interpreter;

import edu.kit.formal.proofscriptparser.TestHelper;
import edu.kit.formal.proofscriptparser.ast.Expression;
import edu.kit.formal.proofscriptparser.ast.Type;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Alexander Weigl
 * @version 1 (16.05.17)
 */
@RunWith(Parameterized.class)
public class EvaluatorTest {
    private final Expression expr, truth;
    private Evaluator eval;

    public EvaluatorTest(String e, String r) {
        expr = TestHelper.toExpr(e);
        truth = TestHelper.toExpr(r);
    }

    @Parameterized.Parameters(name = "{0} => {1}")
    public static Iterable<Object[]> expr() throws IOException {
        return TestHelper.loadLines("eval.txt", 2);
    }

    @Before
    public void setup() {
        GoalNode parent = new GoalNode(null, "pa");
        parent.addVarDecl("a", Type.INT);
        parent.addVarDecl("b", Type.INT);
        VariableAssignment va = parent.getAssignments();
        va.setVar("a", Value.from(1));
        va.setVar("b", Value.from(1));
        GoalNode selected = new GoalNode(parent, "selg");
        eval = new Evaluator(selected);
        eval.setMatcher(new PseudoMatcher());
    }

    @Test
    public void testEval() {
        Value result = eval.eval(expr);
        Value truthValue = eval.eval(truth);
        System.out.println(expr);
        Assert.assertEquals(truthValue, result);
    }

    class PseudoMatcher implements MatcherApi {
        @Override
        public List<VariableAssignment> matchLabel(GoalNode currentState, String label) {
            Pattern p = Pattern.compile(label,Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(currentState.getSequent());
            return m.matches()
                    ? Collections.singletonList(new VariableAssignment())
                    : Collections.emptyList();
        }

        @Override
        public List<VariableAssignment> matchSeq(GoalNode currentState, String data) {
            return Collections.emptyList();
        }
    }
}