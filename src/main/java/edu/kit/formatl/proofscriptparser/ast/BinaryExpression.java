package edu.kit.formatl.proofscriptparser.ast;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
public class BinaryExpression extends Expression<ParserRuleContext> {
    private Expression left,right;
    private Operator operator;

    public BinaryExpression setLeft(Expression left) {
        this.left = left;
        return this;
    }

    public BinaryExpression setRight(Expression right) {
        this.right = right;
        return this;
    }

    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public ASTNode<ParserRuleContext> clone() {
        return null;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Operator getOperator() {
        return operator;
    }
}
