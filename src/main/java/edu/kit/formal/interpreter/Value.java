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
 * @author A. Weigl
 *         //TODO alle restlichen typen ergaenzen
 */
@RequiredArgsConstructor
public class Value<T> {
    public static final Value TRUE = new Value<>(Type.BOOL, true);
    public static final Value FALSE = new Value<>(Type.BOOL, false);

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
        return new Value<>(Type.STRING, s.getText());
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Value<?> value = (Value<?>) o;

        if (getType() != value.getType()) return false;
        return getData().equals(value.getData());
    }

    @Override
    public int hashCode() {
        int result = getType().hashCode();
        result = 31 * result + getData().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Value{" +
                "type=" + type +
                ", data=" + data +
                '}';
    }
}
