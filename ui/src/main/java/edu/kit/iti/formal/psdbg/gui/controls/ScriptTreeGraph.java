package edu.kit.iti.formal.psdbg.gui.controls;

import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import de.uka.ilkd.key.proof.Node;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.dbg.PTreeNode;


public class ScriptTreeGraph {
    private ScriptTreeNode rootNode;

    private final Graph<ScriptTreeNode> graph =
            GraphBuilder.directed().allowsSelfLoops(false).build();

    public void createGraph(PTreeNode<KeyData> rootPTreeNode, Node root) {
        ScriptTreeNode rootNode = new ScriptTreeNode(rootPTreeNode);
        rootNode.setKeyNode(root);
        graph.nodes().add(rootNode);
        this.rootNode = rootNode;

    }



}
