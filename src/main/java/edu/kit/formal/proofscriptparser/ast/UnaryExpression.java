package edu.kit.formal.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.NotWelldefinedException;
import edu.kit.formal.proofscriptparser.Visitor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * @author Alexander Weigl
 * @author Sarah Grebing
 * @version 1 (30.04.17)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnaryExpression extends Expression<ParserRuleContext> {
    @NonNull
    private Operator operator;
    @NonNull
    private Expression expression;

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public UnaryExpression clone() {
        UnaryExpression u = new UnaryExpression(operator, expression.clone());
        return u;
    }

    @Override
    public Type getType(Signature signature) throws NotWelldefinedException {
        if(operator.arity()!=1)
            throw new NotWelldefinedException("Arity mismatch!", this);
        operator.type()[0].equals(expression.getType(signature));
        return operator.returnType();
    }

    @Override
    public int getPrecedence() {
        return Operator.NOT.precedence();//a placeholder, because MINUS is used as binary operator,too
    }
}
