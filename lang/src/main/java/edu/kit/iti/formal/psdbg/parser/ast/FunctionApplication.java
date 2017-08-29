package edu.kit.iti.formal.psdbg.parser.ast;

import edu.kit.iti.formal.psdbg.parser.NotWelldefinedException;
import edu.kit.iti.formal.psdbg.parser.ScriptLanguageParser;
import edu.kit.iti.formal.psdbg.parser.Visitor;
import edu.kit.iti.formal.psdbg.parser.types.Type;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (29.08.17)
 */
@Data
public class FunctionApplication
        extends Expression<ScriptLanguageParser.FunctionAppContext> {
    private String name;
    private final List<Expression> args = new ArrayList<>();

    @Override
    public boolean hasMatchExpression() {
        return args.stream()
                .map(Expression::hasMatchExpression)
                .reduce((a, b) -> a | b)
                .orElse(false);
    }

    @Override
    public int getPrecedence() {
        return 2;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Expression copy() {
        FunctionApplication fa = new FunctionApplication();
        fa.name = name;
        args.stream().map(Expression::copy).forEach(fa.args::add);
        return fa;
    }

    @Override
    public Type getType(Signature signature) throws NotWelldefinedException {
        return null;
    }
}
