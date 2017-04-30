package edu.kit.formatl.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formatl.proofscriptparser.Visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
public class CasesStatement extends Statement<ScriptLanguageParser.CasesListContext> {
    private List<CaseStatement> cases = new ArrayList<>();

    public List<CaseStatement> getCases() {
        return cases;
    }

    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public ASTNode<ScriptLanguageParser.CasesListContext> clone() {
        return null;
    }
}
