package edu.kit.formal.interpreter.graphs;


import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import edu.kit.formal.interpreter.Interpreter;
import edu.kit.formal.interpreter.NodeAddedEvent;
import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.KeyData;
import edu.kit.formal.interpreter.data.State;
import edu.kit.formal.interpreter.exceptions.StateGraphException;
import edu.kit.formal.proofscriptparser.DefaultASTVisitor;
import edu.kit.formal.proofscriptparser.Visitor;
import edu.kit.formal.proofscriptparser.ast.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;

import java.util.*;


/**
 * State graph that is computed on the fly while stepping through script
 * A Node in the graph is a PTreeNode {@link PTreeNode}
 * Edges are computed on the fly while
 */
public class StateGraphWrapper {

    /**
     * Listeners getting informed when new node added to state graph
     */
    @Getter
    private List<GraphChangedListener> changeListeners = new ArrayList();

    /**
     * Interpreter
     */
    private Interpreter<KeyData> currentInterpreter;


    /**
     * Graph that is computed on the fly in order to allow stepping
     */
    private MutableValueGraph<PTreeNode, EdgeTypes> stateGraph;


    /**
     * Root of state graph
     */
    private SimpleObjectProperty<PTreeNode> root = new SimpleObjectProperty<>();

    /**
     * last added node
     */
    private PTreeNode lastNode;

    /**
     * Mapping ASTNode to PTreeNode
     */

    private HashMap<ASTNode, PTreeNode> addedNodes = new LinkedHashMap<>();

    /**
     * Visitor for control flow graph
     */
    private ControlFlowVisitor cfgVisitor;

    private Visitor<Void> entryListener = new StateGraphWrapper.EntryListener();
    private Visitor<Void> exitListener = new StateGraphWrapper.ExitListener();

    private ProofScript mainScript;

    public StateGraphWrapper(Interpreter inter, ProofScript mainScript, ControlFlowVisitor cfgVisitor) {
        stateGraph = ValueGraphBuilder.directed().build();
        this.currentInterpreter = inter;
        this.cfgVisitor = cfgVisitor;
        this.mainScript = mainScript;

        // createRootNode(null);

    }


    public MutableValueGraph<PTreeNode, EdgeTypes> getStateGraph() {
        return stateGraph;
    }


    public PTreeNode getLastNode() {
        return lastNode;
    }


    public void createRootNode(ASTNode node) {
        PTreeNode newStateNode = new PTreeNode(node);
        newStateNode.setState(currentInterpreter.getCurrentState().copy());
        boolean res = stateGraph.addNode(newStateNode);
        if (!res) {
            throw new StateGraphException("Could not create new state for ASTNode " + node + " and add it to the stategraph");
        } else {
            this.root.set(newStateNode);
            lastNode = newStateNode;
        }
    }

    public void setUpGraph() {
        //create empty graph
        stateGraph = ValueGraphBuilder.directed().build();

    }


    //careful TODO look for right edges
    //TODO handle endpoint of graph
    public PTreeNode getStepOver(PTreeNode statePointer) {
        /*Set<PTreeNode> pTreeNodesNeigbours = stateGraph.adjacentNodes(statePointer);
        if(pTreeNodesNeigbours.isEmpty()){
            return null;
        }else{
            pTreeNodesNeigbours.forEach(e-> System.out.println(e.getScriptstmt().getNodeName()+e.getScriptstmt().getStartPosition()));
        }*/
        if (statePointer == null) {
            return this.rootProperty().get();
        }
        Set<PTreeNode> successors = this.stateGraph.successors(statePointer);
        if (successors.isEmpty()) {
            return null;
        } else {
            Object[] sucs = successors.toArray();
            getNodeWithEdgeType(statePointer, EdgeTypes.STATE_FLOW);
            return (PTreeNode) sucs[0];
        }
        //return statePointer;


    }


    public PTreeNode getStepBack(PTreeNode statePointer) {

        Set<PTreeNode> pred = this.stateGraph.predecessors(statePointer);

        if (pred.isEmpty()) {
            return null;
        } else {
            Object[] sucs = pred.toArray();
            return (PTreeNode) sucs[0];
        }

        //return statePointer;
    }

    private PTreeNode getNodeWithEdgeType(PTreeNode source, EdgeTypes type) {
        Set<PTreeNode> predecessors = stateGraph.predecessors(source);
        Set<PTreeNode> chosenNodes = new HashSet<>();

        predecessors.forEach(pred -> {
            EdgeTypes typeToCheck = stateGraph.edgeValue(pred, source);
            if (type.equals(typeToCheck)) {
                chosenNodes.add(pred);
            }
        });

        Set<PTreeNode> successors = stateGraph.successors(source);
        successors.forEach(succ -> {
            EdgeTypes typeToCheck = stateGraph.edgeValue(source, succ);
            if (type.equals(typeToCheck)) {
                chosenNodes.add(succ);
            }
        });
        //chosenNodes.forEach(n -> System.out.println(n.toString()));
        //stateGraph.edgeValue()
        return null;
    }

