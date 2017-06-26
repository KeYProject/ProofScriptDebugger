package edu.kit.formal.interpreter;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import edu.kit.formal.interpreter.funchdl.CommandLookup;
import edu.kit.formal.interpreter.funchdl.ProofScriptHandler;
import edu.kit.formal.proofscriptparser.DefaultASTVisitor;
import edu.kit.formal.proofscriptparser.ast.*;

import java.util.ArrayList;
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
    private ControlFlowNode lastNode;

    /**
     * Control Flow Graph
     */
    private MutableValueGraph<ControlFlowNode, EdgeTypes> graph = ValueGraphBuilder.directed().allowsSelfLoops(true).build();

    private List<ASTNode> context = new ArrayList<>();

    public ProgramFlowVisitor(CommandLookup functionLookup) {
        this.functionLookup = functionLookup;
    }

    public MutableValueGraph<ControlFlowNode, EdgeTypes> getGraph() {
        return graph;
    }

    @Override
    public Void visit(ProofScript proofScript) {

        ControlFlowNode scriptNode = new ControlFlowNode(proofScript);
        graph.addNode(scriptNode);
        System.out.println("\n" + scriptNode + "\n");
        lastNode = scriptNode;

        return this.visit(proofScript.getBody());
    }

    @Override
    public Void visit(AssignmentStatement assignment) {

        ControlFlowNode assignmentNode = new ControlFlowNode(assignment);
        graph.addNode(assignmentNode);
        System.out.println("\n" + assignmentNode + "\n");
        lastNode = assignmentNode;
        return null;
    }

    @Override
    public Void visit(Statements statements) {
        ControlFlowNode curLastNode = lastNode;
        for (Statement stmnt : statements) {
            stmnt.accept(this);
            graph.putEdgeValue(curLastNode, lastNode, EdgeTypes.STEP_OVER);
            graph.putEdgeValue(lastNode, curLastNode, EdgeTypes.STEP_BACK);
            curLastNode = lastNode;
        }
        lastNode = curLastNode;
        return null;
    }

    //stack wenn atomic false
    @Override
    public Void visit(CallStatement call) {
        ControlFlowNode currentNode = new ControlFlowNode(call);
        if (!context.isEmpty()) {
            currentNode.setCallCtx(context);
        }
        graph.addNode(currentNode);
        System.out.println("\n" + currentNode + "\n");
        graph.putEdgeValue(lastNode, currentNode, EdgeTypes.STEP_OVER);
        graph.putEdgeValue(currentNode, lastNode, EdgeTypes.STEP_BACK);
        lastNode = currentNode;
        boolean atomic = functionLookup.isAtomic(call);
        //Annahme: wenn ich zwischendrin keine return kante habe, dann wird solange durchgegangen, bis eine return kante da ist
        if (atomic) {

            context.add(call);
//            graph.putEdgeValue(lastNode, currentNode, EdgeTypes.STEP_RETURN);
            ProofScriptHandler psh = (ProofScriptHandler) functionLookup.getBuilder(call);
            psh.getScript(call.getCommand()).getBody().accept(this);

            //verbinde letzten knoten aus auruf mit step return zu aktuellem knoten
            graph.putEdgeValue(lastNode, currentNode, EdgeTypes.STEP_RETURN);
            graph.putEdgeValue(currentNode, lastNode, EdgeTypes.STEP_INTO);
        }
        context.remove(call);
        lastNode = currentNode;

        return null;
    }

    @Override
    public Void visit(ForeachStatement foreach) {
        ControlFlowNode currentNode = new ControlFlowNode(foreach);
        graph.addNode(currentNode);
        System.out.println("\n" + currentNode + "\n");
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
        ControlFlowNode currentNode = new ControlFlowNode(theOnly);
        graph.addNode(currentNode);
        System.out.println("\n" + currentNode + "\n");
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
        ControlFlowNode currentNode = new ControlFlowNode(repeatStatement);
        graph.addNode(currentNode);
        System.out.println("\n" + currentNode + "\n");
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
        ControlFlowNode currentNode = new ControlFlowNode(casesStatement);
        graph.addNode(currentNode);
        System.out.println("\n" + currentNode + "\n");
        graph.putEdgeValue(lastNode, currentNode, EdgeTypes.STEP_OVER);
        graph.putEdgeValue(currentNode, lastNode, EdgeTypes.STEP_BACK);
        List<CaseStatement> cases = casesStatement.getCases();
        for (CaseStatement aCase : cases) {
            ControlFlowNode caseNode = new ControlFlowNode(aCase);
            graph.addNode(caseNode);
            System.out.println("\n" + caseNode + "\n");
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
