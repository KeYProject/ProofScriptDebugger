package edu.kit.formal.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.NotWelldefinedException;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
public abstract class Expression<T extends ParserRuleContext> extends ASTNode<T> {
    public abstract int getPrecedence();

    @Override public abstract Expression clone();

    public abstract Type getType(Signature signature)
            throws NotWelldefinedException;

}
