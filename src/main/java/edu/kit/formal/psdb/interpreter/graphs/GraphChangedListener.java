package edu.kit.formal.psdb.interpreter.graphs;

import edu.kit.formal.psdb.interpreter.NodeAddedEvent;

/**
 * Listener for Change events in the state graph
 */
public interface GraphChangedListener {


    abstract void graphChanged(NodeAddedEvent nodeAddedEvent);

}
