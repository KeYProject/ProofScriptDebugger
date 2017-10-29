package edu.kit.iti.formal.psdbg.gui.controls;

import de.uka.ilkd.key.proof.Node;
import de.uka.ilkd.key.proof.Proof;
import de.uka.ilkd.key.proof.ProofTreeEvent;
import de.uka.ilkd.key.proof.ProofTreeListener;
import javafx.beans.InvalidationListener;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import lombok.AllArgsConstructor;
import lombok.val;

/**
 * KeY Proof Tree
 */
public class ProofTree extends BorderPane {
    private ObjectProperty<Proof> proof = new SimpleObjectProperty<>();

    private ObjectProperty<Node> root = new SimpleObjectProperty<>();

    private MapProperty colorOfNodes = new SimpleMapProperty<>(FXCollections.observableHashMap());

    @FXML
    private TreeView<NodeOrString> treeProof;

    private ProofTreeListener proofTreeListener = new ProofTreeListener() {
        @Override
        public void proofExpanded(ProofTreeEvent proofTreeEvent) {
            treeProof.refresh();
        }

        @Override
        public void proofIsBeingPruned(ProofTreeEvent proofTreeEvent) {

        }

        @Override
        public void proofPruned(ProofTreeEvent proofTreeEvent) {
            treeProof.refresh();
        }

        @Override
        public void proofStructureChanged(ProofTreeEvent proofTreeEvent) {
            treeProof.refresh();
        }

        @Override
        public void proofClosed(ProofTreeEvent proofTreeEvent) {
            treeProof.refresh();
        }

        @Override
        public void proofGoalRemoved(ProofTreeEvent proofTreeEvent) {
            treeProof.refresh();
        }

        @Override
        public void proofGoalsAdded(ProofTreeEvent proofTreeEvent) {
            treeProof.refresh();
        }

        @Override
        public void proofGoalsChanged(ProofTreeEvent proofTreeEvent) {
            treeProof.refresh();
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

    private void init() {
        if (root.get() != null)
            treeProof.setRoot(new TreeItemNode(root.get()));
        treeProof.refresh();
    }

    private TreeCell<NodeOrString> cellFactory(TreeView<NodeOrString> nodeTreeView) {
        TextFieldTreeCell<NodeOrString> tftc = new TextFieldTreeCell<>();
        StringConverter<NodeOrString> stringConverter = new StringConverter<NodeOrString>() {
            @Override
            public String toString(NodeOrString object) {
                if (object instanceof TString) {
                    TString tString = (TString) object;
                    return tString.label;
                }

                if (object instanceof TNode) {
                    TNode tNode = (TNode) object;
                    String nodeLabel;
                    if (tNode.node.getAppliedRuleApp() != null) {
                        nodeLabel = tNode.node.getAppliedRuleApp().rule().displayName();
                    } else {
                        nodeLabel = tNode.node.isClosed() ? "Closed Goal" : "Open Goal";
                    }
                    return nodeLabel;
                }
                return "";
            }

            @Override
            public NodeOrString fromString(String string) {
                return null;
            }
        };
        tftc.setConverter(stringConverter);
        tftc.itemProperty().addListener((p, o, n) -> repaint(tftc));
        colorOfNodes.addListener((InvalidationListener) o -> repaint(tftc));
        return tftc;
    }

    /**
     * @param tftc
     */

    private void repaint(TextFieldTreeCell<NodeOrString> tftc) {
        Node n = ((TNode) tftc.getItem()).node;
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

    }

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

    static class NodeOrString {
    }

    @AllArgsConstructor
    static class TNode extends NodeOrString {
        Node node;
    }

    @AllArgsConstructor
    static class TString extends NodeOrString {
        String label;
    }
}

class TreeItemNode extends TreeItem<ProofTree.NodeOrString> {
    public TreeItemNode(Node value) {
        super(new ProofTree.TNode(value));
    }

    @Override
    public boolean isLeaf() {
        return ((ProofTree.TNode) getValue()).node.childrenCount() <= 1;
    }

    @Override
    public ObservableList<TreeItem<ProofTree.NodeOrString>> getChildren() {
        ObservableList<TreeItem<ProofTree.NodeOrString>> list = FXCollections.observableArrayList();
        Node n = ((ProofTree.TNode) getValue()).node;

        if (isLeaf()) return list;

        if (n.childrenCount() == 1) {
            val node = n.child(0);
            list.add(new TreeItemNode(node));
            while (node.childrenCount() == 1) {
                Node c = node.child(0);
                list.add(new TreeItemNode(c));
            }
        } else {
            for (Node child : n.children()) {
                val ti = new TreeItemString(child.getNodeInfo().getBranchLabel());
                list.add(ti);
                ti.getChildren().add(new TreeItemNode(child));
            }
        }
        return list;
    }
}

class TreeItemString extends TreeItem<ProofTree.NodeOrString> {
    public TreeItemString(String branchLabel) {
        super(new ProofTree.TString(branchLabel));
    }
}