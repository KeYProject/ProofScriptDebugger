package edu.kit.formal.proofscriptparser;

import edu.kit.formal.proofscriptparser.ast.Expression;
import edu.kit.formal.proofscriptparser.ast.Signature;
import edu.kit.formal.proofscriptparser.ast.Type;
import edu.kit.formal.proofscriptparser.ast.Variable;
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
        Signature s = createSignature();
        System.out.println(expr.getType(s));
    }

    public static Signature createSignature() {
        Signature s = new Signature();
        s.put(new Variable("a"), Type.BOOL);
        s.put(new Variable("b"), Type.BOOL);
        s.put(new Variable("c"), Type.BOOL);
        s.put(new Variable("i"), Type.INT);
        s.put(new Variable("j"), Type.INT);
        s.put(new Variable("k"), Type.INT);
        return s;
    }

}