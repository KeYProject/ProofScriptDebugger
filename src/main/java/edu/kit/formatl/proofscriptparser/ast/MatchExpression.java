package edu.kit.formatl.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;

import java.util.Map;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
public class MatchExpression extends Expression<ScriptLanguageParser.MatchPatternContext> {
    private Map<String, String> signature;
    private TermLiteral term;
    private String variable;

    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public ASTNode<ScriptLanguageParser.MatchPatternContext> clone() {
        return null;
    }

    public void setSignature(Map<String, String> signature) {
        this.signature = signature;
    }

    public Map<String, String> getSignature() {
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
}
