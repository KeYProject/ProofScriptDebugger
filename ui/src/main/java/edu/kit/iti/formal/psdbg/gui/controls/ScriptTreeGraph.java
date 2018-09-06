package edu.kit.iti.formal.psdbg.gui.controls;

import com.google.common.collect.Lists;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import de.uka.ilkd.key.proof.Node;
import edu.kit.iti.formal.psdbg.gui.controls.ScriptTree.*;
import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.data.State;
import edu.kit.iti.formal.psdbg.interpreter.dbg.PTreeNode;
import edu.kit.iti.formal.psdbg.parser.DefaultASTVisitor;
import edu.kit.iti.formal.psdbg.parser.ast.*;
import javafx.scene.control.TreeItem;
import lombok.Getter;

import java.util.*;


public class ScriptTreeGraph {

    @Getter
    private ScriptTreeNode rootNode;

    @Getter
    private Map<Node, AbstractTreeNode> mapping;

    private List<Node> front;

    private PTreeNode<KeyData> currentNode;

    private PTreeNode<KeyData> nextPtreeNode;

    private List<PTreeNode<KeyData>> sortedList;

    private List<PlaceholderNode> placeholderNodes;

    private HashMap<Node, PTreeNode> foreachNodes;
    private final MutableGraph<AbstractTreeNode> graph =
            GraphBuilder.directed().allowsSelfLoops(false).build();

    public void createGraph(PTreeNode<KeyData> rootPTreeNode, Node root) {

        this.currentNode = rootPTreeNode;
        if(currentNode == null) return;
        ScriptTreeNode rootNode = new ScriptTreeNode(root, rootPTreeNode, rootPTreeNode.getStatement().getStartPosition().getLineNumber());
        mapping = new HashMap<>();
        foreachNodes = new HashMap<>();
        placeholderNodes = new ArrayList<>();
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
        addGoals();

        mapping.size();

        //TODO: remove following
        System.out.print(front);
        System.out.println(getMappingString(mapping.get(root)));


    }

