package edu.kit.formatl.proofscriptparser.ast;

import edu.kit.formatl.proofscriptparser.Visitor;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * @author Alexander Weigl
 * @author Sarah Grebing
 * @version 1 (30.04.17)
 */
public class UnaryExpression extends Expression<ParserRuleContext> {
    private Expression expression;
    private Operator operator;

    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public ASTNode<ParserRuleContext> clone() {
        return null;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Operator getOperator() {
        return operator;
    }

    @Override public int getPrecedence() {
        return Operator.NOT.precedence();//a placeholder, because MINUS is used as binary operator,too
    }
}
