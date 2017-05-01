package edu.kit.formal.proofscriptparser.ast;

import lombok.*;
import org.antlr.v4.runtime.Token;

/**
 * @author Alexander Weigl
 */
@Data @RequiredArgsConstructor public class Position implements Cloneable {
    @NonNull final int lineNumber;
    @NonNull final int charInLine;

    public Position() {
        this(-1, -1);
    }

    @Override public Position clone() {
        return new Position(lineNumber, charInLine);
    }

    public static Position from(Token token) {
        return new Position(token.getLine(), token.getCharPositionInLine());
    }
}
