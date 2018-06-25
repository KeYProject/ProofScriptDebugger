package edu.kit.iti.formal.psdbg.gui.controls;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import de.uka.ilkd.key.proof.Node;
import de.uka.ilkd.key.proof.Proof;
import edu.kit.iti.formal.psdbg.ShortCommandPrinter;
import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.dbg.PTreeNode;
import edu.kit.iti.formal.psdbg.interpreter.dbg.ProofTreeManager;
import edu.kit.iti.formal.psdbg.parser.ast.*;
import javafx.scene.control.TreeItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author A. Weigl, S.Grebing
 */
@RequiredArgsConstructor
public class ScriptTreeTransformation extends KeyProofTreeTransformation {
    final ProofTreeManager<KeyData> manager;
    @Getter
    private final Proof proof;
    @Getter
    private final Node root;

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
    private <T> PTreeNode<T> getNextPtreeNode(PTreeNode<T> current) {
        return (current.getStepInto() != null)? current.getStepInto() :
                current.getStepOver();
    }

    @Override
    public TreeItem<ProofTree.TreeNode> create(Proof proof) {
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
    protected TreeItem<ProofTree.TreeNode> populate(String label, Node n) {
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

    protected TreeItem<ProofTree.TreeNode> itemFactory(Node n) {
        ASTNode ast = mutatedBy.get(n);
        String lbl = (String) ast.accept(new ShortCommandPrinter());
        lbl += "  " + n.serialNr() + " " + toString(n);
        TreeItem<ProofTree.TreeNode> ti = new TreeItem<>(new ProofTree.TreeNode(lbl, n));
        return ti;
    }


    //TODO: Reverse ArrayList in the end or nah?
    @Deprecated
    public ArrayList<String> getBranchLabels(ProofTree.TreeNode node) {
        TreeItem<ProofTree.TreeNode> proofTree = create(proof);

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



    public TreeItem<ProofTree.TreeNode> buildScriptTree(PTreeNode<KeyData> node) {
        TreeItem<ProofTree.TreeNode> tree = create(proof);

//            mutatedBy.forEach((n, astNode) -> {
//                System.out.println("node.serialNr() = " + n.serialNr()+ " astNode "+astNode.getNodeName()+astNode.getStartPosition());
//            });
//            entryExitMap.forEach((node1, node2) -> {
//                System.out.println("node in "+node1.serialNr()+" node out"+node2.serialNr());
//            });
        PTreeNode<KeyData> nextNode = node;
        if(nextNode == null) {
            return null;
        }
        Node root = nextNode.getStateBeforeStmt().getSelectedGoalNode().getData().getNode();
        TreeItem<ProofTree.TreeNode> currentItem = new TreeItem<>(new ProofTree.TreeNode("Proof", nextNode.getStateBeforeStmt().getSelectedGoalNode().getData().getNode()));
        //build the first node after the root
        currentItem.getChildren().add(new TreeItem<>(createTreeNode(nextNode)));
        return buildScriptSubTree(currentItem, nextNode);

       /*while(nextNode != null) {
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

        return currentItem;*/
    }

    public TreeItem<ProofTree.TreeNode> buildScriptSubTree(TreeItem<ProofTree.TreeNode> currentItem, PTreeNode<KeyData> nextNode) {
        nextNode = getNextPtreeNode(nextNode);

        while(nextNode != null) {
            if (getNextPtreeNode(nextNode) == null) { //End of Script
                return currentItem;
            }

            //if we arrive at a cases statement we skip this statement

            if(nextNode.getStateAfterStmt() != null && nextNode.getStateAfterStmt().getGoals().size() > 1){
                //we arrived at a branching statement, add it to the tree and add the branches as new children
                currentItem.getChildren().add(new TreeItem<>(createTreeNode(nextNode)));
                for (int i = 0; i < nextNode.getStateAfterStmt().getGoals().size(); i++) {
                    TreeItem<ProofTree.TreeNode> child = new TreeItem<>(new ProofTree.TreeNode("Branch " + i, nextNode.getStateBeforeStmt().getSelectedGoalNode().getData().getNode())); //todo right branches

                    child.getChildren().add(buildScriptSubTree(buildScriptTreeHelper(nextNode, nextNode.getStateAfterStmt().getGoals().get(i)), nextNode));

                    currentItem.getChildren().add(child);

                }
                return currentItem;
            } else {
                if(nextNode.getStateAfterStmt() != null && nextNode.getStateBeforeStmt() != null) {
                   // currentItem.getChildren().add(buildScriptTreeHelper(nextNode, nextNode.getStateAfterStmt().getSelectedGoalNode()));
                    currentItem.getChildren().add(new TreeItem<>(createTreeNode(nextNode)));

                } else {
                    currentItem.getChildren().add(new TreeItem<>(createTreeNode(nextNode)));


                }
            }
            nextNode = getNextPtreeNode(nextNode);

        } return currentItem;
    }

    private TreeItem<ProofTree.TreeNode> buildScriptTreeHelper(PTreeNode<KeyData> node, GoalNode<KeyData> keyDataGoalNode) {
        assert node.getStateBeforeStmt().getGoals().get(0).getData() !=  null;

        TreeItem<ProofTree.TreeNode> currentItem = null;
        PTreeNode<KeyData> nextNode = getNextPtreeNode(node);

        while(currentItem == null){
            //skip if we reach a cases or case
            if(nextNode.getStatement().getNodeName().equals("CasesStatement")
                    || nextNode.getStatement() instanceof CaseStatement) {
                nextNode = getNextPtreeNode(nextNode);
                continue;
            }
            //handle if we reach a matchexpression
            if(nextNode.getStatement() instanceof MatchExpression){
                MatchExpression match = (MatchExpression) nextNode.getStatement();
                Expression pattern = match.getPattern();

                currentItem = new TreeItem<>(new ProofTree.TreeNode(match.getRuleContext().getText()+ " (line "+
                        match.getStartPosition().getLineNumber()+")", keyDataGoalNode.getData().getNode()));
                continue;


            }

            //handle if we reach other statements
            if(nextNode.getStateBeforeStmt() == null ||
                    nextNode.getStateBeforeStmt().getGoals() == null){
                currentItem = new TreeItem<>(new ProofTree.TreeNode("Temp", keyDataGoalNode.getData().getNode()));


            } else {

                boolean bool = mutatedBy.get(keyDataGoalNode.getData().getNode()).equals(nextNode.getStatement());
                if(bool) {
                    currentItem = new TreeItem<>(new ProofTree.TreeNode(((CallStatement) nextNode.getStatement()).getCommand() +
                            " (line " + nextNode.getStatement().getStartPosition().getLineNumber() +
                            ")", keyDataGoalNode.getData().getNode()));

                } else {
                     CallStatement nextNodeName = (CallStatement) mutatedBy.get(keyDataGoalNode.getData().getNode());
                    currentItem = new TreeItem<>(new ProofTree.TreeNode(nextNodeName.getCommand() +
                            " (line " + nextNodeName.getStartPosition().getLineNumber() +
                            ")", keyDataGoalNode.getData().getNode()));

                }
            }
        }
        currentItem = buildScriptSubTree(currentItem, nextNode);
     //   currentItem.getChildren().add(buildScriptTreeHelper(nextNode, keyDataGoalNode));
        return currentItem;
    }



/*       private TreeItem<TreeNode> buildScriptTreewithID(PTreeNode<KeyData> node, GoalNode<KeyData> goalNode) {
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
    }*/

    /**
     * Create a script Node
     * @param node
     * @return
     */
    public ProofTree.TreeNode createTreeNode(PTreeNode<KeyData> node) {
        try {
            if(node == null){
                System.out.println("Node is null");
            }

            if(node.getStatement() instanceof CallStatement) {
                String command = ((CallStatement) node.getStatement()).getCommand();

                int lineNumber = node.getStatement().getStartPosition().getLineNumber();
                if(lineNumber == -1 || command.equals("")){
                    System.out.println(node.getStatement().getNodeName());
                    return new ProofTree.TreeNode("TempEnd", node.getStateBeforeStmt().getGoals().get(0).getData().getNode());
                }
                Node node1 = node.getStateBeforeStmt().getSelectedGoalNode().getData().getNode();
                return new ProofTree.TreeNode(command +
                        " (line " + lineNumber +
                        ")", node1);
            } else {
                System.out.println(node.getStatement().getNodeName());
                return new ProofTree.TreeNode(node.getStatement().getNodeName(), node.getStateBeforeStmt().getGoals().get(0).getData().getNode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Build the script tree, which contains all branchlabels of the proof tree, but nodes are only nodes from the script
     */
/*       public void showScriptTree() {

        if (root != null) {
            TreeItem<TreeNode> tree = create(proof);
            mutatedBy.forEach((node, astNode) -> {
                System.out.println("node.serialNr() = " + node.serialNr()+ "astNode "+astNode.getNodeName()+astNode.getStartPosition());
            });
            treeScript.setRoot(buildScriptTree(manager.getStartNode()));
            treeScript.refresh();
        }



    }*/

}
