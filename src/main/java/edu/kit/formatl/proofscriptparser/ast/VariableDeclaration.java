package edu.kit.formatl.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formatl.proofscriptparser.Visitor;

/**
 * Created by sarah on 4/30/17.
 */
public class VariableDeclaration extends ASTNode<ScriptLanguageParser.VarDeclContext> {
    private Variable identifier;
    private Type type;

    public VariableDeclaration setType(Type type){
        this.type = type;
        return this;
    }

    public VariableDeclaration setIdentifier(Variable id){
        this.identifier = id;
        return this;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public ASTNode<ScriptLanguageParser.VarDeclContext> clone() {
        return null;
    }
}
