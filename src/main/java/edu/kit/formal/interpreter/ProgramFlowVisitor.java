package edu.kit.formal.interpreter;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import edu.kit.formal.interpreter.data.KeyData;
import edu.kit.formal.interpreter.funchdl.CommandLookup;
import edu.kit.formal.interpreter.funchdl.ProofScriptHandler;
import edu.kit.formal.proofscriptparser.DefaultASTVisitor;
import edu.kit.formal.proofscriptparser.ast.*;

/**
 * Visitor to create ProgramFlowGraph
 */
public class ProgramFlowVisitor extends DefaultASTVisitor<Void> {

    private final CommandLookup functionLookup;
    private ControlFlowNode lastNode;

    private MutableValueGraph<ControlFlowNode, EdgeTypes> graph = ValueGraphBuilder.directed().allowsSelfLoops(true).build();

    private Interpreter<KeyData> inter;

    public ProgramFlowVisitor(CommandLookup functionLookup) {
        this.functionLookup = functionLookup;
    }

    public MutableValueGraph<ControlFlowNode, EdgeTypes> getGraph() {
        return graph;
    }

    @Override
    public Void visit(ProofScript proofScript) {
        ControlFlowNode scriptNode = new ControlFlowNode(proofScript);
        lastNode = scriptNode;
        return this.visit(proofScript.getBody());
    }

    @Override
    public Void visit(AssignmentStatement assignment) {
        ControlFlowNode node = new ControlFlowNode(assignment);
        graph.addNode(node);
        lastNode = node;
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

    @Override
    public Void visit(CallStatement call) {
        ControlFlowNode currentNode = new ControlFlowNode(call);

        graph.addNode(currentNode);
        graph.putEdgeValue(lastNode, currentNode, EdgeTypes.STEP_OVER);
        graph.putEdgeValue(currentNode, lastNode, EdgeTypes.STEP_BACK);


        lastNode = currentNode;


        boolean atomic = functionLookup.isAtomic(call);


        //Annahme: wenn ich zwischendrin keine return kante habe, dann wird solange durchgegangen, bis eine return kante da ist
        if (atomic) {
            graph.putEdgeValue(currentNode, lastNode, EdgeTypes.STEP_INTO);
//            graph.putEdgeValue(lastNode, currentNode, EdgeTypes.STEP_RETURN);

            ProofScriptHandler psh = (ProofScriptHandler) functionLookup.getBuilder(call);
            psh.getScript(call.getCommand()).getBody().accept(this);

            //verbinde letzten knoten aus auruf mi step return zu aktuellem knoten
            graph.putEdgeValue(lastNode, currentNode, EdgeTypes.STEP_RETURN);
        }

        lastNode = currentNode;

        return null;
    }

    @Override
    public Void visit(ForeachStatement foreach) {
        ControlFlowNode currentNode = new ControlFlowNode(foreach);
        graph.addNode(currentNode);
        graph.putEdgeValue(lastNode, currentNode, EdgeTypes.STEP_OVER);
        graph.putEdgeValue(currentNode, lastNode, EdgeTypes.STEP_BACK);
        lastNode = currentNode;
        foreach.getBody().accept(this);
        graph.putEdgeValue(currentNode, lastNode, EdgeTypes.STEP_RETURN);
        lastNode = currentNode;
        return null;
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
