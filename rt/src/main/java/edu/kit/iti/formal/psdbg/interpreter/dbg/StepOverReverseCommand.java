package edu.kit.iti.formal.psdbg.interpreter.dbg;

import lombok.val;

public class StepOverReverseCommand<T> extends DebuggerCommand<T> {
    @Override
    public void execute(DebuggerFramework<T> dbg) throws DebuggerException {
        val statePointer = dbg.getCurrentStatePointer();
        PTreeNode<T> stepOverReverse = statePointer.getStepInvInto() != null ?
                statePointer.getStepInvInto() :
                statePointer.getStepInvOver();

        if (stepOverReverse == null) {
            info("There is no previous state to the current one.");
        }
        dbg.setStatePointer(stepOverReverse);

    }
}
