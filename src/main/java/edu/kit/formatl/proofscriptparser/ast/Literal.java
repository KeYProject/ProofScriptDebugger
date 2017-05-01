package edu.kit.formatl.proofscriptparser.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.Optional;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
public abstract class Literal extends Expression<ParserRuleContext> {
    protected Optional<Token> token;

    public Optional<Token> getToken() {
        return token;
    }

    public Literal setToken(Token token) {
        this.token = Optional.of(token);
        return this;
    }

    @Override public int getPrecedence() {
        return 0;
    }

    @Override public Optional<ParserRuleContext> getRuleContext() {
        return Optional.empty();
    }
}
