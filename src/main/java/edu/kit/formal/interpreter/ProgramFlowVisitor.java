package edu.kit.formal.interpreter;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import edu.kit.formal.interpreter.funchdl.CommandLookup;
import edu.kit.formal.interpreter.funchdl.ProofScriptHandler;
import edu.kit.formal.proofscriptparser.DefaultASTVisitor;
import edu.kit.formal.proofscriptparser.ast.*;

import java.util.List;

/**
 * Visitor to create ProgramFlowGraph
 */
public class ProgramFlowVisitor extends DefaultASTVisitor<Void> {

    /**
     * Lookup for names of scripts
     */
    private final CommandLookup functionLookup;

    /**
     * Last visited Node
     */
    private ASTNode lastNode;

    /**
     * Control Flow Graph
     */
    private MutableValueGraph<ASTNode, EdgeTypes> graph = ValueGraphBuilder.directed().allowsSelfLoops(true).build();


    public ProgramFlowVisitor(CommandLookup functionLookup) {
        this.functionLookup = functionLookup;
    }

    public MutableValueGraph<ASTNode, EdgeTypes> getGraph() {
        return graph;
    }

    @Override
    public Void visit(ProofScript proofScript) {

        lastNode = proofScript;
        return this.visit(proofScript.getBody());
    }

    @Override
    public Void visit(AssignmentStatement assignment) {

        graph.addNode(assignment);
        lastNode = assignment;
        return null;
    }

    @Override
    public Void visit(Statements statements) {
        ASTNode curLastNode = lastNode;
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
        ASTNode currentNode = call;

        graph.addNode(currentNode);
        graph.putEdgeValue(lastNode, currentNode, EdgeTypes.STEP_OVER);
        graph.putEdgeValue(currentNode, lastNode, EdgeTypes.STEP_BACK);


        lastNode = currentNode;


        boolean atomic = functionLookup.isAtomic(call);


        //Annahme: wenn ich zwischendrin keine return kante habe, dann wird solange durchgegangen, bis eine return kante da ist
        if (atomic) {

//            graph.putEdgeValue(lastNode, currentNode, EdgeTypes.STEP_RETURN);

            ProofScriptHandler psh = (ProofScriptHandler) functionLookup.getBuilder(call);
            psh.getScript(call.getCommand()).getBody().accept(this);

            //verbinde letzten knoten aus auruf mit step return zu aktuellem knoten
            graph.putEdgeValue(lastNode, currentNode, EdgeTypes.STEP_RETURN);
            graph.putEdgeValue(currentNode, lastNode, EdgeTypes.STEP_INTO);
        }

        lastNode = currentNode;

        return null;
    }

    @Override
    public Void visit(ForeachStatement foreach) {
        ASTNode currentNode = foreach;
        graph.addNode(currentNode);
        graph.putEdgeValue(lastNode, currentNode, EdgeTypes.STEP_OVER);
        graph.putEdgeValue(currentNode, lastNode, EdgeTypes.STEP_BACK);
        lastNode = currentNode;
        foreach.getBody().accept(this);
        graph.putEdgeValue(lastNode, currentNode, EdgeTypes.STEP_RETURN);
        //lastNode = currentNode; ??
        return null;
    }

    @Override
    public Void visit(TheOnlyStatement theOnly) {
        ASTNode currentNode = theOnly;
        graph.addNode(currentNode);
        graph.putEdgeValue(lastNode, currentNode, EdgeTypes.STEP_OVER);
        graph.putEdgeValue(currentNode, lastNode, EdgeTypes.STEP_BACK);
        lastNode = currentNode;
        theOnly.getBody().accept(this);
        graph.putEdgeValue(lastNode, currentNode, EdgeTypes.STEP_RETURN);
        //lastNode = currentNode; ??
        return null;

    }

    @Override
    public Void visit(RepeatStatement repeatStatement) {
        ASTNode currentNode = repeatStatement;
        graph.addNode(currentNode);
        graph.putEdgeValue(lastNode, currentNode, EdgeTypes.STEP_OVER);
        graph.putEdgeValue(currentNode, lastNode, EdgeTypes.STEP_BACK);
        lastNode = currentNode;
        repeatStatement.getBody().accept(this);
        graph.putEdgeValue(lastNode, currentNode, EdgeTypes.STEP_OVER);
        lastNode = currentNode;
        return null;
    }

    @Override
    public Void visit(CasesStatement casesStatement) {
        ASTNode currentNode = casesStatement;
        graph.addNode(currentNode);
        graph.putEdgeValue(lastNode, currentNode, EdgeTypes.STEP_OVER);
        graph.putEdgeValue(currentNode, lastNode, EdgeTypes.STEP_BACK);
        List<CaseStatement> cases = casesStatement.getCases();
        for (CaseStatement aCase : cases) {
            ASTNode caseNode = aCase;
            graph.addNode(caseNode);
            graph.putEdgeValue(currentNode, caseNode, EdgeTypes.STEP_OVER); //??is this right?
            graph.putEdgeValue(caseNode, currentNode, EdgeTypes.STEP_BACK);
            lastNode = caseNode;
            aCase.getBody().accept(this);
            graph.putEdgeValue(lastNode, currentNode, EdgeTypes.STEP_RETURN);
        }
        lastNode = currentNode;
        return null;
    }


    public String asdot() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph G {\nnode [shape=rect]\n ");

        graph.nodes().forEach(n -> {
            sb.append(n.hashCode())
                    .append(" [label=\"")
                    .append(n.getNodeName())
                    .append("@")
                    .append(n.getStartPosition().getLineNumber())
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
