package edu.kit.formal.proofscriptparser;

import edu.kit.formal.proofscriptparser.ast.*;

/**
 * {@link ASTTraversal} provides a visitor with a a default traversal of the given AST.
 *
 * @author Alexander Weigl
 * @version 1 (29.04.17)
 */
public class ASTTraversal<T> implements Visitor<T> {
    @Override public T visit(ProofScript proofScript) {
        proofScript.getBody().accept(this);
        return null;
    }

    @Override public T visit(AssignmentStatement assign) {
        assign.getLhs().accept(this);
        return null;
    }

    @Override public T visit(BinaryExpression e) {
        e.getLeft().accept(this);
        e.getRight().accept(this);
        return null;
    }

    @Override public T visit(MatchExpression match) {
        if (match.getTerm() != null)
            match.getTerm().accept(this);

        return null;
    }

    @Override public T visit(TermLiteral term) {
        term.accept(this);
        return null;
    }

    @Override public T visit(StringLiteral string) {
        return null;
    }

    @Override public T visit(Variable variable) {
        return null;
    }

    @Override public T visit(BooleanLiteral bool) {
        return null;
    }

    @Override public T visit(Statements statements) {
        for (Statement statement : statements) {
            statement.accept(this);
        }
        return null;
    }

    @Override public T visit(IntegerLiteral integer) {
        return null;
    }

    @Override public T visit(CasesStatement casesStatement) {
        for (CaseStatement c : casesStatement.getCases()) {
            c.accept(this);
        }
        return null;
    }

    @Override public T visit(CaseStatement caseStatement) {
        caseStatement.getGuard().accept(this);
        caseStatement.getBody().accept(this);
        return null;
    }

    @Override public T visit(CallStatement call) {
        for (Expression e : call.getParameters().values()) {
            e.accept(this);
        }
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

    @Override public T visit(UnaryExpression e) {
        return null;
    }
}
