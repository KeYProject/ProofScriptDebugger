package edu.kit.iti.formal.psdbg.gui.controls.ScriptTree;

import de.uka.ilkd.key.proof.Node;
import edu.kit.iti.formal.psdbg.gui.controls.TreeNode;

public class PlaceholderNode extends AbstractTreeNode {
    public PlaceholderNode(Node node) {
        super(node);
    }

    @Override
    public TreeNode toTreeNode() {
        return  new TreeNode("-PlaceholderNode-", super.getNode());
    }
}
