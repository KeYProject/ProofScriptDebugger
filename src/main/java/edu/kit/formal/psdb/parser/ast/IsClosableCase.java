package edu.kit.formal.psdb.parser.ast;

import edu.kit.formal.psdb.parser.Visitor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Object representing  a "match isClosable{ Commands}" block
 */
@Data
@NoArgsConstructor
public class IsClosableCase extends CaseStatement {
    private boolean isClosedStmt = true;

    public IsClosableCase(Statements body) {
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
    public IsClosableCase copy() {
        return new IsClosableCase(body.copy());
    }
}
