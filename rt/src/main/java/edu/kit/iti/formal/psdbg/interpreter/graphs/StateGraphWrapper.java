package edu.kit.iti.formal.psdbg.interpreter.graphs;


import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import edu.kit.iti.formal.psdbg.interpreter.Interpreter;
import edu.kit.iti.formal.psdbg.interpreter.NodeAddedEvent;
import edu.kit.iti.formal.psdbg.interpreter.StateAddedEvent;
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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private MutableValueGraph<PTreeNode<T>, EdgeTypes> stateGraph;


    /**
     * Root of state graph
     */
    private SimpleObjectProperty<PTreeNode<T>> root = new SimpleObjectProperty<>();

    /**
     * last added node
     */
    private PTreeNode<T> lastNode;

    /**
     * Mapping ASTNode to PTreeNode for lookup
     */
    private HashMap<ASTNode, PTreeNode<T>> addedNodes = new LinkedHashMap<>();

    /**
     * Visitor for control flow graph
     */
    private ControlFlowVisitor cfgVisitor;

    private EntryListener entryListener = new EntryListener();

    private ExitListener exitListener = new ExitListener();

    private ProofScript mainScript;


    /**

     * Creates a new state graph and adds the root node with the root state
     *  @param inter
     * @param mainScript
     * @param cfgVisitor
     *
     */
    public StateGraphWrapper(Interpreter<T> inter, ProofScript mainScript, ControlFlowVisitor cfgVisitor) {
        stateGraph = ValueGraphBuilder.directed().build();
        this.currentInterpreter = inter;
        this.cfgVisitor = cfgVisitor;
        this.mainScript = mainScript;
        createRootNode(this.mainScript);

    }

    private static int counter = 0;

    public MutableValueGraph<PTreeNode<T>, EdgeTypes> getStateGraph() {
        return stateGraph;
    }

    /**
     * Returns the PTreenNode which was added recently
     *
     * @return
     */
    public PTreeNode getLastNode() {
        return lastNode;
    }


    //careful TODO look for right edges
    //TODO handle endpoint of graph
    public PTreeNode<T> getStepOver(PTreeNode statePointer) {

        if (statePointer == null) {
            LOGGER.info("Stepover requested for null, therefore returning root");
            return this.rootProperty().get();
        }
        LOGGER.info("Stepover requested for node {}@{}", statePointer.getScriptstmt(), statePointer.getScriptstmt().getNodeName());
        //look for successors in the graph
        Set<PTreeNode<T>> successors = this.stateGraph.successors(statePointer);
        //if there are no successors they have to be computed therefore return null, to trigger the proof tree controller
        if (successors.isEmpty()) {

            return null;
        } else {
            //if there are successors we want those which are connetced with State-Flow
            Object[] sucs = successors.toArray();
            getNodeWithEdgeType(statePointer, EdgeTypes.STATE_FLOW);
            return (PTreeNode<T>) sucs[0];
        }

        //return statePointer;


    }

    public SimpleObjectProperty<PTreeNode<T>> rootProperty() {
        return root;
    }

    private PTreeNode<T> getNodeWithEdgeType(PTreeNode<T> source, EdgeTypes type) {
        Set<PTreeNode<T>> predecessors = stateGraph.predecessors(source);
        Set<PTreeNode<T>> chosenNodes = new HashSet<>();

        predecessors.forEach(pred -> {
            EdgeTypes typeToCheck = stateGraph.edgeValue(pred, source);
            if (type.equals(typeToCheck)) {
                chosenNodes.add(pred);
            }
        });

        Set<PTreeNode<T>> successors = stateGraph.successors(source);
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

    public PTreeNode<T> getStepBack(PTreeNode statePointer) {
        Set<PTreeNode<T>> pred = this.stateGraph.predecessors(statePointer);
        //if pred is empty we have reached the root
        if (pred.isEmpty()) {
            return statePointer;
        } else {
            Object[] sucs = pred.toArray();
            return (PTreeNode<T>) sucs[0];
        }

        //return statePointer;
    }

    private Void createNewNode(ASTNode node) {
        return createNewNode(node, false);
    }

    private static Path graph = Paths.get("/tmp/test.dot");

    /**
     * Create the root Node for the state graph
     *
     * @param node
     */
    public void createRootNode(ASTNode node) {
        LOGGER.info("Creating Root for State graph with statement {}@{}", node.getNodeName(), node.getStartPosition());
        PTreeNode<T> newStateNode = new PTreeNode<>(node);
        newStateNode.setContext(new Stack<>());
        newStateNode.getContext().push(node);
        State<T> currentInterpreterStateCopy = currentInterpreter.getCurrentState().copy();
        //copy current state before executing statement
        newStateNode.setState(currentInterpreterStateCopy);

        //create extended State
        InterpreterExtendedState<T> extState = new InterpreterExtendedState<>();
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

    /**
     * Create a new node for the state graph and add edges to already existing nodes
     *
     * @param node
     * @return null
     */
    private Void createNewNode(ASTNode node, boolean isCasesStmt) {
        LOGGER.info("Creating Node for State graph with statement {}@{}", node.getNodeName(), node.getStartPosition());
        //save old pointer of last node
        State<T> lastState;
        lastState = currentInterpreter.getCurrentState().copy();

        //create a new ProofTreeNode in graph with ast node as parameter
        PTreeNode<T> newStateNode = new PTreeNode<>(node);
        newStateNode.setContext(lastNode.getContext());
        //get the state before executing ast node to save it to the extended state
        InterpreterExtendedState<T> extState;

        if (isCasesStmt) {
            newStateNode.getContext().push(node);
            extState = new InterpreterExtendedState<>(lastNode.getExtendedState().copy());
            extState.setStmt(node);
            extState.setStateBeforeStmt(lastState.copy());
            Map<CaseStatement, List<GoalNode<T>>> mappingOfCaseToStates = extState.getMappingOfCaseToStates();
            CasesStatement cs = (CasesStatement) node;
            //initialize the cases in the mapping map
            cs.getCases().forEach(caseStatement -> {
                mappingOfCaseToStates.put(caseStatement, Collections.emptyList());
            });

            extState.setMappingOfCaseToStates(mappingOfCaseToStates);
            //TODO default case
            newStateNode.setState(lastState.copy());

        } else {

            //set pointer to parent extended state
            extState = new InterpreterExtendedState<>(lastNode.getExtendedState().copy());
            extState.setStmt(node);
            //check whether case statement
            if (extState.getMappingOfCaseToStates().containsKey(node)) {
                extState.getMappingOfCaseToStates().get(node).add(lastState.getSelectedGoalNode().deepCopy());
                extState.setStateBeforeStmt(new State<T>(extState.getMappingOfCaseToStates().get(node), lastState.getSelectedGoalNode().deepCopy()));
            } else {
                extState.setStateBeforeStmt(lastState);
            }
        }

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
        //counter++;
        //System.out.println("XXXXXXXXXX counter = " + counter);
        changeListeners.forEach(list -> Platform.runLater(() -> {
            list.graphChanged(nodeAddedEvent);
            //TODO
            //  LOGGER.info("New StateGraphChange \n%%%%%%" + this.asdot() + "\n%%%%%%%");
        }));
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

    private Void addState(ASTNode node) {
        return addState(node, true);
    }

    public void addChangeListener(GraphChangedListener listener) {
        changeListeners.add(listener);
    }

    public void removeChangeListener(GraphChangedListener listener) {
        changeListeners.remove(listener);
    }

    private void deinstall(Interpreter<T> interpreter) {
        if (interpreter != null) {
            interpreter.getEntryListeners().remove(entryListener);
            interpreter.getExitListeners().remove(exitListener);
        }
    }

    public PTreeNode<T> getNode(List<GoalNode<T>> newValue) {
        for (Map.Entry<ASTNode, PTreeNode<T>> next : addedNodes.entrySet()) {
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
                    .append(n.extendedStateToString())
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

        //TODO: debugging
        try (java.io.BufferedWriter writer =
                     java.nio.file.Files.newBufferedWriter(graph, StandardCharsets.UTF_8)
        ) {
            writer.write(sb.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO: end debugging

        return sb.toString();
    }

    private Void addState(ASTNode node, boolean fireStateAdded) {
        LOGGER.info("Adding a new state for statement {}@{}", node.getNodeName(), node.getStartPosition());
        //get node from addedNodes Map
        PTreeNode<T> newStateNode = addedNodes.get(node);
        //copy Current Interpreter state
        State<T> currentState = currentInterpreter.getCurrentState().copy();
        //set the state
        if (node != this.root.get().getScriptstmt()) {
            newStateNode.setState(currentState);
            newStateNode.getExtendedState().setStateAfterStmt(currentState);
            if (newStateNode.getContext().peek().equals(node)) {
                newStateNode.getContext().pop();
            }
        } else {
            //  newStateNode.setState(currentState);
            newStateNode.getExtendedState().setStateAfterStmt(currentState);
            if (newStateNode.getContext().peek().equals(node)) {
                newStateNode.getContext().pop();
            }
        }
        if (fireStateAdded) {
            fireStateAdded(new StateAddedEvent(newStateNode, currentState, newStateNode.getExtendedState()));
        }
        LOGGER.debug("Extended state for {} updated with {} \n", node.getStartPosition(), newStateNode.getExtendedState().toString());
        return null;
    }

    private void fireStateAdded(StateAddedEvent stateAddedEvent) {
        changeListeners.forEach(list -> Platform.runLater(() -> {
            list.graphChanged(stateAddedEvent);
            // LOGGER.debug("New StateGraphChange " + this.asdot());
        }));
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
            return createNewNode(casesStatement, true);
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
        public Void visit(MatchExpression matchExpression) {
            return createNewNode(matchExpression);
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
        public Void visit(ProofScript proofScript) {
            return addState(proofScript, false);
        }

        @Override
        public Void visit(CasesStatement casesStatement) {
            return addState(casesStatement, false);
        }

        /*@Override
        public Void visit(CaseStatement caseStatement) {
            return addState(caseStatement, false);
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
            return addState(simpleCaseStatement, false);
        }

        @Override
        public Void visit(ClosesCase closesCase) {
            return addState(closesCase);
        }


    }

}
