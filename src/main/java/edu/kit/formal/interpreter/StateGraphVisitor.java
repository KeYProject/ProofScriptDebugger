package edu.kit.formal.interpreter;


import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import edu.kit.formal.interpreter.data.KeyData;
import edu.kit.formal.interpreter.data.State;
import edu.kit.formal.proofscriptparser.DefaultASTVisitor;
import edu.kit.formal.proofscriptparser.ast.ASTNode;
import edu.kit.formal.proofscriptparser.ast.ProofScript;

/**
 * Created by sarah on 6/26/17.
 */
public class StateGraphVisitor extends DefaultASTVisitor<Void> {
    Interpreter currentInterpreter;
    /**
     * Graph that is computed on the fly in order to allow stepping
     */
    private MutableValueGraph<PTreeNode, EdgeTypes> stateGraph;
    private PTreeNode root;


    private PTreeNode lastNode;

    public StateGraphVisitor(Interpreter inter, ProofScript mainScript) {
        stateGraph = ValueGraphBuilder.directed().build();
        this.currentInterpreter = inter;
        State<KeyData> initState = currentInterpreter.getCurrentState();
        System.out.println(initState);
        PTreeNode initNode = new PTreeNode(mainScript);
        initNode.setState(initState);
        stateGraph.addNode(initNode);
        root = initNode;
        lastNode = initNode;

    }

    public MutableValueGraph<PTreeNode, EdgeTypes> getStateGraph() {
        return stateGraph;
    }

    public PTreeNode getRootNode() {
        return root;
    }

    public PTreeNode getLastNode() {
        return lastNode;
    }

    @Override
    public Void defaultVisit(ASTNode node) {
        PTreeNode newStateNode = new PTreeNode(node);
        newStateNode.setState(currentInterpreter.getCurrentState());
        stateGraph.addNode(newStateNode);
        stateGraph.putEdgeValue(lastNode, newStateNode, EdgeTypes.STATE_FLOW);
        lastNode = newStateNode;
        System.out.println(asdot());
        return null;
    }

    public String asdot() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph G {\nnode [shape=rect]\n ");

        stateGraph.nodes().forEach(n -> {
            sb.append(n.hashCode())
                    .append(" [label=\"")
                    .append(n.getScriptstmt().getNodeName())
                    .append("@")
                    .append(n.getScriptstmt().getStartPosition().getLineNumber())
                    .append("\"]\n");
        });

        stateGraph.edges().forEach(edge -> {
            sb.append(edge.source().hashCode())
                    .append(" -> ")
                    .append(edge.target().hashCode())
                    .append(" [label=\"")
                    .append(stateGraph.edgeValue(edge.source(), edge.target()).name())
                    .append("\"]\n");
        });

        sb.append("}");
        return sb.toString();
    }
}
