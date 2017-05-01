package edu.kit.formal.proofscriptparser;

import edu.kit.formal.proofscriptparser.ast.Expression;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Alexander Weigl
 * @version 1 (01.05.17)
 */
@Data
@AllArgsConstructor
public class NotWelldefinedException extends Exception {
    private final Expression expr;

    public NotWelldefinedException(String message, Expression e) {
        super(message);
        this.expr = e;
    }
}
