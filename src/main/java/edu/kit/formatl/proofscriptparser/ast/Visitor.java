package edu.kit.formatl.proofscriptparser.ast;

/**
 * @author Alexander Weigl
 * @version 1 (27.04.17)
 */
public interface Visitor<T> {
    T visit(ProofScript proofScript);

    T visit(AssignmentStatement assign);

    T visit(BinaryExpression e);

    T visit(MatchExpression match);

    T visit(TermLiteral term);

    T visit(StringLiteral string);

    T visit(Variable variable);

    T visit(BooleanLiteral bool);

    T visit(Statements statements);

    T visit(IntegerLiteral integer);

    T visit(CasesStatement casesStatement);

    T visit(CaseStatement caseStatement);

    T visit(ScriptCallStatement call);
}
