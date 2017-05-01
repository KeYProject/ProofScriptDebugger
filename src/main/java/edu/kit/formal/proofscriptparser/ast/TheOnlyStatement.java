package edu.kit.formal.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formal.proofscriptparser.Visitor;
import lombok.Data;

/**
 * @author Alexander Weigl
 * @version 1 (29.04.17)
 */
@Data
public class TheOnlyStatement extends GoalSelector<ScriptLanguageParser.TheOnlyStmtContext> {
    public TheOnlyStatement() {
    }

    public TheOnlyStatement(Statements statements) {
        super(statements);
    }

    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public TheOnlyStatement clone() {
        return new TheOnlyStatement(getBody().clone());
    }
}
