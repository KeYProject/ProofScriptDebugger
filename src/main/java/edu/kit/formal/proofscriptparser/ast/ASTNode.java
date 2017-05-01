package edu.kit.formal.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.Visitable;
import edu.kit.formal.proofscriptparser.Visitor;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Optional;

/**
 * @author Alexander Weigl
 * @version 1 (27.04.17)
 */
public abstract class ASTNode<T extends ParserRuleContext>
        implements Visitable, Cloneable {
    protected T ruleContext;
    protected Position startPosition = new Position();
    protected Position endPosition = new Position();

    public void setRuleContext(T c) {
        startPosition = Position.from(c.getStart());
        endPosition = Position.from(c.getStop());
        ruleContext = c;
    }

    public Optional<T> getRuleContext() {
        return Optional.of(ruleContext);
    }

    public Position getStartPosition() {
        return startPosition;
    }

    public Position getEndPosition() {
        return endPosition;
    }

    /**
     * <p>getNodeName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getNodeName() {
        return getClass().getName();
    }

    /**
     * {@inheritDoc}
     */
    public abstract <T> T accept(Visitor<T> visitor);

    /**
     * Deep copy of the AST hierarchy.
     *
     * @return a fresh substree of the AST that is equal to this.
     */
    @Override
    public abstract ASTNode<T> clone();

}
