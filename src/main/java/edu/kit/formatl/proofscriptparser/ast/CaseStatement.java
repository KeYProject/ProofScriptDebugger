package edu.kit.formatl.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formatl.proofscriptparser.Visitor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
@Data
@NoArgsConstructor
public class CaseStatement extends Statement<ScriptLanguageParser.CasesListContext> {
    private Expression guard;
    private Statements body;


    public CaseStatement(Expression guard, Statements body) {
        this.guard = guard;
        this.body = body;
    }


    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public CaseStatement clone() {
        return new CaseStatement(guard.clone(), body.clone());
    }
}
