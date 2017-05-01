package edu.kit.formal.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formal.proofscriptparser.Visitor;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
@Data
public class CasesStatement extends Statement<ScriptLanguageParser.CasesListContext> {
    @NonNull private final List<CaseStatement> cases = new ArrayList<>();
    @NonNull private Statements defaultCase = new Statements();

    /**
     * {@inheritDoc}
     */
    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override public CasesStatement clone() {
        CasesStatement c = new CasesStatement();
        cases.forEach(caseStatement -> c.cases.add(caseStatement.clone()));
        if (defaultCase != null)
            c.defaultCase = defaultCase.clone();
        return c;
    }
}
