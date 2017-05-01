package edu.kit.formal.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formal.proofscriptparser.Visitor;
import lombok.*;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class CallStatement extends Statement<ScriptLanguageParser.ScriptCommandContext> {
    @NonNull private String command;
    private Parameters parameters = new Parameters();

    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public CallStatement clone() {
        return new CallStatement(command, parameters.clone());
    }
}
