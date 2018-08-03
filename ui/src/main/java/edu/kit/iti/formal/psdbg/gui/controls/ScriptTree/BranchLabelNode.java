package edu.kit.iti.formal.psdbg.gui.controls.ScriptTree;

import de.uka.ilkd.key.proof.Node;
import edu.kit.iti.formal.psdbg.gui.controls.TreeNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


public class BranchLabelNode extends AbstractTreeNode {

    @Getter
    private final String labelName;

    public BranchLabelNode(Node node, String labelName) {
        super(node);
        this.labelName = labelName;
    }


    @Override
    public TreeNode toTreeNode() {
        return new TreeNode(labelName, getNode());
    }


}
