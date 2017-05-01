package edu.kit.formal.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.NotWelldefinedException;
import edu.kit.formal.proofscriptparser.Visitor;
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

        checkType(operator.type()[0],left,signature);
        checkType(operator.type()[1],right,signature);

        return operator.returnType();
    }

    @Override
    public int getPrecedence() {
        return operator.precedence();
    }
}