    /**
     * Create a new node for the state graph and add edges to already existing nodes
     *
     * @param node
     * @return
     */
    private Void createNewNode(ASTNode node) {
        State<KeyData> lastState = lastNode.getState();

        PTreeNode newStateNode;
        newStateNode = new PTreeNode(node);

        //State<KeyData> currentState = currentInterpreter.getCurrentState().copy();
        //System.out.println("Equals of states " + currentState.equals(lastState));
        //newStateNode.setState(currentState);

        stateGraph.addNode(newStateNode);
        stateGraph.putEdgeValue(lastNode, newStateNode, EdgeTypes.STATE_FLOW);
        fireNodeAdded(new NodeAddedEvent(lastNode, newStateNode));
        addedNodes.put(node, newStateNode);
        lastNode = newStateNode;

        return null;
    }

    public Void addState(ASTNode node) {
        PTreeNode newStateNode = addedNodes.get(node);
        State<KeyData> currentState = currentInterpreter.getCurrentState().copy();

        newStateNode.setState(currentState);
        return null;
    }

    public void install(Interpreter<KeyData> interpreter) {
        if (currentInterpreter != null) deinstall(interpreter);
        interpreter.getEntryListeners().add(entryListener);
        interpreter.getEntryListeners().add(exitListener);
        currentInterpreter = interpreter;
    }

    public void deinstall(Interpreter<KeyData> interpreter) {
        if (interpreter != null) {
            interpreter.getEntryListeners().remove(entryListener);
            interpreter.getEntryListeners().remove(exitListener);
        }
    }

    public String asdot() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph G {\nnode [shape=rect]\n ");

        stateGraph.nodes().forEach(n -> {
            sb.append(n.hashCode())
                    .append(" [label=\"")
                    .append(n.getScriptstmt().getNodeName())
                    .append("@")
                    .append(n.getScriptstmt().getStartPosition().getLineNumber())
                    .append("\"]\n");
        });

        stateGraph.edges().forEach(edge -> {
            sb.append(edge.source().hashCode())
                    .append(" -> ")
                    .append(edge.target().hashCode())
                    .append(" [label=\"")
                    .append(stateGraph.edgeValue(edge.source(), edge.target()).name())
                    .append("\"]\n");
        });

        sb.append("}");
        return sb.toString();
    }

    public PTreeNode getRoot() {
        return root.get();
    }

    public void setRoot(PTreeNode root) {
        this.root.set(root);
    }

    public SimpleObjectProperty<PTreeNode> rootProperty() {
        return root;
    }

    public void addChangeListener(GraphChangedListener listener) {
        changeListeners.add(listener);
    }

    public void removeChangeListener(GraphChangedListener listener) {
        changeListeners.remove(listener);
    }

    private void fireNodeAdded(NodeAddedEvent nodeAddedEvent) {
        changeListeners.forEach(list -> {
            Platform.runLater(() -> {
                list.graphChanged(nodeAddedEvent);
            });
        });

    }

    public PTreeNode getNode(List<GoalNode<KeyData>> newValue) {

        Iterator<Map.Entry<ASTNode, PTreeNode>> iterator = addedNodes.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<ASTNode, PTreeNode> next = iterator.next();
            PTreeNode value = next.getValue();
            if (value.getState().getGoals().equals(newValue)) {
                return value;
            }
        }
        return null;

    }

    private class EntryListener extends DefaultASTVisitor<Void> {
        @Override
        public Void visit(ProofScript proofScript) {
            if (root.get() == null) {
                createRootNode(proofScript);
            } else {
                createNewNode(proofScript);
            }
            return null;
        }

        @Override
        public Void visit(AssignmentStatement assignment) {
            return createNewNode(assignment);
        }


        @Override
        public Void visit(CasesStatement casesStatement) {
            return createNewNode(casesStatement);
        }

        @Override
        public Void visit(CaseStatement caseStatement) {
            return createNewNode(caseStatement);
        }

        @Override
        public Void visit(CallStatement call) {
            return createNewNode(call);
        }

        @Override
        public Void visit(TheOnlyStatement theOnly) {
            return createNewNode(theOnly);
        }

        @Override
        public Void visit(ForeachStatement foreach) {
            return createNewNode(foreach);
        }

        @Override
        public Void visit(RepeatStatement repeatStatement) {
            return createNewNode(repeatStatement);
        }
    }

    private class ExitListener extends DefaultASTVisitor<Void> {
        @Override
        public Void visit(AssignmentStatement assignment) {
            return addState(assignment);
        }

        @Override
        public Void visit(CasesStatement casesStatement) {
            return addState(casesStatement);
        }

        @Override
        public Void visit(CaseStatement caseStatement) {
            return addState(caseStatement);
        }

        @Override
        public Void visit(CallStatement call) {
            return addState(call);
        }

        @Override
        public Void visit(TheOnlyStatement theOnly) {
            return addState(theOnly);
        }

        @Override
        public Void visit(ForeachStatement foreach) {
            return addState(foreach);
        }

        @Override
        public Void visit(RepeatStatement repeatStatement) {
            return addState(repeatStatement);
        }

       /* @Override
        public Void visit(ProofScript proofScript) {
            return addState(proofScript);
        }*/
    }

}
