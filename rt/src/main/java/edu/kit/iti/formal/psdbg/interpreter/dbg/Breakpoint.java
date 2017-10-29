package edu.kit.iti.formal.psdbg.interpreter.dbg;

import edu.kit.iti.formal.psdbg.parser.Facade;
import edu.kit.iti.formal.psdbg.parser.ast.Expression;
import lombok.Data;
import org.antlr.v4.runtime.CharStreams;

@Data
public class Breakpoint {
    private String sourceName;

    private int lineNumber;

    private boolean enabled;

    private String condition;

    private Expression conditionAst;

    public Breakpoint(String file, int lineNumber) {
        sourceName = file;
        this.lineNumber = lineNumber;
    }

    public boolean isConditional() {
        return condition != null;
    }

    public void setCondition(String condition) {
        this.condition = condition;
        this.conditionAst = Facade.parseExpression(condition);
    }
}