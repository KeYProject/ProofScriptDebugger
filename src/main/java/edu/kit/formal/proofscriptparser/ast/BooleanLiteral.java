package edu.kit.formal.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.NotWelldefinedException;
import edu.kit.formal.proofscriptparser.Visitor;
import lombok.*;
import org.antlr.v4.runtime.Token;

import java.util.Optional;

/**
 * Represents a boolean literal (ie. {@link #FALSE} or {@link #TRUE}).
 * <p>
 * Instantiating can be useful for setting a custom {@link #setToken(Token)} and position.
 *
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
        this.token = token;
    }

    BooleanLiteral(boolean b) {
        this(b, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BooleanLiteral clone() {
        return new BooleanLiteral(value, token);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type getType(Signature signature) throws NotWelldefinedException {
        return Type.BOOL;
    }
}
