package edu.kit.iti.formal.psdbg.parser.ast;

import edu.kit.iti.formal.psdbg.parser.Visitor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Object representing  a "closes closeScriptCall(): { Commands}" block in a cases
 */
@Data
@NoArgsConstructor
public class ClosesCase extends CaseStatement {

    private boolean isClosedStmt = true;

    /**
     * Script that should be executed and shown whether case can be closed
     */
    private Statements closesScript;

    /**
     * A close subscript() {bodyscript} expression
     *
     * @param closesScript the script that is exectued in order to determine whether goal would clos. This proof is pruned afterwards
     * @param body         the actual script that is then executed when closesscript was successful and pruned
     */
    public ClosesCase(Statements closesScript, Statements body) {
        this.body = body;
        this.closesScript = closesScript;
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
    public ClosesCase copy() {
        return new ClosesCase(closesScript.copy(), body.copy());
    }

}
