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
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.util.*;


public class ScriptTreeGraph {

    @Getter
    @Setter
    private AbstractTreeNode rootNode;

    @Getter
    private Map<Node, AbstractTreeNode> mapping;

    private List<Node> front;

    private PTreeNode<KeyData> currentNode;

    private PTreeNode<KeyData> nextPtreeNode;

    private List<PTreeNode<KeyData>> sortedList;

    private List<PlaceholderNode> placeholderNodes;

    private HashMap<Node, PTreeNode> foreachNodes;
    private HashMap<Node, PTreeNode> repeatNodes;

    /**
     * statistic of scripttree
     */

    private int openGoals = 0;
    private int closedGoals = 0;



    public void createGraph(PTreeNode<KeyData> rootPTreeNode, Node root) {

        AbstractTreeNode rootNode;
        if (rootPTreeNode != null) {
            this.currentNode = rootPTreeNode;
            if (currentNode == null) return;
            rootNode = new ScriptTreeNode(root, rootPTreeNode, rootPTreeNode.getStatement().getStartPosition().getLineNumber());
            mapping = new HashMap<>();
            foreachNodes = new HashMap<>();
            repeatNodes = new HashMap<>();
            placeholderNodes = new ArrayList<>();
            front = new ArrayList<>();
            sortedList = new ArrayList<>();
            State<KeyData> stateAfterStmt = rootPTreeNode.getStateAfterStmt();
            if (stateAfterStmt != null) {
                for (GoalNode<KeyData> g : stateAfterStmt.getGoals()) {
                    putIntoMapping(g.getData().getNode(), null);
                    putIntoFront(g.getData().getNode());
                }
            }
        } else {
            rootNode = new DummyGoalNode(root, root.isClosed());
        }

        this.rootNode = rootNode;
        computeList();
        compute();
        addGoals();
        System.out.println("openGoals = " + openGoals);
        System.out.println("closedGoals = " + closedGoals);
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
                //TODO: Hack for double foreach-end recognition
                if(atn.getParent() == childlist.get(0).getParent()) return;
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
        if (sortedList == null) {
            return;
        }
            Iterator<PTreeNode<KeyData>> iter = sortedList.listIterator(0);
            ScriptVisitor visitor = new ScriptVisitor();

            ASTNode statement;

            while (iter.hasNext()) {
                nextPtreeNode = iter.next();
                statement = nextPtreeNode.getStatement();
                    statement.accept(visitor);

            }

            mapping.size();
           // mapping.forEach((node, abstractTreeNode) -> System.out.println("node.serialNr() = " + node.serialNr() + " " + abstractTreeNode.toTreeNode().label));
        }

        /*
    public TreeItem<AbstractTreeNode> toView () {
        TreeItem<AbstractTreeNode> treeItem;
        if(rootNode == null) {
            treeItem = new TreeItem<>(new AbstractTreeNode(null));
            DummyGoalNode dummy = new DummyGoalNode(null, false);
            treeItem.getChildren().add(new TreeItem<>(dummy));
            return treeItem;
        }
            treeItem = new TreeItem<>(new AbstractTreeNode(null));


            List<AbstractTreeNode> children = mapping.get(rootNode.getNode()).getChildren();
            if (children == null) return treeItem;
            treeItem.getChildren().add(new TreeItem<>(mapping.get(rootNode.getNode())));

            while (children.size() == 1) {
                treeItem.getChildren().add(new TreeItem<>(children.get(0)));
                children = children.get(0).getChildren();
                if(children == null) return treeItem;
            }

            if (children.size() != 0) {
                children.forEach(k -> treeItem.getChildren().add(rekursiveToView(k)));
            }

            return treeItem;
        }

        private TreeItem<AbstractTreeNode> rekursiveToView (AbstractTreeNode current){
            TreeItem<AbstractTreeNode> treeItem = new TreeItem<>(current); //

            List<AbstractTreeNode> children = current.getChildren();


            while (children != null && children.size() == 1) {
                if(children.get(0) == null) return treeItem;
                    treeItem.getChildren().add(new TreeItem<>(children.get(0)));
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
        */

        private class ScriptVisitor extends DefaultASTVisitor<Void> {

