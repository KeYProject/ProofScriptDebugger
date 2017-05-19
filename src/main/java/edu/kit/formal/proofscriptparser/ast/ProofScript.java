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



import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formal.proofscriptparser.Visitor;
import lombok.Data;
import lombok.NonNull;

/**
 * @author Alexander Weigl
 * @version 1 (27.04.17)
 */
@Data
public class ProofScript extends ASTNode<ScriptLanguageParser.ScriptContext> {
    @NonNull private String name = "_";
    private Signature signature = new Signature();
    private Statements body = new Statements();

    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public ProofScript copy() {
        ProofScript ps = new ProofScript();
        ps.setName(getName());
        ps.setBody(body.copy());
        ps.setSignature(signature.copy());
        return ps;
    }


}
