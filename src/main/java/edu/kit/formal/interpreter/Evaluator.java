package edu.kit.formal.interpreter;

import edu.kit.formal.proofscriptparser.DefaultASTVisitor;
import edu.kit.formal.proofscriptparser.ast.*;

/**
 * Class handling evaluation of expressions (visitor for expressions)
 *
 * @author S.Grebing
 */
public class Evaluator extends DefaultASTVisitor<Value> {
    State currentState;

    public Evaluator(State s) {
        this.currentState = s;
    }


    @Override
    public Value visit(BinaryExpression e) {
        Value v1 = (Value) e.getLeft().accept(this);
        Value v2 = (Value) e.getLeft().accept(this);
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
        return null;
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
        GoalNode n = currentState.getSelectedGoalNode();
        Value v = n.lookupVarValue(id);
        if (v != null) {
            return v;
        } else {
            throw new RuntimeException("Variable " + variable + " was not initialized");
        }

    }

    @Override
    public Value visit(BooleanLiteral bool) {
        return Value.from(bool);
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
