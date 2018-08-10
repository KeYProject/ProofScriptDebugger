package edu.kit.iti.formal.psdbg.gui.controls;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import de.uka.ilkd.key.proof.Node;
import edu.kit.iti.formal.psdbg.gui.controls.ScriptTree.AbstractTreeNode;
import edu.kit.iti.formal.psdbg.gui.controls.ScriptTree.BranchLabelNode;
import edu.kit.iti.formal.psdbg.gui.controls.ScriptTree.DummyGoalNode;
import edu.kit.iti.formal.psdbg.gui.controls.ScriptTree.ScriptTreeNode;
import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.data.State;
import edu.kit.iti.formal.psdbg.interpreter.dbg.PTreeNode;
import edu.kit.iti.formal.psdbg.parser.DefaultASTVisitor;
import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;
import edu.kit.iti.formal.psdbg.parser.ast.CallStatement;
import edu.kit.iti.formal.psdbg.parser.ast.GuardedCaseStatement;
import javafx.scene.control.TreeItem;
import sun.reflect.generics.tree.Tree;

import java.util.*;


public class ScriptTreeGraph {

    private ScriptTreeNode rootNode;

    private Map<Node, AbstractTreeNode> mapping;

    private List<Node> front;

    private PTreeNode<KeyData> currentNode;

    private PTreeNode<KeyData> nextPtreeNode;

    private List<PTreeNode<KeyData>> sortedList;
    private final MutableGraph<AbstractTreeNode> graph =
            GraphBuilder.directed().allowsSelfLoops(false).build();

    public void createGraph(PTreeNode<KeyData> rootPTreeNode, Node root) {

        this.currentNode = rootPTreeNode;
        if(currentNode == null) return;
        ScriptTreeNode rootNode = new ScriptTreeNode(root, rootPTreeNode, rootPTreeNode.getStatement().getStartPosition().getLineNumber());
        mapping = new HashMap<>();
        front = new ArrayList<>();
        sortedList = new ArrayList<>();
        State<KeyData> stateAfterStmt = rootPTreeNode.getStateAfterStmt();
        if (stateAfterStmt != null) {
            for (GoalNode<KeyData> g : stateAfterStmt.getGoals()) {
                putIntoMapping(g.getData().getNode(), null);
                putIntoFront(g.getData().getNode());
            }
        } else {

        }
        this.rootNode = rootNode;
        computeList();
        compute();
        addParentsAndChildren();
        addGoals();
        mapping.size();

        //TODO: remove following
        System.out.print(front);
        System.out.println(getMappingString(mapping.get(root)));


    }

    /**
     * creates the connection between parent and children in mapping, which haven't been set yet
     */
    private void addParentsAndChildren() {
        Node currentNode;
        AbstractTreeNode currentTreenode;

        Iterator<PTreeNode<KeyData>> iter = sortedList.listIterator(0);
        while (iter.hasNext()) {
            PTreeNode<KeyData> next = iter.next();
            if (next.getStateAfterStmt() != null) {
                Map.Entry<Node, AbstractTreeNode> entry = getMap(next); //get entry in mapping
                if (entry == null) {
                    continue;
                }
                currentTreenode = entry.getValue();
                currentNode = entry.getKey();

                    //set parent
                    Node parent = currentNode.parent();
                    if(parent != null && currentTreenode != null) {
                    addToSubChildren(parent, mapping.get(currentNode));

                }

                //set children
                for (GoalNode<KeyData> gn: next.getStateAfterStmt().getGoals()) {
                        if(mapping.get(gn.getData().getNode()).getParent() != null)
                    mapping.get(gn.getData().getNode()).setParent(currentTreenode);
                    addToChildren(currentNode, mapping.get(gn.getData().getNode()));

                }

                }


            }


        }

    /**
     * Adds an AbstractTreeNode atn to the children in the mapping list with given key node
     * @param node
     * @param atn
     */
    private void addToChildren(Node node, AbstractTreeNode atn) {
            if(mapping.get(node) == atn) return;
            if (mapping.get(node).getChildren() == null) {
                List<AbstractTreeNode> childlist = new ArrayList<>();
                childlist.add(atn);
                mapping.get(node).setChildren(childlist);
            } else {
                if (mapping.get(node).getChildren().contains(atn)) return;
                mapping.get(node).getChildren().add(atn);
            }
        }

