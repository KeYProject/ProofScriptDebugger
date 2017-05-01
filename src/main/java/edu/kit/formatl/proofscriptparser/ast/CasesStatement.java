package edu.kit.formatl.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formatl.proofscriptparser.Visitor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
@Data
public class CasesStatement extends Statement<ScriptLanguageParser.CasesListContext> {
    private List<CaseStatement> cases = new ArrayList<>();
    private Statements defaultCase = new Statements();

    public List<CaseStatement> getCases() {
        return cases;
    }

    public Statements getDefaultCase() {
        return defaultCase;
    }

    public CasesStatement setDefaultCase(Statements defaultCase) {
        this.defaultCase = defaultCase;
        return this;
    }

    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public CasesStatement clone() {
        CasesStatement c = new CasesStatement();
        cases.forEach(caseStatement -> c.cases.add(caseStatement.clone()));
        if (defaultCase != null)
            c.defaultCase = defaultCase.clone();
        return c;
    }
}
