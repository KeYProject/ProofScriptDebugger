package edu.kit.iti.formal.psdbg.gui.controller;

import com.google.common.eventbus.Subscribe;
import com.google.common.graph.MutableValueGraph;
import edu.kit.iti.formal.psdbg.InterpretingService;
import edu.kit.iti.formal.psdbg.gui.controls.ASTNodeHiglightListener;
import edu.kit.iti.formal.psdbg.gui.controls.DebuggerStatusBar;
import edu.kit.iti.formal.psdbg.gui.controls.Utils;
import edu.kit.iti.formal.psdbg.gui.model.Breakpoint;
import edu.kit.iti.formal.psdbg.interpreter.KeyInterpreter;
import edu.kit.iti.formal.psdbg.interpreter.NodeAddedEvent;
import edu.kit.iti.formal.psdbg.interpreter.StateAddedEvent;
import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.InterpreterExtendedState;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.data.State;
import edu.kit.iti.formal.psdbg.interpreter.graphs.*;
import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;
import edu.kit.iti.formal.psdbg.parser.ast.ProofScript;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.concurrent.Worker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Class controlling and maintaining proof tree structure for debugger and handling step functions for the debugger
 *
 * @author S. Grebing
 */
public class ProofTreeController {
    private static final Logger LOGGER = LogManager.getLogger(ProofTreeController.class);

    /**
     * To control stepping
     */
    private final PuppetMaster blocker = new PuppetMaster();
    /**
     * Goals of the state which is referenced by the statePointer
     */
    private final ListProperty<GoalNode<KeyData>> currentGoals = new SimpleListProperty<>(FXCollections.observableArrayList());

    /**
     * Selected Goal of the state which is referenced by the statePointer
     */
    private final SimpleObjectProperty<GoalNode<KeyData>> currentSelectedGoal = new SimpleObjectProperty<>();


    /**
     * Service to run interpreter in own thread
     */
    private InterpretingService interpreterService = new InterpretingService(blocker);


    private ReadOnlyBooleanProperty alreadyExecuted = interpreterService.hasRunSucessfullyProperty();


    /**
     * To identify when interpreterservice is running
     */
    private ReadOnlyBooleanProperty executeNotPossible = interpreterService.runningProperty();


    /**
     * Node that is updated whenever a new node is added to the stategraph
     */
    private SimpleObjectProperty<PTreeNode> nextComputedNode = new SimpleObjectProperty<>();

    /**
     * Instead of start and end position.
     */
    private ObjectProperty<ASTNode> currentHighlightNode = new SimpleObjectProperty<>();


    /**
     * Visitor to retrieve state graph
     */
    private StateGraphWrapper<KeyData> stateGraphWrapper;
    /**
     * Graph that is computed on the fly in order to allow stepping
     */

    private ControlFlowVisitor controlFlowGraphVisitor;


    /**
     * Current interpreter
     */
    private KeyInterpreter currentInterpreter;

    /**
     * Pointer to current selected state in graph
     */
    private PTreeNode<KeyData> statePointer = null;

    /**
     * The mainscipt that is executed
     */
    private SimpleObjectProperty<ProofScript> mainScript = new SimpleObjectProperty<>();

    /*public State getBlockerState() {
        return blockerState.get();
    }

    public SimpleObjectProperty<State> blockerStateProperty() {
        return blockerState;
    }

    public void setBlockerState(State blockerState) {
        this.blockerState.set(blockerState);
    }
*/
    //  private SimpleObjectProperty<State> blockerState = new SimpleObjectProperty<>();

