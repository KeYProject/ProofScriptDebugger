package edu.kit.formal.proofscriptparser.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.Optional;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
public abstract class Literal extends Expression<ParserRuleContext> {
    protected Token token;

    public Optional<Token> getToken() {
        return Optional.of(token);
    }

    public Literal setToken(Token token) {
        this.token = token;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPrecedence() {
        return 0;
    }

    /**
     * {@link Literal}s do not have any {@link ParserRuleContext}
     *
     * @return always {@link Optional#empty()}
     */
    @Override
    public Optional<ParserRuleContext> getRuleContext() {
        return Optional.empty();
    }
}
