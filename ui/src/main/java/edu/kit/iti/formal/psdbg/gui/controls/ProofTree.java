package edu.kit.iti.formal.psdbg.gui.controls;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.proof.Node;
import de.uka.ilkd.key.proof.Proof;
import de.uka.ilkd.key.proof.ProofTreeEvent;
import de.uka.ilkd.key.proof.ProofTreeListener;
import edu.kit.iti.formal.psdbg.ShortCommandPrinter;
import edu.kit.iti.formal.psdbg.gui.controller.DebuggerMain;
import edu.kit.iti.formal.psdbg.gui.controller.Events;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.dbg.PTreeNode;
import edu.kit.iti.formal.psdbg.interpreter.dbg.ProofTreeManager;
import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import lombok.*;
import sun.reflect.generics.tree.Tree;

import java.util.*;
import java.util.function.Consumer;


/**
 * KeY Proof Tree
 *
 * @author weigl
 */
public class ProofTree extends BorderPane {
    @Getter
    @Setter
    private Services services;// = DebuggerMain.FACADE.getService();
    private ObjectProperty<Proof> proof = new SimpleObjectProperty<>();
    private ObjectProperty<Node> root = new SimpleObjectProperty<>();
    private SetProperty<Node> sentinels = new SimpleSetProperty<>(FXCollections.observableSet());
    private MapProperty<Node, String> colorOfNodes = new SimpleMapProperty<Node, String>(FXCollections.observableHashMap());
    @FXML
    private TreeView<TreeNode> treeProof;

    //TODO: added for testing
    @FXML
    private TreeView<TreeNode> treeScript;


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
    private TreeTransformationKey treeCreation;
    @Getter
    private TreeTransformationScript treeScriptCreation;
    private DebuggerMain main;

    public ProofTree(DebuggerMain main) {
        Utils.createWithFXML(this);
        //TODO remove this hack for a better solution
        //main.getModel().debuggerFrameworkProperty().addListener((p, n, m) -> {
        //    treeCreation = new TreeTransformationScript(m.getPtreeManager());
        //});
        this.main = main;
        treeCreation = new TreeTransformationKey();

        treeProof.setCellFactory(this::cellFactory);
        treeScript.setCellFactory(this::cellFactory);

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
                treeScript.setRoot(item);
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
        treeScriptCreation = new TreeTransformationScript(main.getModel().getDebuggerFramework().getPtreeManager());


        if (deactivateRefresh.get())
            return;

        if (root.get() != null) {
            TreeItem<TreeNode> item = treeCreation.create(proof.get());
            treeProof.setRoot(item);

            TreeItem<TreeNode> scriptTree = treeScriptCreation.buildScriptTree(root.get());
            treeScript.setRoot(scriptTree);
        }
        treeProof.refresh();
        treeScript.refresh();
    }


    @AllArgsConstructor
    private static class TreeNode {
        String label;
        Node node;
    }


    class TreeTransformationKey {

        public TreeItem<TreeNode> create(Proof proof) {
            TreeItem<TreeNode> self1 = new TreeItem<>(new TreeNode("Proof", null));
            self1.getChildren().add(populate("Proof", proof.root()));
            return self1;
        }

        protected TreeItem<TreeNode> itemFactory(Node n) {
            return new TreeItem<>(new TreeNode(n.serialNr() + ": " + toString(n), n));
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
        protected TreeItem<TreeNode> populate(String label, Node n) {
            val treeNode = new TreeNode(label, n);
            TreeItem<TreeNode> currentItem = itemFactory(n);
            new TreeItem<>(treeNode);

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
                do {
                    currentItem.getChildren().add(itemFactory(node));
                    node = node.child(0);
                } while (node.childrenCount() == 1);
            }

            // if the current node has more zero children. abort.
            if (node.childrenCount() == 0) return currentItem;

            assert node.childrenCount() > 0; // there is at least 2 children

            Iterator<Node> nodeIterator = node.childrenIterator();
            int branchCounter = 1;
            while (nodeIterator.hasNext()) {
                Node childNode = nodeIterator.next();
                if (childNode.getNodeInfo().getBranchLabel() != null) {
                    TreeItem<TreeNode> populate = populate(childNode.getNodeInfo().getBranchLabel(), childNode);
                    currentItem.getChildren().add(populate);
                } else {
                    TreeItem<TreeNode> populate = populate("BRANCH " + branchCounter, childNode);
                    TreeItem<TreeNode> self = itemFactory(childNode);
                    populate.getChildren().add(0, self);
                    currentItem.getChildren().add(populate);
                    branchCounter++;
                }
            }
            return currentItem;
        }
    }


    @RequiredArgsConstructor
    class TreeTransformationScript extends TreeTransformationKey {
        final ProofTreeManager<KeyData> manager;
        /**
         * maps a node to its siblings, that were created by an mutator call.
         */
        Multimap<Node, Node> entryExitMap = HashMultimap.create();

        /**
         * maps a node to its mutator, that was applied on it.
         */
        Map<Node, ASTNode> mutatedBy = new HashMap<>();

