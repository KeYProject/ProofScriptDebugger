package edu.kit.formal.interpreter;

import edu.kit.formal.gui.controller.PuppetMaster;
import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.KeyData;
import edu.kit.formal.proofscriptparser.Visitor;
import edu.kit.formal.proofscriptparser.ast.ProofScript;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

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
    private final SimpleObjectProperty<List<GoalNode<KeyData>>> currentGoals = blocker.currentGoalsProperty();
    private final SimpleObjectProperty<GoalNode<KeyData>> currentSelectedGoal = blocker.currentSelectedGoalProperty();
    //new SimpleBooleanProperty(false);
    private InterpretingService interpreterService = new InterpretingService(blocker);
    private ReadOnlyBooleanProperty executeNotPossible = interpreterService.runningProperty();
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

    public ProofTreeController() {


    }

    private ProofTreeController(Interpreter<KeyData> inter, ProofScript mainScript) {
        blocker.deinstall();

        this.currentInterpreter = inter;
        buildControlFlowGraph(mainScript);
        this.stateGraphVisitor = new StateGraphVisitor(inter, mainScript, controlFlowGraphVisitor);
        //statePointer = stateGraphVisitor.getRoot();

        blocker.getStepUntilBlock().set(1);
        blocker.install(currentInterpreter);
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

    public PTreeNode stepOver() {
        PTreeNode currentPointer = statePointer;
        if (statePointer == null) {
            System.out.println("StatePointer is null");
        } else {
            PTreeNode nextNode = stateGraphVisitor.getStepOver(currentPointer);
            if (nextNode != null) {
                this.statePointer = nextNode;
            } else {
                //let interpreter run for one step
                blocker.getStepUntilBlock().addAndGet(1);
                blocker.unlock();
                //sync problem???
                //TODO make this right!
                //TODO handle endpoint of graph
                nextNode = stateGraphVisitor.getStepOver(currentPointer);
                while (nextNode == null) {
                    nextNode = stateGraphVisitor.getStepOver(currentPointer);
                }
                this.statePointer = nextNode;
            }
            System.out.println("NEXT NODE \n\n" + this.statePointer + "\n-------------------\n");
        }


        return null;
    }

    //TODO handle endpoint of graph
    public PTreeNode stepBack() {
        PTreeNode current = statePointer;
        this.statePointer = stateGraphVisitor.getStepBack(current);
        System.out.println("PRED NODE \n\n" + this.statePointer + "\n-------------------\n");
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
