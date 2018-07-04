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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


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

        while (currentNode.getStateAfterStmt() != null) {
            List<GoalNode<KeyData>> goalsAfterStmt = currentNode.getStateAfterStmt().getGoals();
            if (goalsAfterStmt.size() == 1) {
                PTreeNode<KeyData> nextPTreeNode = currentNode.computeNextPtreeNode(currentNode);
                ScriptTreeNode newNode = new ScriptTreeNode(nextPTreeNode, goalsAfterStmt.get(0).getData().getNode());

                //set parent and children relation
                newNode.setParent(currentScriptNode);
                currentScriptNode.setChildren(Collections.singletonList(newNode));

                //reset Pointer for next round
                currentNode = nextPTreeNode;
                currentScriptNode = newNode;
            } else if (goalsAfterStmt.size() > 1) {

            } else {

            }
        }
    }





}
