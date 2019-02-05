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
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author A. Weigl, S.Grebing
 */
@RequiredArgsConstructor
public class ScriptTreeTransformation extends KeyProofTreeTransformation {
    final ProofTreeManager<KeyData> manager;

    private PTreeNode<KeyData> currentPointer;

    public void setPointer(PTreeNode<KeyData> p){
        currentPointer = p;
    }
    /**
     * maps a node to its siblings, that were created by a mutator call.
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

    private <T> PTreeNode<T> computeNextPtreeNode(PTreeNode<T> current) {
        if(current == null){
            return current;
        }
        if(getNextPtreeNode(current) != null){
            return getNextPtreeNode(current);
        }
        if(current.getStepReturn() != null){
            return current.getStepReturn();
        } else {
            while(current.getStepInvInto() != null){
                current = current.getStepInvInto();
            }
            return getNextPtreeNode(current);
        }
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


    public TreeItem<TreeNode> buildTree(PTreeNode<KeyData> rootNode, Proof proof){

        setPointer(rootNode);

        TreeItem<TreeNode> rootTree = new TreeItem<>(new TreeNode("Proof", proof.root()));
        return rootTree;
    }


    public TreeItem<TreeNode> buildScriptTree(PTreeNode<KeyData> node, Proof proof) {

        TreeItem<TreeNode> tree = create(proof);
        PTreeNode<KeyData> nextNode = node;
        setPointer(nextNode);
        if(nextNode == null) {
            return null;
        }
        //build the root and the first node
        GoalNode<KeyData> rootGoalNode = nextNode.getStateBeforeStmt().getSelectedGoalNode();
        Node root = rootGoalNode.getData().getNode();
        TreeItem<TreeNode> currentItem = new TreeItem<>(new TreeNode("Proof", nextNode.getStateBeforeStmt().getSelectedGoalNode().getData().getNode()));
        //build the first node after the root
        TreeNode t = createTreeNode(nextNode);
        if (t != null) currentItem.getChildren().add(new TreeItem<>(t));
        return buildScriptSubTree(currentItem, nextNode, nextNode.getStateAfterStmt().getSelectedGoalNode());


    }

    /**
     * Build the subtrees for the script tree structure
     * @param currentItem
     * @param nextNode
     * @return
     */
    public TreeItem<TreeNode> buildScriptSubTree(TreeItem<TreeNode> currentItem, PTreeNode<KeyData> nextNode, GoalNode<KeyData> currentNode) {

        if (nextNode == null || getNextPtreeNode(nextNode) == null ) { //End of Script
            return currentItem;
        }

        nextNode = getNextPtreeNode(nextNode);
        setPointer(nextNode);

        while(nextNode != null) {

            /*if(nextNode.getStatement() instanceof MatchExpression){
                return currentItem;
            }*/
            //if we have reached a state where we do not need a selector
            if(currentNode == null && nextNode.getStateAfterStmt().getGoals().size() < 2){
                TreeNode t = createTreeNode(nextNode);
                if (t!=null) {
                    currentItem.getChildren().add(new TreeItem<>(t));
                }
                return currentItem;
            }
            if (getNextPtreeNode(nextNode) == null || getNextPtreeNode(nextNode).isLastNode()) { //End of Script
                System.out.println("nextNode = " + nextNode.getStatement());
                return currentItem;
            }

            //if we arrive at a cases statement we skip this statement
            if(nextNode.getStateAfterStmt() != null && nextNode.getStateAfterStmt().getGoals().size() > 1){
                //we arrived at a branching statement, add it to the tree and add the branches as new children
                TreeNode t = createTreeNode(nextNode);
                if(t != null) {
                    currentItem.getChildren().add(new TreeItem<>(t));
                }
                List<GoalNode<KeyData>> goals = nextNode.getStateAfterStmt().getGoals();
                int size = goals.size();
                for (int i = 0; i < size; i++) {
                    currentNode = goals.get(i);
                    TreeItem<TreeNode> child = new TreeItem<>(new TreeNode("Case " + i+1, goals.get(i).getData().getNode())); //todo right branches

                    Pair<TreeItem<TreeNode>, PTreeNode<KeyData>> treeItemPTreeNodePair = buildScriptTreeHelper(nextNode, goals.get(i));
                    child.getChildren().add(treeItemPTreeNodePair.getKey());
                    currentItem.getChildren().add(child);
                    nextNode = computeNextPtreeNode(treeItemPTreeNodePair.getValue());
                    setPointer(nextNode);

                }
                return currentItem;
            } else {
                TreeNode t = createTreeNode(nextNode);
                if (t != null) {
                    currentItem.getChildren().add(new TreeItem<>(t));
                }
            }


            nextNode = getNextPtreeNode(nextNode);
            setPointer(nextNode);
            if(nextNode.getStateAfterStmt() != null) {
                currentNode = nextNode.getStateAfterStmt().getSelectedGoalNode();
            }
        } return currentItem;
    }

