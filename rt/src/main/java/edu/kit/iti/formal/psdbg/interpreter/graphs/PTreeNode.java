package edu.kit.iti.formal.psdbg.interpreter.graphs;

import edu.kit.iti.formal.psdbg.interpreter.data.InterpreterExtendedState;
import edu.kit.iti.formal.psdbg.interpreter.data.State;
import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Stack;

/**
 * Inner class representing nodes in the stategraph graph
 * A node contains a reference to the ASTNode and a reference to the corresponding interpreter state if this state is already interpreted, null otherwise.
 */
@Data
public class PTreeNode<T> {
    /**
     * State  after statement
     */
    //private State<T> state;

    private InterpreterExtendedState<T> extendedState = new InterpreterExtendedState<>(null);

    /**
     * Statement
     */
    private ASTNode scriptStmt;


    /**
     * Call context
     */
    @Getter
    @Setter
    private Stack<ASTNode> context = new Stack<>();

    private boolean root;

    PTreeNode(ASTNode node) {
        this.setScriptStmt(node);
    }

    public void setExtendedState(InterpreterExtendedState<T> extendedState) {
        this.extendedState = extendedState;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Node {");
        if (scriptStmt != null) {
            sb.append(scriptStmt.getNodeName() + "\n");
        } else {
            sb.append("Root Node");
        }
       /* if (hasState()) {
            sb.append("\nState " + state.getGoals() + "\n");
        } else {
            sb.append("No State yet");
        }*/
        if (extendedState != null) {
            sb.append(extendedStateToString());
        } else {
            sb.append("No extended State yet");
        }
        sb.append("}");
        return sb.toString();
    }


    public String extendedStateToString() {
        return this.extendedState.toString();
    }
}
