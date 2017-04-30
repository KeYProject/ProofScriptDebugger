package edu.kit.formatl.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formatl.proofscriptparser.Visitor;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
public class CallStatement extends Statement<ScriptLanguageParser.ScriptCommandContext> {
    private String command;
    private Parameters parameters = new Parameters();

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

    public CallStatement setParameters(Parameters parameters) {
        this.parameters = parameters;
        return this;
    }

    public Parameters getParameters() {
        return parameters;
    }

}
