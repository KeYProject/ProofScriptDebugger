package edu.kit.formatl.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formatl.proofscriptparser.Visitor;

/**
 * @author Alexander Weigl
 * @version 1 (29.04.17)
 */
public class TheOnlyStatement extends GoalSelector<ScriptLanguageParser.TheOnlyStmtContext> {
    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public ASTNode<ScriptLanguageParser.TheOnlyStmtContext> clone() {
        return null;
    }
}
