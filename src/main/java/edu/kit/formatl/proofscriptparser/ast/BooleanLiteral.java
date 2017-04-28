package edu.kit.formatl.proofscriptparser.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.Optional;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
public class BooleanLiteral extends Literal {
    public static final BooleanLiteral FALSE = new BooleanLiteral(false);
    public static final BooleanLiteral TRUE = new BooleanLiteral(true);
    private final boolean value;

    public BooleanLiteral(boolean value, Token token) {
        this.value = value;
        if (token != null)
            this.token = Optional.of(token);
        else
            this.token = Optional.empty();

    }

    BooleanLiteral(boolean b) {
        this(b, null);
    }

    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public ASTNode<ParserRuleContext> clone() {
        return null;
    }

    @Override public String toString() {
        return "BooleanLiteral{" + "value=" + value + '}';
    }

    public boolean getValue() {
        return value;
    }
}
