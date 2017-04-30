package edu.kit.formatl.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formatl.proofscriptparser.Visitor;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
public class AssignmentStatement extends Statement<ScriptLanguageParser.AssignmentContext> {
    private Variable rhs;
    private Expression lhs;

    //TODO type missing, if initialization

    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public ASTNode<ScriptLanguageParser.AssignmentContext> clone() {
        return null;
    }

    public void setRhs(Variable rhs) {
        this.rhs = rhs;
    }

    public Variable getRhs() {
        return rhs;
    }

    public void setLhs(Expression lhs) {
        this.lhs = lhs;
    }

    public Expression getLhs() {
        return lhs;
    }

    @Override public String toString() {
        return String.format("%s := %s", rhs, lhs);
    }
}
