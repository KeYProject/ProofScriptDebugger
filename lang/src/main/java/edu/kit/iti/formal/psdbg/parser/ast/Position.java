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



import lombok.*;
import org.antlr.v4.runtime.Token;

/**
 * @author Alexander Weigl
 */
@Data @Value @RequiredArgsConstructor public class Position implements Copyable<Position> {
    private final int lineNumber;
    private final int charInLine;

    public Position() {
        this(-1, -1);
    }

    @Override public Position copy() {
        return new Position(lineNumber, charInLine);
    }

    public static Position from(Token token) {
        return new Position(token.getLine(), token.getCharPositionInLine());
    }
}
