package edu.kit.formatl.proofscriptparser.ast;

import lombok.*;
import org.antlr.v4.runtime.Token;

@Data @RequiredArgsConstructor public class Position implements Cloneable {
    @NonNull final int lineNumber;
    @NonNull final int charInLine;

    public Position() {
        this(-1, -1);
    }

    @Override public Position clone() {
        return new Position(lineNumber, charInLine);
    }

    @Override public String toString() {
        return "@" + lineNumber + ":" + charInLine;
    }

    public static Position from(Token token) {
        return new Position(token.getLine(), token.getCharPositionInLine());
    }
}
