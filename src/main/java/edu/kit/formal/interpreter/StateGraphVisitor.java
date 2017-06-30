package edu.kit.formal.interpreter;


import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import edu.kit.formal.gui.controller.PuppetMaster;
import edu.kit.formal.interpreter.data.KeyData;
import edu.kit.formal.proofscriptparser.DefaultASTVisitor;
import edu.kit.formal.proofscriptparser.ast.*;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * State graph that is computed on the fly while stepping through script
 * A Node in the graph is a PTreeNode {@link edu.kit.formal.interpreter.PTreeNode}
 * Edges are computed on the fly while
 */
public class StateGraphVisitor extends DefaultASTVisitor<Void> {

    /**
     * Listeners getting informed when new node added to state graph
     */
    @Getter
    private List<GraphChangedListener> changeListeners = new ArrayList();

    /**
     * Interpreter
     */
    private Interpreter<KeyData> currentInterpreter;

    private PuppetMaster blocker;
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
     * Visitor for control flow graph
     */
    private ProgramFlowVisitor cfgVisitor;

    /**
     * Pointer for stepping
     */
    private PTreeNode currentStatePointer;

    private ProofScript mainScript;

    public StateGraphVisitor(Interpreter inter, ProofScript mainScript, ProgramFlowVisitor cfgVisitor) {
        stateGraph = ValueGraphBuilder.directed().build();
        this.currentInterpreter = inter;
        this.cfgVisitor = cfgVisitor;
        this.mainScript = mainScript;
        createRootNode();

    }


    public MutableValueGraph<PTreeNode, EdgeTypes> getStateGraph() {
        return stateGraph;
    }


    public PTreeNode getLastNode() {
        return lastNode;
    }


    public void createRootNode() {
        PTreeNode newStateNode = new PTreeNode(null);
        newStateNode.setState(currentInterpreter.getCurrentState());
        stateGraph.addNode(newStateNode);
        this.root.set(newStateNode);
        lastNode = newStateNode;
    }


    //careful TODO look for right edges
    //TODO handle endpoint of graph
    public PTreeNode getStepOver(PTreeNode statePointer) {
        Set<PTreeNode> successors = this.stateGraph.successors(statePointer);
        if (successors.isEmpty()) {
            return null;
        } else {
            Object[] sucs = successors.toArray();
            getNodeWithEdgeType(statePointer, EdgeTypes.STATE_FLOW);
            return (PTreeNode) sucs[0];
        }


    }


    public PTreeNode getStepBack(PTreeNode statePointer) {

        Set<PTreeNode> pred = this.stateGraph.predecessors(statePointer);
        if (pred.isEmpty()) {
            return null;
        } else {
            Object[] sucs = pred.toArray();
            return (PTreeNode) sucs[0];
        }

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
    public Void createNewNode(ASTNode node) {
        PTreeNode newStateNode;
        newStateNode = new PTreeNode(node);

        newStateNode.setState(currentInterpreter.getCurrentState());
        stateGraph.addNode(newStateNode);


        stateGraph.putEdgeValue(lastNode, newStateNode, EdgeTypes.STATE_FLOW);
        fireNodeAdded(new NodeAddedEvent(lastNode, newStateNode));
        lastNode = newStateNode;
        return null;
    }


    @Override
    public Void visit(ProofScript proofScript) {
        createNewNode(proofScript);
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


    protected void fireNodeAdded(NodeAddedEvent nodeAddedEvent) {
        System.out.println("New Node added " + nodeAddedEvent.toString());
        changeListeners.forEach(list -> {
            list.graphChanged(nodeAddedEvent);
        });

    }


}
