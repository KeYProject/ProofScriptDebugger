package edu.kit.formal.interpreter.graphs;

import edu.kit.formal.interpreter.NodeAddedEvent;

/**
 * Listener for Change events in the state graph
 */
public interface GraphChangedListener {


    abstract void graphChanged(NodeAddedEvent nodeAddedEvent);

}
