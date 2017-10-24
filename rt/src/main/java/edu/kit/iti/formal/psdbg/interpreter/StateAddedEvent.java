package edu.kit.iti.formal.psdbg.interpreter;


import edu.kit.iti.formal.psdbg.interpreter.data.InterpreterExtendedState;
import edu.kit.iti.formal.psdbg.interpreter.data.State;
import edu.kit.iti.formal.psdbg.interpreter.graphs.PTreeNode;
import lombok.Getter;

/**
 * Event that is fired if a new state was added to a PTreeNode in the Stategraph
 */
public class StateAddedEvent {
    @Getter
    PTreeNode changedNode;

    @Getter
    State addedState;

    @Getter
    InterpreterExtendedState addedExtState;

    public StateAddedEvent(PTreeNode addedNode, State addedState, InterpreterExtendedState addedExtState) {
        this.changedNode = addedNode;
        this.addedState = addedState;
        this.addedExtState = addedExtState;
    }

    public String toString() {
        return "State added to " + changedNode.getScriptstmt().getNodeName() + changedNode.getScriptstmt().getStartPosition();
    }

}
