package edu.kit.formal.proofscriptparser;

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


    @Test
    public void test() throws IOException {
        ScriptLanguageParser slp = TestHelper.getParser(testExpression);
        slp.expression();
        Assert.assertNotEquals(0, slp.getNumberOfSyntaxErrors());
    }

}