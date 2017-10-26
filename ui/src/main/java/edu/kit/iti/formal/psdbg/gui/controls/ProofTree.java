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

import java.util.stream.Collectors;

/**
 * KeY Proof Tree
 */
public class ProofTree extends BorderPane {
    private ObjectProperty<Proof> proof = new SimpleObjectProperty<>();
    private ObjectProperty<Node> root = new SimpleObjectProperty<>();
    private MapProperty colorOfNodes = new SimpleMapProperty<>(FXCollections.observableHashMap());

    @FXML
    private TreeView<Node> treeProof;
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

    private void init() {
        if (root.get() != null)
            treeProof.setRoot(new TreeItemNode(root.get()));
        treeProof.refresh();
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


    private TreeCell<Node> cellFactory(TreeView<Node> nodeTreeView) {
        TextFieldTreeCell<Node> tftc = new TextFieldTreeCell<>();
        tftc.setConverter(new StringConverter<Node>() {
            @Override
            public String toString(Node object) {
               /* if (object.getAppliedRuleApp() != null) {
                    return object.getAppliedRuleApp().rule().displayName();
                } else {
                   return object.name();
                }*/
                String nodeLabel;
                if (object.getAppliedRuleApp() != null) {
                    nodeLabel = object.getAppliedRuleApp().rule().displayName();
                } else {
                    nodeLabel = object.isClosed() ? "Closed Goal" : "Open Goal";
                }
                return nodeLabel;
                // return object.sequent().toString();
            }

            @Override
            public Node fromString(String string) {
                return null;
            }
        });
        tftc.itemProperty().addListener((p, o, n) -> repaint(tftc));
        colorOfNodes.addListener((InvalidationListener) o -> repaint(tftc));
        return tftc;
    }

    /**
     *
     * @param tftc
     */

    private void repaint(TextFieldTreeCell<Node> tftc) {
        Node n = tftc.getItem();
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
}



class TreeItemNode extends TreeItem<Node> {
    public TreeItemNode(Node value) {
        super(value);
    }

    @Override
    public boolean isLeaf() {
        return getValue().leaf();
    }

    @Override
    public ObservableList<TreeItem<Node>> getChildren() {
        if (super.getChildren().size() != getValue().children().size())
            super.getChildren().setAll(
                    getValue().children().stream().map(TreeItemNode::new).collect(Collectors.toList()));
        return super.getChildren();
    }
}