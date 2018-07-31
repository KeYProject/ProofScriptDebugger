package edu.kit.iti.formal.psdbg.gui.controls.ScriptTree;

import de.uka.ilkd.key.proof.Node;
import edu.kit.iti.formal.psdbg.gui.controls.TreeNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BranchLabelNode extends AbstractTreeNode {
    @Getter
    private final Node keyBranchNode;

    @Getter
    private final String labelName;


    @Override
    public TreeNode toTreeNode() {
        return new TreeNode(labelName, keyBranchNode);
    }


}
