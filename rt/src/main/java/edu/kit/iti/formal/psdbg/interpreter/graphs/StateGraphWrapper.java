package edu.kit.iti.formal.psdbg.interpreter.graphs;


import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import edu.kit.iti.formal.psdbg.interpreter.Interpreter;
import edu.kit.iti.formal.psdbg.interpreter.NodeAddedEvent;
import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.InterpreterExtendedState;
import edu.kit.iti.formal.psdbg.interpreter.data.State;
import edu.kit.iti.formal.psdbg.interpreter.exceptions.StateGraphException;
import edu.kit.iti.formal.psdbg.parser.DefaultASTVisitor;
import edu.kit.iti.formal.psdbg.parser.ast.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * State graph that is computed on the fly while stepping through script
 * A Node in the graph is a PTreeNode {@link PTreeNode}
 * Edges are computed on the fly while
 */
public class StateGraphWrapper<T> {
    private static final Logger LOGGER = LogManager.getLogger(StateGraphWrapper.class);

    /**
     * Listeners getting informed when new node added to state graph
     */
    @Getter
    private List<GraphChangedListener> changeListeners = new ArrayList<>();

    /**
     * Interpreter
     */
    private Interpreter<T> currentInterpreter;


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
    private PTreeNode<T> lastNode;

    /**
     * Mapping ASTNode to PTreeNode for lookup
     */

    private HashMap<ASTNode, PTreeNode> addedNodes = new LinkedHashMap<>();

    /**
     * Visitor for control flow graph
     */
    private ControlFlowVisitor cfgVisitor;

    private EntryListener entryListener = new EntryListener();
    private ExitListener exitListener = new ExitListener();

    private ProofScript mainScript;

    public StateGraphWrapper(Interpreter<T> inter, ProofScript mainScript, ControlFlowVisitor cfgVisitor) {
        stateGraph = ValueGraphBuilder.directed().build();
        this.currentInterpreter = inter;
        this.cfgVisitor = cfgVisitor;
        this.mainScript = mainScript;


        // createRootNode(this.mainScript);

    }


    public MutableValueGraph<PTreeNode, EdgeTypes> getStateGraph() {
        return stateGraph;
    }


    public PTreeNode getLastNode() {
        return lastNode;
    }


