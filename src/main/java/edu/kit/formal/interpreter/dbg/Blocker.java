package edu.kit.formal.interpreter.dbg;

import edu.kit.formal.proofscriptparser.DefaultASTVisitor;
import edu.kit.formal.proofscriptparser.ast.*;

import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by weigl on 21.05.2017.
 */
public class Blocker extends DefaultASTVisitor<Void> {
    AtomicInteger stepUntilBlock = new AtomicInteger(-1);
    //needs to threadable
    Set<Integer> brkpnts = new TreeSet<>();
    final Lock lock = new ReentrantLock();
    final Condition block = lock.newCondition();

    //better a semaphore?
    //Semaphore semaphore = new Semaphore();

    public Void checkForHalt(ASTNode node) {
        if (stepUntilBlock.get() > 0)
            stepUntilBlock.decrementAndGet();

        if (stepUntilBlock.get() == 0)
            block();

        int lineNumber = node.getStartPosition().getLineNumber();
        if (brkpnts.contains(lineNumber)) {
            block();
        }

        return super.defaultVisit(node);
    }

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
