package edu.kit.formatl.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexander Weigl
 * @version 1 (27.04.17)
 */
public class ProofScript extends ASTNode<ScriptLanguageParser.StartContext> {
    private String name = "_";
    private Statements body = new Statements();
    private Map<String, String> parameters = new HashMap<>();

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

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String toPrettyString() {
        return String.format("script %s (%s) { %s }", name, parameters, body);
    }

    @Override public String toString() {
        return "ProofScript{" + "name='" + name + '\'' + ", body=" + body + ", parameters=" + parameters + '}';
    }
}
