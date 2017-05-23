package edu.kit.formal.proofscriptparser.lint.checkers;

import edu.kit.formal.proofscriptparser.ast.MatchExpression;
import edu.kit.formal.proofscriptparser.ast.UnaryExpression;

/**
 * @author Alexander Weigl
 * @version 1 (23.05.17)
 */
public class NegatedMatchWithUsing extends AbstractLintRule {
    protected NegatedMatchWithUsing(SearcherFactory factory) {
        super(NMWUSearcher::new);
    }

    static class NMWUSearcher extends Searcher {
        @Override
        public Void visit(UnaryExpression ue) {
            if (ue.getExpression() instanceof MatchExpression) {
                MatchExpression me = (MatchExpression) ue.getExpression();
                if (me.getSignature() != null && me.getSignature().size() > 0) {
                    problem("NMWU").message("negated match with using ").nodes(ue, me);
                }
            }
            return super.visit(ue);
        }
    }
}
