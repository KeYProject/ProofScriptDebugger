package edu.kit.formal.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formal.proofscriptparser.Visitor;
import lombok.*;

/**
 * An {@link AssignmentStatement} encapsulate the assigned variable
 * {@link AssignmentStatement#getLhs()} and an expression {@link AssignmentStatement#getRhs()}.
 *
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
@Data
@ToString(includeFieldNames = true)
@NoArgsConstructor
@RequiredArgsConstructor()
@AllArgsConstructor
public class AssignmentStatement
        extends Statement<ScriptLanguageParser.AssignmentContext> {
    @NonNull
    private Variable lhs;
    @NonNull
    private Expression rhs;

    private Type type;

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AssignmentStatement clone() {
        return new AssignmentStatement(lhs.clone(), rhs.clone(), type);
    }

    /**
     * Returns true, iff this assignment declares the assigned variable.
     */
    public boolean isDeclaration() {
        return type != null;
    }

}
