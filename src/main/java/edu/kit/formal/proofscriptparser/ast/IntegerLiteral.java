package edu.kit.formal.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.NotWelldefinedException;
import edu.kit.formal.proofscriptparser.Visitor;
import lombok.Data;
import org.antlr.v4.runtime.Token;

import java.math.BigInteger;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
@Data
public class IntegerLiteral extends Literal {
    private final BigInteger value;

    public IntegerLiteral() {
        this(BigInteger.ZERO);
    }

    public IntegerLiteral(BigInteger value) {
        this.value = value;
    }

    public IntegerLiteral(Token digits) {
        setToken(digits);
        value = BigInteger.valueOf(Long.parseLong(digits.getText()));
    }

    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public IntegerLiteral clone() {
        IntegerLiteral il = new IntegerLiteral(value);
        il.token = token;
        return il;
    }

    @Override
    public Type getType(Signature signature) throws NotWelldefinedException {
        return Type.INT;
    }


    public BigInteger getValue() {
        return value;
    }
}
