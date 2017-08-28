package edu.kit.iti.formal.psdbg.lint;

import edu.kit.iti.formal.psdbg.lint.LintProblem;
import edu.kit.iti.formal.psdbg.lint.LinterStrategy;
import edu.kit.iti.formal.psdbg.parser.Facade;
import edu.kit.iti.formal.psdbg.parser.ast.ProofScript;
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
        assertEquals(5, LinterStrategy.getDefaultLinter().getCheckers().size());
    }

    @Test
    public void testLint1() throws IOException {
        runLint("lint1.kps");
    }


    @Test
    public void testLint2() throws IOException {
        runLint("lint2.kps");
    }

    public void runLint(String filename) throws IOException {
        LinterStrategy ls = LinterStrategy.getDefaultLinter();
        List<ProofScript> nodes = Facade.getAST(CharStreams.fromStream(getClass().getResourceAsStream(filename)));
        List<LintProblem> problems = ls.check(nodes);

        for (LintProblem lp : problems) {
            System.out.printf("@%03d > (%s-%04d|%s) : %s%n",
                    lp.getLineNumber(),
                    lp.getIssue().getSeverity(),
                    lp.getIssue().getId(),
                    lp.getIssue().getRulename(),
                    lp.getMessage().trim());
        }
    }

}