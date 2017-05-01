package edu.kit.formal.proofscriptparser;

import edu.kit.formal.proofscriptparser.ast.*;

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

    T visit(CallStatement call);

    T visit(TheOnlyStatement theOnly);

    T visit(ForeachStatement foreach);

    T visit(RepeatStatement repeat);

    T visit(Signature signature);

    T visit(Parameters parameters);

    T visit(UnaryExpression e);
}
