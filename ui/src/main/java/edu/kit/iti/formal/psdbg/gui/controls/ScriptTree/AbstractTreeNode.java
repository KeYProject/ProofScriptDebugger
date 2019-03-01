package edu.kit.iti.formal.psdbg.gui.controls.ScriptTree;

import de.uka.ilkd.key.proof.Node;
import edu.kit.iti.formal.psdbg.gui.controls.TreeNode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class AbstractTreeNode {

    @Getter @Setter
    private AbstractTreeNode parent;

    @Getter @Setter
    private List<AbstractTreeNode> children;

    @Getter
    private final Node node;

    @Getter @Setter
    private boolean isMatchEx = false; //is a match expression

    @Getter @Setter
    private boolean isSucc = true; //applied successfully

    public AbstractTreeNode(Node node) {
        this.node = node;
    }

    public TreeNode toTreeNode() {
        return new TreeNode("Proof", null);
    }

}
