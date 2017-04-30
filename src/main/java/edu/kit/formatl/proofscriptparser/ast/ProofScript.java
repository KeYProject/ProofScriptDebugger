package edu.kit.formatl.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formatl.proofscriptparser.Visitor;

/**
 * @author Alexander Weigl
 * @version 1 (27.04.17)
 */
public class ProofScript extends ASTNode<ScriptLanguageParser.StartContext> {
    private String name = "_";
    private Statements body = new Statements();
    private Signature signature = new Signature();

    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public ASTNode<ScriptLanguageParser.StartContext> clone() {
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Statements getBody() {
        return body;
    }

    public void setBody(Statements body) {
        this.body = body;
    }

    public Signature getSignature() {
        return signature;
    }

    public void setSignature(Signature parameters) {
        this.signature = parameters;
    }

    public String toPrettyString() {
        return String.format("script %s (%s) { %s }", name, signature, body);
    }

    @Override public String toString() {
        return "ProofScript{" + "name='" + name + '\'' + ", body=" + body + ", signature=" + signature + '}';
    }
}
