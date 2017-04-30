package edu.kit.formatl.proofscriptparser.ast;

/**
 * Created by sarah on 4/30/17.
 */
public enum Type {
    INT("int"), BOOL("bool");

    private final String symbol;

    Type(String symbol) {
        this.symbol = symbol;
    }

    public  String symbol() {
        return symbol;
    }
}
