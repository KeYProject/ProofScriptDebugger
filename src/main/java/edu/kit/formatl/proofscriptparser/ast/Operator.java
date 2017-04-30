package edu.kit.formatl.proofscriptparser.ast;

/**
 * Precedence: zero is preserved for literals!
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
public enum Operator {
    /**
     * special entry for marking match as an atomic expression.
     */
    MATCH("match", 1000),
    NOT("not", "¬", 10),
    MULTIPLY("*", "×", 20),
    DIVISION("/", "÷", 20),
    PLUS("+",  30),
    MINUS("-", 30),
    LE("<",40),
    GE(">",40),
    LEQ("<=", "≤",40),
    GEQ(">=", "≥",40),
    EQ("=", "≡", 50),
    NEQ("<>","≢", 50),
    AND("&&", "∧", 60),
    OR("||", "∨", 70),
    IMPLICATION("==>", "⇒", 80),
    EQUIVALENCE("<=>", "⇔", 90);

    private final String symbol;
    private final String unicode;
    private final int precedence;

    Operator(String symbol, int precedence) {
        this(symbol, symbol, precedence);
    }

    Operator(String symbol, String unicode, int precedence) {
        this.symbol = symbol;
        this.unicode = unicode;
        this.precedence = precedence;
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

}