    /**
     * Add a change listener for the stategraph, whenever a new node is added it receives an event
     */
    private GraphChangedListener graphChangedListener = new GraphChangedListener() {
        @Override
        public void graphChanged(NodeAddedEvent nodeAddedEvent) {
            PTreeNode added = nodeAddedEvent.getAddedNode();
            if (added.getState() != null) {
                LOGGER.info("Graph changed with the following PTreeNode: {} and the statepointer points to {}", nodeAddedEvent.getAddedNode(), statePointer);
                nextComputedNode.setValue(nodeAddedEvent.getAddedNode());
                // Events.fire(new Events.NewNodeExecuted(nodeAddedEvent.getAddedNode().getScriptstmt()));
            }

        }

        @Override
        public void graphChanged(StateAddedEvent stateAddedEvent) {
            PTreeNode changedNode = stateAddedEvent.getChangedNode();
            LOGGER.info("Graph changed by adding a state to PTreeNode: {} and the statepointer points to {}", stateAddedEvent, statePointer);
            nextComputedNode.set(changedNode);
            //Events.fire(new Events.NewNodeExecuted(changedNode.getScriptstmt()));

        }

    };

    public ProofScript getMainScript() {
        return mainScript.get();
    }

    public void setMainScript(ProofScript mainScript) {
        this.mainScript.set(mainScript);
    }

    public SimpleObjectProperty<ProofScript> mainScriptProperty() {
        return mainScript;
    }


    /**
     *  Create a new ProofTreeController
     *  and bind properties
     */
    public ProofTreeController() {

       /* blocker.currentStateProperty().addListener((observable, oldValue, newValue) -> {
            //setNewState(newValue);
            setBlockerState(newValue);
            LOGGER.info("The state in the Puppetmaster changed to " + newValue.toString());
        });*/


        //add listener to nextcomputed node, that is updated whenever a new node is added to the stategraph
        nextComputedNode.addListener((observable, oldValue, newValue) -> {
            //update statepointer
            if (newValue != null) {
                LOGGER.info("New node {} was computed and the statepointer was set to {}", newValue.getScriptstmt(), newValue);
                this.statePointer = newValue;

                //setNewState(blocker.currentStateProperty().get());
                setNewState(newValue.getState());
            }

        });



    }


    /**
     * Sets the properties that may notify GUI about statechanges with new state values
     * CurrentGoalsProperty and SelectedGoal are both listened by InspectionViewModel
     * @param state
     */
    private void setNewState(State<KeyData> state) {

        LOGGER.info("Setting new State " + state.toString());
        //Statepointer null wenn anfangszustand?
        if (statePointer != null && state != null) {
            //setCurrentHighlightNode(statePointer.getScriptstmt());
            //get all goals that are open
            Object[] arr = state.getGoals().stream().filter(keyDataGoalNode -> !keyDataGoalNode.isClosed()).toArray();
            //if there is no selected goal node we might have reached
            //a closed proof
            if (state.getSelectedGoalNode() == null) {
                setCurrentSelectedGoal(null);
                setCurrentGoals(arr.length == 0 ? Collections.emptyList() : state.getGoals());

            } else {
                setCurrentGoals(state.getGoals());
                setCurrentSelectedGoal(state.getSelectedGoalNode());
            }


            LOGGER.debug("New State from this command: {}@{}",
                    this.statePointer.getScriptstmt().getNodeName(),
                    this.statePointer.getScriptstmt().getStartPosition());
        } else {
            throw new RuntimeException("The state pointer was null when setting new state");
        }


    }

    //TODO handle endpoint

    private static boolean compareCtrlFlowNodes(ControlFlowNode newNode, ControlFlowNode oldNode) {
        return newNode.getScriptstmt().getNodeName().equals(oldNode.getScriptstmt().getNodeName());

    }

    //TODO handle endpoint of graph

    private static boolean comparePTreeNodes(PTreeNode newTreeNode, PTreeNode oldTreeNode) {
        return false;
    }

