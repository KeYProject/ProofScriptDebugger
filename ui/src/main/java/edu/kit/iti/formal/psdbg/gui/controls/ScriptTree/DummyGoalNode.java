package edu.kit.iti.formal.psdbg.gui.controls.ScriptTree;

import de.uka.ilkd.key.proof.Node;
import edu.kit.iti.formal.psdbg.gui.controls.TreeNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DummyGoalNode extends AbstractTreeNode {

    @Getter
    private final boolean closedGoal;

    @Getter
    private final Node node;

    @Override
    public TreeNode toTreeNode() {
        return  new TreeNode((closedGoal? "CLOSED":"OPEN"), node); //TODO:
    }
}
