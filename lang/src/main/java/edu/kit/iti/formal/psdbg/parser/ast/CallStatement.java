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
import edu.kit.iti.formal.psdbg.parser.Visitor;
import lombok.*;

/**
 * A call to a subroutine or atomic function.
 *
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CallStatement extends Statement<ScriptLanguageParser.ScriptCommandContext> {
    /**
     * The name of the command.
     */
    @NonNull
    private String command = "";

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

    @Override
    public ASTNode[] getChildren() {
        return new ASTNode[]{getParameters()};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CallStatement copy() {
        CallStatement s = new CallStatement(command, parameters.copy());
        s.setRuleContext(this.getRuleContext());
        return s;
    }

    @Override
    public boolean eq(ASTNode o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CallStatement that = (CallStatement) o;

        if (!getCommand().equals(that.getCommand())) return false;
        return getParameters().equals(that.getParameters());
    }
}
