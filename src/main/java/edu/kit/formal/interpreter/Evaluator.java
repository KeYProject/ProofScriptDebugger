package edu.kit.formal.interpreter;

import edu.kit.formal.proofscriptparser.DefaultASTVisitor;
import edu.kit.formal.proofscriptparser.ast.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Class handling evaluation of expressions (visitor for expressions)
 *
 * @author S.Grebing
 */
public class Evaluator extends DefaultASTVisitor<Value> {
    private GoalNode currentState;
    private List<VariableAssignment> matchedVariables = new ArrayList<>();
    @Getter
    @Setter
    private MatcherApi matcher;

    public Evaluator(GoalNode s) {
        this.currentState = s;
    }

    /**
     * Evaluation of an expression.
     *
     * @param truth
     * @return
     */
    public Value eval(Expression truth) {
        return (Value) truth.accept(this);
    }

    @Override
    public Value visit(BinaryExpression e) {
        Value v1 = (Value) e.getLeft().accept(this);
        Value v2 = (Value) e.getRight().accept(this);
        Operator op = e.getOperator();
        return op.evaluate(v1, v2);
    }

    /**
     * TODO Connect with KeY
     *
     * @param match
     * @return
     */
    @Override
    public Value visit(MatchExpression match) {
        Value pattern = (Value) match.getPattern().accept(this);

        List<VariableAssignment> va = null;
        if (pattern.getType() == Type.STRING) {
            va = matcher.matchLabel(currentState, (String) pattern.getData());
        } else if (pattern.getType() == Type.TERM) {
            va = matcher.matchSeq(currentState, (String) pattern.getData());
        }

        return va != null && va.size() > 0 ? Value.TRUE : Value.FALSE;
    }

    /**
     * TODO Connect with KeY
     *
     * @param term
     * @return
     */
    @Override
    public Value visit(TermLiteral term) {
        return null;
    }

    @Override
    public Value visit(StringLiteral string) {
        return Value.from(string);
    }

    @Override
    public Value visit(Variable variable) {
        //get variable value
        String id = variable.getIdentifier();
        Value v = currentState.lookupVarValue(id);
        if (v != null) {
            return v;
        } else {
            throw new RuntimeException("Variable " + variable + " was not initialized");
        }

    }

    @Override
    public Value visit(BooleanLiteral bool) {
        return bool.isValue() ? Value.TRUE : Value.FALSE;
    }


    @Override
    public Value visit(IntegerLiteral integer) {
        return Value.from(integer);
    }

    @Override
    public Value visit(UnaryExpression e) {
        Operator op = e.getOperator();
        Expression expr = e.getExpression();
        Value exValue = (Value) expr.accept(this);
        return op.evaluate(exValue);
    }


}
