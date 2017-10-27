package edu.kit.iti.formal.psdbg.parser.ast;

import edu.kit.iti.formal.psdbg.parser.Visitor;
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
        SimpleCaseStatement scs = new SimpleCaseStatement(guard.copy(), body.copy());
        scs.setRuleContext(this.ruleContext);
        return scs;
    }


}
