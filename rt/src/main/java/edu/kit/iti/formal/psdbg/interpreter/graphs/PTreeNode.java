package edu.kit.iti.formal.psdbg.interpreter.graphs;

import edu.kit.iti.formal.psdbg.interpreter.data.InterpreterExtendedState;
import edu.kit.iti.formal.psdbg.interpreter.data.State;
import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;
import edu.kit.iti.formal.psdbg.parser.ast.CaseStatement;

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


    private InterpreterExtendedState<T> extendedState;
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

    public State<T> getState() {
        return state;
    }

    public void setState(State<T> state) {
        this.state = state;
    }

    public ASTNode getScriptstmt() {
        return scriptstmt;
    }

    public void setScriptstmt(ASTNode scriptstmt) {
        this.scriptstmt = scriptstmt;
    }

    public LinkedList<ASTNode> getContext() {
        return context;
    }

    public void setContext(LinkedList<ASTNode> context) {
        this.context = context;
    }

    public InterpreterExtendedState<T> getExtendedState() {
        return extendedState;
    }

    public void setExtendedState(State<T> state) {
        this.extendedState = new InterpreterExtendedState<>(state.getGoals(), state.getSelectedGoalNode());
        if (getScriptstmt() instanceof CaseStatement) {

        }
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




}
