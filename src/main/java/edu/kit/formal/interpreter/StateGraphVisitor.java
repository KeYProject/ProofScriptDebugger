package edu.kit.formal.interpreter;


import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import edu.kit.formal.proofscriptparser.DefaultASTVisitor;
import edu.kit.formal.proofscriptparser.ast.*;


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

    /**
     * Control Flow Graph
     */
    private MutableValueGraph<ControlFlowNode, EdgeTypes> graph;

    public StateGraphVisitor(Interpreter inter, ProofScript mainScript, MutableValueGraph<ControlFlowNode, EdgeTypes> cfg) {
        stateGraph = ValueGraphBuilder.directed().build();
        this.currentInterpreter = inter;
        graph = cfg;
        System.out.println(cfg.nodes().stream().toString());

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

    public Void createNewNode(ASTNode node) {
        PTreeNode newStateNode = new PTreeNode(node);
        newStateNode.setState(currentInterpreter.getCurrentState());
        stateGraph.addNode(newStateNode);
        if (lastNode == null) {
            root = newStateNode;
        } else {
            stateGraph.putEdgeValue(lastNode, newStateNode, EdgeTypes.STATE_FLOW);

        }
        lastNode = newStateNode;

        return null;
    }


    @Override
    public Void visit(ProofScript proofScript) {
        createNewNode(proofScript);
        return null;
    }

    @Override
    public Void visit(AssignmentStatement assignment) {
        return createNewNode(assignment);
    }

    @Override
    public Void visit(Statements statements) {
        return createNewNode(statements);
    }

    @Override
    public Void visit(CasesStatement casesStatement) {
        return createNewNode(casesStatement);
    }

    @Override
    public Void visit(CaseStatement caseStatement) {
        return createNewNode(caseStatement);
    }

    @Override
    public Void visit(CallStatement call) {
        return createNewNode(call);
    }

    @Override
    public Void visit(TheOnlyStatement theOnly) {
        return createNewNode(theOnly);
    }

    @Override
    public Void visit(ForeachStatement foreach) {
        return createNewNode(foreach);
    }

    @Override
    public Void visit(RepeatStatement repeatStatement) {
        return createNewNode(repeatStatement);
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
