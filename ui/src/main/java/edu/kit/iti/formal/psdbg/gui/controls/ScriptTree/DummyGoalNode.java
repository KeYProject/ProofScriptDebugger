package edu.kit.iti.formal.psdbg.gui.controls.ScriptTree;

import edu.kit.iti.formal.psdbg.gui.controls.ScriptTree.AbstractTreeNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DummyGoalNode extends AbstractTreeNode {

    @Getter
    private boolean closedGoal;

}
