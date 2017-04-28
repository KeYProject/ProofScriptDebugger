package edu.kit.formatl.proofscriptparser.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.math.BigInteger;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
public class IntegerLiteral extends Literal {
    private final BigInteger value;

    public IntegerLiteral(Token digits) {
        setToken(digits);
        value = BigInteger.valueOf(Long.parseLong(digits.getText()));
    }

    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public ASTNode<ParserRuleContext> clone() {
        return null;
    }

    public BigInteger getValue() {
        return value;
    }
}
