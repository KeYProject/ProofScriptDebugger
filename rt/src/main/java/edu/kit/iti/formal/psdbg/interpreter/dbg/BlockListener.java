package edu.kit.iti.formal.psdbg.interpreter.dbg;

import edu.kit.iti.formal.psdbg.interpreter.HistoryListener;
import edu.kit.iti.formal.psdbg.interpreter.Interpreter;
import edu.kit.iti.formal.psdbg.parser.DefaultASTVisitor;
import edu.kit.iti.formal.psdbg.parser.Visitor;
import edu.kit.iti.formal.psdbg.parser.ast.*;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by weigl on 21.05.2017.
 */
public class BlockListener<T> {
    private static Logger LOGGER = LogManager.getLogger(BlockListener.class);

    private final Lock lock = new ReentrantLock();
    private final Condition block = lock.newCondition();
    @Getter
    private final Set<Integer> breakpoints = new TreeSet<>();
    private Interpreter<T> puppet;
    private AtomicInteger stepUntilBlock = new AtomicInteger(-1);
    private Visitor<Void> entryListener = new EntryListener();
    private Visitor<Void> exitListener = new ExitListener();

    public BlockListener() {
    }

    public BlockListener(Interpreter<T> inter) {
        install(puppet);
    }

    public void install(Interpreter<T> interpreter) {
        if (puppet != null) deinstall(puppet);
        interpreter.getEntryListeners().add(entryListener);
        interpreter.getEntryListeners().add(exitListener);
        puppet = interpreter;
    }

    public void deinstall(Interpreter<T> interpreter) {
        if (interpreter != null) {
            interpreter.getEntryListeners().remove(entryListener);
            interpreter.getEntryListeners().remove(exitListener);
        }
    }

    public Void checkForHalt(ASTNode node) {
        //("BlockListener CheckForHalt node = [" + node + "]");

        //<0 run
        if (stepUntilBlock.get() > 0)
            stepUntilBlock.decrementAndGet();

        if (stepUntilBlock.get() == 0) {
            //publishState();
            block();
        }

        int lineNumber = node.getStartPosition().getLineNumber();

        if (breakpoints.contains(lineNumber)) {
            //publishState();
            block();
        }
        return null;
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
        breakpoints.add(i);
    }

    public void unlock() {
        try {
            lock.lock();
            block.signal();
        } finally {
            lock.unlock();
        }
    }

    public void deinstall() {
        deinstall(puppet);
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
        public Void visit(MatchExpression matchExpression) {
//           System.out.println("Checkforhalt matchexpression");
            return checkForHalt(matchExpression);
        }

        @Override
        public Void visit(CallStatement call) {
            //          System.out.println("Checkforhalt callstatement");
            return checkForHalt(call);
        }

        @Override
        public Void visit(SimpleCaseStatement simpleCaseStatement) {
            return checkForHalt(simpleCaseStatement);
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
