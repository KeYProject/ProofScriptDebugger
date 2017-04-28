package edu.kit.formatl.proofscriptparser.ast;

import org.antlr.v4.runtime.Token;

public class Position implements Cloneable {
    final int lineNumber;
    final int charInLine;

    public Position(int lineNumber, int charInLine) {
        this.lineNumber = lineNumber;
        this.charInLine = charInLine;
    }

    public Position() {
        this(-1, -1);
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getCharInLine() {
        return charInLine;
    }

    @Override public Position clone() {
        return new Position(lineNumber, charInLine);
    }

    @Override public String toString() {
        return "@" + lineNumber + ":" + charInLine;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Position position = (Position) o;

        if (lineNumber != position.lineNumber)
            return false;
        return charInLine == position.charInLine;
    }

    @Override public int hashCode() {
        int result = lineNumber;
        result = 31 * result + charInLine;
        return result;
    }

    public static Position from(Token token) {
        return new Position(token.getLine(), token.getCharPositionInLine());
    }
}
