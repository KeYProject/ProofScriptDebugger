package edu.kit.formal.dbg;

import edu.kit.formal.proofscriptparser.DefaultASTVisitor;
import edu.kit.formal.proofscriptparser.ast.ASTNode;

import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by weigl on 21.05.2017.
 */
public class Blocker extends DefaultASTVisitor<Void> {
    public AtomicInteger stepUntilBlock = new AtomicInteger(-1);
    //needs to threadable
    public Set<Integer> brkpnts = new TreeSet<>();
    public final Lock lock = new ReentrantLock();
    public final Condition block = lock.newCondition();

    @Override
    public Void defaultVisit(ASTNode node) {
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
}
