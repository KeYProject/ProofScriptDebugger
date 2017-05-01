package edu.kit.formal.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.NotWelldefinedException;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Abstract representation of an expression.
 *
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
public abstract class Expression<T extends ParserRuleContext> extends ASTNode<T> {
    /**
     * Returns the precedence of the operator expression.
     * <p>
     * For {@link BinaryExpression} and {@link UnaryExpression}
     * this is the precedence of the operator.
     * For {@link Literal} is this zero.
     */
    public abstract int getPrecedence();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract Expression clone();

    /**
     * {@inheritDoc}
     */
    public abstract Type getType(Signature signature)
            throws NotWelldefinedException;

    /**
     * @param type
     * @param e
     * @param signature
     * @throws NotWelldefinedException
     */
    public static final void checkType(Type type, Expression e, Signature signature) throws NotWelldefinedException {
        Type got = e.getType(signature);
        if (!type.equals(got)) {
            throw new NotWelldefinedException("Typemismatch in expected " + type + ", got" + got, e);
        }
    }
}
