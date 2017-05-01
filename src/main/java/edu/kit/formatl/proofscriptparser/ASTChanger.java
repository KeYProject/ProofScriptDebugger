package edu.kit.formatl.proofscriptparser;

import edu.kit.formatl.proofscriptparser.ast.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * {@link ASTChanger} provides a visitor with for replacing or substiting nodes (in situ).
 *
 * @author Alexander Weigl
 * @version 1 (29.04.17)
 */
public class ASTChanger extends DefaultASTVisitor<ASTNode> {
    @Override public ProofScript visit(ProofScript proofScript) {
        proofScript.setBody((Statements) proofScript.getBody().accept(this));
        return proofScript;
    }

    @Override public AssignmentStatement visit(AssignmentStatement assign) {
        assign.setRhs((Variable) assign.getRhs().accept(this));
        assign.setLhs((Expression) assign.getLhs().accept(this));
        return assign;
    }

    @Override public Expression visit(BinaryExpression e) {
        e.setLeft((Expression) e.getLeft().accept(this));
        e.setRight((Expression) e.getRight().accept(this));
        return e;
    }

    @Override public MatchExpression visit(MatchExpression match) {
        if (match.getTerm() != null)
            match.setTerm((TermLiteral) match.getTerm().accept(this));
        return match;
    }

    @Override public TermLiteral visit(TermLiteral term) {
        return term;
    }

    @Override public StringLiteral visit(StringLiteral string) {
        return string;
    }

    @Override public Variable visit(Variable variable) {
        return variable;
    }

    @Override public BooleanLiteral visit(BooleanLiteral bool) {
        return bool;
    }

    @Override public Statements visit(Statements statements) {
        ArrayList copy = new ArrayList<>(statements.size());
        for (Statement statement : statements) {
            copy.add(statement.accept(this));
        }
        statements.clear();
        statements.addAll(copy);
        return statements;
    }

    @Override public IntegerLiteral visit(IntegerLiteral integer) {
        return integer;
    }

    @Override public CasesStatement visit(CasesStatement casesStatement) {
        for (CaseStatement c : casesStatement.getCases()) {
            c.accept(this);
        }
        return casesStatement;
    }

    @Override public CaseStatement visit(CaseStatement caseStatement) {
        caseStatement.getGuard().accept(this);
        caseStatement.getBody().accept(this);
        return caseStatement;
    }

    @Override public CallStatement visit(CallStatement call) {
        call.setParameters((Parameters) call.getParameters().accept(this));
        return call;
    }

    @Override public ASTNode visit(TheOnlyStatement theOnly) {
        theOnly.setBody((Statements) theOnly.getBody().accept(this));
        return theOnly;
    }

    @Override public ASTNode visit(ForeachStatement foreach) {
        foreach.setBody((Statements) foreach.getBody().accept(this));
        return foreach;
    }

    @Override public ASTNode visit(RepeatStatement repeat) {
        repeat.setBody((Statements) repeat.getBody().accept(this));
        return repeat;
    }

    @Override public ASTNode visit(Signature signature) {
        Set<Map.Entry<Variable, Type>> entries = signature.entrySet();
        signature.clear();
        for (Map.Entry<Variable, Type> e : entries) {
            signature.put((Variable) e.getKey().accept(this), e.getValue());
        }
        return signature;
    }

    @Override public ASTNode visit(Parameters parameters) {
        Set<Map.Entry<Variable, Expression>> entries = parameters.entrySet();
        parameters.clear();
        for (Map.Entry<Variable, Expression> e : entries) {
            parameters.put((Variable) e.getKey().accept(this), e.getValue());
        }
        return parameters;
    }

    @Override public ASTNode visit(UnaryExpression e) {
        e.setExpression((Expression) e.getExpression().accept(this));
        return e;
    }
}
