package edu.kit.formal.proofscriptparser.lint.checkers;

import edu.kit.formal.proofscriptparser.ast.ForeachStatement;
import edu.kit.formal.proofscriptparser.ast.GoalSelector;
import edu.kit.formal.proofscriptparser.ast.RepeatStatement;
import edu.kit.formal.proofscriptparser.ast.TheOnlyStatement;
import edu.kit.formal.proofscriptparser.lint.LintProblem;

import java.util.Objects;

/**
 * @author Alexander Weigl
 * @version 1 (23.05.17)
 */
public class SuccessiveGoalSelector extends AbstractLintRule {

    public SuccessiveGoalSelector() {
        super(SGSearcher::new);
    }

    static class SGSearcher extends Searcher {
        @Override
        public Void visit(TheOnlyStatement theOnly) {
            if (check(theOnly, theOnly.getNodeName())) {
                problems.add(LintProblem.create("")
                        .level('W')
                        .message("succ. same goal selector")
                        .nodes(theOnly, theOnly.getBody().get(0)));
            }

            if (check(theOnly, "ForeachStatement")) {
                problems.add(LintProblem.create("")
                        .level('W')
                        .message("foreach following theonly has no effect")
                        .nodes(theOnly, theOnly.getBody().get(0)));
            }

            return super.visit(theOnly);
        }

        private boolean check(GoalSelector gs, String forbidden) {
            return gs.getBody().size() > 0 && Objects.equals(gs.getBody().get(0).getNodeName(), forbidden);
        }

        @Override
        public Void visit(ForeachStatement foreach) {
            if (check(foreach, foreach.getNodeName())) {
                problems.add(LintProblem.create("")
                        .level('W')
                        .message("succ. same goal selector")
                        .nodes(foreach, foreach.getBody().get(0)));
            }

            if (check(foreach, "TheOnlyStatement")) {
                problems.add(LintProblem.create("")
                        .level('W')
                        .message("theonly following foreach has no effect")
                        .nodes(foreach, foreach.getBody().get(0)));
            }
            return super.visit(foreach);
        }

        @Override
        public Void visit(RepeatStatement repeatStatement) {
            if (check(repeatStatement, repeatStatement.getNodeName())) {
                problems.add(LintProblem.create("")
                        .level('W')
                        .message("succ. same goal selector")
                        .nodes(repeatStatement, repeatStatement.getBody().get(0)));
            }
            return super.visit(repeatStatement);
        }
    }
}