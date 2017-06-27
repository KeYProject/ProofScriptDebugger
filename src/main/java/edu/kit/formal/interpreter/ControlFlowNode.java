package edu.kit.formal.interpreter;

import edu.kit.formal.proofscriptparser.ast.ASTNode;

import java.util.LinkedList;
import java.util.List;

/**
 * ControlFlowNode for ControlFlowGraph to look up step-edges for teh debugger.
 */

public class ControlFlowNode {

    /**
     * Statement node
     */
    private ASTNode scriptstmt;

    /**
     * Call context
     */
    private LinkedList<ASTNode> callCtx = new LinkedList<>();


    public ControlFlowNode(ASTNode node) {
        this.setScriptstmt(node);
    }


    public List<ASTNode> getCallCtx() {
        return callCtx;
    }

    public void setCallCtx(LinkedList<ASTNode> callCtx) {
        this.callCtx = callCtx;
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
            sb.append(scriptstmt.getNodeName().toString() + "@" + scriptstmt.getStartPosition());
        } else {
            sb.append("No Stmt");
        }
        if (callCtx.isEmpty()) {
            sb.append("Empty Context");
        } else {
            sb.append(callCtx.toString());
        }
        sb.append("}");
        return sb.toString();
    }


}
