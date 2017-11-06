package edu.kit.iti.formal.psdbg.gui.controls;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.pp.LogicPrinter;
import de.uka.ilkd.key.proof.Node;
import de.uka.ilkd.key.proof.Proof;
import de.uka.ilkd.key.proof.ProofTreeEvent;
import de.uka.ilkd.key.proof.ProofTreeListener;
import edu.kit.iti.formal.psdbg.LabelFactory;
import edu.kit.iti.formal.psdbg.gui.controller.Events;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;


/**
 * KeY Proof Tree
 */
public class ProofTree extends BorderPane {
    private ObjectProperty<Proof> proof = new SimpleObjectProperty<>();

    private ObjectProperty<Node> root = new SimpleObjectProperty<>();

    private SetProperty<Node> sentinels = new SimpleSetProperty<>(FXCollections.observableSet());


    private MapProperty colorOfNodes = new SimpleMapProperty<>(FXCollections.observableHashMap());

    @FXML
    private TreeView<TreeNode> treeProof;

    private ContextMenu contextMenu;

    @Getter @Setter
    private Services services;// = DebuggerMain.FACADE.getService();

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
    private static void expandRootToItem(TreeItem candidate) {
        if (candidate != null) {
            expandRootToItem(candidate.getParent());
            if (!candidate.isLeaf()) {
                candidate.setExpanded(true);
            }
        }
    }

    private static void expandRootToLeaves(TreeItem candidate) {
        if (candidate != null) {
            if (!candidate.isLeaf()) {
                candidate.setExpanded(true);
                ObservableList<TreeItem> children = candidate.getChildren();
                children.forEach(treeItem -> expandRootToLeaves(treeItem));

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
            MenuItem copyBranchLabel = new MenuItem("Branch Label");
            copyBranchLabel.setOnAction(evt -> consumeNode(n -> Utils.intoClipboard(
                    LabelFactory.getBranchingLabel(n)), "Copied!"));

            MenuItem copyProgramLines = new MenuItem("Program Lines");
            copyProgramLines.setOnAction(evt -> {
                consumeNode(n -> {
                    Utils.intoClipboard(
                            LabelFactory.getProgramLines(n));
                }, "Copied!");
            });

            MenuItem copySequent = new MenuItem("Sequent");
            copySequent.setOnAction(evt -> {
                consumeNode(n -> {
                    assert services != null : "set KeY services!";
                    String s = LogicPrinter.quickPrintSequent(n.sequent(), services);
                    Utils.intoClipboard(s);
                }, "Copied!");
            });

            MenuItem copyRulesLabel = new MenuItem("Rule labels");
            copyRulesLabel.setOnAction(evt -> {
                consumeNode(n -> {
                    Utils.intoClipboard(
                            LabelFactory.getRuleLabel(n));
                }, "Copied!");
            });

            MenuItem copyProgramStatements = new MenuItem("Statements");
            copyProgramStatements.setOnAction(event -> {
                consumeNode(n -> {
                    Utils.intoClipboard(
                            LabelFactory.getProgramStatmentLabel(n));
                }, "Copied!");
            });

            Menu copy = new Menu("Copy", new MaterialDesignIconView(MaterialDesignIcon.CONTENT_COPY),
                    copyBranchLabel, copyProgramLines,
                    copyProgramStatements, copyRulesLabel,
                    copySequent);

            MenuItem createCases = new MenuItem("Created Case for Open Goals");
            createCases.setOnAction((evt) ->
            {
                if (proof.get() != null) {
                    List<String[]> labels = LabelFactory.getLabelOfOpenGoals(proof.get(),
                            LabelFactory::getBranchingLabel);
                    String text;
                    if (labels.isEmpty()) {
                        text = "// no open goals";
                    } else if (labels.size() == 1) {
                        text = "// only one goal";
                    } else {
                        int upperLimit = 0;
                        /* trying to find the common suffix*/
                        try {
                            String[] ref = labels.get(0);
                            for (; true; upperLimit++) {
                                for (String[] lbl : labels) {
                                    if (!lbl[upperLimit].equals(ref[upperLimit])) {
                                        break;
                                    }
                                }
                                upperLimit++;
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                        }

                        int finalUpperLimit = upperLimit;
                        text = labels.stream()
                                .map(a -> Arrays.stream(a, finalUpperLimit, a.length))
                                .map(s -> s.reduce((a, b) -> b + LabelFactory.SEPARATOR + a).orElse("error"))
                                .map(s -> String.format("\tcase match \"%s\" :\n\t\t//commands", s))
                                .reduce((a, b) -> a + "\n" + b)
                                .orElse("ERROR");
                    }

                    String s = "cases {\n" + text + "\n}";
                    Events.fire(new Events.InsertAtTheEndOfMainScript(s));
                    Events.fire(new Events.PublishMessage("Copied to Clipboard"));
                } else {

                }
            });


            MenuItem showSequent = new MenuItem("Show Sequent");
            showSequent.setOnAction((evt) ->
                    consumeNode(n -> Events.fire(new Events.ShowSequent(n)), ""));

            MenuItem showGoal = new MenuItem("Show in Goal List");
            showGoal.setOnAction((evt) -> {
                consumeNode(n -> Events.fire(new Events.SelectNodeInGoalList(n)), "Found!");
            });

            MenuItem expandAllNodes = new MenuItem("Expand Tree");
            expandAllNodes.setOnAction(event -> {

                expandRootToLeaves(treeProof.getRoot());
            });

            contextMenu = new ContextMenu(expandAllNodes, new SeparatorMenuItem(), copy, createCases, showSequent, showGoal);
            contextMenu.setAutoFix(true);
            contextMenu.setAutoHide(true);

        }
        return contextMenu;
    }

    private void init() {
        if (deactivateRefresh.get())
            return;

        if (root.get() != null) {
            TreeItem<TreeNode> item = populate("Proof", root.get());
            treeProof.setRoot(item);
        }

        treeProof.refresh();
    }

    private TreeItem<TreeNode> populate(String label, Node n) {
        val treeNode = new TreeNode(label, n);
        TreeItem<TreeNode> ti = new TreeItem<>(treeNode);

        // abort the traversing iff we have reached a sentinel!
        if (sentinels.contains(n)) {
            return ti;
        }

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
            Iterator<Node> nodeIterator = node.childrenIterator();

            Node childNode;
            int branchCounter = 1;
            while (nodeIterator.hasNext()) {
                childNode = nodeIterator.next();
                if (childNode.getNodeInfo().getBranchLabel() != null) {
                    ti.getChildren().add(populate(childNode.getNodeInfo().getBranchLabel(), childNode));
                } else {
                    ti.getChildren().add(populate("BRANCH " + branchCounter, childNode));
                    branchCounter++;
                }
            }

/*            node.childrenIterator().forEachRemaining(child -> ti.getChildren().add(
                    populate(child.getNodeInfo().getBranchLabel(), child)));
*/
            //node.children().forEach(child ->
            //        ti.getChildren().add(populate(child.getNodeInfo().getBranchLabel(), child)));
        }

        return ti;
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

    @AllArgsConstructor
    private static class TreeNode {
        String label;

        Node node;
    }
}
