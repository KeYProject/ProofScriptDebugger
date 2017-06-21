package edu.kit.formal.interpreter;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import edu.kit.formal.proofscriptparser.DefaultASTVisitor;
import edu.kit.formal.proofscriptparser.ast.AssignmentStatement;
import edu.kit.formal.proofscriptparser.ast.ProofScript;
import edu.kit.formal.proofscriptparser.ast.Statement;
import edu.kit.formal.proofscriptparser.ast.Statements;

/**
 * Visitor to create ProgramFlowGraph
 */
public class ProgramFlowVisitor extends DefaultASTVisitor<Void> {

    private PTreeNode lastNode;
    private MutableValueGraph<PTreeNode, EdgeTypes> graph = ValueGraphBuilder.directed().build();

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
}
