package edu.kit.formatl.proofscriptparser;

import edu.kit.formatl.proofscriptparser.ast.*;

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

    T visit(CasesStatement cases);

    T visit(CaseStatement case_);

    T visit(ScriptCallStatement call);

    T visit(TheOnlyStatement theOnly);

    T visit(ForeachStatement foreach);

    T visit(RepeatStatement repeatStatement);

    T visit(Signature signature);

    T visit(Parameters parameters);

    T visit(UnaryExpression unaryExpression);

    T visit(VariableDeclaration variableDeclaration);
}
