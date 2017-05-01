package edu.kit.formal.proofscriptparser;

import edu.kit.formal.proofscriptparser.ast.Expression;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;

@RunWith(Parameterized.class)
public class BadExpressionTest {
    @Parameterized.Parameters(name = "{0}")
    public static Iterable<Object[]> getBadExpressions() throws IOException {
        return TestHelper.loadLines("badexpr.txt");
    }

    @Parameterized.Parameter
    public String testExpression;


    @Test(expected = NotWelldefinedException.class)
    public void test() throws IOException, NotWelldefinedException {
        ScriptLanguageParser slp = TestHelper.getParser(testExpression);
        ScriptLanguageParser.ExpressionContext e = slp.expression();
        //Assert.assertNotEquals(0, slp.getNumberOfSyntaxErrors());
        Expression expr = (Expression) e.accept(new TransformAst());
        expr.getType(GoodExpressionTest.createSignature());
    }

}