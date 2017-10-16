package edu.kit.iti.formal.psdbg.interpreter.graphs;

import edu.kit.iti.formal.psdbg.interpreter.NodeAddedEvent;

/**
 * Listener for Change events in the state graph
 */
public interface GraphChangedListener {


    abstract void graphChanged(NodeAddedEvent nodeAddedEvent);


}
