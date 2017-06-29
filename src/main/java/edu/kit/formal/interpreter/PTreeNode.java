package edu.kit.formal.interpreter;

import edu.kit.formal.interpreter.data.KeyData;
import edu.kit.formal.interpreter.data.State;
import edu.kit.formal.proofscriptparser.ast.ASTNode;

import java.util.LinkedList;

/**
 * Inner class representing nodes in the graph
 * A node contains a reference to the ASTNode and a reference to the corresponding interpreter state if this state is already interpreted, null otherwise.
 */
public class PTreeNode {
    /**
     * State  after statement
     */
    private State<KeyData> state;

    /**
     * Statement
     */
    private ASTNode scriptstmt;


    /**
     * Call context
     */
    private LinkedList<ASTNode> context = new LinkedList<>();

    private boolean root;



    public PTreeNode(ASTNode node) {
        this.setScriptstmt(node);
    }

    public State<KeyData> getState() {
        return state;
    }

    public void setState(State<KeyData> state) {
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

    public LinkedList<ASTNode> getContext() {
        return context;
    }

    public void setContext(LinkedList<ASTNode> context) {
        this.context = context;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Node {");
        if (scriptstmt != null) {
            sb.append(scriptstmt.getNodeName() + "\n");
        } else {
            sb.append("Root Node");
        }
        if (hasState()) {
            sb.append("\nState " + state.getGoals() + "\n");
        } else {
            sb.append("No State yet");
        }
        sb.append("}");
        return sb.toString();
    }


}
