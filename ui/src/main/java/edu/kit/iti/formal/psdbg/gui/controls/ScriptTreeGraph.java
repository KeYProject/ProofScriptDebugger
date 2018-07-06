package edu.kit.iti.formal.psdbg.gui.controls;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import de.uka.ilkd.key.proof.Node;
import edu.kit.iti.formal.psdbg.gui.controls.ScriptTree.AbstractTreeNode;
import edu.kit.iti.formal.psdbg.gui.controls.ScriptTree.ScriptTreeNode;
import edu.kit.iti.formal.psdbg.interpreter.KeYProofFacade;
import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.data.State;
import edu.kit.iti.formal.psdbg.interpreter.dbg.PTreeNode;
import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;
import edu.kit.iti.formal.psdbg.parser.ast.CallStatement;
import edu.kit.iti.formal.psdbg.parser.ast.CaseStatement;

import java.util.*;


public class ScriptTreeGraph {

    private ScriptTreeNode rootNode;

    private Map<Node, AbstractTreeNode> mapping;

    private List<Node> front;

    private PTreeNode<KeyData> currentNode;

    private List<PTreeNode<KeyData>> sortedList;
    private final MutableGraph<AbstractTreeNode> graph =
            GraphBuilder.directed().allowsSelfLoops(false).build();

    public void createGraph(PTreeNode<KeyData> rootPTreeNode, Node root) {

        this.currentNode = rootPTreeNode;
        ScriptTreeNode rootNode = new ScriptTreeNode(rootPTreeNode, root);
        mapping = new HashMap<Node, AbstractTreeNode>();
        front = new ArrayList<>();
        sortedList = new ArrayList<>();
        mapping.put(root, rootNode);
        State<KeyData> stateAfterStmt = rootPTreeNode.getStateAfterStmt();
        if (stateAfterStmt != null) {
            for (GoalNode<KeyData> g : stateAfterStmt.getGoals()) {
                mapping.put(g.getData().getNode(), null);
                front.add(g.getData().getNode());
            }
        }
        this.rootNode = rootNode;
        computeList();
        compute();
        addParents();
        mapping.size();


    }

    private void addParents() {
        Node current = rootNode.getKeyNode();
        AbstractTreeNode currentParent = mapping.get(current);

        Iterator<PTreeNode<KeyData>> iter = sortedList.listIterator(0);
        while(iter.hasNext()){
            PTreeNode<KeyData> next = iter.next();
            if (next.getStateAfterStmt() != null) {
                for (GoalNode<KeyData> gn: next.getStateAfterStmt().getGoals()) {
                    mapping.get(gn.getData().getNode()).setParent(currentParent);
                }
            }
            if(next.getStateBeforeStmt().getSelectedGoalNode() != null) {
                current = next.getStateBeforeStmt().getSelectedGoalNode().getData().getNode();
                currentParent = mapping.get(current);
            }
        }

        mapping.size();
    }

    private void computeList() {
        while (currentNode != null) {
            sortedList.add(currentNode);
            currentNode = currentNode.getNextPtreeNode(currentNode);
        }
    }

    public void compute() {
        Iterator<PTreeNode<KeyData>> iter = sortedList.listIterator(0);
        //Node current = rootNode.getKeyNode();
        while (iter.hasNext()) {
            PTreeNode<KeyData> nextPtreeNode = iter.next();
            ASTNode statement = nextPtreeNode.getStatement();
            //check for selected node and find the node in map
            if (nextPtreeNode.getStateBeforeStmt().getSelectedGoalNode() != null) {
                if (statement instanceof CallStatement) {
                    Node n = nextPtreeNode.getStateBeforeStmt().getSelectedGoalNode().getData().getNode();
                    ScriptTreeNode sn = new ScriptTreeNode(nextPtreeNode, n);
                   // sn.setParent(mapping.get(current));
                    mapping.replace(n, sn);
                    front.remove(n);
                   // current = n;
                    for (GoalNode<KeyData> gn : nextPtreeNode.getStateAfterStmt().getGoals()) {
                        mapping.put(gn.getData().getNode(), null);
                        front.add(gn.getData().getNode());
                    }

                }
                if(statement instanceof CaseStatement){

                }
            }
        }
        mapping.size();
        mapping.forEach((node, abstractTreeNode) -> System.out.println("node.serialNr() = " + node.serialNr() + " " + abstractTreeNode.toString()));
    }
}
