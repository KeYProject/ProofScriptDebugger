package edu.kit.formatl.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
public class CaseStatement extends Statement<ScriptLanguageParser.CasesListContext> {
    private Expression guard;
    private Statements body;

    public Expression getGuard() {
        return guard;
    }

    public CaseStatement setGuard(Expression guard) {
        this.guard = guard;
        return this;
    }

    public Statements getBody() {
        return body;
    }

    public CaseStatement setBody(Statements body) {
        this.body = body;
        return this;
    }

    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public ASTNode<ScriptLanguageParser.CasesListContext> clone() {
        return null;
    }
}
