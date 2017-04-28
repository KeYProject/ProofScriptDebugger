package edu.kit.formatl.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
public class AssignmentStatement extends Statement<ScriptLanguageParser.AssignmentContext> {
    private String rhs;
    private Expression lhs;

    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public ASTNode<ScriptLanguageParser.AssignmentContext> clone() {
        return null;
    }

    public void setRhs(String rhs) {
        this.rhs = rhs;
    }

    public String getRhs() {
        return rhs;
    }

    public void setLhs(Expression lhs) {
        this.lhs = lhs;
    }

    public Expression getLhs() {
        return lhs;
    }
}