    /**
     * creates the connection between parent and children in mapping, which haven't been set yet
     */
    @Deprecated
    private void addParentsAndChildren() {
        Node currentNode;
        AbstractTreeNode currentTreenode;

        Iterator<PTreeNode<KeyData>> iter = sortedList.listIterator(0);

        //iterate through all ptreenodes in execution order
        while (iter.hasNext()) {
            PTreeNode<KeyData> next = iter.next();

            if (next.getStateAfterStmt() != null) {
                Map.Entry<Node, AbstractTreeNode> entry = getMappingEntry(next); //get entry in mapping
                if (entry == null) {
                    continue;
                }
                currentTreenode = entry.getValue();
                currentNode = entry.getKey();

                    //set parent
                    Node parent = currentNode.parent();
                    if(parent != null && currentTreenode != null) {
                    addToSubChildren(searchParentInMapping(parent), mapping.get(currentNode));

                }

                //set children
                for (GoalNode<KeyData> gn: next.getStateAfterStmt().getGoals()) {
                        if(mapping.get(gn.getData().getNode()) != null && mapping.get(gn.getData().getNode()).getParent() != null)
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
            if (atn == null) return;
            if(mapping.get(node) == null || mapping.get(node) == atn) return;
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
        if(atn == null) return;
        if(mapping.get(node) == atn) return;

            //no children
            if (mapping.get(node).getChildren() == null) {
                List<AbstractTreeNode> childlist = new ArrayList<>();
                childlist.add(atn);
                mapping.get(node).setChildren(childlist);
            } else {

                List<AbstractTreeNode> childlist = mapping.get(node).getChildren();
                if(childlist.contains(atn)) return;

                while (childlist.get(0) != null && childlist.get(0).getChildren() != null) {
                    if(childlist.contains(atn)) return;
                    childlist = childlist.get(0).getChildren();

                }
                List<AbstractTreeNode> subchild = new ArrayList<>();
                subchild.add(atn);
                childlist.get(0).setChildren(subchild);
                }
        }

    /**
     * Returns an entry in mapping that was created on given PTreeNode ptn
     * @param ptn
     * @return
     */
    private Map.Entry<Node, AbstractTreeNode> getMappingEntry(PTreeNode < KeyData > ptn) {
            Iterator it = mapping.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Node, AbstractTreeNode> pair = (Map.Entry) it.next();
                if (pair.getValue() instanceof ScriptTreeNode) {
                    if (((ScriptTreeNode) pair.getValue()).getScriptState() == ptn) {
                        return pair;
                    }
                }
            }
            return null;
        }

    /**
     * Fills sortedList with PtreeNodes in execution order
     */
    private void computeList () {
            while (currentNode != null) {
                sortedList.add(currentNode);
                currentNode = currentNode.getNextPtreeNode(currentNode);
            }
        }

    /**
     * Fills mapping/Creates model for graph
     */
    public void compute () {
            Iterator<PTreeNode<KeyData>> iter = sortedList.listIterator(0);
            ScriptVisitor visitor = new ScriptVisitor();

            ASTNode statement;

            while (iter.hasNext()) {
                nextPtreeNode = iter.next();
                statement = nextPtreeNode.getStatement();
                    statement.accept(visitor);

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
            treeItem.getChildren().add(new TreeItem<>(dummy.toTreeNode()));
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
            TreeItem<TreeNode> treeItem = new TreeItem<>(current.toTreeNode()); //

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
                if (nextPtreeNode.toString().equals("End of Script")) return null;
                Node callnode = nextPtreeNode.getStateBeforeStmt().getSelectedGoalNode().getData().getNode();
                ScriptTreeNode sn = new ScriptTreeNode(callnode, nextPtreeNode, nextPtreeNode.getStatement().getStartPosition().getLineNumber());
                //check if statement was applicable
                if (nextPtreeNode.getStateBeforeStmt() == nextPtreeNode.getStateAfterStmt()) {
                    sn.setSucc(false);
                }

                replacePlaceholder(callnode, sn);
                putIntoMapping(callnode, sn);
                front.remove(callnode);


                List<GoalNode<KeyData>> children = nextPtreeNode.getStateAfterStmt().getGoals();


                switch (children.size()) {
                    case 0:
                        putIntoFront(callnode);
                        checkIfForeachEnd(callnode);
                        addPlaceholder(sn, sn.getNode());
                        break;
                    case 1:
                       // putIntoMapping(children.get(0).getData().getNode(), null);
                        putIntoFront(children.get(0).getData().getNode());
                        addPlaceholder(sn, children.get(0).getData().getNode());
                        break;
                    default: //multiple open goals/children -> branch labels
                        int branchcounter = 1;
                        for (GoalNode<KeyData> gn : children) {
                            Node childnode = gn.getData().getNode();

                            List<BranchLabelNode> branchlabel = getBranchLabels(nextPtreeNode.getStateBeforeStmt().getSelectedGoalNode(), gn);

                            if(branchlabel.size() != 0) {

                                //TODO: remove
                                System.out.println("_______Branchlabels");
                                Lists.reverse(branchlabel).forEach(k -> System.out.println(k.getNode().serialNr() + " " + k.getLabelName()));

                                insertBranchLabels(callnode, branchlabel);
                                addPlaceholder(branchlabel.get(0), gn.getData().getNode());

                            } else {
                                BranchLabelNode branchNode = new BranchLabelNode(childnode, "Case " + branchcounter);

                                // Link Case -Branchnodes to parent
                                branchNode.setParent(sn);
                                addToChildren(callnode, branchNode);

                                if (mapping.get(childnode) != null) putIntoMapping(childnode, branchNode);

                                addPlaceholder(branchNode, childnode);
                                branchcounter++;
                            }

                            putIntoFront(childnode);
                        }
                }

                if(children.size() > 0) {
                    children.forEach(k -> checkIfForeachEnd(k.getData().getNode()));
                }

                return null;
            }


            @Override
            public Void visit(GuardedCaseStatement caseStatement) {
                PTreeNode<KeyData> nextintoptn = nextPtreeNode.getStepInto();
                ScriptTreeNode match = new ScriptTreeNode(nextPtreeNode.getStateBeforeStmt().getSelectedGoalNode().getData().getNode(),nextPtreeNode,  nextPtreeNode.getStatement().getStartPosition().getLineNumber());

                match.setMatchEx(true);

                // check if match was sucessful
                if (nextintoptn == null) {
                    match.setSucc(false);
                }
                Node n = nextPtreeNode.getStateBeforeStmt().getSelectedGoalNode().getData().getNode();

                replacePlaceholder(n, match);
                putIntoMapping(n, match);
                addPlaceholder(match, n);
                //front.remove(n);
                return null;
            }

            @Override
            public Void visit(DefaultCaseStatement caseStatement) {
                PTreeNode<KeyData> nextintoptn = nextPtreeNode.getStepInto();

                //Default-Case empty or no Default-Case
                if(nextintoptn == null) return null;

                ScriptTreeNode match = new ScriptTreeNode(nextPtreeNode.getStepInto().getStateBeforeStmt().getSelectedGoalNode().getData().getNode(),nextPtreeNode,  nextPtreeNode.getStatement().getStartPosition().getLineNumber());
                match.setMatchEx(true);
                match.setSucc(true);
                Node n = nextintoptn.getStateBeforeStmt().getSelectedGoalNode().getData().getNode();
                replacePlaceholder(n, match);
                putIntoMapping(n, match);
                addPlaceholder(match, n);
                //front.remove(n);
                return null;
            }

            @Override
            public  Void visit(ForeachStatement foreachStatement) {
                List<GoalNode<KeyData>> goals = nextPtreeNode.getStateBeforeStmt().getGoals();
                if (goals.size() == 0) return null;

                goals.forEach(k -> {
                    ForeachTreeNode ftn = new ForeachTreeNode(
                            k.getData().getNode(),
                            nextPtreeNode,
                            nextPtreeNode.getStatement().getStartPosition().getLineNumber(),
                            true);

                    replacePlaceholder(k.getData().getNode(), ftn);

                    addPlaceholder(ftn, k.getData().getNode());
                });

                // add to foreachNodes list -> so the end of a foreach can be catched
                List<GoalNode<KeyData>> aftergoals = (nextPtreeNode.getStateAfterStmt() != null)? nextPtreeNode.getStateAfterStmt().getGoals()
                        : (nextPtreeNode.getStepOver() != null && nextPtreeNode.getStepOver().getStateBeforeStmt() != null) ? nextPtreeNode.getStepOver().getStateBeforeStmt().getGoals()
                        : new ArrayList<>();
                aftergoals.forEach(k -> foreachNodes.put(k.getData().getNode(), nextPtreeNode));
                //
                return null;

            }
        }

        private void addPlaceholder(AbstractTreeNode parent, Node current) {
        PlaceholderNode phn = new PlaceholderNode(current);
        phn.setParent(parent);

        if(mapping.get(current) == null) {
            putIntoMapping(current, phn);
        } else {
                addToSubChildren(parent.getNode(), phn);

        }

        placeholderNodes.add(phn);

        }

        private void replacePlaceholder(Node n, AbstractTreeNode atn) {

        Iterator<PlaceholderNode> iterator = placeholderNodes.iterator();

        while (iterator.hasNext()) {
            PlaceholderNode next = iterator.next();

                if (next.getNode() == n) {
                //search for placeholder in mapping
                AbstractTreeNode current = mapping.get(n);

                if (current instanceof PlaceholderNode ) {
                    atn.setParent(current.getParent()); //TODO -> leads to concurretn exception
                    current.getParent().setChildren(new ArrayList<>(Arrays.asList(atn)));
                    mapping.put(n, atn);
                    iterator.remove();
                    return;
                }


                while (!(current instanceof PlaceholderNode)) {
                   //TODO: if(current.getChildren().size() > 0) return;
                    if (current.getChildren() == null) return;
                    if (current.getChildren().get(0) instanceof PlaceholderNode) {

                        //TODO: insert a variable instead of using atn?
                        atn.setParent(current.getChildren().get(0).getParent());
                        current.setChildren(new ArrayList<>(Arrays.asList(atn)));
                        iterator.remove();
                        return;
                    }
                    current = current.getChildren().get(0);
                }
            }
        };
        return;
        }


    /**
     * put abstracttreenode to right location in the mapping hashmap
     * @param node
     * @param treeNode
     */
    private void putIntoMapping (Node node, AbstractTreeNode treeNode){
        if(treeNode == null)
            return;
            if (mapping.get(node) == null) {
                mapping.put(node, treeNode);
            } else {
                addToSubChildren(node, treeNode);
            }
    }

    private Node searchParentInMapping (Node node) {
        Node parent = node;
        while (mapping.get(parent) == null) {
            parent = parent.parent();
        }
        return parent;
    }

    private void putIntoFront (Node node) {
        if(!front.contains(node)) {
            front.add(node);
        }
    }

    private void checkIfForeachEnd(Node n) {
        if(foreachNodes.containsKey(n)) {
            PTreeNode ptn = foreachNodes.get(n);
            ForeachTreeNode ftn = new ForeachTreeNode(
                    n,
                    ptn,
                    ptn.getStatement().getStartPosition().getLineNumber(),
                    false);

            replacePlaceholder(n, ftn);
            //TODO: replace n or if else
            addPlaceholder(ftn, nextPtreeNode.getStateBeforeStmt().getSelectedGoalNode().getData().getNode());
            foreachNodes.remove(n);
        }
    }

    private List<BranchLabelNode> getBranchLabels(GoalNode<KeyData> parent, GoalNode<KeyData> child) {
        List<BranchLabelNode> branchlabels = new ArrayList<>();
        Node parentnode = parent.getData().getNode();
        Node childnode = child.getData().getNode();
        if(childnode.getNodeInfo().getBranchLabel() != null && isBranchLabel(childnode.getNodeInfo().getBranchLabel())) branchlabels.add( new BranchLabelNode(childnode, childnode.getNodeInfo().getBranchLabel()));
        try {
            while (childnode.parent() != parentnode) {
                if (childnode.getNodeInfo().getBranchLabel() != null
                        && isBranchLabel(childnode.getNodeInfo().getBranchLabel())&& !sameBranchlabelBefore(branchlabels, childnode))
                    branchlabels.add(new BranchLabelNode(childnode, childnode.getNodeInfo().getBranchLabel()));
                childnode = childnode.parent();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return branchlabels;
    }


    private boolean sameBranchlabelBefore(List<BranchLabelNode> branchlabels, Node node) {
        if(branchlabels.size() == 0) return false;
        return branchlabels.get(branchlabels.size()-1).getLabelName().equals(node.getNodeInfo().getBranchLabel());

    }

    private boolean isBranchLabel(String label) {
        if (label.contains("rule") || label.contains("rules")) return false;
        return true;
    }

    private void insertBranchLabels(Node node, List<BranchLabelNode> branchlabels) {
        if(branchlabels == null) return;
        Node topBranchNode = branchlabels.get(branchlabels.size()-1).getNode(); //start from last to first
        int i = branchlabels.size()- 1;

        List<AbstractTreeNode> currentChildren;

        //check which Branchlabels are already in mapping -> afterwards forall x of [0,i]; branchlabels[x] not in mapping yet
        if(mapping.containsKey(topBranchNode) && ((BranchLabelNode)mapping.get(topBranchNode)).getLabelName() == branchlabels.get(i).getLabelName()) {
            currentChildren = mapping.get(topBranchNode).getChildren();
            i--;
            while (0 <= i && currentChildren != null && currentChildren.contains(branchlabels.get(i))) {
                currentChildren = currentChildren.get(currentChildren.indexOf(branchlabels.get(i))).getChildren();
                i--;
            }
        }

        //insert all missing branchlabels

        for (; 0 <= i; i--) {

            //if topBranchNode not in mapping yet
            if(i == branchlabels.size() -1) {
                BranchLabelNode bn = branchlabels.get(i);
                bn.setParent(mapping.get(node));
                addToChildren(node, branchlabels.get(i));
                putIntoMapping(branchlabels.get(i).getNode(), bn);
            } else { //some branchlabels in mapping
                addToChildren(branchlabels.get(i+1).getNode(), branchlabels.get(i));
                putIntoMapping(branchlabels.get(i).getNode(), branchlabels.get(i));
            }

            if (0 <= i - 1) {
                BranchLabelNode child = branchlabels.get(i - 1);
                BranchLabelNode parent = branchlabels.get(i);
                child.setParent(parent);

                addToChildren(parent.getNode(), child);
                /*
                if (i == branchlabels.size() - 2) {
                    addToChildren(node, child);
                } else {
                    addToChildren(parent.getNode(), child);
                }
                   */

            }
        /* old implementation
        mapping.put(node, branchlabels.get(branchlabels.size()-1));


        for (int i = branchlabels.size() -2; 0 <= i; i--) {
            //to check if Branchlabel already exists
            Node n = branchlabels.get(i).getNode();
            if(!mapping.containsKey(n) || mapping.get(n) != branchlabels.get(i)) {
                putIntoMapping(branchlabels.get(i).getNode(), branchlabels.get(i));
                if(0 <= i-1) {

                    BranchLabelNode child = branchlabels.get(i-1);
                    BranchLabelNode parent = branchlabels.get(i);
                    child.setParent(parent);

                    if(i == branchlabels.size() -2) {
                        addToChildren(node, child);
                    } else {
                        addToChildren(parent.getNode(), child);
                    }

                }
            }
        }
*/
        }

    }

    private void addGoals() {
        front.forEach(k ->  replacePlaceholder(k, new DummyGoalNode(k, k.isClosed())));

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

