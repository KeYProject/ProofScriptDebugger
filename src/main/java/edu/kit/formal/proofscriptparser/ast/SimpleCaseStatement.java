package edu.kit.formal.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.Visitor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by sarah on 7/17/17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleCaseStatement extends CaseStatement {
    private Expression guard;
    private boolean isClosedStmt = false;

    public SimpleCaseStatement(Expression guard, Statements body) {
        this.guard = guard;
        this.body = body;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SimpleCaseStatement copy() {
        return new SimpleCaseStatement(guard.copy(), body.copy());
    }


}
