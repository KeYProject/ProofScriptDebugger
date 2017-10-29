package edu.kit.iti.formal.psdbg.interpreter.graphs;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import edu.kit.iti.formal.psdbg.interpreter.funchdl.CommandLookup;
import edu.kit.iti.formal.psdbg.interpreter.funchdl.ProofScriptHandler;
import edu.kit.iti.formal.psdbg.parser.DefaultASTVisitor;
import edu.kit.iti.formal.psdbg.parser.ast.*;
import javafx.util.Pair;

import java.util.*;

/**
 * Visitor to create ProgramFlowGraph
 */
public class ControlFlowVisitor extends DefaultASTVisitor<Void> {

    /**
     * Lookup for names of scripts
     */
    private final CommandLookup functionLookup;
    /**
     * Mapping of ASTNodes to CFG nodes
     */
    private Map<ASTNode, ControlFlowNode> mappingOfNodes;
    /**
     * Last visited Node
     */
    private ControlFlowNode lastNode;

    /**
     * Control Flow Graph
     */
    private MutableValueGraph<ControlFlowNode, EdgeTypes> graph = ValueGraphBuilder.directed().allowsSelfLoops(true).build();

    /**
     * Call context
     */
    private LinkedList<ASTNode> context = new LinkedList<>();


    public ControlFlowVisitor(CommandLookup functionLookup) {
        this.functionLookup = functionLookup;
        mappingOfNodes = new HashMap<>();
    }

    /**
     * Return all nodes that have the parameter node as target in the graph
     *
     * @param node
     * @return
     */

    public Set<EndpointPair<ControlFlowNode>> getAllEdgesForNodeAsTarget(ASTNode node) {

        ControlFlowNode assocNode = mappingOfNodes.get(node);
        if (assocNode != null) {
            Set<EndpointPair<ControlFlowNode>> edges = new HashSet<>();

            graph.edges().forEach(e -> {
                if (e.target().equals(assocNode)) {

                    edges.add(e);
                    //System.out.println(e.target());
                    //System.out.println(e.source());
                    //System.out.println(graph.edgeValue(e.target(), e.source()));
                }
            });
            return edges;
        } else {
            return Collections.EMPTY_SET;
        }
    }

