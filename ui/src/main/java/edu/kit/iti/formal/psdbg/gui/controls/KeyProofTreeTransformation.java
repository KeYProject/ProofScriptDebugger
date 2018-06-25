package edu.kit.iti.formal.psdbg.gui.controls;

import de.uka.ilkd.key.proof.Node;
import de.uka.ilkd.key.proof.Proof;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TreeItem;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Iterator;

@RequiredArgsConstructor
@Data
public class KeyProofTreeTransformation {
    @Getter
    @Setter
    private SetProperty<Node> sentinels;


    public KeyProofTreeTransformation(SetProperty<Node> sentinels) {
        this.sentinels = sentinels;
    }

    public TreeItem<ProofTree.TreeNode> create(Proof proof) {
        TreeItem<ProofTree.TreeNode> self1 = new TreeItem<>(new ProofTree.TreeNode("Proof", null));
        self1.getChildren().add(populate("", proof.root()));
        return self1;
    }
    protected TreeItem<ProofTree.TreeNode> itemFactory(Node n, String label) {
        if(label.equals("")){
            return itemFactory(n);
        } else {
            return new TreeItem<>(new ProofTree.TreeNode(label, n));
        }
    }

    protected TreeItem<ProofTree.TreeNode> itemFactory(Node n) {
        return new TreeItem<>(new ProofTree.TreeNode(n.serialNr() + ": " + toString(n), n));
    }

    protected String toString(Node object) {
        if (object.getAppliedRuleApp() != null) {
            return object.getAppliedRuleApp().rule().name().toString();
        } else {
            return object.isClosed() ? "CLOSED GOAL" : "OPEN GOAL";
        }
    }

    /**
     * recursive population.
     *
     * @param label
     * @param n
     * @return
     */
    protected TreeItem<ProofTree.TreeNode> populate(String label, Node n) {
        TreeItem<ProofTree.TreeNode> currentItem = itemFactory(n, label);

        // abort the traversing iff we have reached a sentinel!
        if (sentinels.contains(n)) {
            return currentItem;
        }

        //if we are at a leaf we need to check goal state
        if (n.childrenCount() == 0) {
            //  TreeItem<TreeNode> e = new TreeItem<>(new TreeNode(
            //           n.isClosed() ? "CLOSED GOAL" : "OPEN GOAL", null));
            // currentItem.getChildren().addCell(e);
            return currentItem;
        }

        assert n.childrenCount() > 0; // there is at least one children

        //consume child proof nodes until there are more than one child, then recursion!
        Node node = n.child(0);
        if (n.childrenCount() == 1) {
            currentItem.getChildren().add(new TreeItem<>(new ProofTree.TreeNode(node.serialNr() + ": " + toString(node), node)));
        while (node.childrenCount() == 1) {
            node = node.child(0);
            currentItem.getChildren().add(new TreeItem<>(new ProofTree.TreeNode(node.serialNr() + ": " + toString(node), node)));
        }
        }

        // if the current node has more zero children. abort.
        if (node.childrenCount() == 0) return currentItem;

        assert node.childrenCount() > 0; // there is at least 2 children

        Iterator<Node> nodeIterator = node.childrenIterator();
        int branchCounter = 1;

        while (nodeIterator.hasNext()) {
            Node childNode = nodeIterator.next();
            if (childNode.getNodeInfo().getBranchLabel() != null) {
                TreeItem<ProofTree.TreeNode> populate = populate(childNode.getNodeInfo().getBranchLabel(), childNode);
                currentItem.getChildren().add(populate);
            } else {
                TreeItem<ProofTree.TreeNode> populate = populate("BRANCH " + branchCounter, childNode);
                TreeItem<ProofTree.TreeNode> self = itemFactory(childNode);
                populate.getChildren().add(0, self);
                currentItem.getChildren().add(populate);
                branchCounter++;
            }
        }
        return currentItem;
    }
}
