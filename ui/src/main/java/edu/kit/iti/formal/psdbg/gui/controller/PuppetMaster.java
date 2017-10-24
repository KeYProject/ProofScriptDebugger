package edu.kit.iti.formal.psdbg.gui.controller;

import edu.kit.iti.formal.psdbg.interpreter.HistoryListener;
import edu.kit.iti.formal.psdbg.interpreter.Interpreter;
import edu.kit.iti.formal.psdbg.interpreter.KeyInterpreter;
import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.data.State;
import edu.kit.iti.formal.psdbg.parser.DefaultASTVisitor;
import edu.kit.iti.formal.psdbg.parser.Visitor;
import edu.kit.iti.formal.psdbg.parser.ast.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by weigl on 21.05.2017.
 */
public class PuppetMaster {
    private final Lock lock = new ReentrantLock();
    private final Condition block = lock.newCondition();
    /**
     * Properties that are changed, when new states are added using the blocker
     */
    private final SimpleObjectProperty<List<GoalNode<KeyData>>> currentGoals = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<GoalNode<KeyData>> currentSelectedGoal = new SimpleObjectProperty<>();

    private final SimpleObjectProperty<State<KeyData>> currentState = new SimpleObjectProperty<>();

    public State<KeyData> getCurrentState() {
        return currentState.get();
    }

    public void setCurrentState(State<KeyData> currentState) {
        this.currentState.set(currentState);
    }

    public SimpleObjectProperty<State<KeyData>> currentStateProperty() {
        return currentState;
    }
    private KeyInterpreter puppet;
    private AtomicInteger stepUntilBlock = new AtomicInteger(-1);
    private HistoryListener historyLogger;
    private Set<Integer> brkpnts = new ConcurrentSkipListSet<>();
    private Visitor<Void> entryListener = new EntryListener();
    private Visitor<Void> exitListener = new ExitListener();

    public PuppetMaster() {
    }

    public PuppetMaster(Interpreter<KeyData> inter) {
        install(puppet);
    }

    public SimpleObjectProperty<List<GoalNode<KeyData>>> currentGoalsProperty() {
        return currentGoals;
    }

    public HistoryListener getHistoryLogger() {
        return historyLogger;
    }

    public void install(KeyInterpreter interpreter) {
        if (puppet != null) deinstall(puppet);
        interpreter.getEntryListeners().add(entryListener);
        interpreter.getEntryListeners().add(exitListener);
        puppet = interpreter;
    }

    public void deinstall(Interpreter<KeyData> interpreter) {
        if (interpreter != null) {
            interpreter.getEntryListeners().remove(entryListener);
            interpreter.getEntryListeners().remove(exitListener);
        }
    }

    public Void checkForHalt(ASTNode node) {
        System.out.println("node = [" + node + "]");

        //<0 run
        if (stepUntilBlock.get() > 0)
            stepUntilBlock.decrementAndGet();
        System.out.println("Blocker: " + stepUntilBlock.get());
        if (stepUntilBlock.get() == 0) {
            publishState();
            block();
        }

        int lineNumber = node.getStartPosition().getLineNumber();
        if (brkpnts.contains(lineNumber)) {
            publishState();
            block();
        }
        return null;
    }

    /**
     * Publish state is called after the interpreter or debugger thread terminated. The resulting goals are set in the root model
     */
    public void publishState() {
        //("PuppetMaster.publishState");
        //puppet is null if successful interpreter state and publish state
        if (puppet != null) {

            final State<KeyData> state = puppet.getCurrentState().copy();
            this.setCurrentState(state);

            Platform.runLater(() -> {


                //filter whether all goals are closed
                Object[] arr = state.getGoals().stream().filter(keyDataGoalNode -> !keyDataGoalNode.isClosed()).toArray();
                currentState.set(state);
                if (state.getSelectedGoalNode() == null) {
                    if (arr.length == 0) {
                        currentGoals.set(Collections.emptyList());
                        //currentSelectedGoal.set(state.getGoals().get(0));
                        currentSelectedGoal.set(null);
                    } else {
                        currentGoals.set(state.getGoals());
                        currentSelectedGoal.set(null);
                    }
                } else {
                    currentGoals.set(state.getGoals());
                    currentSelectedGoal.set(state.getSelectedGoalNode());
                }

            });
        } else {
            //if puppet is null an empty state may be reached therefore state get goals etc returns null

            Platform.runLater(() -> {
                setCurrentState(new State<KeyData>(Collections.emptyList(), null));
                currentGoals.set(Collections.emptyList());
                currentSelectedGoal.set(null);
            });

        }

    }

    /**
     * Blocks the current thread. Makes him awakable on {@code block}.
     */
    private void block() {
        try {
            lock.lock();
            block.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void addBreakpoint(int i) {
        brkpnts.add(i);
    }

    public void unlock() {
        try {
            lock.lock();
            block.signal();
        } finally {
            lock.unlock();
        }
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

    public List<GoalNode<KeyData>> getCurrentGoals() {
        return currentGoals.get();
    }

    public void setCurrentGoals(List<GoalNode<KeyData>> currentGoals) {
        this.currentGoals.set(currentGoals);
    }

  /*  public SimpleObjectProperty<List<GoalNode<KeyData>>> currentGoalsProperty() {
        return currentGoals;
    }*/

    public Set<Integer> getBreakpoints() {
        return brkpnts;
    }

    public AtomicInteger getStepUntilBlock() {
        return stepUntilBlock;
    }

    public void deinstall() {
        deinstall(puppet);
    }

    public void addHistoryLogger(HistoryListener historyLogger) {
        this.historyLogger = historyLogger;
    }

    private class EntryListener extends DefaultASTVisitor<Void> {

        @Override
        public Void visit(ProofScript proofScript) {
            return checkForHalt(proofScript);
        }

        @Override
        public Void visit(AssignmentStatement assignment) {
            return checkForHalt(assignment);
        }

        @Override
        public Void visit(CasesStatement casesStatement) {
            return checkForHalt(casesStatement);
        }

        @Override
        public Void visit(CaseStatement caseStatement) {
            return checkForHalt(caseStatement);
        }

        @Override
        public Void visit(CallStatement call) {
            return checkForHalt(call);
        }

        @Override
        public Void visit(TheOnlyStatement theOnly) {
            return checkForHalt(theOnly);
        }

        @Override
        public Void visit(ForeachStatement foreach) {
            return checkForHalt(foreach);
        }

        @Override
        public Void visit(RepeatStatement repeatStatement) {
            return checkForHalt(repeatStatement);
        }
    }

    private class ExitListener extends DefaultASTVisitor<Void> {

    }
}