            @Override
            public Void visit(CallStatement call) {
                if (nextPtreeNode.toString().equals("End of Script")) return null;
                Node callnode = nextPtreeNode.getStateBeforeStmt().getSelectedGoalNode().getData().getNode();
                ScriptTreeNode sn = new ScriptTreeNode(callnode, nextPtreeNode, nextPtreeNode.getStatement().getStartPosition().getLineNumber());
                //check if statement was applicable
                if (nextPtreeNode.getStateAfterStmt().getGoals().equals(nextPtreeNode.getStateBeforeStmt().getGoals())) {
                    sn.setSucc(false);
                }

                replacePlaceholder(callnode, sn);
                putIntoMapping(callnode, sn);
                front.remove(callnode);


                List<GoalNode<KeyData>> children = nextPtreeNode.getStateAfterStmt().getGoals();


                switch (children.size()) {
                    case 0:
                        putIntoFront(callnode);
                        checkIfRepeatEnd(callnode);
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

                                Lists.reverse(branchlabel).forEach(k ->
                                        System.out.println(k.getNode().serialNr() + " " + k.getLabelName()));

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
                    children.forEach(k -> {
                        checkIfRepeatEnd(k.getData().getNode());
                        checkIfForeachEnd(k.getData().getNode());
                    });
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
                return null;
            }

            @Override
            public Void visit(DefaultCaseStatement caseStatement) {
                PTreeNode<KeyData> nextintoptn = nextPtreeNode.getStepInto();
                ScriptTreeNode match = new ScriptTreeNode(nextPtreeNode.getStepInto().getStateBeforeStmt().getSelectedGoalNode().getData().getNode(),nextPtreeNode,  nextPtreeNode.getStatement().getStartPosition().getLineNumber());
                match.setMatchEx(true);
                match.setSucc(true);
                Node n = nextintoptn.getStateBeforeStmt().getSelectedGoalNode().getData().getNode();
                replacePlaceholder(n, match);
                putIntoMapping(n, match);
                addPlaceholder(match, n);
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

                    replacePlaceholder(
                            searchParentInMapping(k.getData().getNode()), ftn);

                    addPlaceholder(ftn, k.getData().getNode());
                });

                // add to foreachNodes list -> so the end of a foreach can be catched
                List<GoalNode<KeyData>> aftergoals = (nextPtreeNode.getStateAfterStmt() != null)? nextPtreeNode.getStateAfterStmt().getGoals()
                        : (nextPtreeNode.getStepOver() != null && nextPtreeNode.getStepOver().getStateBeforeStmt() != null) ? nextPtreeNode.getStepOver().getStateBeforeStmt().getGoals()
                        : new ArrayList<>();
                aftergoals.forEach(k -> foreachNodes.put(k.getData().getNode(), nextPtreeNode));
                return null;



            }

            @Override
            public Void visit(RepeatStatement repeat){
                List<GoalNode<KeyData>> goals = nextPtreeNode.getStateBeforeStmt().getGoals();
                if (goals.size() == 0) return null;

                goals.forEach(k -> {
                    RepeatTreeNode ftn = new RepeatTreeNode(
                            k.getData().getNode(),
                            nextPtreeNode,
                            nextPtreeNode.getStatement().getStartPosition().getLineNumber(),
                            true);

                    replacePlaceholder(
                            searchParentInMapping(k.getData().getNode()), ftn);

                    addPlaceholder(ftn, k.getData().getNode());
                });

                // add to repeat list -> so the end of a repeat can be catched
                List<GoalNode<KeyData>> aftergoals = (nextPtreeNode.getStateAfterStmt() != null)? nextPtreeNode.getStateAfterStmt().getGoals()
                        : (nextPtreeNode.getStepOver() != null && nextPtreeNode.getStepOver().getStateBeforeStmt() != null) ? nextPtreeNode.getStepOver().getStateBeforeStmt().getGoals()
                        : new ArrayList<>();
                aftergoals.forEach(k -> repeatNodes.put(k.getData().getNode(), nextPtreeNode));
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

                    current.getParent().setChildren(new ArrayList<>(Arrays.asList(atn)));
                    mapping.put(n, atn);
                    iterator.remove();
                    return;
                }


