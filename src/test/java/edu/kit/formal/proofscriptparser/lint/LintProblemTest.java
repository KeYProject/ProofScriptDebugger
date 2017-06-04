package edu.kit.formal.proofscriptparser.lint;

import org.antlr.v4.runtime.CommonToken;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Alexander Weigl
 * @version 1 (04.06.17)
 */
public class LintProblemTest {
    Issue issue;
    LintProblem lp;

    @Before
    public void setup() {
        issue = new Issue();
        issue.setId(2);
        issue.setRulename("test");
        issue.setValue(//"{{#toks}}{{text}}{{/toks}}" +
                "" +
                "{{toks.0.text}}" +
                "");

        lp = new LintProblem(issue);
        lp.tokens(
                new CommonToken(1, "test"),
                new CommonToken(1, "abc")
        );
    }

    @Test
    public void getMessage() throws Exception {
        Assert.assertEquals("test", lp.getMessage());
    }

}