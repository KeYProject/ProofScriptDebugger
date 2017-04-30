package edu.kit.formatl.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formatl.proofscriptparser.Visitor;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
public class MatchExpression extends Expression<ScriptLanguageParser.MatchPatternContext> {
    private Signature signature;
    private TermLiteral term;
    private String variable;

    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public ASTNode<ScriptLanguageParser.MatchPatternContext> clone() {
        return null;
    }

    public void setSignature(Signature signature) {
        this.signature = signature;
    }

    public Signature getSignature() {
        return signature;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public void setTerm(TermLiteral term) {
        this.term = term;
    }

    public TermLiteral getTerm() {
        return term;
    }

    @Override public int getPrecedence() {
        return Operator.MATCH.precedence();
    }
}
