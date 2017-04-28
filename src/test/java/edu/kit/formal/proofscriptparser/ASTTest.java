package edu.kit.formal.proofscriptparser;

import edu.kit.formatl.proofscriptparser.Facade;
import edu.kit.formatl.proofscriptparser.ast.ProofScript;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
@RunWith(Parameterized.class) public class ASTTest {
    @Parameterized.Parameters(name = "{0}") public static Iterable<Object[]> getScripts() throws IOException {
        return ScriptTest.getScripts();
    }

    @Parameterized.Parameter public File f;

    @Test public void testBuildAst() throws IOException {
        List<ProofScript> list = Facade.getAST(f);
        list.forEach(s -> {
            System.out.println(s);
            System.out.println(Facade.prettyPrint(s));
        });
    }
}