        @Override
        public TreeItem<TreeNode> create(Proof proof) {
            Set<PTreeNode<KeyData>> nodes = manager.getNodes();
            entryExitMap.clear();
            mutatedBy.clear();
            nodes.forEach(pn -> {
                        try {
                            if (pn.isAtomic()) {
                                Node startNode = pn.getStateBeforeStmt().getSelectedGoalNode().getData().getNode();
                                mutatedBy.put(startNode, pn.getStatement());
                                pn.getMutatedNodes().forEach(mn -> {
                                    entryExitMap.put(startNode, mn.getData().getNode());
                                });
                            }
                        } catch (NullPointerException e) {
                        }
                    }
            );
            return super.create(proof);
        }

        @Override
        protected TreeItem<TreeNode> populate(String label, Node n) {
            val currentItem = itemFactory(n);
            for (Node child : entryExitMap.get(n)) {
                if (isMutated(child)) {
                    currentItem.getChildren().add(populate("", child));
                } else {
                    currentItem.getChildren().add(super.itemFactory(child));
                }
            }
            return currentItem;
        }

        private boolean isMutated(Node child) {
            return mutatedBy.containsKey(child);
        }

        protected TreeItem<TreeNode> itemFactory(Node n) {
            ASTNode ast = mutatedBy.get(n);
            String lbl = (String) ast.accept(new ShortCommandPrinter());
            lbl += "  " + n.serialNr() + " " + toString(n);
            TreeItem<TreeNode> ti = new TreeItem<>(new TreeNode(lbl, n));
            return ti;
        }


        //TODO: Reverse ArrayList in the end or nah?
        @Deprecated
        public ArrayList<String> getBranchLabels(TreeNode node) {
            TreeItem<TreeNode> proofTree = create(proof.get());

            ArrayList<String> branchlabels = new ArrayList<>();

            int i = 0;
            branchlabels.set(0, node.label);
            while (node != null) {
                if (!branchlabels.get(i).equals(node.label)) {
                    i++;
                    branchlabels.set(i, node.label);
                }
                //TODO: node = node.parent
            }

            return branchlabels;
        }

        public ArrayList<String> getBranchLabels(Node node) {
            ArrayList<String> branchlabels = new ArrayList<>();

            int i = 0;
            //TODO: branchlabel = all branchlabels or only next one
            branchlabels.set(0, node.getNodeInfo().getBranchLabel());
            Node n = node.parent();
            while (n != null) {
                if (!branchlabels.get(i).equals(n.getNodeInfo().getBranchLabel())) {
                    i++;
                    branchlabels.set(i, n.getNodeInfo().getBranchLabel());
                }
                n = n.parent();
            }

            return branchlabels;
        }



        public void showScriptTree() {
            if (main.getModel().getDebuggerFramework().getPtreeManager() == null) {
               Utils.showInfoDialog("Execute Script","Execute Script","Please execute a script first.");
                return;
            }
            treeScriptCreation = new TreeTransformationScript(main.getModel().getDebuggerFramework().getPtreeManager());

            if (root != null) {
                //TODO not sure prooftree for what
                TreeItem<TreeNode> tree = treeScriptCreation.create(proof.get());
                treeScript.setRoot(treeScriptCreation.buildScriptTree(root.get()));
                treeScript.refresh();
            }



        }

        private TreeItem<TreeNode> buildScriptTree(Node node) {
            TreeItem<TreeNode> currentItem = new TreeItem<>(createTreeNode(node));

            if (node == null || currentItem.getValue() == null) {
                return null;
            }
            assert node.childrenCount() >= 0;


            if (sentinels.contains(node)) {
                return currentItem;
            }
            if (node.childrenCount() == 0) {
                //  TreeItem<TreeNode> e = new TreeItem<>(new TreeNode(
                //           n.isClosed() ? "CLOSED GOAL" : "OPEN GOAL", null));
                // currentItem.getChildren().addCell(e);
                return currentItem;
            }

            //no branchlabel needed
            if (node.childrenCount() == 1) {

                //check if same mutator as child
                Node child = node.child(0);
                if (!entryExitMap.get(node).contains(child)) {
                    currentItem.getChildren().add(buildScriptTree(child));
                    return currentItem;

                } else {
                    //skip node
                    return  buildScriptTree(child);
                }
            }

            //node.childrenCount() > 1
            //branch labels

                Iterator<Node> iterator = node.childrenIterator();
                int counter = 0;
                while (iterator.hasNext()) {
                    Node child = iterator.next();
                    String branchlabel = "BRANCH " + counter;//TODO:getBranchLabels(child).get(0);
                    currentItem.getChildren().add(counter, new TreeItem<>(new TreeNode(branchlabel, child)));
                    currentItem.getChildren().get(counter).getChildren().add(buildScriptTree(child.child(0)));
                    counter++;
                }

                return currentItem;






        }

        public TreeNode createTreeNode(Node node) {
            try { //TODO stupid hack for now
                return new TreeNode(node.getAppliedRuleApp().rule().name() + " (line TODO)", node);
            } catch (NullPointerException e) {
                System.out.println(node);
                e.printStackTrace();
                return null;
            }
        }
    }
}