    public Collection<Pair<ControlFlowNode, EdgeTypes>> getPredecessorsAndTheirEdges(ASTNode node) {
        ControlFlowNode assocNode = getCorrespondingNode(node);
        List<Pair<ControlFlowNode, EdgeTypes>> allPreds = new LinkedList<>();
        if (assocNode != null) {
            graph.edges().forEach(edge -> {
                if (edge.target().equals(assocNode)) {
                    allPreds.add(new Pair(edge.source(), graph.edgeValue(edge.source(), edge.target())));

                }

            });
            return allPreds;
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    public ControlFlowNode getCorrespondingNode(ASTNode node) {
        ControlFlowNode ctrl = null;
        for (ASTNode astNode : mappingOfNodes.keySet()) {
            if (astNode.getStartPosition().equals(node.getStartPosition())
                    && astNode.getEndPosition().equals(node.getEndPosition())) {
                ctrl = mappingOfNodes.get(astNode);
                break;
            }
        }

        return ctrl;
    }

    public Collection<Pair<ControlFlowNode, EdgeTypes>> getPredecessorsAsTarget(ASTNode node) {
        ControlFlowNode assocNode = getCorrespondingNode(node);
        List<Pair<ControlFlowNode, EdgeTypes>> allPreds = new LinkedList<>();

        if (assocNode != null) {

            graph.edges().forEach(edge -> {
                if (edge.source().equals(assocNode)) {
                    allPreds.add(new Pair(edge.target(), graph.edgeValue(edge.source(), edge.target())));

                }

            });
            return allPreds;
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public Void visit(ProofScript proofScript) {

        ControlFlowNode scriptNode = new ControlFlowNode(proofScript);
        context.add(proofScript);
        mappingOfNodes.put(proofScript, scriptNode);
        graph.addNode(scriptNode);
        lastNode = scriptNode;

        return this.visit(proofScript.getBody());
    }

    @Override
    public Void visit(AssignmentStatement assignment) {

        ControlFlowNode assignmentNode = new ControlFlowNode(assignment);
        mappingOfNodes.put(assignment, assignmentNode);
        graph.addNode(assignmentNode);
        lastNode = assignmentNode;
        return null;
    }

    @Override
    public Void visit(Statements statements) {
        //ControlFlowNode curLastNode = lastNode;
        for (Statement stmnt : statements) {
            stmnt.accept(this);
            //graph.putEdgeValue(curLastNode, lastNode, EdgeTypes.STEP_OVER);
            //graph.putEdgeValue(lastNode, curLastNode, EdgeTypes.STEP_BACK);
            //curLastNode = lastNode;
        }
        //lastNode = curLastNode;
        return null;
    }


    @Override
    public Void visit(CallStatement call) {
        ControlFlowNode currentNode = new ControlFlowNode(call);
        if (!context.isEmpty()) {
            currentNode.setCallCtx(context);
        }
        mappingOfNodes.put(call, currentNode);
        graph.addNode(currentNode);

        graph.putEdgeValue(lastNode, currentNode, EdgeTypes.STEP_OVER);
        graph.putEdgeValue(currentNode, lastNode, EdgeTypes.STEP_BACK);

        lastNode = currentNode;
        boolean atomic = functionLookup.isAtomic(call);
        //Annahme: wenn ich zwischendrin keine return kante habe, dann wird solange durchgegangen, bis eine return kante da ist
        if (atomic) {
            LinkedList<ASTNode> copiedContext = new LinkedList<>();

            //to check whether context is restored properly, copy context before call
            context.stream().forEach(e -> {
                copiedContext.add(e);
            });
            context.add(call);

            ProofScriptHandler psh = (ProofScriptHandler) functionLookup.getBuilder(call);
            psh.getScript(call.getCommand()).getBody().accept(this);

            //verbinde letzten knoten aus auruf mit step return zu aktuellem knoten
            graph.putEdgeValue(lastNode, currentNode, EdgeTypes.STEP_RETURN);
            graph.putEdgeValue(currentNode, lastNode, EdgeTypes.STEP_INTO);

            context.removeLast();
            //check if context is restored properly
            //copiedcontext == context

        }

        lastNode = currentNode;

        return null;
    }

    @Override
    public Void visit(ForeachStatement foreach) {
        ControlFlowNode currentNode = new ControlFlowNode(foreach);
        mappingOfNodes.put(foreach, currentNode);
        graph.addNode(currentNode);
        //System.out.println("\n" + currentNode + "\n");
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
        mappingOfNodes.put(theOnly, currentNode);
        graph.addNode(currentNode);
        //System.out.println("\n" + currentNode + "\n");
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
        mappingOfNodes.put(repeatStatement, currentNode);
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
        ControlFlowNode casesNode = new ControlFlowNode(casesStatement);
        mappingOfNodes.put(casesStatement, casesNode);
        graph.addNode(casesNode);
        //System.out.println("\n" + casesNode + "\n");
        graph.putEdgeValue(lastNode, casesNode, EdgeTypes.STEP_OVER);
        graph.putEdgeValue(casesNode, lastNode, EdgeTypes.STEP_BACK);
        List<CaseStatement> cases = casesStatement.getCases();
        for (CaseStatement aCase : cases) {

//            if (aCase.isClosedStmt) {

//            } else {
            ControlFlowNode caseNode = new ControlFlowNode(aCase);
            mappingOfNodes.put(aCase, caseNode);
            graph.addNode(caseNode);
            //TODO rethink
            graph.putEdgeValue(casesNode, caseNode, EdgeTypes.STEP_INTO); //??is this right?
            graph.putEdgeValue(caseNode, casesNode, EdgeTypes.STEP_BACK);
            lastNode = caseNode;
            aCase.getBody().accept(this);
            graph.putEdgeValue(lastNode, casesNode, EdgeTypes.STEP_RETURN);
            //           }
        }
        //casesStatement.getDefaultCase()
        if (casesStatement.getDefCaseStmt() != null) {
            DefaultCaseStatement defCase = casesStatement.getDefCaseStmt();
            ControlFlowNode caseNode = new ControlFlowNode(defCase);
            mappingOfNodes.put(defCase, caseNode);
            graph.addNode(caseNode);
//TODO rethink
            graph.putEdgeValue(casesNode, caseNode, EdgeTypes.STEP_INTO); //??is this right?
            graph.putEdgeValue(caseNode, casesNode, EdgeTypes.STEP_BACK);
            lastNode = caseNode;
            defCase.getBody().accept(this);
            graph.putEdgeValue(lastNode, casesNode, EdgeTypes.STEP_RETURN);
            /*Statements defaultCase = defCase.getBody();

            ControlFlowNode caseNode = new ControlFlowNode(defaultCase);
            mappingOfNodes.put(defaultCase, caseNode);
            graph.addNode(caseNode);
            //System.out.println("\n" + caseNode + "\n");
            graph.putEdgeValue(casesNode, caseNode, EdgeTypes.STEP_OVER); //??is this right?
            graph.putEdgeValue(caseNode, casesNode, EdgeTypes.STEP_BACK);
            lastNode = caseNode;
            defaultCase.accept(this);
            graph.putEdgeValue(lastNode, casesNode, EdgeTypes.STEP_RETURN);*/
        }
        lastNode = casesNode;
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

    public MutableValueGraph<ControlFlowNode, EdgeTypes> getGraph() {
        return graph;
    }



}
