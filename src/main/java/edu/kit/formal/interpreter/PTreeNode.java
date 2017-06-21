package edu.kit.formal.interpreter;

import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.KeyData;
import edu.kit.formal.interpreter.data.State;
import edu.kit.formal.proofscriptparser.ast.ASTNode;

/**
 * Inner class representing nodes in the graph
 * A node contains a reference to the ASTNode and a reference to the corresponding interpreter state if this state is already interpreted, null otherwise.
 */
public class PTreeNode {
    State<GoalNode<KeyData>> state;

    ASTNode scriptstmt;

    public PTreeNode(ASTNode node) {
        this.setScriptstmt(node);
    }

    public State<GoalNode<KeyData>> getState() {
        return state;
    }

    public void setState(State<GoalNode<KeyData>> state) {
        this.state = state;
    }

    public ASTNode getScriptstmt() {
        return scriptstmt;
    }

    public void setScriptstmt(ASTNode scriptstmt) {
        this.scriptstmt = scriptstmt;
    }

    public boolean hasState() {
        return state != null;
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