    /**
     * Adds an AbstractTreeNode atn to the next available child slot in the mapping list, which hasn't been set yet, with given key node
     * method for upholding the script execution order
     * @param node
     * @param atn
     */
    private void addToSubChildren(Node node, AbstractTreeNode atn) {
            if(mapping.get(node) == atn) return;
            if (mapping.get(node).getChildren() == null) {
                List<AbstractTreeNode> childlist = new ArrayList<>();
                childlist.add(atn);
                mapping.get(node).setChildren(childlist);
            } else {

                List<AbstractTreeNode> childlist = mapping.get(node).getChildren();
                if(childlist.contains(atn)) return;

                while (childlist.get(0).getChildren() != null) {
                    if(childlist.contains(atn)) return;
                    childlist = childlist.get(0).getChildren();

                }
                List<AbstractTreeNode> subchild = new ArrayList<>();
                subchild.add(atn);
                childlist.get(0).setChildren(subchild);
                }
        }

        private Map.Entry<Node, AbstractTreeNode> getMap (PTreeNode < KeyData > treeNode) {
            Iterator it = mapping.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Node, AbstractTreeNode> pair = (Map.Entry) it.next();
                if (pair.getValue() instanceof ScriptTreeNode) {
                    if (((ScriptTreeNode) pair.getValue()).getScriptState() == treeNode) {
                        return pair;
                    }
                }
            }
            return null;
        }

        private void computeList () {
            while (currentNode != null) {
                sortedList.add(currentNode);
                currentNode = currentNode.getNextPtreeNode(currentNode);
            }
        }

        public void compute () {
            Iterator<PTreeNode<KeyData>> iter = sortedList.listIterator(0);
            ScriptVisitor visitor = new ScriptVisitor();

            ASTNode statement;
            //Node current = rootNode.getKeyNode();
            while (iter.hasNext()) {
                nextPtreeNode = iter.next();
                statement = nextPtreeNode.getStatement();
                //check for selected node and find the node in map
                if (nextPtreeNode.getStateBeforeStmt().getSelectedGoalNode() != null) {
                    statement.accept(visitor);
                }
            }

            mapping.size();
            mapping.forEach((node, abstractTreeNode) -> System.out.println("node.serialNr() = " + node.serialNr() + " " + abstractTreeNode.toTreeNode().label));
        }

    /**
     * returns treeItem that represents current Script tree
     * @return
     */
    public TreeItem<TreeNode> toView () {
        TreeItem<TreeNode> treeItem;
        if(rootNode == null) {
            treeItem = new TreeItem<>(new TreeNode("Proof", null));
            DummyGoalNode dummy = new DummyGoalNode(null, false);
            treeItem.getChildren().add(new TreeItem<TreeNode>(dummy.toTreeNode()));
            return treeItem;
        }
            treeItem = new TreeItem<>(new TreeNode("Proof", rootNode.getNode()));


            List<AbstractTreeNode> children = mapping.get(rootNode.getNode()).getChildren();
            if (children == null) return treeItem;
            treeItem.getChildren().add(new TreeItem<>(mapping.get(rootNode.getNode()).toTreeNode()));

            while (children.size() == 1) {
                treeItem.getChildren().add(new TreeItem<>(children.get(0).toTreeNode()));
                children = children.get(0).getChildren();
                if(children == null) return treeItem;
            }

            if (children.size() != 0) {
                children.forEach(k -> treeItem.getChildren().add(rekursiveToView(k)));
            }
            return treeItem;
        }

        private TreeItem<TreeNode> rekursiveToView (AbstractTreeNode current){
            TreeItem<TreeNode> treeItem = new TreeItem<>(current.toTreeNode()); //TODO: sollte eig immer ein Branchlabel sein

            List<AbstractTreeNode> children = current.getChildren();


            while (children != null && children.size() == 1) {
                if(children.get(0) == null) return treeItem;
                    treeItem.getChildren().add(new TreeItem<>(children.get(0).toTreeNode()));
                children = children.get(0).getChildren();
            }
            if (children == null) {
                return treeItem;
            }

            if (children.size() != 0) {
                children.forEach(k -> treeItem.getChildren().add(rekursiveToView(k)));
            }
            return treeItem;
        }

        private class ScriptVisitor extends DefaultASTVisitor<Void> {

            @Override
            public Void visit(CallStatement call) {
                Node n = nextPtreeNode.getStateBeforeStmt().getSelectedGoalNode().getData().getNode();
                ScriptTreeNode sn = new ScriptTreeNode(n, nextPtreeNode, nextPtreeNode.getStatement().getStartPosition().getLineNumber());
                // sn.setParent(mapping.get(current));
                if (nextPtreeNode.getStateBeforeStmt() == nextPtreeNode.getStateAfterStmt()) {
                    sn.setSucc(false);
                }
                putIntoMapping(n, sn);
                //mapping.replace(n,sn);
                front.remove(n);
                // current = n;

                List<GoalNode<KeyData>> children = nextPtreeNode.getStateAfterStmt().getGoals();

                switch (children.size()) {
                    case 0:
                        /*
                        DummyGoalNode goalnode = new DummyGoalNode(nextPtreeNode.getStateBeforeStmt().getSelectedGoalNode().getData().getNode(), nextPtreeNode.getStateAfterStmt().getGoals().size() == 0);
                        goalnode.setParent(sn);

                        
                        List<AbstractTreeNode> childlist = new ArrayList<>();
                        childlist.add(goalnode);
                        sn.setChildren(childlist);


                        putIntoMapping(sn.getNode(), goalnode);*/
                        putIntoFront(n);
                        break;
                    case 1:
                        putIntoMapping(children.get(0).getData().getNode(), null);
                        putIntoFront(children.get(0).getData().getNode());
                        break;
                    default: //multiple open goals/children -> branch labels
                        int branchcounter = 1;
                        for (GoalNode<KeyData> gn : children) {
                            Node node = gn.getData().getNode();
                            BranchLabelNode branchNode = (node.getNodeInfo().getBranchLabel() != null) ? new BranchLabelNode(node, node.getNodeInfo().getBranchLabel()) :
                                    new BranchLabelNode(node, "Case " + branchcounter);
                            mapping.put(node, branchNode);
                            putIntoFront(node);
                            branchcounter++;
                        }
                }
                return null;
            }


            @Override
            public Void visit(GuardedCaseStatement caseStatement) {
                PTreeNode<KeyData> nextintoptn = nextPtreeNode.getStepInto();
                ScriptTreeNode match = new ScriptTreeNode(nextPtreeNode.getStateBeforeStmt().getSelectedGoalNode().getData().getNode(),nextPtreeNode,  nextPtreeNode.getStatement().getStartPosition().getLineNumber());
                match.setMatchEx(true);

                if (nextintoptn == null) {
                    match.setSucc(false);
                    //TODO: add stuff
                    return null;
                }
                Node n = nextintoptn.getStateBeforeStmt().getSelectedGoalNode().getData().getNode();
                putIntoMapping(n, match);
                //front.remove(n);
                return null;
            }


        }

    /**
     * put abstracttreenode to right location in the mapping hashmap
     * @param node
     * @param treeNode
     */
    private void putIntoMapping (Node node, AbstractTreeNode treeNode){

            if (mapping.get(node) == null) {
                mapping.put(node, treeNode);
            } else {
                addToSubChildren(node, treeNode);
            }
    }

    private void putIntoFront (Node node) {
        if(!front.contains(node)) {
            front.add(node);
        }
    }

    private void addGoals() {
        front.forEach(k -> putIntoMapping(k, new DummyGoalNode(k, k.isClosed())));
    }
        private String getMappingString (AbstractTreeNode node){
            String s = "";
            if (node != null) {
                s += node.toTreeNode().label + "\n";
                List<AbstractTreeNode> children = node.getChildren();
                if (children == null) return s;
                if (children.size() == 1) {
                    s += getMappingString(children.get(0));
                } else {
                    s += "  Children:\n";
                    for (AbstractTreeNode child : children) {
                        s += "  " + getMappingString(child);
                    }
                }
            }
            return s;
        }
    }
