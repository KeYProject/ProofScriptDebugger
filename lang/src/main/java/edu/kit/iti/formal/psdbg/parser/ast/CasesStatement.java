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
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
@Data
public class CasesStatement extends Statement<ScriptLanguageParser.CasesStmtContext> {
    @NonNull private final List<CaseStatement> cases = new ArrayList<>();
    // @NonNull private Statements defaultCase = new Statements();
    @NonNull
    private DefaultCaseStatement defCaseStmt = new DefaultCaseStatement();

    /**
     * {@inheritDoc}
     */
    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override public CasesStatement copy() {
        CasesStatement c = new CasesStatement();
        cases.forEach(caseStatement -> c.cases.add(caseStatement.copy()));
        //  if (defaultCase != null)
        //      c.defaultCase = defaultCase.copy();
        if (defCaseStmt != null)
            c.defCaseStmt = defCaseStmt.copy();
        return c;
    }
}
