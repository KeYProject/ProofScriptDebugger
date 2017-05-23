package edu.kit.formal.proofscriptparser.lint;

import edu.kit.formal.proofscriptparser.Facade;
import edu.kit.formal.proofscriptparser.ast.ProofScript;
import org.antlr.v4.runtime.CharStreams;

import java.io.*;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (23.05.17)
 */
public class Linter {
    public static void main(String[] args) throws IOException {
        Reader is = openInput(args);
        List<ProofScript> scripts = Facade.getAST(CharStreams.fromReader(is));
        LinterStrategy ls = LinterStrategy.getDefaultLinter();
        List<LintProblem> problems = ls.check(scripts);
        for (LintProblem lp : problems) {
            System.out.printf("%s-%04d: %s", lp.getLevel(), lp.getNumber(), lp.getMessage());
        }
    }

    private static Reader openInput(String[] args) throws FileNotFoundException {
        if (args.length == 0) {
            System.out.println("Reading from stdin...");
            return new BufferedReader(new InputStreamReader(System.in));
        } else {
            return new FileReader(args[0]);
        }
    }
}