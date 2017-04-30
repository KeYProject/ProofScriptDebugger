package edu.kit.formatl.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formatl.proofscriptparser.Visitor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
public class ScriptCallStatement extends Statement<ScriptLanguageParser.ScriptCommandContext> {
    private String command;
    private Map<String, Expression> parameters = new LinkedHashMap<>();

    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public ASTNode<ScriptLanguageParser.ScriptCommandContext> clone() {
        return null;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public ScriptCallStatement setParameters(Map<String, Expression> parameters) {
        this.parameters = parameters;
        return this;
    }

    public Map<String, Expression> getParameters() {
        return parameters;
    }

}
