package edu.kit.formal.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formal.proofscriptparser.Visitor;
import lombok.*;

/**
 * A call to a subroutine or atomic function.
 *
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class CallStatement extends Statement<ScriptLanguageParser.ScriptCommandContext> {
    /**
     * The name of the command.
     */
    @NonNull
    private String command;

    /**
     * The list of parameters.
     */
    private Parameters parameters = new Parameters();

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
    public CallStatement clone() {
        return new CallStatement(command, parameters.clone());
    }
}
