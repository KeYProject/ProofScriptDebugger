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


import edu.kit.iti.formal.psdbg.parser.ScriptLanguageParser;
import lombok.*;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * @author Alexander Weigl
 * @version 1 (29.04.17)
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class ConditionalBlock
        extends Statement<ScriptLanguageParser.ConditionalBlockContext> {
    @Getter
    @Setter
    @NonNull protected Statements body = new Statements();

    @Getter
    @Setter
    @NonNull protected Expression condition = BooleanLiteral.TRUE;

    @Override
    public boolean eq(ASTNode o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ConditionalBlock that = (ConditionalBlock) o;

        return getBody() != null ? getBody().eq(that.getBody()) : that.getBody() == null;
    }

}
