package edu.kit.iti.formal.psdbg.interpreter;

import edu.kit.iti.formal.psdbg.interpreter.dbg.PTreeNode;
import lombok.Getter;

/**
 * Event that is fired when a node is added to the StateGraph
 */
public class NodeAddedEvent {

    @Getter
    PTreeNode addedNode;

    @Getter
    PTreeNode lastPointer;

    public NodeAddedEvent(PTreeNode lastPointer, PTreeNode addedNode) {
        this.addedNode = addedNode;
        this.lastPointer = lastPointer;
    }

    public String toString() {
        return addedNode.getScriptStmt().getNodeName() + addedNode.getScriptStmt().getStartPosition();
    }


}
