package edu.kit.formatl.proofscriptparser.ast;

import edu.kit.formatl.proofscriptparser.Visitor;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
public class Variable extends Literal {
    private final String identifier;

    public Variable(String identifier) {
        this.identifier = identifier;
    }

    public Variable(Token variable) {
        this(variable.getText());
        setToken(variable);
    }

    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public ASTNode<ParserRuleContext> clone() {
        return null;
    }

    @Override public String toString() {
        return "Variable{" + "identifier='" + identifier + '\'' + '}';
    }

    public String getIdentifier() {
        return identifier;
    }
}
