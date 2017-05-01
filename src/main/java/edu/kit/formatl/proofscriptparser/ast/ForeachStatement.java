package edu.kit.formatl.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formatl.proofscriptparser.Visitor;
import lombok.Data;

/**
 * @author Alexander Weigl
 * @version 1 (29.04.17)
 */
@Data
public class ForeachStatement extends GoalSelector<ScriptLanguageParser.ForEachStmtContext> {
    public ForeachStatement() {
    }

    public ForeachStatement(Statements statements) {
        super(statements);
    }

    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public ForeachStatement clone() {
        return new ForeachStatement(getBody().clone());
    }
}
