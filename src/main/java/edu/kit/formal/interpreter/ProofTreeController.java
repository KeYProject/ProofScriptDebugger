package edu.kit.formal.interpreter;

import com.google.common.graph.MutableValueGraph;
import edu.kit.formal.interpreter.data.KeyData;
import edu.kit.formal.proofscriptparser.ast.ASTNode;
import edu.kit.formal.proofscriptparser.ast.ProofScript;

/**
 * Class controlling and maintaining proof tree structure for debugger and handling step functions for the debugger
 *
 * @author S. Grebing
 */
public class ProofTreeController {
    //TODO Listener auf den Interperter
    StateGraphVisitor stateGraphVisitor;
    /**
     * ControlFlowGraph to lookup edges
     */
    private MutableValueGraph<ASTNode, EdgeTypes> controlFlowGraph;
    /**
     * Graph that is computed on the fly in order to allow stepping
     */
    private MutableValueGraph<PTreeNode, EdgeTypes> stateGraph;
    /**
     * Current interpreter
     */
    private Interpreter<KeyData> currentInterpreter;
    /**
     * Current State in graph
     */
    private PTreeNode statePointer;

    public ProofTreeController(Interpreter<KeyData> inter, ProofScript mainScript, StateGraphVisitor stateGraphVisitor) {
        this.currentInterpreter = inter;

        buildControlFlowGraph(mainScript);
        stateGraph = stateGraphVisitor.getStateGraph();
        statePointer = stateGraphVisitor.getRootNode();
        this.stateGraphVisitor = stateGraphVisitor;
        // System.out.println(stateGraph.nodes());
        //initializeStateGraph(mainScript);


    }

    /**
     * Build the control flow graph for looking up step-edges for the given script inligning called script commands
     *
     * @param mainScript
     */
    private void buildControlFlowGraph(ProofScript mainScript) {
        ProgramFlowVisitor visitor = new ProgramFlowVisitor(currentInterpreter.getFunctionLookup());

        mainScript.accept(visitor);
        this.controlFlowGraph = visitor.getGraph();
        // System.out.println(visitor.asdot());

    }

 /*   private void initializeStateGraph(ProofScript mainScript){
       stateGraph = ValueGraphBuilder.directed().build();

        State<KeyData> initState = currentInterpreter.getCurrentState();
        System.out.println(initState);
        PTreeNode initNode = new PTreeNode(mainScript);
        initNode.setState(initState);
        stateGraph.addNode(initNode);

       // statePointer = initNode;

    }*/


    public PTreeNode stepOver() {
        PTreeNode current = statePointer;
        ASTNode stmt = current.getScriptstmt();
        if (controlFlowGraph.asGraph().nodes().contains(stmt)) {
            // System.out.println("\n\nAdjacent:{\n"+controlFlowGraph.asGraph().adjacentNodes(stmt)+"}\n\n\n");
            Object[] nodeArray = controlFlowGraph.asGraph().adjacentNodes(stmt).toArray();
            ASTNode firtSucc = (ASTNode) nodeArray[0];
            for (PTreeNode succ : stateGraph.successors(current)) {
                if (succ.getScriptstmt().equals(firtSucc)) {
                    statePointer = succ;
                }
            }
        }
        //System.out.println(stateGraphVisitor.asdot());



        return null;
    }

    public PTreeNode stepBack() {
        return null;
    }

    public PTreeNode stepInto() {
        return null;
    }

    public PTreeNode stepReturn() {
        return null;
    }


}
