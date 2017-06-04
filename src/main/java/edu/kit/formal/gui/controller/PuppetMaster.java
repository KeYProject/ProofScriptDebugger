package edu.kit.formal.gui.controller;

import edu.kit.formal.interpreter.Interpreter;
import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.KeyData;
import edu.kit.formal.interpreter.data.State;
import edu.kit.formal.proofscriptparser.DefaultASTVisitor;
import edu.kit.formal.proofscriptparser.Visitor;
import edu.kit.formal.proofscriptparser.ast.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;

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
    private Interpreter<KeyData> puppet;

    private AtomicInteger stepUntilBlock = new AtomicInteger(-1);
    private Set<Integer> brkpnts = new ConcurrentSkipListSet<>();
    private final Lock lock = new ReentrantLock();
    private final Condition block = lock.newCondition();

    private final SimpleObjectProperty<List<GoalNode<KeyData>>> currentGoals = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<GoalNode<KeyData>> currentSelectedGoal = new SimpleObjectProperty<>();


    private Visitor<Void> entryListener = new EntryListener();
    private Visitor<Void> exitListener = new ExitListener();

    public PuppetMaster() {
    }

    public PuppetMaster(Interpreter<KeyData> inter) {
        install(puppet);
    }

    public void install(Interpreter<KeyData> interpreter) {
        if (puppet != null) deinstall(puppet);
        interpreter.getEntryListeners().add(entryListener);
        interpreter.getEntryListeners().add(exitListener);
        puppet = interpreter;
    }

    public void deinstall(Interpreter<KeyData> interpreter) {
        interpreter.getEntryListeners().remove(entryListener);
        interpreter.getEntryListeners().remove(exitListener);
    }

    public Void checkForHalt(ASTNode node) {
        if (stepUntilBlock.get() > 0)
            stepUntilBlock.decrementAndGet();

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

    private void publishState() {
        System.out.println("PuppetMaster.publishState");
        final State<KeyData> state = puppet.getCurrentState().copy();
        Platform.runLater(() -> {
            currentGoals.set(state.getGoals());
            currentSelectedGoal.set(state.getSelectedGoalNode());
        });
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

    public SimpleObjectProperty<GoalNode<KeyData>> currentSelectedGoalProperty() {
        return currentSelectedGoal;
    }

    public void setCurrentSelectedGoal(GoalNode<KeyData> currentSelectedGoal) {
        this.currentSelectedGoal.set(currentSelectedGoal);
    }

    public List<GoalNode<KeyData>> getCurrentGoals() {
        return currentGoals.get();
    }

    public SimpleObjectProperty<List<GoalNode<KeyData>>> currentGoalsProperty() {
        return currentGoals;
    }

    public void setCurrentGoals(List<GoalNode<KeyData>> currentGoals) {
        this.currentGoals.set(currentGoals);
    }

    public Set<Integer> getBreakpoints() {
        return brkpnts;
    }

    public AtomicInteger getStepUntilBlock() {
        return stepUntilBlock;
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
