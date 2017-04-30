package edu.kit.formatl.proofscriptparser.ast;

import edu.kit.formatl.proofscriptparser.Visitor;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
public class StringLiteral extends Literal {
    private final String text;

    public StringLiteral(String text) {
        this.text = text;
    }

    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public ASTNode<ParserRuleContext> clone() {
        return null;
    }

    @Override public String toString() {
        return "StringLiteral{" + "text='" + text + '\'' + '}';
    }

    public String getText() {
        return text;
    }
}
