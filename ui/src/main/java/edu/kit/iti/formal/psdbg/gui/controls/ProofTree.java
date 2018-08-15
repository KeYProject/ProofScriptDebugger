package edu.kit.iti.formal.psdbg.gui.controls;

import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.proof.Node;
import de.uka.ilkd.key.proof.Proof;
import de.uka.ilkd.key.proof.ProofTreeEvent;
import de.uka.ilkd.key.proof.ProofTreeListener;
import edu.kit.iti.formal.psdbg.gui.controller.DebuggerMain;
import edu.kit.iti.formal.psdbg.gui.controller.Events;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.dbg.PTreeNode;
import edu.kit.iti.formal.psdbg.interpreter.dbg.ProofTreeManager;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import lombok.*;

import java.util.function.Consumer;


/**
 * KeY Proof Tree
 *
 * @author weigl
 */
public class ProofTree extends BorderPane {
    @Getter
    @Setter
    private Services services;
    private ObjectProperty<Proof> proof = new SimpleObjectProperty<>();
    private ObjectProperty<Node> root = new SimpleObjectProperty<>();
    private SetProperty<Node> sentinels = new SimpleSetProperty<>(FXCollections.observableSet());
    private MapProperty<Node, String> colorOfNodes = new SimpleMapProperty<Node, String>(FXCollections.observableHashMap());
    @FXML
    private TreeView<TreeNode> treeProof;



    private ContextMenu contextMenu;
    private BooleanProperty deactivateRefresh = new SimpleBooleanProperty();

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
    private KeyProofTreeTransformation treeCreation;
    @Getter
    private ScriptTreeTransformation treeScriptCreation;

    public ProofTree(DebuggerMain main) {
        Utils.createWithFXML(this);
        //TODO remove this hack for a better solution
        main.getModel().debuggerFrameworkProperty().addListener((p, n, m) -> {
            treeScriptCreation = new ScriptTreeTransformation(m.getPtreeManager());
        });

        treeCreation = new KeyProofTreeTransformation(sentinels);

        treeProof.setCellFactory(this::cellFactory);

        root.addListener(o -> init());
        proof.addListener((prop, old, n) -> {
            if (old != null) {
                old.removeProofTreeListener(proofTreeListener);
            }
            if (n != null)
                n.addProofTreeListener(proofTreeListener);
        });
        setOnContextMenuRequested(evt -> {
            getContextMenu().show(this, evt.getScreenX(), evt.getScreenY());
        });
        init();
    }

    /**
     * From https://www.programcreek.com/java-api-examples/index.php?api=javafx.scene.control.TreeItem
     *
     * @param candidate
     */
    private static void expandRootToItem(TreeItem<TreeNode> candidate) {
        if (candidate != null) {
            expandRootToItem(candidate.getParent());
            if (!candidate.isLeaf()) {
                candidate.setExpanded(true);
            }
        }
    }

    static void expandRootToLeaves(TreeItem candidate) {
        if (candidate != null) {
            if (!candidate.isLeaf()) {
                candidate.setExpanded(true);
                ObservableList<TreeItem> children = candidate.getChildren();
                children.forEach(treeItem -> expandRootToLeaves(treeItem));

            }
        }
    }


    public void setNodeColor(Node n, String color) {
        this.colorOfNodes.put(n, color);
    }

    public void expandRootToSentinels() {
        if (getTreeProof().getRoot() == null) {
            if (root.get() != null) {
                TreeItem<TreeNode> item = populate("Proof", root.get());
                //populate(root.get().serialNr() + ": " + toString(root.get()), root.get());
                //val treeNode = new TreeNode("Proof", root.get());

                treeProof.setRoot(item);
            }

        }
        //expandRootToLeaves(getTreeProof().getRoot());
    }

    public TreeView<TreeNode> getTreeProof() {
        return treeProof;
    }

    public void consumeNode(Consumer<Node> consumer, String success) {
        TreeItem<TreeNode> item = treeProof.getSelectionModel().getSelectedItem();
        Node n = item.getValue().node;
        if (n != null) {
            consumer.accept(n);
            Events.fire(new Events.PublishMessage(success));
        } else {
            Events.fire(new Events.PublishMessage("Current item does not have a node.", 2));
        }
    }

    public ContextMenu getContextMenu() {
        if (contextMenu == null) {
            contextMenu = new ProofTreeContextMenu(this);
        }
        return contextMenu;
    }

    private void init() {
    }

    private TreeCell<TreeNode> cellFactory(TreeView<TreeNode> nodeTreeView) {
        TextFieldTreeCell<TreeNode> tftc = new TextFieldTreeCell<>();
        StringConverter<TreeNode> stringConverter = new StringConverter<TreeNode>() {
            @Override
            public String toString(TreeNode object) {
                return object.label;
            }

            @Override
            public TreeNode fromString(String string) {
                return null;
            }
        };
        tftc.setConverter(stringConverter);
        tftc.itemProperty().addListener((p, o, n) -> {
            if (n != null)
                repaint(tftc);
        });

        //colorOfNodes.addListener((InvalidationListener) o -> repaint(tftc));
        return tftc;
    }

    private void repaint(TextFieldTreeCell<TreeNode> tftc) {
        TreeNode item = tftc.getItem();
        Node n = item.node;
        tftc.setStyle("");
        if (n != null) {
            if (n.leaf() && !item.label.contains("BRANCH")) {
                if (n.isClosed()) {
                    colorOfNodes.putIfAbsent(n, "lightseagreen");
                    //tftc.setStyle("-fx-background-color: greenyellow");
                } else {
                    colorOfNodes.putIfAbsent(n, "indianred");
                }
                if (colorOfNodes.containsKey(n)) {
                    tftc.setStyle("-fx-background-color: " + colorOfNodes.get(n) + ";");
                }
            }
            //TODO for Screenshot tftc.setStyle("-fx-font-size: 18pt");
           /* if (colorOfNodes.containsKey(n)) {
                tftc.setStyle("-fx-border-color: "+colorOfNodes.get(n)+";");
            }*/
        }

        expandRootToItem(tftc.getTreeItem());

    }

    public MapProperty<Node, String> colorOfNodesProperty() {
        return colorOfNodes;
    }

    public ObservableMap<Node, String> getColorOfNodes() {
        return colorOfNodes.get();
    }

    public void setColorOfNodes(ObservableMap<Node, String> colorOfNodes) {
        this.colorOfNodes.set(colorOfNodes);
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

    public ObservableSet<Node> getSentinels() {
        return sentinels.get();
    }

    public void setSentinels(ObservableSet<Node> sentinels) {
        this.sentinels.set(sentinels);
    }

    public SetProperty<Node> sentinelsProperty() {
        return sentinels;
    }

    public boolean isDeactivateRefresh() {
        return deactivateRefresh.get();
    }

    public void setDeactivateRefresh(boolean deactivateRefresh) {
        this.deactivateRefresh.set(deactivateRefresh);
    }

    public BooleanProperty deactivateRefreshProperty() {
        return deactivateRefresh;
    }

    private TreeItem<TreeNode> populate(String label, Node node) {
        return null;
    }

    public void repopulate() {

        if (deactivateRefresh.get())
            return;

        if (root.get() != null) {
            TreeItem<TreeNode> item = treeCreation.create(proof.get());
            treeProof.setRoot(item);

            ProofTreeManager<KeyData> manager = treeScriptCreation.manager;
        }

        treeProof.refresh();
    }


}
