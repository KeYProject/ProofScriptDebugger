package edu.kit.iti.formal.psdbg.parser.ast;


import edu.kit.iti.formal.psdbg.parser.Visitor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DerivableCase extends CaseStatement{

    private Expression expression;

    public DerivableCase(Statements copy, Expression copy1) {
        super(copy);
        this.expression=copy1;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public CaseStatement copy() {
        DerivableCase dc = new DerivableCase(body.copy(), expression.copy());
        dc.setRuleContext(this.getRuleContext());
        return dc;
    }
}
