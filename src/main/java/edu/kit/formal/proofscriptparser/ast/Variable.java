package edu.kit.formal.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.NotWelldefinedException;
import edu.kit.formal.proofscriptparser.Visitor;
import lombok.Data;
import org.antlr.v4.runtime.Token;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
@Data
public class Variable extends Literal {
    private final String identifier;

    public Variable(String identifier) {
        this.identifier = identifier;
    }

    public Variable(Token variable) {
        this(variable.getText());
        setToken(variable);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Variable clone() {
        Variable v = new Variable(identifier);
        v.token = token;
        return v;
    }

    /**
     * @param signature
     * @return
     * @throws NotWelldefinedException
     */
    @Override
    public Type getType(Signature signature) throws NotWelldefinedException {
        if (signature.containsKey(this))
            return signature.get(this);
        throw new NotWelldefinedException(toString() + "not defined in signature.", this);
    }

    @Override
    public String toString() {
        return "Variable{" + "identifier='" + identifier + '\'' + '}';
    }

    public String getIdentifier() {
        return identifier;
    }
}
