package edu.kit.iti.formal.psdbg.interpreter.graphs;

import edu.kit.iti.formal.psdbg.interpreter.NodeAddedEvent;
import edu.kit.iti.formal.psdbg.interpreter.StateAddedEvent;

/**
 * Listener for Change events in the state graph
 */
public interface GraphChangedListener {


    abstract void graphChanged(NodeAddedEvent nodeAddedEvent);

    abstract void graphChanged(StateAddedEvent stateAddedEvent);


}
