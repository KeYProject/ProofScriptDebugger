package edu.kit.formatl.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formatl.proofscriptparser.Visitor;
import lombok.*;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
@Data @ToString(includeFieldNames = true) @NoArgsConstructor @RequiredArgsConstructor() @AllArgsConstructor
public class AssignmentStatement
        extends Statement<ScriptLanguageParser.AssignmentContext> {
    @NonNull private Variable rhs;
    @NonNull private Expression lhs;
    private Type type;

    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public AssignmentStatement clone() {
        return new AssignmentStatement(rhs.clone(), lhs.clone());
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

}
