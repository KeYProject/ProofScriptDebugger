package edu.kit.formatl.proofscriptparser.ast;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * @author Alexander Weigl
 * @version 1 (29.04.17)
 */
public abstract class GoalSelector<T extends ParserRuleContext> extends ASTNode<T> {
    private Statements body;

    public Statements getBody() {
        return body;
    }

    public GoalSelector<T> setBody(Statements body) {
        this.body = body;
        return this;
    }
}
