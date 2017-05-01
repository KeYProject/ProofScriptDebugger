package edu.kit.formal.proofscriptparser;

import edu.kit.formatl.proofscriptparser.NotWelldefinedException;
import edu.kit.formatl.proofscriptparser.TransformAst;
import edu.kit.formatl.proofscriptparser.ast.Expression;
import edu.kit.formatl.proofscriptparser.ast.Signature;
import edu.kit.formatl.proofscriptparser.ast.Type;
import edu.kit.formatl.proofscriptparser.ast.Variable;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;

@RunWith(Parameterized.class)
public class GoodExpressionTest {
    @Parameterized.Parameters(name = "{0}")
    public static Iterable<Object[]> getGoodExpressions() throws IOException {
        return TestHelper.loadLines("goodexpr.txt");
    }

    @Parameterized.Parameter
    public String testExpression;


    @Test
    public void test() throws IOException, NotWelldefinedException {
        ScriptLanguageParser slp = TestHelper.getParser(testExpression);
        ScriptLanguageParser.ExpressionContext e = slp.expression();
        Assert.assertEquals(0, slp.getNumberOfSyntaxErrors());

       Expression expr = (Expression) e.accept(new TransformAst());
        Signature s = new Signature();
        s.put(new Variable("a"), Type.integer);
        System.out.println(expr.getType(s));
    }

}