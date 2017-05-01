package edu.kit.formal.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formal.proofscriptparser.NotWelldefinedException;
import edu.kit.formal.proofscriptparser.Visitor;
import lombok.Data;

/**
 * A match expression contains an argument and a uses clause.
 *
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
@Data
public class MatchExpression extends Expression<ScriptLanguageParser.MatchPatternContext> {
    private Signature signature;
    private TermLiteral term;
    private Variable variable;

    /**
     * {@inheritDoc}
     */
    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override public MatchExpression clone() {
        MatchExpression me = new MatchExpression();
        if(signature!=null)
            me.signature=signature.clone();
        if(term!=null)
            me.term = term.clone();
        if(variable!=null)
            me.variable = variable.clone();
        return me;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type getType(Signature signature) throws NotWelldefinedException {
        if(term==null && variable==null)
            throw new NotWelldefinedException("Missing parameter", this);
        return Type.BOOL;
    }

    /**
     * {@inheritDoc}
     */
    @Override public int getPrecedence() {
        return Operator.MATCH.precedence();
    }
}
