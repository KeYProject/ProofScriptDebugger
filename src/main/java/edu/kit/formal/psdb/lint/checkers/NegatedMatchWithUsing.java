package edu.kit.formal.psdb.lint.checkers;

import edu.kit.formal.psdb.parser.ast.MatchExpression;
import edu.kit.formal.psdb.parser.ast.UnaryExpression;
import edu.kit.formal.psdb.lint.Issue;
import edu.kit.formal.psdb.lint.IssuesRepository;

/**
 * @author Alexander Weigl
 * @version 1 (23.05.17)
 */
public class NegatedMatchWithUsing extends AbstractLintRule {
    private static Issue ISSUE = IssuesRepository.getIssue(IssuesId.NEGATED_MATCH_WITH_USING);

    public NegatedMatchWithUsing() {
        super(NMWUSearcher::new);
    }

    static class NMWUSearcher extends Searcher {
        @Override
        public Void visit(UnaryExpression ue) {
            if (ue.getExpression() instanceof MatchExpression) {
                MatchExpression me = (MatchExpression) ue.getExpression();
                if (me.getSignature() != null && me.getSignature().size() > 0) {
                    problem(ISSUE, ue, me);
                }
            }
            return super.visit(ue);
        }
    }
}