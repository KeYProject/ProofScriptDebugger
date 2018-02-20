package edu.kit.iti.formal.psdbg.parser.ast;

import edu.kit.iti.formal.psdbg.parser.NotWelldefinedException;
import edu.kit.iti.formal.psdbg.parser.ScriptLanguageParser;
import edu.kit.iti.formal.psdbg.parser.Visitor;
import edu.kit.iti.formal.psdbg.parser.types.Type;
import lombok.Getter;
import lombok.Setter;

public class NamespaceSetExpression extends Expression<ScriptLanguageParser.NamespacesetContext>{

    @Getter @Setter
    private Expression expression;

    @Getter @Setter
    private Signature signature = new Signature();

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean hasMatchExpression() {
        return expression.hasMatchExpression();
    }

    @Override
    public int getPrecedence() {
        return 5;
    }

    @Override
    public NamespaceSetExpression copy() {
        NamespaceSetExpression nse = new NamespaceSetExpression();
        nse.expression = expression.copy();
        nse.signature = signature.copy();
        return nse;
    }

    @Override
    public Type getType(Signature signature) throws NotWelldefinedException {
        return expression.getType(signature);
    }
}
