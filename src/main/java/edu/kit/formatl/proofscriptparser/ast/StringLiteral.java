package edu.kit.formatl.proofscriptparser.ast;

import edu.kit.formatl.proofscriptparser.NotWelldefinedException;
import edu.kit.formatl.proofscriptparser.Visitor;
import lombok.Data;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
@Data
public class StringLiteral extends Literal {
    private final String text;

    public StringLiteral(String text) {
        this.text = text;
    }

    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public StringLiteral clone() {
        return new StringLiteral(text);
    }

    @Override
    public Type getType(Signature signature) throws NotWelldefinedException {
        return Type.STRING;
    }
}
