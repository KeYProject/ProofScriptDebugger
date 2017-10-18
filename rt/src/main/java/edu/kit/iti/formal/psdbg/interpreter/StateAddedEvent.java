package edu.kit.iti.formal.psdbg.interpreter;


import edu.kit.iti.formal.psdbg.interpreter.data.State;
import edu.kit.iti.formal.psdbg.interpreter.graphs.PTreeNode;
import lombok.Getter;

public class StateAddedEvent {
    @Getter
    PTreeNode changedNode;

    @Getter
    State addedState;

    public StateAddedEvent(PTreeNode addedNode, State addedState) {
        this.changedNode = addedNode;
        this.addedState = addedState;
    }

    public String toString() {
        return "State added to " + changedNode.getScriptstmt().getNodeName() + changedNode.getScriptstmt().getStartPosition();
    }

}
