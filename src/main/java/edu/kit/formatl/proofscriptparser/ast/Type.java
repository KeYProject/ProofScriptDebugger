package edu.kit.formatl.proofscriptparser.ast;

import java.util.Arrays;

/**
 * Created by sarah on 4/30/17.
 */
public enum Type {
    STRING("string"), TERM("term"), ANY("any"),
    INT("int"), BOOL("bool");

    private final String symbol;

    Type(String symbol) {
        this.symbol = symbol;
    }

    public String symbol() {
        return symbol;
    }


    public static Type findType(String n) {
        for (Type t : Type.values()) {
            if (t.symbol().equals(n))
                return t;
        }
        throw new IllegalStateException("Type " + n + " is not defined. Valid types are: " + Arrays.toString(values()));
    }
}
