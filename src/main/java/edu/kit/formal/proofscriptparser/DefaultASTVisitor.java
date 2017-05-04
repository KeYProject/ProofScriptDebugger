package edu.kit.formal.proofscriptparser;

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



import edu.kit.formal.proofscriptparser.ast.*;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
public class DefaultASTVisitor<T> implements Visitor<T> {
    @Override public T visit(ProofScript proofScript) {
        return null;
    }

    @Override public T visit(AssignmentStatement assignmentStatement) {
        return null;
    }

    @Override public T visit(BinaryExpression binaryExpression) {
        return null;
    }

    @Override public T visit(MatchExpression matchExpression) {
        return null;
    }

    @Override public T visit(TermLiteral termLiteral) {
        return null;
    }

    @Override public T visit(StringLiteral stringLiteral) {
        return null;
    }

    @Override public T visit(Variable variable) {
        return null;
    }

    @Override public T visit(BooleanLiteral booleanLiteral) {
        return null;
    }

    @Override public T visit(Statements statements) {
        return null;
    }

    @Override public T visit(IntegerLiteral integer) {
        return null;
    }

    @Override public T visit(CasesStatement casesStatement) {
        return null;
    }

    @Override public T visit(CaseStatement caseStatement) {
        return null;
    }

    @Override public T visit(CallStatement call) {
        return null;
    }

    @Override public T visit(TheOnlyStatement theOnly) {
        return null;
    }

    @Override public T visit(ForeachStatement foreach) {
        return null;
    }

    @Override public T visit(RepeatStatement repeatStatement) {
        return null;
    }

    @Override public T visit(Signature signature) {
        return null;
    }

    @Override public T visit(Parameters parameters) {
        return null;
    }

    @Override public T visit(UnaryExpression unaryExpression) {
        return null;
    }
}

