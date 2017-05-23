package edu.kit.formal.proofscriptparser.lint.checkers;

import edu.kit.formal.proofscriptparser.ASTTraversal;
import edu.kit.formal.proofscriptparser.DefaultASTVisitor;
import edu.kit.formal.proofscriptparser.lint.LintProblem;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (23.05.17)
 */
public abstract class Searcher implements ASTTraversal<Void> {
    @Getter
    protected final List<LintProblem> problems = new ArrayList<>(10);

    LintProblem problem(String rulename) {
        LintProblem lp = LintProblem.create(rulename);
        problems.add(lp);
        return lp;
    }
}
