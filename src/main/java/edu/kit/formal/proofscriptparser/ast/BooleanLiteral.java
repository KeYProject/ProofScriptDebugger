package edu.kit.formal.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.NotWelldefinedException;
import edu.kit.formal.proofscriptparser.Visitor;
import lombok.*;
import org.antlr.v4.runtime.Token;

import java.util.Optional;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Getter
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

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public BooleanLiteral clone() {
        return new BooleanLiteral(value, token.orElse(null));
    }

    @Override
    public Type getType(Signature signature) throws NotWelldefinedException {
        return Type.BOOL;
    }
}
