package edu.kit.formal.psdb.interpreter;

import edu.kit.formal.psdb.interpreter.graphs.PTreeNode;
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
        return addedNode.getScriptstmt().getNodeName() + addedNode.getScriptstmt().getStartPosition();
    }


}
