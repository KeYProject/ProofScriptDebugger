package edu.kit.formal.interpreter;

import edu.kit.formal.proofscriptparser.TestHelper;
import edu.kit.formal.proofscriptparser.ast.Expression;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

    @Before public void setup() {
        GoalNode parent = new GoalNode(null, "pa");
        GoalNode selected = new GoalNode(parent,"sel");
        GoalNode g1 = new GoalNode(parent,"g1");
        GoalNode g2 = new GoalNode(parent,"g2");
        List<GoalNode> goals = Arrays.asList(g1,g2,selected);
        State state = new State(goals, selected);
        eval = new Evaluator(state);
    }

    @Test
    public void testEval() {
        Value result = eval.eval(expr);
        Value truthValue = eval.eval(truth);
        System.out.println(expr);
        Assert.assertEquals(truthValue, result);
    }
}