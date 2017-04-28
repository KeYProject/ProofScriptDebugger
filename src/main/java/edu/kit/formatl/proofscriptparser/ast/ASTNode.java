package edu.kit.formatl.proofscriptparser.ast;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Optional;

/**
 * @author Alexander Weigl
 * @version 1 (27.04.17)
 */
public abstract class ASTNode<T extends ParserRuleContext> implements Visitable, Cloneable {
    private Optional<T> ruleContext;
    private Position startPosition = new Position();
    private Position endPosition = new Position();

    public void setRuleContext(T c) {
        startPosition = Position.from(c.getStart());
        endPosition = Position.from(c.getStop());
        ruleContext = Optional.of(c);
    }

    public Optional<T> getRuleContext() {
        return ruleContext;
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

    @Override public abstract ASTNode<T> clone();
}
