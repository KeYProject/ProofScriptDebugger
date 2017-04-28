package edu.kit.formatl.proofscriptparser.ast;

/**
 * @author Alexander Weigl
 * @version 1 (27.04.17)
 */
public interface Visitor<T> {
    T visit(ProofScript proofScript);

    T visit(AssignmentStatement assignmentStatement);

    T visit(BinaryExpression binaryExpression);

    T visit(MatchExpression matchExpression);

    T visit(TermLiteral termLiteral);

    T visit(StringLiteral stringLiteral);

    T visit(Variable variable);

    T visit(BooleanLiteral booleanLiteral);
}
