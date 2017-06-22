package edu.kit.formal.interpreter;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import edu.kit.formal.interpreter.funchdl.CommandLookup;
import edu.kit.formal.proofscriptparser.DefaultASTVisitor;
import edu.kit.formal.proofscriptparser.ast.*;

/**
 * Visitor to create ProgramFlowGraph
 */
public class ProgramFlowVisitor extends DefaultASTVisitor<Void> {
    private final CommandLookup functionLookup;
    private PTreeNode lastNode;

    private MutableValueGraph<PTreeNode, EdgeTypes> graph = ValueGraphBuilder.directed().allowsSelfLoops(true).build();

    public ProgramFlowVisitor(CommandLookup functionLookup) {
        this.functionLookup = functionLookup;
    }

    public MutableValueGraph<PTreeNode, EdgeTypes> getGraph() {
        return graph;
    }

    @Override
    public Void visit(ProofScript proofScript) {
        PTreeNode scriptNode = new PTreeNode(proofScript);
        lastNode = scriptNode;
        return this.visit(proofScript.getBody());
    }

    @Override
    public Void visit(AssignmentStatement assignment) {
        PTreeNode node = new PTreeNode(assignment);
        graph.addNode(node);
        lastNode = node;
        return null;
    }

    @Override
    public Void visit(Statements statements) {
        PTreeNode curLastNode = lastNode;
        for (Statement stmnt : statements) {
            stmnt.accept(this);
            graph.putEdgeValue(curLastNode, lastNode, EdgeTypes.STEP_OVER);
            graph.putEdgeValue(lastNode, curLastNode, EdgeTypes.STEP_BACK);
            curLastNode = lastNode;
        }
        lastNode = curLastNode;
        return null;
    }

    @Override
    public Void visit(CallStatement call) {
        PTreeNode currentNode = new PTreeNode(call);
        //fixme handle stepinto

        graph.addNode(currentNode);
        graph.putEdgeValue(lastNode, currentNode, EdgeTypes.STEP_OVER);
        graph.putEdgeValue(currentNode, lastNode, EdgeTypes.STEP_BACK);
        lastNode = currentNode;
        return null;
    }

    @Override
    public Void visit(ForeachStatement foreach) {
        return super.visit(foreach);
    }

    @Override
    public Void visit(TheOnlyStatement theOnly) {
        return super.visit(theOnly);
    }

    @Override
    public Void visit(RepeatStatement repeatStatement) {
        return super.visit(repeatStatement);
    }

    @Override
    public Void visit(CasesStatement casesStatement) {
        return super.visit(casesStatement);
    }


    public String asdot() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph G {\nnode [shape=rect]\n ");

        graph.nodes().forEach(n -> {
            sb.append(n.hashCode())
                    .append(" [label=\"")
                    .append(n.getScriptstmt().getNodeName())
                    .append("@")
                    .append(n.getScriptstmt().getStartPosition().getLineNumber())
                    .append("\"]\n");
        });

        graph.edges().forEach(edge -> {
            sb.append(edge.source().hashCode())
                    .append(" -> ")
                    .append(edge.target().hashCode())
                    .append(" [label=\"")
                    .append(graph.edgeValue(edge.source(), edge.target()).name())
                    .append("\"]\n");
        });

        sb.append("}");
        return sb.toString();
    }


}