    /**
     * StepOver and return the node to which the state pointer is pointing to
     *
     * @return
     */
    public void stepOver() {
        //get current pointer into stategraph
        PTreeNode currentPointer = statePointer;
        //if pointer is null, we do not have a root yet
        if (currentPointer == null) {
            //ask for root
            currentPointer = stateGraphWrapper.rootProperty().get();
            statePointer = currentPointer;
            nextComputedNode.setValue(statePointer);
        }
        //get next node
        PTreeNode<KeyData> nextNode = stateGraphWrapper.getStepOver(currentPointer);

        //if nextnode is null ask interpreter to execute next statement and compute next state
        if (nextNode != null) {
            //setCurrentHighlightNode(nextNode.getScriptstmt());
        }

        if (nextNode != null && nextNode.getExtendedState().getStateAfterStmt() != null) {
            PTreeNode<KeyData> lastNode = this.statePointer;
            PTreeNode<KeyData> possibleextNode = nextNode;

            InterpreterExtendedState<KeyData> extendedStateOfStmt = possibleextNode.getExtendedState();
            //check whether we have reached an endpoint in the graph
            if (lastNode.equals(nextNode)) {
                nextComputedNode.setValue(lastNode);
            } else {
                if (extendedStateOfStmt.getStateBeforeStmt() == null || extendedStateOfStmt.getStateBeforeStmt().getGoals() == null || extendedStateOfStmt.getStateBeforeStmt().getGoals().isEmpty()) {
                    nextComputedNode.setValue(lastNode);
                } else {
                    nextComputedNode.setValue(possibleextNode);
                }
            }
        } else {
            //no next node is present yet
            //let interpreter run for one step and let listener handle updating the statepointer
            blocker.getStepUntilBlock().addAndGet(1);
            blocker.unlock();
        }
    }

    /**
     * Step Back one Node in the stategraph
     *
     * @return PTreeNode of current pointer
     */
    public void stepBack() {
        PTreeNode current = this.statePointer;
        if (current != null) {
            this.statePointer = stateGraphWrapper.getStepBack(current);
            if (this.statePointer != null) {
                setNewState(statePointer.getState());
            } else {
                this.statePointer = current;
            }
        }
    }

    public PTreeNode stepInto() {
        return null;
    }

    public PTreeNode stepReturn() {
        return null;
    }

    /**
     * Execute script with breakpoints
     * @param debugMode
     * @param statusBar
     * @param breakpoints
     */
    public void executeScript(boolean debugMode, DebuggerStatusBar statusBar, Set<Breakpoint> breakpoints) {
        breakpoints.forEach(breakpoint -> blocker.addBreakpoint(breakpoint.getLineNumber()));
        executeScript(debugMode, statusBar);
    }

