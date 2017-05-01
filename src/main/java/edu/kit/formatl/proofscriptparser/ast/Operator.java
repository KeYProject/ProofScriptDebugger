package edu.kit.formatl.proofscriptparser.ast;

import static edu.kit.formatl.proofscriptparser.ast.Type.*;

/**
 * Precedence: zero is preserved for literals!
 *
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
public enum Operator {
    /**
     * special entry for marking match as an atomic expression.
     */
    MATCH("match", 1000, bool),
    /**
     *
     */
    NOT("not", "¬", 10, bool, bool),
    /** */
    NEGATE("-", "-", 10, integer, integer),
    /** */
    MULTIPLY("*", "×", 20, integer, integer, integer),
    /** */
    DIVISION("/", "÷", 20, integer, integer, integer),
    /** */
    PLUS("+", 30, integer, integer, integer),
    /** */
    MINUS("-", 30, integer, integer, integer),
    /** */
    LE("<", 40, integer, integer, bool),
    /** */
    GE(">", 40, integer, integer, bool),
    /** */
    LEQ("<=", "≤", 40, integer, integer, bool),
    /** */
    GEQ(">=", "≥", 40, integer, integer, bool),
    /** */
    EQ("=", "≡", 50, integer, integer, bool),
    /** */
    NEQ("<>", "≢", 50, integer, integer, bool),
    /** */
    AND("&", "∧", 60, bool, bool, bool),
    /** */
    OR("|", "∨", 70, bool, bool, bool),
    /** */
    IMPLICATION("==>", "⇒", 80, bool, bool, bool),
    /**
     *
     * */
    EQUIVALENCE("<=>", "⇔", 90, bool, bool, bool);

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

    public String unicode() {
        return unicode;
    }

    public String symbol() {
        return symbol;
    }

    public int precedence() {
        return precedence;
    }

    public Type[] type() {
        return type;
    }

    public Type returnType() {
        return type[type.length - 1];
    }


    public int arity() {
        return type.length - 1;
    }
}