    public void createRootNode(ASTNode node) {
        LOGGER.info("Creating Root for State graph");
        PTreeNode newStateNode = new PTreeNode(node);
        State<T> currentInterpreterStateCopy = currentInterpreter.getCurrentState().copy();
        //copy current state before executing statement
        newStateNode.setState(currentInterpreterStateCopy);

        //create extended State
        InterpreterExtendedState extState = new InterpreterExtendedState();
        extState.setStmt(node);
        extState.setStateBeforeStmt(currentInterpreterStateCopy);
        newStateNode.setExtendedState(extState);
        //add node to state graph
        boolean res = stateGraph.addNode(newStateNode);
        if (!res) {
            throw new StateGraphException("Could not create new state for ASTNode " + node + " and add it to the stategraph");
        } else {
            this.root.set(newStateNode);
            //to keep track of added nodes
            addedNodes.put(node, newStateNode);
            //   fireNodeAdded(new NodeAddedEvent(null, newStateNode));
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

        if (statePointer == null) {
            return this.rootProperty().get();
        }

        //look for successors in the graph
        Set<PTreeNode> successors = this.stateGraph.successors(statePointer);
        //if there are no successors they have to be computed therefore return null, to trigger the proof tree controller
        if (successors.isEmpty()) {
            return null;
        } else {
            //if there are successors we want those which are connetced with State-Flow
            Object[] sucs = successors.toArray();
            getNodeWithEdgeType(statePointer, EdgeTypes.STATE_FLOW);
            return (PTreeNode) sucs[0];
        }
        //return statePointer;


    }


    public PTreeNode getStepBack(PTreeNode statePointer) {
        Set<PTreeNode> pred = this.stateGraph.predecessors(statePointer);
        //if pred is empty we have reached the root
        if (pred.isEmpty()) {
            return statePointer;
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
        //save old pointer of last node
        State<T> lastState = lastNode.getState();
        //create a new ProofTreeNode in graph with ast node as parameter
        PTreeNode newStateNode;
        newStateNode = new PTreeNode(node);
        //get the state before executing ast node to save it to the extended state
        //State<T> currentState = currentInterpreter.getCurrentState().copy();
        InterpreterExtendedState<T> extState;
        //set pointer to parent extended state
        if (lastNode.getExtendedState() != null) {
            extState = new InterpreterExtendedState<T>(lastNode.getExtendedState().copy());
        } else {
            extState = new InterpreterExtendedState<T>();
        }
        extState.setStmt(node);
//        extState.setStateBeforeStmt(lastNode.getExtendedState().getStateAfterStmt());
        extState.setStateBeforeStmt(lastState);
        newStateNode.setExtendedState(extState);

        stateGraph.addNode(newStateNode);
        stateGraph.putEdgeValue(lastNode, newStateNode, EdgeTypes.STATE_FLOW);
        //inform listeners about a new node in the graph
        fireNodeAdded(new NodeAddedEvent(lastNode, newStateNode));
        addedNodes.put(node, newStateNode);
        lastNode = newStateNode;
        return null;
    }

    private void fireNodeAdded(NodeAddedEvent nodeAddedEvent) {
        changeListeners.forEach(list -> {
            Platform.runLater(() -> {
                list.graphChanged(nodeAddedEvent);
                // System.out.println("New StateGraphChange " + this.asdot());
            });
        });

    }

    /**
     * If visting a cases statement special care has to be taken for the extended state
     *
     * @param casesStatement
     * @return
     */
    private Void createNewCasesNode(CasesStatement casesStatement) {
        State<T> lastState = lastNode.getState();
        PTreeNode newStateNode;
        newStateNode = new PTreeNode(casesStatement);

        State<T> currentState = currentInterpreter.getCurrentState().copy();
        //merge des alten ext fehlt
        InterpreterExtendedState extState = new InterpreterExtendedState(lastNode.getExtendedState().copy());
        extState.setStateBeforeStmt(lastState);
        extState.setStmt(casesStatement);
        newStateNode.setExtendedState(extState);


        stateGraph.addNode(newStateNode);
        stateGraph.putEdgeValue(lastNode, newStateNode, EdgeTypes.STATE_FLOW);

        fireNodeAdded(new NodeAddedEvent(lastNode, newStateNode));
        addedNodes.put(casesStatement, newStateNode);
        lastNode = newStateNode;
        return null;
    }

    public Void addState(ASTNode node) {
        //get node from addedNodes Map
        PTreeNode newStateNode = addedNodes.get(node);
        //copy Current Interpreter state
        State<T> currentState = currentInterpreter.getCurrentState().copy();
        //set the state
        if (node != this.root.get().getScriptstmt()) {
            newStateNode.setState(currentState);
            newStateNode.getExtendedState().setStateAfterStmt(currentState);
        } else {
            //  newStateNode.setState(currentState);
            newStateNode.getExtendedState().setStateAfterStmt(currentState);
        }
        System.out.println("\n%%%%%%%%%%%%%%%%%%\nExtended State for " + node.getStartPosition() + "\n" + newStateNode.getExtendedState().toString() + "\n%%%%%%%%%%%%%%%%%%\n");
        return null;
    }

    public void install(Interpreter<T> interpreter) {
        if (currentInterpreter != null) deinstall(interpreter);
        interpreter.getEntryListeners().add(entryListener);
        interpreter.getExitListeners().add(exitListener);
        currentInterpreter = interpreter;
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

    public void deinstall(Interpreter<T> interpreter) {
        if (interpreter != null) {
            interpreter.getEntryListeners().remove(entryListener);
            interpreter.getExitListeners().remove(exitListener);
        }
    }

    public PTreeNode getNode(List<GoalNode<T>> newValue) {

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

    /**
     * Helper for debugging
     * @return
     */

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

    private class EntryListener extends DefaultASTVisitor<Void> {
        @Override
        public Void visit(ProofScript proofScript) {
            if (root.get() == null) {
                createRootNode(proofScript);
            } else {
                if (!root.get().getScriptstmt().equals(proofScript)) {
                    createNewNode(proofScript);
                }
            }

            return null;
        }

        @Override
        public Void visit(AssignmentStatement assignment) {
            return createNewNode(assignment);
        }


        @Override
        public Void visit(CasesStatement casesStatement) {
            //     return createNewCasesNode(casesStatement);
            return createNewNode(casesStatement);
        }

        /* @Override
         public Void visit(CaseStatement caseStatement) {
             return createNewNode(caseStatement);
         }
         */
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

        @Override
        public Void visit(TryCase tryCase) {
            return createNewNode(tryCase);
        }

        @Override
        public Void visit(SimpleCaseStatement simpleCaseStatement) {
            return createNewNode(simpleCaseStatement);
        }

        @Override
        public Void visit(ClosesCase closesCase) {
            return createNewNode(closesCase);
        }

        @Override
        public Void visit(DefaultCaseStatement defCase) {
            return createNewNode(defCase);
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

        /*@Override
        public Void visit(CaseStatement caseStatement) {
            return addState(caseStatement);
        }*/

        @Override
        public Void visit(DefaultCaseStatement defCase) {
            return addState(defCase);
        }

        @Override
        public Void visit(CallStatement call) {
            //  System.out.println("Call "+call.getCommand()+" "+currentInterpreter.getCurrentState());
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

        @Override
        public Void visit(TryCase tryCase) {
            return addState(tryCase);
        }

        @Override
        public Void visit(SimpleCaseStatement simpleCaseStatement) {
            return addState(simpleCaseStatement);
        }

        @Override
        public Void visit(ClosesCase closesCase) {
            return addState(closesCase);
        }

        @Override
        public Void visit(ProofScript proofScript) {
            return addState(proofScript);

        }
    }

}
