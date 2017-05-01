package edu.kit.formatl.proofscriptparser.ast;

import edu.kit.formatl.proofscriptparser.NotWelldefinedException;
import edu.kit.formatl.proofscriptparser.Visitor;
import lombok.Data;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
@Data
public class BinaryExpression extends Expression<ParserRuleContext> {
    private Expression left, right;
    private Operator operator;

    public BinaryExpression() {
    }

    public BinaryExpression(Expression left, Operator operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public BinaryExpression clone() {
        BinaryExpression be = new BinaryExpression(left.clone(), operator, right.clone());
        return be;
    }

    @Override
    public Type getType(Signature signature) throws NotWelldefinedException {
        if (operator.arity() != 2)
            throw new NotWelldefinedException("Arity mismatch", this);

        operator.type()[0].equals(left.getType(signature));
        operator.type()[1].equals(right.getType(signature));

        return operator.returnType();
    }

    @Override
    public int getPrecedence() {
        return operator.precedence();
    }
}
