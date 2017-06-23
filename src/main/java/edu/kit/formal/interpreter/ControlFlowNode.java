package edu.kit.formal.interpreter;

import edu.kit.formal.proofscriptparser.ast.ASTNode;

/**
 * ControlFlowNode for ControlFlowGraph to look up step-edges for teh debugger.
 */
public class ControlFlowNode {

    ASTNode scriptstmt;

    public ControlFlowNode(ASTNode node) {
        this.setScriptstmt(node);
    }


    public ASTNode getScriptstmt() {
        return scriptstmt;
    }

    public void setScriptstmt(ASTNode scriptstmt) {
        this.scriptstmt = scriptstmt;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Node {");
        if (scriptstmt != null) {
            sb.append(scriptstmt.toString());
        } else {
            sb.append("No Stmt");
        }
        sb.append("}");
        return sb.toString();
    }

}
