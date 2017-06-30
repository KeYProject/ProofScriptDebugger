package edu.kit.formal.interpreter;

import edu.kit.formal.gui.controller.PuppetMaster;
import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.KeyData;
import edu.kit.formal.proofscriptparser.Visitor;
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
    private final SimpleObjectProperty<List<GoalNode<KeyData>>> currentGoals = new SimpleObjectProperty<>();
    //= blocker.currentGoalsProperty();
    private final ListProperty<GoalNode<KeyData>> currentGoalList = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final SimpleObjectProperty<GoalNode<KeyData>> currentSelectedGoal = new SimpleObjectProperty<>();
    //= blocker.currentSelectedGoalProperty();
    //new SimpleBooleanProperty(false);
    private InterpretingService interpreterService = new InterpretingService(blocker);
    private ReadOnlyBooleanProperty executeNotPossible = interpreterService.runningProperty();

    private SimpleObjectProperty<PTreeNode> nextComputedNode = new SimpleObjectProperty<>();

    /**
     * Visitor to retrieve state graph
     */
    private StateGraphVisitor stateGraphVisitor;
    /**
     * Graph that is computed on the fly in order to allow stepping
     */

    private ProgramFlowVisitor controlFlowGraphVisitor;


    /**
     * Current interpreter
     */
    private Interpreter<KeyData> currentInterpreter;

    /**
     * Current State in graph
     */
    private PTreeNode statePointer = null;


    private ProofScript mainScript;

    /**
     * Add a change listener for the stategraph
     */
    private GraphChangedListener graphChangedListener = nodeAddedEvent -> {

        if (statePointer.equals(nodeAddedEvent.getLastPointer())) {
            nextComputedNode.setValue(nodeAddedEvent.getAddedNode());

        }

    };

    public ProofTreeController() {
        this.currentGoals.bindBidirectional(blocker.currentGoalsProperty());
        this.currentSelectedGoal.bindBidirectional(blocker.currentSelectedGoalProperty());

    }


    /**
     * Build the control flow graph for looking up step-edges for the given script inligning called script commands
     *
     * @param mainScript
     */
    private void buildControlFlowGraph(ProofScript mainScript) {
        this.controlFlowGraphVisitor = new ProgramFlowVisitor(currentInterpreter.getFunctionLookup());
        mainScript.accept(controlFlowGraphVisitor);
        System.out.println("CFG\n" + controlFlowGraphVisitor.asdot());

    }

    //TODO handle endpoint
    //handle inconsistency
    public PTreeNode stepOver() {
        PTreeNode currentPointer = statePointer;

        if (currentPointer == null) {
            System.out.println("StatePointer is null");
        } else {
            if (this.statePointer.getState() != null && this.statePointer.getScriptstmt() != null) {
                System.out.println("StatePointer is " + currentPointer.getScriptstmt().getNodeName() + currentPointer.getScriptstmt().getStartPosition());
            } else {
                System.out.println("Possibly root node");
            }
            PTreeNode nextNode = stateGraphVisitor.getStepOver(currentPointer);

            if (nextNode != null) {
                this.statePointer = nextNode;
            } else {
                nextComputedNode.addListener((observable, oldValue, newValue) -> {
                    this.statePointer = newValue;
                    System.out.println("Added new Value to Statepointer: " + this.statePointer.getScriptstmt().getNodeName() + this.statePointer.getScriptstmt().getStartPosition());
                });
                //let interpreter run for one step
                blocker.getStepUntilBlock().addAndGet(1);
                blocker.unlock();

                //stateGraphListener

                //this.statePointer = nextNode;
            }

        }


        return null;
    }

    //TODO handle endpoint of graph
    public PTreeNode stepBack() {
        PTreeNode current = statePointer;
        this.statePointer = stateGraphVisitor.getStepBack(current);
        if (this.statePointer == null) {

        } else {
            this.currentGoals.set(this.statePointer.getState().getGoals());
            this.currentSelectedGoal.set(this.statePointer.getState().getSelectedGoalNode());
        }
        System.out.println("PRED NODE \n\n" + this.statePointer.getState().getSelectedGoalNode().getData().getNode().serialNr() + ":" +
                this.statePointer.getScriptstmt().getNodeName() + "@" + this.statePointer.getScriptstmt().getStartPosition() + "\n-------------------\n");
        return null;
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
            //build CFG
            buildControlFlowGraph(mainScript);
            //build StateGraph
            this.stateGraphVisitor = new StateGraphVisitor(this.currentInterpreter, this.mainScript, this.controlFlowGraphVisitor);
            //  this.stateGraphVisitor = new StateGraphVisitor(blocker, this.mainScript, this.controlFlowGraphVisitor);

            currentInterpreter.getEntryListeners().add(this.stateGraphVisitor);
            this.stateGraphVisitor.addChangeListener(graphChangedListener);
            //
            // stateGraphVisitor.rootProperty().addListener((observable, oldValue, newValue) -> {
            //     this.statePointer = newValue;
            // });
            this.statePointer = stateGraphVisitor.getRoot();

            blocker.getStepUntilBlock().set(1);
        }
        interpreterService.interpreterProperty().set(currentInterpreter);
        interpreterService.mainScriptProperty().set(mainScript);

        interpreterService.start();


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

    public Visitor getStateVisitor() {
        return this.stateGraphVisitor;
    }

    public List<GoalNode<KeyData>> getCurrentGoals() {
        return currentGoals.get();
    }

    public void setCurrentGoals(List<GoalNode<KeyData>> currentGoals) {
        this.currentGoals.set(currentGoals);
    }

    public SimpleObjectProperty<List<GoalNode<KeyData>>> currentGoalsProperty() {
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


}
