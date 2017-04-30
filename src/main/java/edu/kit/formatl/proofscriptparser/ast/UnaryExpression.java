package edu.kit.formatl.proofscriptparser.ast;

import edu.kit.formatl.proofscriptparser.Visitor;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by S.Grebing
 */
public class UnaryExpression extends Expression<ParserRuleContext>{
    private Operator operator;
    private Expression expression;


    public UnaryExpression setOperator(Operator op){
        this.operator = op;
        return this;
    }
    public Operator getOperator() {
        return operator;
    }

    public UnaryExpression setExpression(Expression expr){
        this.expression = expr;
        return this;
    }
    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public ASTNode<ParserRuleContext> clone() {
        return null;
    }
}
