package edu.kit.formal.interpreter;

/**
 * Listener for Change events in the state graph
 */
public interface GraphChangedListener {


    abstract void graphChanged(NodeAddedEvent nodeAddedEvent);

}
