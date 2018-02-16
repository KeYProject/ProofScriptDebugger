package edu.kit.iti.formal.psdbg.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Alexander Weigl
 * @version 1 (16.02.18)
 */
public class TransformAstTest {
    @Test
    public void testSplitIdentifier() {
        Assert.assertEquals(
                Facade.parseExpression("- a"),
                Facade.parseExpression("-a")
        );
        Assert.assertEquals(
                Facade.parseExpression("- a"),
                TransformAst.splitIdentifier("-a")
        );

        Assert.assertEquals(
                Facade.parseExpression("- a - b"),
                TransformAst.splitIdentifier("-a-b")
        );
    }

}
