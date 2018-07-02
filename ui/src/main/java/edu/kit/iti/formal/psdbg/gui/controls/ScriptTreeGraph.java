package edu.kit.iti.formal.psdbg.gui.controls;

import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import de.uka.ilkd.key.proof.Node;
import edu.kit.iti.formal.psdbg.gui.controls.ScriptTree.AbstractTreeNode;
import edu.kit.iti.formal.psdbg.gui.controls.ScriptTree.DummyGoalNode;
import edu.kit.iti.formal.psdbg.gui.controls.ScriptTree.ScriptTreeNode;
import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.dbg.PTreeNode;
import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;

import java.util.Collections;


public class ScriptTreeGraph {
    private ScriptTreeNode rootNode;

    private final MutableGraph<AbstractTreeNode> graph =
            GraphBuilder.directed().allowsSelfLoops(false).build();

    public void createGraph(PTreeNode<KeyData> rootPTreeNode, Node root) {
        ScriptTreeNode rootNode = new ScriptTreeNode(rootPTreeNode, root);
//        rootNode.setKeyNode(root);
//        graph.addNode(rootNode);
        this.rootNode = rootNode;
        computeOuterGraph();

    }

    public void computeOuterGraph(){
    //Am Anfang: rootNode
        ScriptTreeNode currentScriptNode = rootNode;
        PTreeNode<KeyData> currentNode = rootNode.getScriptState();
        while(currentNode.getStateAfterStmt() != null) {
            if (currentNode.getStateAfterStmt() != null) {
                if (currentNode.getStateAfterStmt().getGoals().size() == 1) {
                    PTreeNode<KeyData> nextPTreeNode = currentNode.computeNextPtreeNode(currentNode);
                    ScriptTreeNode newNode = new ScriptTreeNode(nextPTreeNode, currentNode.getStateAfterStmt().getGoals().get(0).getData().getNode());

                    //set parent and children relation
                    newNode.setParent(currentScriptNode);
                    currentScriptNode.setChildren(Collections.singletonList(newNode));

                    //reset Pointer for next round
                    currentNode = nextPTreeNode;
                    currentScriptNode = newNode;
                } else {
                    if (currentNode.getStateAfterStmt().getGoals().size() < 1) {

                    }
                }
            } else {
                break;
            }
        }
    }





}
