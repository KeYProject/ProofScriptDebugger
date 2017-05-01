package edu.kit.formal.proofscriptparser;

import edu.kit.formal.proofscriptparser.ast.ProofScript;
import edu.kit.formal.proofscriptparser.ast.Variable;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (01.05.17)
 */
public class EqualsTest {
    @Test public void test() throws IOException {
        List<ProofScript> list = Facade
                .getAST(new File("src/test/resources/edu/kit/formal/proofscriptparser/scripts/justarithmetic.txt"));
        ProofScript a = list.get(0);
        ProofScript b = a.clone();

        Assert.assertEquals("Test equality for 'a'",
                a.getSignature().get(new Variable("a")), b.getSignature().get(new Variable("a")));

        Assert.assertEquals("signatures not equals",
                a.getSignature().entrySet(),
                b.getSignature().entrySet());
        Assert.assertEquals(a.getBody(), b.getBody());
        Assert.assertEquals(a, b);
    }
}
