package edu.kit.formal.psdb.parser.ast;

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



import edu.kit.formal.psdb.parser.ScriptLanguageParser;
import edu.kit.formal.psdb.parser.Visitor;
import lombok.*;

/**
 * A call to a subroutine or atomic function.
 *
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class CallStatement extends Statement<ScriptLanguageParser.ScriptCommandContext> {
    /**
     * The name of the command.
     */
    @NonNull
    private String command;

    /**
     * The list of parameters.
     */
    private Parameters parameters = new Parameters();

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CallStatement copy() {
        return new CallStatement(command, parameters.copy());
    }
}
