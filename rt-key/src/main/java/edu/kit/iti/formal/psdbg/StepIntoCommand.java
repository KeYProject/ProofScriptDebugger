package edu.kit.iti.formal.psdbg;

import edu.kit.iti.formal.psdbg.interpreter.dbg.*;

public class StepIntoCommand<T> extends DebuggerCommand<T> {
    @Override
    public void execute(DebuggerFramework<T> dbg) {
        PTreeNode<T> statePointer = dbg.getStatePointer();
        assert statePointer != null;
        if (statePointer.isAtomic()) {
            new StepOverCommand<T>().execute(dbg);
        } else {
            dbg.releaseUntil(new Blocker.CounterBlocker(1));
        }
    }
}
