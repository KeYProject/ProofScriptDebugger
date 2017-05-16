package edu.kit.formal.proofscriptparser.ast;

/*-
 * #%L
 * ProofScriptParser
 * %%
 * Copyright (C) 2017 Application-oriented Formal Verification
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */



import edu.kit.formal.proofscriptparser.Visitable;
import edu.kit.formal.proofscriptparser.Visitor;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Optional;

/**
 * @author Alexander Weigl
 * @version 1 (27.04.17)
 */
public abstract class ASTNode<T extends ParserRuleContext>
        implements Visitable, Copyable<ASTNode<T>> {
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
    public abstract ASTNode<T> copy();

}