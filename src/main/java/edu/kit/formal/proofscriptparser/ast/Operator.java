package edu.kit.formal.proofscriptparser.ast;

import static edu.kit.formal.proofscriptparser.ast.Type.*;

/**
 * An enum which contains meta-data to all operators.
 * <p>
 * <p>
 * Precedence: zero is preserved for literals!
 *
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
public enum Operator {
    /**
     * special entry for marking match as an atomic expression.
     */
    MATCH("match", 1000, BOOL),
    /**
     *
     */
    NOT("not", "¬", 10, BOOL, BOOL),
    /** */
    NEGATE("-", "-", 10, INT, INT),
    /** */
    MULTIPLY("*", "×", 20, INT, INT, INT),
    /** */
    DIVISION("/", "÷", 20, INT, INT, INT),
    /** */
    PLUS("+", 30, INT, INT, INT),
    /** */
    MINUS("-", 30, INT, INT, INT),
    /** */
    LE("<", 40, INT, INT, BOOL),
    /** */
    GE(">", 40, INT, INT, BOOL),
    /** */
    LEQ("<=", "≤", 40, INT, INT, BOOL),
    /** */
    GEQ(">=", "≥", 40, INT, INT, BOOL),
    /** */
    EQ("=", "≡", 50, INT, INT, BOOL),
    /** */
    NEQ("<>", "≢", 50, INT, INT, BOOL),
    /** */
    AND("&", "∧", 60, BOOL, BOOL, BOOL),
    /** */
    OR("|", "∨", 70, BOOL, BOOL, BOOL),
    /** */
    IMPLICATION("==>", "⇒", 80, BOOL, BOOL, BOOL),
    /**
     *
     * */
    EQUIVALENCE("<=>", "⇔", 90, BOOL, BOOL, BOOL);

    private final String symbol;
    private final String unicode;
    private final int precedence;
    private final Type[] type;

    Operator(String symbol, int precedence, Type... type) {
        this(symbol, symbol, precedence, type);
    }

    Operator(String symbol, String unicode, int precedence, Type... type) {
        this.symbol = symbol;
        this.unicode = unicode;
        this.precedence = precedence;
        this.type = type;
    }

    /**
     * unicode symbol of this operator
     *
     * @return a non-null string
     */
    public String unicode() {
        return unicode;
    }

    /**
     * a ascii symbol of this operator.
     *
     * @return a non-null string
     */
    public String symbol() {
        return symbol;
    }

    /**
     * Returns the precedence (bind strength) of this operator.
     * <p>
     * A low precedence bind stronger, e.g. literals are 0.
     */
    public int precedence() {
        return precedence;
    }

    /**
     * Returns all types (arguments and returntype) of operator.
     *
     * @return an array with the last entry is the return type
     * of this operator and all previous entries are the types of arguments.
     * @see #returnType()
     */
    public Type[] type() {
        return type;
    }

    /**
     * Returns the return type of operator.
     */
    public Type returnType() {
        return type[type.length - 1];
    }

    /**
     * Number of arguments of this operator.
     */
    public int arity() {
        return type.length - 1;
    }
}