    /**
     * Execute the script that is identified by the mainscript.
     * If this method is executed with debug mode true, it executes only statements after invoking the methods stepOver() and stepInto()
     *
     * @param debugMode
     * @param statusBar
     */
    public void executeScript(boolean debugMode, DebuggerStatusBar statusBar) {
        Events.register(this);

        blocker.deinstall();
        blocker.install(currentInterpreter);

        statusBar.setText("Starting to interpret script " + mainScript.getName());
        statusBar.indicateProgress();
        //setCurrentHighlightNode(mainScript.get());

        //build CFG
        buildControlFlowGraph(mainScript.get());
        statusBar.publishMessage("Controlflow graph ws build");

        //build StateGraph
        this.stateGraphWrapper = new StateGraphWrapper(currentInterpreter, mainScript.get(), this.controlFlowGraphVisitor);
        this.stateGraphWrapper.install(currentInterpreter);
        this.stateGraphWrapper.addChangeListener(graphChangedListener);

        ASTNodeHiglightListener astNodeHiglightListener = new ASTNodeHiglightListener(currentInterpreter);
        astNodeHiglightListener.install(currentInterpreter);
        astNodeHiglightListener.currentHighlightNodeProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ASTNode astNode = (ASTNode) newValue;
                setCurrentHighlightNode(astNode);
                //this.setCurrentHighlightNode();
            }
        });

        statusBar.publishMessage("Stategraph was set up");
        statusBar.stopProgress();

        //create interpreter service and start
        if (interpreterService.getState() == Worker.State.SUCCEEDED
                || interpreterService.getState() == Worker.State.CANCELLED) {
            interpreterService.reset();
        }
        interpreterService.interpreterProperty().set(currentInterpreter);

        interpreterService.mainScriptProperty().set(mainScript.get());

        interpreterService.start();
        interpreterService.setOnSucceeded(event -> {
            statusBar.setText("Executed until end of script.");
            System.out.println("Number of Goals " + currentGoals.get().size());
            //TODO is this the right position??
            if (currentGoals.isEmpty()) {

                Utils.showClosedProofDialog(mainScript.get().getName());
            }
            statusBar.stopProgress();
        });
        interpreterService.setOnFailed(event -> {
            statusBar.setText("Failed to execute script");
            statusBar.stopProgress();
        });
    }

    /**
     * Build the control flow graph for looking up step-edges for the given script inligning called script commands
     *
     * @param mainScript
     */
    private void buildControlFlowGraph(ProofScript mainScript) {
        this.controlFlowGraphVisitor = new ControlFlowVisitor(currentInterpreter.getFunctionLookup());
        mainScript.accept(controlFlowGraphVisitor);
        this.setMainScript(mainScript);
        LOGGER.debug("CFG\n" + controlFlowGraphVisitor.asdot());

    }

    /**
     * Handle the event that the script was modified
     *
     * @param mod
     */
    @Subscribe
    public void handle(Events.ScriptModificationEvent mod) {
        LOGGER.debug("ProofTreeController.handleScriptModificationEvent");
        System.out.println("Handling ScriptCommand");
        currentInterpreter.visit(mod.getCs());
        this.setCurrentSelectedGoal(currentInterpreter.getSelectedNode());
        this.setCurrentGoals(currentInterpreter.getCurrentGoals());
    }

    /**
     * Save all data structures to compare before reexecution
     */
    public void saveGraphs() {
        MutableValueGraph stateGraph = stateGraphWrapper.getStateGraph();
        MutableValueGraph ctrlFlow = controlFlowGraphVisitor.getGraph();
    }

    /**************************************************************************************************************
     *
     *                                              Getter and Setter
     *
     *************************************************************************************************************
     * @param currentInterpreter*/

    public void setCurrentInterpreter(KeyInterpreter currentInterpreter) {
        this.currentInterpreter = currentInterpreter;
    }

    public StateGraphWrapper getStateVisitor() {
        return this.stateGraphWrapper;
    }

    public List<GoalNode<KeyData>> getCurrentGoals() {
        return currentGoals.get();
    }

    public void setCurrentGoals(List<GoalNode<KeyData>> currentGoals) {
        this.currentGoals.get().setAll(currentGoals);
    }

    public ListProperty<GoalNode<KeyData>> currentGoalsProperty() {
        return currentGoals;
    }

    public GoalNode<KeyData> getCurrentSelectedGoal() {
        return currentSelectedGoal.get();
    }

    public void setCurrentSelectedGoal(GoalNode<KeyData> currentSelectedGoal) {
        this.currentSelectedGoal.set(currentSelectedGoal);
    }

    public SimpleObjectProperty<GoalNode<KeyData>> currentSelectedGoalProperty() {
        return currentSelectedGoal;
    }

    public boolean isExecuteNotPossible() {
        return executeNotPossible.get();
    }

    public ReadOnlyBooleanProperty executeNotPossibleProperty() {
        return executeNotPossible;
    }

    public ASTNode getCurrentHighlightNode() {
        return currentHighlightNode.get();
    }

    public void setCurrentHighlightNode(ASTNode currentHighlightNode) {
        this.currentHighlightNode.set(currentHighlightNode);
    }

    public ObjectProperty<ASTNode> currentHighlightNodeProperty() {
        return currentHighlightNode;
    }


    public boolean isAlreadyExecuted() {
        return alreadyExecuted.get();
    }

    public PTreeNode getNextComputedNode() {
        return nextComputedNode.get();
    }

    public void setNextComputedNode(PTreeNode nextComputedNode) {
        this.nextComputedNode.set(nextComputedNode);
    }

    public SimpleObjectProperty<PTreeNode> nextComputedNodeProperty() {
        return nextComputedNode;
    }
   /* public ReadOnlyBooleanProperty stepNotPossibleProperty() {

    }*/
}
