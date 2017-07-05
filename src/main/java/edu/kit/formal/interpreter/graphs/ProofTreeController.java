package edu.kit.formal.interpreter.graphs;

import edu.kit.formal.gui.controller.PuppetMaster;
import edu.kit.formal.interpreter.Interpreter;
import edu.kit.formal.interpreter.InterpretingService;
import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.KeyData;
import edu.kit.formal.interpreter.data.State;
import edu.kit.formal.proofscriptparser.ast.Position;
import edu.kit.formal.proofscriptparser.ast.ProofScript;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;

import java.util.List;

/**
 * Class controlling and maintaining proof tree structure for debugger and handling step functions for the debugger
 *
 * @author S. Grebing
 */
public class ProofTreeController {

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

    /**
     * To identify when interpreterservice is running
     */
    private ReadOnlyBooleanProperty executeNotPossible = interpreterService.runningProperty();

    /**
     * Node that is updated whenever a new node is added to the stategraph (this can only happen in debug mode when stepover is invoked)
     */
    private SimpleObjectProperty<PTreeNode> nextComputedNode = new SimpleObjectProperty<>();


    private SimpleObjectProperty<Position> startHighlightPositionProperty = new SimpleObjectProperty<>();

    private SimpleObjectProperty<Position> endHighlightPositionProperty = new SimpleObjectProperty<>();

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

    public ProofTreeController() {

        //get state from blocker, who communicates with interpreter
        this.currentSelectedGoal.bindBidirectional(blocker.currentSelectedGoalProperty());

        //add listener to nextcomputed node, that is updated whenever a new node is added to the stategraph
        nextComputedNode.addListener((observable, oldValue, newValue) -> {
            //update statepointer
            this.statePointer = newValue;
            setNewState(this.statePointer.getState());

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

    //TODO handle endpoint

    /**
     * StepOver and return the node to shich the state pointer is pointing to
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
        //if nextnode is null ask interpreter
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
     * @return
     */
    public PTreeNode stepBack() {
        PTreeNode current = statePointer;
        this.statePointer = stateGraphWrapper.getStepBack(current);
        setNewState(statePointer.getState());
        //setHighlightStmt(this.statePointer.getScriptstmt().getStartPosition(), this.statePointer.getScriptstmt().getStartPosition());
        return statePointer;

    }

    public PTreeNode stepInto() {
        return null;
    }

    public PTreeNode stepReturn() {
        return null;
    }


    public void executeScript(boolean debugMode) {
        blocker.deinstall();
        blocker.install(currentInterpreter);
        if (!debugMode) {
            blocker.getStepUntilBlock().set(-1);
        } else {
            this.startHighlightPositionProperty.set(mainScript.getSignature().getStartPosition());
            this.endHighlightPositionProperty.set(mainScript.getSignature().getEndPosition());
            //build CFG
            buildControlFlowGraph(mainScript);
            //build StateGraph
            this.stateGraphWrapper = new StateGraphWrapper(currentInterpreter, this.mainScript, this.controlFlowGraphVisitor);

            this.stateGraphWrapper.install(currentInterpreter);
            this.stateGraphWrapper.addChangeListener(graphChangedListener);
            blocker.getStepUntilBlock().set(1);
        }
        interpreterService.interpreterProperty().set(currentInterpreter);
        interpreterService.mainScriptProperty().set(mainScript);

        interpreterService.start();


    }

    /**
     * Sets the properties that may notify GUI about statechanges with new state values
     *
     * @param state
     */
    private void setNewState(State<KeyData> state) {
        this.setCurrentGoals(state.getGoals());
        this.setCurrentSelectedGoal(state.getSelectedGoalNode());
        setHighlightStmt(this.statePointer.getScriptstmt().getStartPosition(), this.statePointer.getScriptstmt().getStartPosition());
        System.out.println("New State from this command: " + this.statePointer.getScriptstmt().getNodeName() + "@" + this.statePointer.getScriptstmt().getStartPosition());
    }

    /**
     * Set Position for highlighting statement
     *
     * @param start
     * @param end
     */
    private void setHighlightStmt(Position start, Position end) {

        this.startHighlightPositionProperty.set(start);
        this.endHighlightPositionProperty.set(end);


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

    public Position getStartHighlightPositionProperty() {
        return startHighlightPositionProperty.get();
    }

    public void setStartHighlightPositionProperty(Position startHighlightPositionProperty) {
        this.startHighlightPositionProperty.set(startHighlightPositionProperty);
    }

    public SimpleObjectProperty<Position> startHighlightPositionPropertyProperty() {
        return startHighlightPositionProperty;
    }

    public Position getEndHighlightPositionProperty() {
        return endHighlightPositionProperty.get();
    }

    public void setEndHighlightPositionProperty(Position endHighlightPositionProperty) {
        this.endHighlightPositionProperty.set(endHighlightPositionProperty);
    }

    public SimpleObjectProperty<Position> endHighlightPositionPropertyProperty() {
        return endHighlightPositionProperty;
    }

}
