package edu.kit.iti.formal.psdbg.parser.ast;

import edu.kit.iti.formal.psdbg.parser.ScriptLanguageParser;
import edu.kit.iti.formal.psdbg.parser.Visitor;
import lombok.Data;

@Data
public class DefaultCaseStatement extends Statement<ScriptLanguageParser.StmtListContext> {

    protected Statements body;

    public DefaultCaseStatement() {
        this.body = new Statements();
    }

    public DefaultCaseStatement(Statements body) {
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
    public DefaultCaseStatement copy() {
        return new DefaultCaseStatement(body.copy());
    }
}



