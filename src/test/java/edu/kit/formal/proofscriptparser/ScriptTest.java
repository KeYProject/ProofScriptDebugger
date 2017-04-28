package edu.kit.formal.proofscriptparser;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Alexander Weigl
 * @version 1 (27.04.17)
 */
@RunWith(Parameterized.class)
public class ScriptTest {
    @Parameterized.Parameters(name = "{0}")
    public static Iterable<Object[]> getStructuredTextFiles() throws IOException {
        return TestHelper.getResourcesAsParameters("edu/kit/formal/proofscriptparser/scripts");
    }

    @Parameterized.Parameter
    public File f;

    @Test public void parse() throws IOException {
        ScriptLanguageParser slp = TestHelper.getParser(f);
        slp.start();
        Assert.assertEquals(0, slp.getNumberOfSyntaxErrors());
    }

}
