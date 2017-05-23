package edu.kit.formal.proofscriptparser.lint;

import edu.kit.formal.proofscriptparser.Facade;
import edu.kit.formal.proofscriptparser.ast.ProofScript;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Alexander Weigl
 * @version 1 (23.05.17)
 */
public class LinterTest {
    @Test
    public void testRegisteredLinter() {
        assertEquals(4, LinterStrategy.getDefaultLinter().getCheckers().size());
    }

    @Test
    public void testLint1() throws IOException {
        LinterStrategy ls = LinterStrategy.getDefaultLinter();
        List<ProofScript> nodes = Facade.getAST(CharStreams.fromStream(getClass().getResourceAsStream("lint1.kps")));
        List<LintProblem> problems = ls.check(nodes);

        for (LintProblem lp : problems) {
            System.out.printf("@%s > (%s-%04d|%s) : %s%n",
                    lp.getLineNumber(),
                    lp.getIssue().getSeverity(),
                    lp.getIssue().getId(),
                    lp.getIssue().getRulename(),
                    lp.getMessage().trim());
        }
    }

}