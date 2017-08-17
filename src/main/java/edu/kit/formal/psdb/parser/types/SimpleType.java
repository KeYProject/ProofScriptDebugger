package edu.kit.formal.psdb.parser.types;

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


/**
 * Represents the possible types (including scriptVarTypes).
 *
 * Created at 30.04.2017
 * @author Sarah Grebing
 */
public enum SimpleType implements Type {
    STRING("string"), ANY("any"),
    INT("int"), BOOL("bool"), INT_ARRAY("int[]"), OBJECT("object"),
    HEAP("heap"), FIELD("field"), LOCSET("locset"), NULL("null"), FORMULA("formula"), SEQ("Seq");

    private final String symbol;

    SimpleType(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String symbol() {
        return symbol;
    }
}
