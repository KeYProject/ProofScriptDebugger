package edu.kit.iti.formal.psdbg.parser;

import edu.kit.iti.formal.psdbg.parser.ast.*;

public class ASTDiff implements Visitor<ASTNode> {
    private ASTNode other;

    private ProofScript newScript;

    @Override
    public ProofScript visit(ProofScript proofScript) {
        newScript = new ProofScript();
        newScript.setName(proofScript.getName());
        newScript.setSignature(proofScript.getSignature());

        other = ((ProofScript) other).getBody();
        newScript.setBody(visit(newScript.getBody()));
        return newScript;
    }

    @Override
    public ASTNode visit(AssignmentStatement assign) {
        return null;
    }

    @Override
    public ASTNode visit(BinaryExpression e) {
        return null;
    }

    @Override
    public ASTNode visit(MatchExpression match) {
        return null;
    }

    @Override
    public ASTNode visit(TermLiteral term) {
        return null;
    }

    @Override
    public ASTNode visit(StringLiteral string) {
        return null;
    }

    @Override
    public ASTNode visit(Variable variable) {
        return null;
    }

    @Override
    public ASTNode visit(BooleanLiteral bool) {
        return null;
    }

    @Override
    public Statements visit(Statements statements) {
        Statements s = new Statements();
        Statements other = (Statements) this.other;
        assert statements.size() <= other.size();

        int i = 0;
        for (; i < statements.size(); i++) {
            if (statements.get(i).eq(other.get(i))) {
                break;
            }
        }

        for (int j = i; j < other.size(); j++) {
            s.add(other.get(i));
        }

        return s;
    }

    @Override
    public ASTNode visit(IntegerLiteral integer) {
        return null;
    }

    @Override
    public ASTNode visit(CasesStatement cases) {
        return null;
    }

    @Override
    public ASTNode visit(DefaultCaseStatement defCase) {
        return null;
    }

    @Override
    public ASTNode visit(CallStatement call) {
        return null;
    }

    @Override
    public ASTNode visit(TheOnlyStatement theOnly) {
        return null;
    }

    @Override
    public ASTNode visit(ForeachStatement foreach) {
        return null;
    }

    @Override
    public ASTNode visit(RepeatStatement repeat) {
        return null;
    }

    @Override
    public ASTNode visit(Signature signature) {
        return null;
    }

    @Override
    public ASTNode visit(Parameters parameters) {
        return null;
    }

    @Override
    public ASTNode visit(UnaryExpression e) {
        return null;
    }

    @Override
    public ASTNode visit(TryCase TryCase) {
        return null;
    }

    @Override
    public ASTNode visit(GuardedCaseStatement guardedCaseStatement) {
        return null;
    }

    @Override
    public ASTNode visit(CaseStatement caseStatement) {
        return null;
    }

    @Override
    public ASTNode visit(SubstituteExpression subst) {
        return null;
    }

    @Override
    public ASTNode visit(ClosesCase closesCase) {
        return null;
    }

    public ProofScript diff(ProofScript old, ProofScript rev) {
        other = rev;
        return (ProofScript) visit(old);
    }
}
