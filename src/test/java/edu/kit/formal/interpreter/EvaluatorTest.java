package edu.kit.formal.interpreter;

import edu.kit.formal.dbg.Debugger;
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
        va.setVarValue("a", Value.from(1));
        va.setVarValue("b", Value.from(1));
        GoalNode selected = new GoalNode(parent, "selg");
        eval = new Evaluator(selected.getAssignments(), selected);
        eval.setMatcher(new Debugger.PseudoMatcher());
    }

    @Test
    public void testEval() {
        Value result = eval.eval(expr);
        Value truthValue = eval.eval(truth);
        System.out.println(expr);
        Assert.assertEquals(truthValue, result);
    }
}