                while (!(current instanceof PlaceholderNode)) {

                    if (current.getChildren() == null) return;
                    if (current.getChildren().get(0) instanceof PlaceholderNode) {


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

    /*
        checks if given node is the end of a foreach-statement
     */
    private void checkIfForeachEnd(Node n) {
        if(foreachNodes.containsKey(n)) {
            PTreeNode ptn = foreachNodes.get(n);
            ForeachTreeNode ftn = new ForeachTreeNode(
                    n,
                    ptn,
                    ptn.getStatement().getStartPosition().getLineNumber(),
                    false);
            replacePlaceholder(n, ftn);
            addPlaceholder(ftn, n);

            foreachNodes.remove(n);

        }
    }

    /*
    checks if given node is the end of a repeat-statement
 */
    private void checkIfRepeatEnd(Node n) {
        if(repeatNodes.containsKey(n)) {
            PTreeNode ptn = repeatNodes.get(n);
            RepeatTreeNode ftn = new RepeatTreeNode(
                    n,
                    ptn,
                    ptn.getStatement().getStartPosition().getLineNumber(),
                    false);
            replacePlaceholder(n, ftn);
            addPlaceholder(ftn, n);

            repeatNodes.remove(n);

        }
    }

    /**
     * Get all Branchlabels from child to parent GoalNode
     * Order of Branchlabels is from bottom to top
     * @param parent
     * @param child
     * @return
     */
    private List<BranchLabelNode> getBranchLabels(GoalNode<KeyData> parent, GoalNode<KeyData> child) {
        List<BranchLabelNode> branchlabels = new ArrayList<>();
        Node parentnode = parent.getData().getNode();
        Node childnode = child.getData().getNode();

        //iterate from bottom to top and check if childnode has siblings -> Branchlabel required
        try {
            while (childnode.parent() != parentnode) {
                if (childnode.parent().childrenCount() > 1) {
                    String calcBranchlabel = (childnode.getNodeInfo().getBranchLabel() == null)?
                            "Case " + (childnode.parent().getChildNr(childnode) + 1)
                            : childnode.getNodeInfo().getBranchLabel();
                    branchlabels.add(new BranchLabelNode(childnode, calcBranchlabel));
                }

                childnode = childnode.parent();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return branchlabels;
    }

    /**
     * Inserts given node after Branchlabelnodes
     * @param node
     * @param branchlabels
     */
    private void insertBranchLabels(Node node, List<BranchLabelNode> branchlabels) {
        if(branchlabels == null) return;
        Node topBranchNode = branchlabels.get(branchlabels.size()-1).getNode(); //start from last to first
        int i = branchlabels.size()- 1;

        List<AbstractTreeNode> currentChildren;

        //check which Branchlabels are already in mapping -> afterwards forall x of [0,i]; branchlabels[x] not in mapping yet
        if(mapping.containsKey(topBranchNode) && ((BranchLabelNode)mapping.get(topBranchNode)).getLabelName() == branchlabels.get(i).getLabelName()) {

            currentChildren = mapping.get(topBranchNode).getChildren();
            i--;
            //check
            while (0 <= i && currentChildren != null && 0 <= getIndexOfAbsNode(currentChildren, branchlabels.get(i))) {
                currentChildren = currentChildren.get(getIndexOfAbsNode(currentChildren, branchlabels.get(i))).getChildren();
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

            // add parent and child relation
            if (0 <= i - 1) {
                BranchLabelNode child = branchlabels.get(i - 1);
                BranchLabelNode parent = branchlabels.get(i);
                child.setParent(parent);

                addToChildren(parent.getNode(), child);

            }


        }

    }

    /**
     * Returns index of AbstractTreeNode in given list if it exists, else -1
     * @param nodeList
     * @param node
     * @return
     */
    private int getIndexOfAbsNode(List<AbstractTreeNode> nodeList, AbstractTreeNode node) {
        for(int i = 0; i < nodeList.size(); i++) {
            if(nodeList.get(i).getNode().serialNr() == node.getNode().serialNr()) {
                return i;
            }
        }
        return -1;
    }

    private void addGoals() {
        if (front == null) {
            return;
        }
        front.forEach(k -> {
            replacePlaceholder(k, new DummyGoalNode(k, k.isClosed()));
            
            //fill statistics
            if(k.isClosed()) {
                closedGoals++;
            } else {
                openGoals ++;
            }
        });
    }






    }

