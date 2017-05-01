package edu.kit.formal.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.NotWelldefinedException;
import edu.kit.formal.proofscriptparser.Visitor;
import lombok.Data;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
@Data
public class TermLiteral extends Literal {
    private final String text;

    public TermLiteral(String text) {
        if(text.charAt(0) == '`')
            text = text.substring(1);
        if(text.charAt(0) == '`')
            text = text.substring(0, text.length() - 2);
        this.text = text;
    }

    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public TermLiteral clone() {
        return new TermLiteral(text);
    }

    @Override
    public Type getType(Signature signature) throws NotWelldefinedException {
        return Type.TERM;
    }
}
