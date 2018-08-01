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
import edu.kit.iti.formal.psdbg.parser.ast.CaseStatement;
import edu.kit.iti.formal.psdbg.parser.ast.GuardedCaseStatement;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import sun.reflect.generics.tree.Tree;

import java.security.Key;
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
        ScriptTreeNode rootNode = new ScriptTreeNode(rootPTreeNode, root, rootPTreeNode.getStatement().getStartPosition().getLineNumber());
        mapping = new HashMap<Node, AbstractTreeNode>();
        front = new ArrayList<>();
        sortedList = new ArrayList<>();
        //putIntoMapping(root,rootNode);//mapping.put(root, rootNode); TODO: for testing
        State<KeyData> stateAfterStmt = rootPTreeNode.getStateAfterStmt();
        if (stateAfterStmt != null) {
            for (GoalNode<KeyData> g : stateAfterStmt.getGoals()) {
                putIntoMapping(g.getData().getNode(), null);
                front.add(g.getData().getNode());
            }
        }
        this.rootNode = rootNode;
        computeList();
        compute();
        addParentsAndChildren();
        mapping.size();

        //TODO: remove
        System.out.println(getMappingString(mapping.get(root)));


    }

    private void addParentsAndChildren() {
        Node current;
        AbstractTreeNode currentParent;

        Iterator<PTreeNode<KeyData>> iter = sortedList.listIterator(0);
        while(iter.hasNext()){
            PTreeNode<KeyData> next = iter.next();
            if (next.getStateAfterStmt() != null) {
                Map.Entry<Node, AbstractTreeNode> entry = getMap(next);
                if(entry == null) {
                    continue;
                }
                currentParent = entry.getValue();
                current = entry.getKey();

                //no children -> last mutator

                if(next.getStateAfterStmt().getGoals().size() == 0 && currentParent instanceof ScriptTreeNode) {
                     /* PTreeNode<KeyData> previous = (next.getStepInvInto() != null)? next.getStepInvInto(): next.getStepInvOver();
                    if(previous == null) {
                        continue;
                    }

                    Map.Entry<Node, AbstractTreeNode> preventry = getMap(previous);
                    Node prevNode = previous.getStateBeforeStmt().getSelectedGoalNode().getData().getNode();

                    AbstractTreeNode prevParent =(preventry == null)? mapping.get(prevNode) :preventry.getValue();
                    Node prev =(preventry == null)? prevNode: preventry.getKey(); //TODO: sinnnlos?

                    mapping.get(current).setParent(prevParent);

                    //add children TODO: remove redundancy
                    if(mapping.get(prev).getChildren() == null) {
                        List<AbstractTreeNode> childlist = new ArrayList<>();
                        childlist.add(mapping.get(current));
                        mapping.get(prev).setChildren(childlist);
                    } else {
                        mapping.get(prev).getChildren().add(mapping.get(current));
                    }

                    continue;
                    */
                }

                //has children
                for (GoalNode<KeyData> gn: next.getStateAfterStmt().getGoals()) {
                    mapping.get(gn.getData().getNode()).setParent(currentParent);

                    //add children
                    if(mapping.get(current).getChildren() == null) {
                        List<AbstractTreeNode> childlist = new ArrayList<>();
                        childlist.add(mapping.get(gn.getData().getNode()));
                        mapping.get(current).setChildren(childlist);
                    } else {
                        mapping.get(current).getChildren().add(mapping.get(gn.getData().getNode()));
                    }


                    /*
                    //set children
                    List<AbstractTreeNode> children = mapping.get(gn.getData().getNode()).getParent().getChildren();
                    if( children == null) {
                        List<AbstractTreeNode> childlist = new ArrayList<>();
                        childlist.add(mapping.get(gn.getData().getNode()));
                        mapping.get(gn.getData().getNode()).getParent().setChildren(childlist);
                    } else {
                        if(!children.contains(mapping.get(gn.getData().getNode()))) {
                            children.add(mapping.get(gn.getData().getNode()));
                        }
                    }
                    */
                }
            }
            /*
            if(next.getStateBeforeStmt().getSelectedGoalNode() != null) {
                current = next.getStateBeforeStmt().getSelectedGoalNode().getData().getNode();
                currentParent = mapping.get(current);
            }
            */
        }

        //mapping.size();
    }

    /*
    private void insertBranchLabels(Node parent, Node child) {
        //TODO: add in mappimng and don't forget to double link
        String[] branchlabels;

        String currentBranchlabel = child.getNodeInfo().getBranchLabel();
        while(currentBranchlabel != parent.getNodeInfo().getBranchLabel()) {

        }

    }
*/

    private Map.Entry<Node, AbstractTreeNode>  getMap(PTreeNode<KeyData> treeNode) {
        Iterator it = mapping.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<Node, AbstractTreeNode> pair = (Map.Entry)it.next();
            if (pair.getValue() instanceof  ScriptTreeNode) {
                if(((ScriptTreeNode)pair.getValue()).getScriptState() == treeNode) {
                    return pair;
                }
            }
        }
        return null;
    }

    private void computeList() {
        while (currentNode != null) {
            sortedList.add(currentNode);
            currentNode = currentNode.getNextPtreeNode(currentNode);
        }
    }

    public void compute() {
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

    public TreeItem<TreeNode> toView() {
        TreeItem<TreeNode> treeItem = new TreeItem<>(new TreeNode("Proof", rootNode.getKeyNode()));


        List<AbstractTreeNode> children = mapping.get(rootNode.getKeyNode()).getChildren();
            treeItem.getChildren().add(new TreeItem<TreeNode>(mapping.get(rootNode.getKeyNode()).toTreeNode()));
        while (children.size() == 1) {
            treeItem.getChildren().add(new TreeItem<>(children.get(0).toTreeNode()));
            children = children.get(0).getChildren();
        }

        if(children.size() != 0) {
            children.forEach(k -> treeItem.getChildren().add(rekursiveToView(k)));
        }
        return treeItem;
    }

    private TreeItem<TreeNode> rekursiveToView(AbstractTreeNode current) {
        TreeItem<TreeNode> treeItem = new TreeItem<>(current.toTreeNode()); //TODO: sollte eig immer ein Branchlabel sein

        List<AbstractTreeNode> children = current.getChildren();


        while (children != null && children.size() == 1) {
            treeItem.getChildren().add(new TreeItem<>(children.get(0).toTreeNode()));
            children = children.get(0).getChildren();
        }
        if (children == null) {
           return treeItem;
        }

        if(children.size() != 0) {
            children.forEach(k -> treeItem.getChildren().add(rekursiveToView(k)));
        }
        return treeItem;
    }

    private class ScriptVisitor extends DefaultASTVisitor<Void> {

        @Override
        public Void visit(CallStatement call) {
            Node n = nextPtreeNode.getStateBeforeStmt().getSelectedGoalNode().getData().getNode();
            ScriptTreeNode sn = new ScriptTreeNode(nextPtreeNode, n, nextPtreeNode.getStatement().getStartPosition().getLineNumber());
            // sn.setParent(mapping.get(current));
           if(nextPtreeNode.getStateBeforeStmt() == nextPtreeNode.getStateAfterStmt()) {
               sn.setSucc(false);
           }
            putIntoMapping(n, sn);
           //mapping.replace(n,sn);
            front.remove(n);
            // current = n;

            List<GoalNode<KeyData>> children = nextPtreeNode.getStateAfterStmt().getGoals();

            switch (children.size()) {
                case 0:
                    DummyGoalNode goalnode = new DummyGoalNode(nextPtreeNode.getStateBeforeStmt().getSelectedGoalNode().isClosed(), nextPtreeNode.getStateBeforeStmt().getSelectedGoalNode().getData().getNode());
                    goalnode.setParent(sn);

                    List<AbstractTreeNode> childlist = new ArrayList<>();
                    childlist.add(goalnode);
                    sn.setChildren(childlist);


                    putIntoMapping(sn.getKeyNode(), goalnode);
                    break;
                case 1:
                    putIntoMapping(children.get(0).getData().getNode(), null);
                    front.add(children.get(0).getData().getNode());
                    break;
                default: //multiple open goals/children -> branch labels
                    int branchcounter = 1;
                    for (GoalNode<KeyData> gn : children) {
                        Node node = gn.getData().getNode();
                        BranchLabelNode branchNode = (node.getNodeInfo().getBranchLabel() != null)? new BranchLabelNode(node, node.getNodeInfo().getBranchLabel()):
                                new BranchLabelNode(node, "Case " + branchcounter);
                        putIntoMapping(node, branchNode);
                        front.add(node);
                        branchcounter++;
                    }
            }
            return null;
        }



        @Override
        public Void visit(GuardedCaseStatement caseStatement) {
            PTreeNode<KeyData> nextintoptn = nextPtreeNode.getStepInto();
            ScriptTreeNode match = new ScriptTreeNode(nextPtreeNode, nextPtreeNode.getStateBeforeStmt().getSelectedGoalNode().getData().getNode(), nextPtreeNode.getStatement().getStartPosition().getLineNumber());
            match.setMatchEx(true);

           if (nextintoptn == null) {
                match.setSucc(false);
                //TODO: add stuff
               return null;
            }
            Node n = nextintoptn.getStateBeforeStmt().getSelectedGoalNode().getData().getNode();
            putIntoMapping(n,match);
            return null;
        }


    }
    private void putIntoMapping(Node node, AbstractTreeNode treeNode) { //TODO: more usage?

        if(mapping.get(node) == null) {
            mapping.put(node, treeNode);
        } else {
            if(mapping.get(node).getChildren() == null) {
                mapping.get(node).setChildren(new ArrayList<AbstractTreeNode>());
            }
            if(!mapping.get(node).getChildren().contains(treeNode))
            mapping.get(node).getChildren().add(treeNode);
        }
    }

    private String getMappingString(AbstractTreeNode node) {
        String s = "";
        if(node != null) {
            s += node.toTreeNode().label + "\n";
            List<AbstractTreeNode> children = node.getChildren();
            if (children == null) return s;
            if(children.size() == 1) {
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