    private Pair<TreeItem<TreeNode>, PTreeNode<KeyData>> buildScriptTreeHelper(PTreeNode<KeyData> node, GoalNode<KeyData> keyDataGoalNode) {
        assert node.getStateBeforeStmt().getGoals().get(0).getData() !=  null;

        PTreeNode<KeyData> nextNode;
        TreeItem<TreeNode> currentItem = null;

        if(getNextPtreeNode(node) != null){
            nextNode = getNextPtreeNode(node);
            setPointer(nextNode);
        } else {
            nextNode = node;
            setPointer(nextNode);
        }

        while(currentItem == null){

            //handle if we reach a matchexpression
            if(nextNode.getStatement() instanceof GuardedCaseStatement){
                GuardedCaseStatement stmt = (GuardedCaseStatement) nextNode.getStatement();
                Expression match = (Expression) stmt.getGuard();
                assert nextNode.getStateBeforeStmt().getSelectedGoalNode().equals(keyDataGoalNode);

                currentItem = new TreeItem<>(new TreeNode(match.getRuleContext().getText()+ " (line "+
                        match.getStartPosition().getLineNumber()+")", keyDataGoalNode.getData().getNode()));
                continue;


            }

            //skip if we reach a cases or case
            if(nextNode.getStatement().getNodeName().equals("CasesStatement")
                    || nextNode.getStatement() instanceof CaseStatement
                    || nextNode.getStatement() instanceof MatchExpression) {
                assert nextNode.getStateBeforeStmt().getGoals().contains(keyDataGoalNode);
                nextNode = getNextPtreeNode(nextNode);
                setPointer(nextNode);
                keyDataGoalNode = nextNode.getStateBeforeStmt().getSelectedGoalNode();
                continue;
            }


            //handle if we reach other statements
            if(nextNode.getStateBeforeStmt() == null ||
                    nextNode.getStateBeforeStmt().getGoals() == null
                    //||
                    //nextNode.getStatement().getStartPosition().getLineNumber() < 1
             ){
                continue;


            } else {
                
                assert nextNode.getStateBeforeStmt().getSelectedGoalNode().equals(keyDataGoalNode);
                
                Node node1 = keyDataGoalNode.getData().getNode();
                ASTNode statement= nextNode.getStatement();
                ASTNode astNode = mutatedBy.get(node1);

                if(astNode == null || astNode.equals(statement)) {
                    currentItem = new TreeItem<>(new TreeNode(((CallStatement) nextNode.getStatement()).getCommand() +
                            " (line " + nextNode.getStatement().getStartPosition().getLineNumber() +
                            ")", keyDataGoalNode.getData().getNode()));

                } else {
                    CallStatement nextNodeName = (CallStatement) mutatedBy.get(keyDataGoalNode.getData().getNode());
                    currentItem = new TreeItem<>(new TreeNode(nextNodeName.getCommand() +
                            " (line " + nextNodeName.getStartPosition().getLineNumber() +
                            ")", keyDataGoalNode.getData().getNode()));

                }
                  keyDataGoalNode = nextNode.getStateAfterStmt().getSelectedGoalNode();
            }
        }
        currentItem = buildScriptSubTree(currentItem, nextNode, keyDataGoalNode);
        return new Pair<>(currentItem, currentPointer);
    }





    /**
     * Create a script Node
     * @param node
     * @return
     */
    public TreeNode createTreeNode(PTreeNode<KeyData> node) {
        try {
            if(node == null){
                return null;
            }

            if(node.getStatement() instanceof CallStatement) {
                String command = ((CallStatement) node.getStatement()).getCommand();

                int lineNumber = node.getStatement().getStartPosition().getLineNumber();
                if(lineNumber == -1 || command.equals("")){

                    return new TreeNode("TempEnd", node.getStateBeforeStmt().getGoals().get(0).getData().getNode());
                }
                Node node1 = node.getStateBeforeStmt().getSelectedGoalNode().getData().getNode();
                return new TreeNode(command +
                        " (line " + lineNumber +
                        ")", node1);
            } else {
                return null;
//                return new TreeNode(node.getStatement().getNodeName(), node.getStateBeforeStmt().getGoals().get(0).getData().getNode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
