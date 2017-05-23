package edu.kit.formal.proofscriptparser.lint.checkers;

import edu.kit.formal.proofscriptparser.ast.Statements;
import edu.kit.formal.proofscriptparser.lint.LintProblem;

/**
 * @author Alexander Weigl
 * @version 1 (23.05.17)
 */
public class EmptyBlocks extends AbstractLintRule {
    public EmptyBlocks() {
        super(EmptyBlockSearcher::new);
    }

    private static class EmptyBlockSearcher extends Searcher {
        @Override
        public Void visit(Statements statements) {
            if (statements.size() == 0) {
                problems.add(LintProblem.create("")
                        .level('I')
                        .message("Empty Block ")
                        .nodes(statements)
                );
            }
            return super.visit(statements);
        }
    }
}
