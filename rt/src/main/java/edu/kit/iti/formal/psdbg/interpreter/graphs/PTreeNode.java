package edu.kit.iti.formal.psdbg.interpreter.graphs;

import edu.kit.iti.formal.psdbg.interpreter.data.InterpreterExtendedState;
import edu.kit.iti.formal.psdbg.interpreter.data.State;
import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;

import java.util.LinkedList;

/**
 * Inner class representing nodes in the stategraph graph
 * A node contains a reference to the ASTNode and a reference to the corresponding interpreter state if this state is already interpreted, null otherwise.
 */
public class PTreeNode<T> {
    /**
     * State  after statement
     */
    private State<T> state;

    private InterpreterExtendedState<T> extendedState = new InterpreterExtendedState<>();

    /**
     * Statement
     */
    private ASTNode scriptstmt;


    /**
     * Call context
     */
    private LinkedList<ASTNode> context = new LinkedList<>();

    private boolean root;

    PTreeNode(ASTNode node) {
        this.setScriptstmt(node);
    }

    public State<T> getState() {
        return state;
    }

    public void setState(State<T> state) {
        this.state = state;
    }

    public ASTNode getScriptstmt() {
        return scriptstmt;
    }

    private void setScriptstmt(ASTNode scriptstmt) {
        this.scriptstmt = scriptstmt;
    }

    public LinkedList<ASTNode> getContext() {
        return context;
    }

    public void setContext(LinkedList<ASTNode> context) {
        this.context = context;
    }

    InterpreterExtendedState<T> getExtendedState() {
        return extendedState;
    }

    void setExtendedState(InterpreterExtendedState<T> extendedState) {
        this.extendedState = extendedState;
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

    public boolean hasState() {
        return state != null;
    }

    public String extendedStateToString() {
        return this.extendedState.toString();
    }
}
