package edu.kit.formal.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formal.proofscriptparser.Visitor;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RepeatStatement clone() {
        return new RepeatStatement(getBody().clone());
    }
}
