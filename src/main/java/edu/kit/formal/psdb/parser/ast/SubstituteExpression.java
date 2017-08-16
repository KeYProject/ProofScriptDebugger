package edu.kit.formal.psdb.parser.ast;

import edu.kit.formal.psdb.parser.NotWelldefinedException;
import edu.kit.formal.psdb.parser.ScriptLanguageParser;
import edu.kit.formal.psdb.parser.Visitor;
import edu.kit.formal.psdb.parser.types.Type;
import edu.kit.formal.psdb.parser.types.TypeFacade;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Alexander Weigl
 * @version 1 (15.08.17)
 */
@Data
public class SubstituteExpression extends Expression<ScriptLanguageParser.ExprSubstContext> {
    private Expression sub;
    private Map<String, Expression> substitution = new LinkedHashMap<>();

    @Override
    public boolean hasMatchExpression() {
        return sub.hasMatchExpression();
    }

    @Override
    public int getPrecedence() {
        return Operator.SUBST.precedence();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Expression copy() {
        SubstituteExpression se = new SubstituteExpression();
        se.sub = sub.copy();
        se.substitution = new LinkedHashMap<>(substitution);
        return se;
    }

    @Override
    public Type getType(Signature signature) throws NotWelldefinedException {
        Type t = sub.getType(signature);
        if(!TypeFacade.isTerm(t)){
            throw new NotWelldefinedException("Term<?> expected, got " + t.symbol(), this);
        }
        return TypeFacade.ANY_TERM;
    }
}
