package edu.kit.formal.proofscriptparser.ast;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * @author Alexander Weigl
 * @version 1 (27.04.17)
 */
public abstract class Statement<T extends ParserRuleContext> extends ASTNode<T> {
    /**
     * {@inheritDoc}
     */
    @Override public abstract Statement<T> clone();
}
