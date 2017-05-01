package edu.kit.formal.proofscriptparser.ast;

import java.util.Arrays;

/**
 * Represents the possible types.
 *
 * Created at 30.04.2017
 * @author Sarah Grebing
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
