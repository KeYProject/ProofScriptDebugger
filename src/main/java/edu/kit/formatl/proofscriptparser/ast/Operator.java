package edu.kit.formatl.proofscriptparser.ast;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
public enum Operator {
    MULTIPLY("*"), DIVISION("/"), AND("&&"), OR("||"), IMPLICATION("==>"),
    PLUS("+"), MINUS("-"), EQ("="), NEQ("<>");

    private final String symbol;

    Operator(String symbol) {
        this.symbol = symbol;
    }

    public  String symbol() {
        return symbol;
    }

}
