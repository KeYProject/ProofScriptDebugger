package edu.kit.formatl.proofscriptparser.ast;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
public abstract class Expression<T extends ParserRuleContext> extends ASTNode<T> {
    public abstract int getPrecedence();
}
