package edu.kit.iti.formal.psdbg.gui.controls;

import com.google.common.collect.Streams;
import edu.kit.iti.formal.psdbg.parser.ASTTraversal;
import edu.kit.iti.formal.psdbg.parser.ast.*;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author Alexander Weigl
 * @version 1 (12.03.18)
 */
@RequiredArgsConstructor
public class FindNearestASTNode implements ASTTraversal<ASTNode> {
    private final int pos;

    @Override
    public ASTNode visit(ProofScript proofScript) {
        return childOrMe(proofScript, proofScript.getSignature(), proofScript.getBody());
    }

    public ASTNode childOrMe(ASTNode me, Stream<? extends ASTNode> nodes) {
        // range check
        if (me == null) {
            return null;
        }

        try {
            int start = me.getStartPosition().getOffset();
            int stop = me.getEndPosition().getOffset();
            if (start <= pos && pos <= stop) {
                ASTNode a = null;
                try {
                    a = oneOf(nodes);
                } catch (NullPointerException e1) {
                    e1.printStackTrace();
                }
                return a != null ? a : me;
            }
        } catch (NullPointerException e) {
            System.err.println("No rule context set for instance " + me);
            e.printStackTrace();
        }
        return null;
    }

    private ASTNode childOrMe(ASTNode me, Collection<? extends ASTNode> children) {
        return childOrMe(me, children.stream());
    }

    private ASTNode childOrMe(ASTNode me, ASTNode... nodes) {
        return childOrMe(me, Arrays.stream(nodes));
    }

    @Override
    public ASTNode visit(AssignmentStatement assign) {
        return childOrMe(assign, assign.getLhs(), assign.getRhs());
    }

    @Override
    public ASTNode visit(BinaryExpression e) {
        return childOrMe(e, e.getLeft(), e.getRight());
    }

    @Override
    public ASTNode visit(MatchExpression match) {
        return childOrMe(match, match.getDerivableTerm(), match.getPattern());
    }

    @Override
    public ASTNode visit(TermLiteral term) {
        return childOrMe(term);
    }

    @Override
    public ASTNode visit(StringLiteral string) {
        return childOrMe(string);
    }

    @Override
    public ASTNode visit(Variable variable) {
        return childOrMe(variable);
    }

    @Override
    public ASTNode visit(BooleanLiteral bool) {
        return childOrMe(bool);
    }

    @Override
    public ASTNode visit(Statements statements) {
        return childOrMe(statements, statements.stream());
    }

    @Override
    public ASTNode visit(IntegerLiteral integer) {
        return childOrMe(integer);
    }

    @Override
    public ASTNode visit(CasesStatement casesStatement) {
        return childOrMe(casesStatement,
                Streams.concat(casesStatement.getCases().stream(),
                        Stream.of(casesStatement.getDefCaseStmt())
                ));
    }

    @Override
    public ASTNode visit(CaseStatement caseStatement) {
        return childOrMe(caseStatement, caseStatement.getBody());
    }

    @Override
    public ASTNode visit(CallStatement call) {
        return childOrMe(call, call.getParameters());
    }

    @Override
    public ASTNode visit(TheOnlyStatement theOnly) {
        return childOrMe(theOnly, theOnly.getBody());
    }

    @Override
    public ASTNode visit(ForeachStatement foreach) {
        return childOrMe(foreach, foreach.getBody());
    }

    @Override
    public ASTNode visit(RepeatStatement repeatStatement) {
        return childOrMe(repeatStatement, repeatStatement.getBody());
    }

    @Override
    public ASTNode visit(Signature signature) {
        return childOrMe(signature);
    }

    @Override
    public ASTNode visit(Parameters parameters) {
        return childOrMe(parameters, parameters.values());
    }

    @Override
    public ASTNode visit(UnaryExpression e) {
        return childOrMe(e, e.getExpression());
    }

    @Override
    public ASTNode visit(TryCase tryCase) {
        return childOrMe(tryCase, tryCase.getBody());
    }

    @Override
    public ASTNode visit(GuardedCaseStatement guardedCaseStatement) {
        return childOrMe(guardedCaseStatement,
                guardedCaseStatement.getGuard(),
                guardedCaseStatement.getBody());
    }

    @Override
    public ASTNode visit(SubstituteExpression subst) {
        return childOrMe(subst);
    }

    @Override
    public ASTNode visit(ClosesCase closesCase) {
        return null;
    }

    @Override
    public ASTNode visit(DefaultCaseStatement defCase) {
        return null;
    }

    @Override
    public ASTNode visit(FunctionCall func) {
        return null;
    }

    @Override
    public ASTNode visit(WhileStatement ws) {
        return null;
    }

    @Override
    public ASTNode visit(IfStatement is) {
        return null;
    }

    @Override
    public ASTNode visit(StrictBlock strictBlock) {
        return null;
    }

    @Override
    public ASTNode visit(RelaxBlock relaxBlock) {
        return null;
    }

    @Override
    public ASTNode visit(NamespaceSetExpression nss) {
        return null;
    }

    public Optional<ASTNode> find(List<ProofScript> ast) {
        return ast.stream().map(a -> a.accept(this))
                .filter(Objects::nonNull)
                .findFirst();
    }
}
