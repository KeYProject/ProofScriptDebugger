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
import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.dbg.PTreeNode;
import edu.kit.iti.formal.psdbg.interpreter.dbg.ProofTreeManager;
import edu.kit.iti.formal.psdbg.parser.ast.*;
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
import jdk.nashorn.internal.codegen.CompilerConstants;
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
    private Services services;
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

    public ProofTree(DebuggerMain main) {
        Utils.createWithFXML(this);
        //TODO remove this hack for a better solution
        main.getModel().debuggerFrameworkProperty().addListener((p, n, m) -> {
            treeScriptCreation = new TreeTransformationScript(m.getPtreeManager());
        });

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

            treeScript.setRoot(treeScriptCreation.buildScriptTree(treeScriptCreation.manager.getStartNode()));
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
            self1.getChildren().add(populate("", proof.root()));
            return self1;
        }
        protected TreeItem<TreeNode> itemFactory(Node n, String label) {
            if(label.equals("")){
                return itemFactory(n);
            } else {
                return new TreeItem<>(new TreeNode(label, n));
            }
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
            TreeItem<TreeNode> currentItem = itemFactory(n, label);

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
                currentItem.getChildren().add(new TreeItem<>(new TreeNode(node.serialNr() + ": " + toString(node), node)));
            while (node.childrenCount() == 1) {
                node = node.child(0);
                currentItem.getChildren().add(new TreeItem<>(new TreeNode(node.serialNr() + ": " + toString(node), node)));
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

        /**
         * Compute the next PTreeNode from the PTreeGraph
         * @param current
         * @return
         */
        private PTreeNode getNextPtreeNode(PTreeNode current) {
            return (current.getStepInto() != null)? current.getStepInto() :
                    current.getStepOver();
        }

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


        /**
         * Build the script tree, which contains all branchlabels of the proof tree, but nodes are only nodes from the script
         */
        public void showScriptTree() {

            if (root != null) {
                TreeItem<TreeNode> tree = treeScriptCreation.create(proof.get());
                mutatedBy.forEach((node, astNode) -> {
                    System.out.println("node.serialNr() = " + node.serialNr()+ "astNode "+astNode.getNodeName()+astNode.getStartPosition());
                });
                treeScript.setRoot(treeScriptCreation.buildScriptTree(manager.getStartNode()));
                treeScript.refresh();
            }



        }

        private TreeItem<TreeNode> buildScriptTree(PTreeNode<KeyData> node) {
            TreeItem<TreeNode> tree = treeScriptCreation.create(proof.get());
            mutatedBy.forEach((n, astNode) -> {
                System.out.println("node.serialNr() = " + n.serialNr()+ " astNode "+astNode.getNodeName()+astNode.getStartPosition());
            });
            entryExitMap.forEach((node1, node2) -> {
                System.out.println("node in "+node1.serialNr()+" node out"+node2.serialNr());
            });
            PTreeNode<KeyData> nextNode = node;
            if(nextNode == null) {
                return null;
            }
            Node root = nextNode.getStateBeforeStmt().getSelectedGoalNode().getData().getNode();
            TreeItem<TreeNode> currentItem = new TreeItem<>(new TreeNode("Proof", nextNode.getStateBeforeStmt().getSelectedGoalNode().getData().getNode()));
            //build the first node after the root
            currentItem.getChildren().add(new TreeItem<>(createTreeNode(nextNode)));
            while(nextNode != null) {
                nextNode = getNextPtreeNode(nextNode);
                //if we arrive at a cases statement we skip this statement

                if(nextNode.getStateAfterStmt() != null && nextNode.getStateAfterStmt().getGoals().size() > 1){
                    //we arrived at a branching statement, add it to the tree and add the branches as new children
                    currentItem.getChildren().add(new TreeItem<>(createTreeNode(nextNode)));
                    for (int i = 0; i < nextNode.getStateAfterStmt().getGoals().size(); i++) {
                        TreeItem<TreeNode> child = new TreeItem<>(new TreeNode("Branch " + i, nextNode.getStateBeforeStmt().getSelectedGoalNode().getData().getNode())); //todo right branches
                        child.getChildren().add(buildScriptTreeHelper(nextNode, nextNode.getStateAfterStmt().getGoals().get(i)));

                        currentItem.getChildren().add(child);

                    }
                    return currentItem;
                } else {
                    if(nextNode.getStateAfterStmt() != null && nextNode.getStateBeforeStmt() != null) {
                        currentItem.getChildren().add(new TreeItem<>(createTreeNode(nextNode)));
                    } else {
                        return currentItem;

                    }
                }
            }

            return currentItem;
        }

        private TreeItem<TreeNode> buildScriptTreeHelper(PTreeNode<KeyData> node, GoalNode<KeyData> keyDataGoalNode) {
            assert node.getStateBeforeStmt().getGoals().get(0).getData() !=  null;

            TreeItem<TreeNode> currentItem = null;
            PTreeNode<KeyData> nextNode = getNextPtreeNode(node);

            while(currentItem == null){
                //skip if we reach a cases or case
                if(nextNode.getStatement().getNodeName().equals("CasesStatement")
                        || nextNode.getStatement() instanceof  CaseStatement) {
                    nextNode = getNextPtreeNode(nextNode);
                    continue;
                }
                //handle if we reach a matchexpression
                if(nextNode.getStatement() instanceof  MatchExpression){
                    MatchExpression match = (MatchExpression) nextNode.getStatement();
                    Expression pattern = match.getPattern();

                    currentItem = new TreeItem<>(new TreeNode(match.getRuleContext().getText()+ " (line "+
                            match.getStartPosition().getLineNumber()+")", keyDataGoalNode.getData().getNode()));


                }
                //handle if we reach other statements
                if(nextNode.getStateBeforeStmt() == null ||
                        nextNode.getStateBeforeStmt().getGoals() == null){
                    currentItem = new TreeItem<>(new TreeNode("Temp", keyDataGoalNode.getData().getNode()));


                } else {

                    boolean bool = mutatedBy.get(keyDataGoalNode.getData().getNode()).equals(nextNode.getStatement());
                    if(bool) {
                        currentItem = new TreeItem<>(new TreeNode(((CallStatement) nextNode.getStatement()).getCommand() +
                                " (line " + nextNode.getStatement().getStartPosition().getLineNumber() +
                                ")", keyDataGoalNode.getData().getNode()));

                    } else {
                         CallStatement nextNodeName = (CallStatement) mutatedBy.get(keyDataGoalNode.getData().getNode());
                        currentItem = new TreeItem<>(new TreeNode(nextNodeName.getCommand() +
                                " (line " + nextNodeName.getStartPosition().getLineNumber() +
                                ")", keyDataGoalNode.getData().getNode()));

                    }
                }
            }
            currentItem.getChildren().add(buildScriptTreeHelper(nextNode, keyDataGoalNode));
            return currentItem;
        }



     /*   @Deprecated
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






        }*/


        private TreeItem<TreeNode> buildScriptTreewithID(PTreeNode<KeyData> node, GoalNode<KeyData> goalNode) {
            PTreeNode<KeyData> nextNode = getNextPtreeNode(node);
            TreeItem<TreeNode> currentItem = null;

            while(currentItem == null) {
                currentItem = new TreeItem<>(createTreeNode(nextNode));

                if(getNextPtreeNode(nextNode) == null) {
                    String goalState;
                    if(nextNode.getStateAfterStmt().getSelectedGoalNode() != null) {
                        goalState = (nextNode.getStateAfterStmt().getSelectedGoalNode().isClosed())? "Closed Goal" : "Open Goal";
                    } else {
                        int indexGoal = nextNode.getStateAfterStmt().getGoals().indexOf(goalNode);
                        goalState = (nextNode.getStateAfterStmt().getGoals().get(indexGoal).isClosed())? "Closed Goal" : "Open Goal";
                    }
                    return new TreeItem<>(new TreeNode(goalState, nextNode.getStateAfterStmt().getGoals().get(0).getData().getNode()));
                }
                nextNode = getNextPtreeNode(nextNode);
            }



            do {
                if (getNextPtreeNode(nextNode) == null) { //End of Script
                    String goalState;
                    if(nextNode.getStateAfterStmt().getSelectedGoalNode() != null) {
                        goalState = (nextNode.getStateAfterStmt().getSelectedGoalNode().isClosed())? "Closed Goal" : "Open Goal";
                    } else {
                        int indexGoal = nextNode.getStateAfterStmt().getGoals().indexOf(goalNode);
                        goalState = (nextNode.getStateAfterStmt().getGoals().get(indexGoal).isClosed())? "Closed Goal" : "Open Goal";
                    }
                    currentItem.getChildren().add(new TreeItem<>(new TreeNode(goalState, nextNode.getStateAfterStmt().getGoals().get(0).getData().getNode())));
                    return currentItem;
                }
                //TODO: get matches
                if(nextNode.getStatement().getNodeName().equals("CasesStatement")) {
                    nextNode = getNextPtreeNode(nextNode);
                    continue;
                }

                if (!nextNode.getStateBeforeStmt().getSelectedGoalNode().equals(goalNode)) {
                    nextNode= getNextPtreeNode(nextNode);
                    continue;
                }

                currentItem.getChildren().add(new TreeItem<>(createTreeNode(nextNode)));

                //splitting command
                if (nextNode.getStateAfterStmt() != null) { //TODO catch guarded match here
                    if (nextNode.getStateAfterStmt().getGoals().size() > 1) {
                        for (int i = 0; i < nextNode.getStateAfterStmt().getGoals().size(); i++) {
                            TreeItem<TreeNode> child = new TreeItem<>(new TreeNode("Branch " + i, nextNode.getStateBeforeStmt().getSelectedGoalNode().getData().getNode())); //todo right branches
                            //TODO:Branch labels
                            child.getChildren().add(buildScriptTreewithID(nextNode, nextNode.getStateAfterStmt().getGoals().get(i)));
                            currentItem.getChildren().add(child);
                        }
                        return currentItem;
                    }
                }

                nextNode = getNextPtreeNode(nextNode);
            } while (nextNode != null);
            return currentItem;
        }

        @Deprecated
        public TreeNode createTreeNode(Node node) {
            try { //TODO stupid hack for now
                return new TreeNode(node.getAppliedRuleApp().rule().name() + " (line TODO)", node);
            } catch (NullPointerException e) {
                System.out.println(node);
                e.printStackTrace();
                return null;
            }
        }

        /**
         * Create a script Node
         * @param node
         * @return
         */
        public TreeNode createTreeNode(PTreeNode<KeyData> node) {
            try {
                if(node.getStatement() instanceof CallStatement) {
                    return new TreeNode(((CallStatement) node.getStatement()).getCommand() +
                            " (line " + node.getStatement().getStartPosition().getLineNumber() +
                            ")", node.getStateBeforeStmt().getSelectedGoalNode().getData().getNode());
                } else {
                    System.out.println(node.getStatement().getNodeName());
                    return new TreeNode("Temp", node.getStateBeforeStmt().getGoals().get(0).getData().getNode());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }



    }
}
