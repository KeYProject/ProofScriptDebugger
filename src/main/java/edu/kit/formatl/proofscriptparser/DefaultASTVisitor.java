package edu.kit.formatl.proofscriptparser;

import edu.kit.formatl.proofscriptparser.Visitor;
import edu.kit.formatl.proofscriptparser.ast.*;

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
