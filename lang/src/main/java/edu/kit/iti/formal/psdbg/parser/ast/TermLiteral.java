package edu.kit.iti.formal.psdbg.parser.ast;

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



import edu.kit.iti.formal.psdbg.parser.NotWelldefinedException;
import edu.kit.iti.formal.psdbg.parser.Visitor;
import edu.kit.iti.formal.psdbg.parser.types.Type;
import edu.kit.iti.formal.psdbg.parser.types.TypeFacade;
import lombok.Data;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
@Data
public class TermLiteral extends Literal {
    private final String text;

    public TermLiteral(String text) {
        if (text.charAt(0) == '`')
            text = text.substring(1);
        if (text.charAt(text.length() - 1) == '`') //remove last backtick
            text = text.substring(0, text.length() - 1);
        if (text.charAt(0) == '`')
            text = text.substring(0, text.length() - 2);
        this.text = text;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean hasMatchExpression() {
        return false;
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public TermLiteral copy() {
        return new TermLiteral(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type getType(Signature signature) throws NotWelldefinedException {
        return TypeFacade.ANY_TERM;
    }

}
