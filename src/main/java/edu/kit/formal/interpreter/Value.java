package edu.kit.formal.interpreter;

import edu.kit.formal.proofscriptparser.ast.BooleanLiteral;
import edu.kit.formal.proofscriptparser.ast.IntegerLiteral;
import edu.kit.formal.proofscriptparser.ast.StringLiteral;
import edu.kit.formal.proofscriptparser.ast.Type;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

/**
 * Class representing the values our variables may have
 *
 * @author S.Grebing
 *         //TODO alle restlichen typen ergaenzen
 */
@RequiredArgsConstructor
public class Value<T> {
    @Getter
    private final Type type;
    @Getter
    private final T data;


    public static Value<BigInteger> from(IntegerLiteral i) {
        return new Value<>(Type.INT, i.getValue());
    }

    public static Value<BigInteger> from(Integer i) {
        return new Value<>(Type.INT, BigInteger.valueOf(i));
    }

    public static Value<String> from(StringLiteral s) {
        return new Value<>(Type.INT, s.getText());
    }

    public static Value<Boolean> from(BooleanLiteral b) {
        return new Value<>(Type.BOOL, b.isValue());
    }

    public static Value<Boolean> from(boolean equals) {
        return new Value<>(Type.BOOL, equals);
    }

    public static Value<BigInteger> from(BigInteger apply) {
        return new Value<>(Type.INT, apply);
    }

    @Override
    public String toString() {
        return "Value{" +
                "type=" + type +
                ", data=" + data +
                '}';
    }
}
