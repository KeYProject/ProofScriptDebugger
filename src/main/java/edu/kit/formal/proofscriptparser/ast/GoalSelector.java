package edu.kit.formal.proofscriptparser.ast;

import lombok.*;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * @author Alexander Weigl
 * @version 1 (29.04.17)
 */
@Data @AllArgsConstructor @NoArgsConstructor public abstract class GoalSelector<T extends ParserRuleContext>
        extends Statement<T> {
    @NonNull private Statements body = new Statements();
}
