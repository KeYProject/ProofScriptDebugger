package edu.kit.formatl.proofscriptparser.ast;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
public class StringLiteral extends Literal {
    public StringLiteral(String text) {
    }

    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public ASTNode<ParserRuleContext> clone() {
        return null;
    }
}
