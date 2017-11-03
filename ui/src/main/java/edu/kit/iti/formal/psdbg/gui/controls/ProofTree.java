package edu.kit.iti.formal.psdbg.gui.controls;

import de.uka.ilkd.key.proof.Node;
import de.uka.ilkd.key.proof.Proof;
import de.uka.ilkd.key.proof.ProofTreeEvent;
import de.uka.ilkd.key.proof.ProofTreeListener;
import javafx.application.Platform;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;


/**
 * KeY Proof Tree
 */
public class ProofTree extends BorderPane {
    private ObjectProperty<Proof> proof = new SimpleObjectProperty<>();

    private ObjectProperty<Node> root = new SimpleObjectProperty<>();

    private MapProperty colorOfNodes = new SimpleMapProperty<>(FXCollections.observableHashMap());

    @FXML
    private TreeView<TreeNode> treeProof;

    private ProofTreeListener proofTreeListener = new ProofTreeListener() {
        @Override
        public void proofExpanded(ProofTreeEvent proofTreeEvent) {
            Platform.runLater(() -> init());
        }

        @Override
        public void proofIsBeingPruned(ProofTreeEvent proofTreeEvent) {

        }

        @Override
        public void proofPruned(ProofTreeEvent proofTreeEvent) {
            Platform.runLater(() -> init());
        }

        @Override
        public void proofStructureChanged(ProofTreeEvent proofTreeEvent) {
            Platform.runLater(() -> init());
        }

        @Override
        public void proofClosed(ProofTreeEvent proofTreeEvent) {
            Platform.runLater(() -> init());
        }

        @Override
        public void proofGoalRemoved(ProofTreeEvent proofTreeEvent) {
            Platform.runLater(() -> init());
        }

        @Override
        public void proofGoalsAdded(ProofTreeEvent proofTreeEvent) {
            Platform.runLater(() -> init());
        }

        @Override
        public void proofGoalsChanged(ProofTreeEvent proofTreeEvent) {
            Platform.runLater(() -> init());
        }

        @Override
        public void smtDataUpdate(ProofTreeEvent proofTreeEvent) {

        }

        @Override
        public void notesChanged(ProofTreeEvent proofTreeEvent) {

        }
    };

    public ProofTree() {
        Utils.createWithFXML(this);
        treeProof.setCellFactory(this::cellFactory);
        root.addListener(o -> init());
        proof.addListener((prop, old, n) -> {
            if (old != null) {
                old.removeProofTreeListener(proofTreeListener);
            }
            if (n != null)
                n.addProofTreeListener(proofTreeListener);
        });
        init();
    }

    /**
     * From https://www.programcreek.com/java-api-examples/index.php?api=javafx.scene.control.TreeItem
     *
     * @param candidate
     */
    private static void expandRootToItem(TreeItem candidate) {
        if (candidate != null) {
            expandRootToItem(candidate.getParent());
            if (!candidate.isLeaf()) {
                candidate.setExpanded(true);
            }
        }
    }

    public static String toString(Node object) {
        if (object.getAppliedRuleApp() != null) {
            return object.getAppliedRuleApp().rule().name().toString();
        } else {
            return object.isClosed() ? "CLOSED GOAL" : "OPEN GOAL";
        }
    }

    private void init() {
        if (root.get() != null) {
            TreeItem<TreeNode> item = populate("Proof", root.get());
            treeProof.setRoot(item);
        }
        treeProof.refresh();
    }

    private TreeItem<TreeNode> populate(String label, Node n) {
        val treeNode = new TreeNode(label, n);
        TreeItem<TreeNode> ti = new TreeItem<>(treeNode);
        if (n.childrenCount() == 0) {
            ti.getChildren().add(new TreeItem<>(new TreeNode(
                    n.isClosed() ? "CLOSED GOAL" : "OPEN GOAL", null)));
            return ti;
        }

        Node node = n.child(0);
        if (n.childrenCount() == 1) {
            ti.getChildren().add(new TreeItem<>(new TreeNode(toString(node), node)));
            while (node.childrenCount() == 1) {
                node = node.child(0);
                ti.getChildren().add(new TreeItem<>(new TreeNode(toString(node), node)));
            }
        }

        if (node.childrenCount() == 0) {

        } else { // children count > 1
            node.children().forEach(child ->
                    ti.getChildren().add(populate(child.getNodeInfo().getBranchLabel(), child)));
        }

        return ti;
    }

    private TreeCell<TreeNode> cellFactory(TreeView<TreeNode> nodeTreeView) {
        TextFieldTreeCell<TreeNode> tftc = new TextFieldTreeCell<>();
        StringConverter<TreeNode> stringConverter = new StringConverter<TreeNode>() {
            @Override
            public String toString(TreeNode object) {
                return object.getLabel();
            }

            @Override
            public TreeNode fromString(String string) {
                return null;
            }
        };
        tftc.setConverter(stringConverter);

        //tftc.itemProperty().addListener((p, o, n) -> repaint(tftc));

        //colorOfNodes.addListener((InvalidationListener) o -> repaint(tftc));
        return tftc;
    }

    /*private void repaint(TextFieldTreeCell<TreeNode> tftc) {
        Node n = tftc.getItem().node;
        tftc.setStyle("");
        if (n != null) {
            if (n.isClosed()) {
                colorOfNodes.putIfAbsent(n, "green");
                //tftc.setStyle("-fx-background-color: greenyellow");
            }
            if (colorOfNodes.containsKey(n)) {
                tftc.setStyle("-fx-background-color: " + colorOfNodes.get(n) + ";");
            }
        }
        expandRootToItem(tftc.getTreeItem());

    }*/

    public Object getColorOfNodes() {
        return colorOfNodes.get();
    }

    public void setColorOfNodes(Object colorOfNodes) {
        this.colorOfNodes.set(colorOfNodes);
    }

    public MapProperty colorOfNodesProperty() {
        return colorOfNodes;
    }

    public Node getRoot() {
        return root.get();
    }

    public void setRoot(Node root) {
        this.root.set(root);
    }

    public ObjectProperty<Node> rootProperty() {
        return root;
    }

    public Proof getProof() {
        return proof.get();
    }

    public void setProof(Proof proof) {
        this.proof.set(proof);
    }

    public ObjectProperty<Proof> proofProperty() {
        return proof;
    }


    @AllArgsConstructor
    @Data
    private static class TreeNode {
        String label;

        Node node;
    }
}