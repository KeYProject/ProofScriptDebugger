package edu.kit.formal.proofscriptparser;

import edu.kit.formal.proofscriptparser.ast.Expression;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;

/**
 * @author Alexander Weigl
 * @version 1 (01.05.17)
 */
@RunWith(Parameterized.class)
public class BadlyTypedExpression {
    @Parameterized.Parameters(name = "{0}")
    public static Iterable<Object[]> getBadExpressions() throws IOException {
        return TestHelper.loadLines("badlytypedexpr.txt");
    }

    @Parameterized.Parameter
    public String testExpression;

    @Test(expected = NotWelldefinedException.class)
    public void test() throws NotWelldefinedException {
        ScriptLanguageParser slp = TestHelper.getParser(testExpression);
        ScriptLanguageParser.ExpressionContext e = slp.expression();
        Assert.assertEquals(0, slp.getNumberOfSyntaxErrors());
        Expression expr = (Expression) e.accept(new TransformAst());
        expr.getType(GoodExpressionTest.createSignature());
    }
}
