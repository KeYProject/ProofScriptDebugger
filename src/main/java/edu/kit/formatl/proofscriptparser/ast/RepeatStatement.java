package edu.kit.formatl.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formatl.proofscriptparser.Visitor;
import lombok.Data;

/**
 * @author Alexander Weigl
 * @version 1 (29.04.17)
 */
@Data
public class RepeatStatement extends GoalSelector<ScriptLanguageParser.RepeatStmtContext> {
    public RepeatStatement() {
    }

    public RepeatStatement(Statements statements) {
        super(statements);
    }

    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public RepeatStatement clone() {
        return new RepeatStatement(getBody().clone());
    }
}
