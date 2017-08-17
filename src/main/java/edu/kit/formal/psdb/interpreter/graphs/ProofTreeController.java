package edu.kit.formal.psdb.interpreter.graphs;

import com.google.common.eventbus.Subscribe;
import edu.kit.formal.psdb.gui.controller.Events;
import edu.kit.formal.psdb.gui.controller.PuppetMaster;
import edu.kit.formal.psdb.gui.controls.DebuggerStatusBar;
import edu.kit.formal.psdb.interpreter.Interpreter;
import edu.kit.formal.psdb.interpreter.InterpretingService;
import edu.kit.formal.psdb.interpreter.data.GoalNode;
import edu.kit.formal.psdb.interpreter.data.KeyData;
import edu.kit.formal.psdb.interpreter.data.State;
import edu.kit.formal.psdb.parser.ast.ASTNode;
import edu.kit.formal.psdb.parser.ast.ProofScript;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.concurrent.Worker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
     * Node that is updated whenever a new node is added to the stategraph (this can only happen in debug mode when stepover is invoked)
     */
    private SimpleObjectProperty<PTreeNode> nextComputedNode = new SimpleObjectProperty<>();

    /**
     * Instead of start and end position.
     */
    private ObjectProperty<ASTNode> currentHighlightNode = new SimpleObjectProperty<>();


    /**
     * Visitor to retrieve state graph
     */
    private StateGraphWrapper stateGraphWrapper;
    /**
     * Graph that is computed on the fly in order to allow stepping
     */

    private ControlFlowVisitor controlFlowGraphVisitor;


    /**
     * Current interpreter
     */
    private Interpreter<KeyData> currentInterpreter;

    /**
     * Pointer to current selected state in graph
     */
    private PTreeNode statePointer = null;

    /**
     * Teh mainscipt that is executed
     */
    private ProofScript mainScript;

    /**
     * Add a change listener for the stategraph, whenever a new node is added it receives an event
     */
    private GraphChangedListener graphChangedListener = nodeAddedEvent -> {

        if (statePointer.equals(nodeAddedEvent.getLastPointer())) {
            //set value of newly computed node
            nextComputedNode.setValue(nodeAddedEvent.getAddedNode());
            //setNewState(this.statePointer.getState());

        }

    };

    /**
     *  Create a new ProofTreeController
     *  and bind properties
     */
    public ProofTreeController() {

        //get state from blocker, who communicates with interpreter
        //this.currentSelectedGoal.bindBidirectional(blocker.currentSelectedGoalProperty());
        blocker.currentSelectedGoalProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                this.setCurrentSelectedGoal(newValue);
            });
        });
        blocker.currentGoalsProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                this.setCurrentGoals(newValue);
            });
        });

        //add listener to nextcomputed node, that is updated whenever a new node is added to the stategraph
        nextComputedNode.addListener((observable, oldValue, newValue) -> {
            //update statepointer
            this.statePointer = newValue;
            setNewState(this.statePointer.getState());

        });


    }

    /**
     * Sets the properties that may notify GUI about statechanges with new state values
     *
     * @param state
     */
    private void setNewState(State<KeyData> state) {
        setCurrentGoals(state == null ? null : state.getGoals());
        setCurrentSelectedGoal(state == null ? null : state.getSelectedGoalNode());
        setCurrentHighlightNode(statePointer.getScriptstmt());
        LOGGER.debug("New State from this command: {}@{}",
                this.statePointer.getScriptstmt().getNodeName(),
                this.statePointer.getScriptstmt().getStartPosition());
    }

    //TODO handle endpoint

    /**
     * StepOver and return the node to which the state pointer is pointing to
     *
     * @return
     */
    public PTreeNode stepOver() {
        //get current pointer into stategraph
        PTreeNode currentPointer = statePointer;
        //if pointer is null, we do not have a root yet
        if (currentPointer == null) {
            //ask for root
            currentPointer = stateGraphWrapper.rootProperty().get();
            statePointer = currentPointer;

        }
        //get next node
        PTreeNode nextNode = stateGraphWrapper.getStepOver(currentPointer);
        //if nextnode is null ask interpreter to execute next statement and compute next state
        if (nextNode != null) {
            this.statePointer = nextNode;
            setNewState(statePointer.getState());
            //  setHighlightStmt(this.statePointer.getScriptstmt().getStartPosition(), this.statePointer.getScriptstmt().getStartPosition());
        } else {
            //no next node is present yet
            //let interpreter run for one step and let listener handle updating the statepointer
            blocker.getStepUntilBlock().addAndGet(1);
            blocker.unlock();
        }
        return statePointer;
    }

    //TODO handle endpoint of graph

    /**
     * Step Back one Node in the stategraph
     *
     * @return PTreeNode of current pointer
     */
    public PTreeNode stepBack() {
        PTreeNode current = this.statePointer;
        if (current != null) {
            this.statePointer = stateGraphWrapper.getStepBack(current);
            if (this.statePointer != null) {
                setNewState(statePointer.getState());
            } else {
                this.statePointer = current;
            }
        }
        //setHighlightStmt(this.statePointer.getScriptstmt().getStartPosition(), this.statePointer.getScriptstmt().getStartPosition());
        return statePointer;

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

        if (!debugMode) {
            statusBar.setText("Starting in execution mode for script " + mainScript.getName());
            statusBar.indicateProgress();
            blocker.getStepUntilBlock().set(-1);
        } else {
            statusBar.setText("Starting in debug mode for script " + mainScript.getName());
            statusBar.indicateProgress();
            setCurrentHighlightNode(mainScript.getSignature());

            //build CFG
            buildControlFlowGraph(mainScript);
            //build StateGraph
            this.stateGraphWrapper = new StateGraphWrapper(currentInterpreter, this.mainScript, this.controlFlowGraphVisitor);

            this.stateGraphWrapper.install(currentInterpreter);
            this.stateGraphWrapper.addChangeListener(graphChangedListener);

            blocker.getStepUntilBlock().set(1);
        }

        //create interpreter service and start
        if (interpreterService.getState() == Worker.State.SUCCEEDED || interpreterService.getState() == Worker.State.CANCELLED) {
            interpreterService.reset();
        }
        interpreterService.interpreterProperty().set(currentInterpreter);
        interpreterService.mainScriptProperty().set(mainScript);
        interpreterService.start();
        interpreterService.setOnSucceeded(event -> {
            statusBar.setText("Executed script until end of script.");
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
        System.out.println("CFG\n" + controlFlowGraphVisitor.asdot());

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


    private boolean compareCtrlFlowNodes(ControlFlowNode newNode, ControlFlowNode oldNode) {
        return newNode.getScriptstmt().getNodeName().equals(oldNode.getScriptstmt().getNodeName());

    }

    private boolean comparePTreeNodes(PTreeNode newTreeNode, PTreeNode oldTreeNode) {

        return false;
    }

    /**************************************************************************************************************
     *
     *                                              Getter and Setter
     *
     **************************************************************************************************************/

    public void setCurrentInterpreter(Interpreter<KeyData> currentInterpreter) {
        this.currentInterpreter = currentInterpreter;
    }

    public void setMainScript(ProofScript mainScript) {
        this.mainScript = mainScript;

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

}
