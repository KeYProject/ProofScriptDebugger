package edu.kit.formal.interpreter;

import com.google.common.graph.MutableValueGraph;
import edu.kit.formal.interpreter.data.KeyData;
import edu.kit.formal.proofscriptparser.ast.ProofScript;

/**
 * Class controlling and maintaining proof tree structure for debugger and handling step functions for the debugger
 *
 * @author S. Grebing
 */
public class ProofTreeController {

    /**
     * ControlFlowGraph to lookup edges
     */
    private MutableValueGraph<ControlFlowNode, EdgeTypes> controlFlowGraph;

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

    public ProofTreeController(Interpreter<KeyData> inter, ProofScript mainScript) {
        this.currentInterpreter = inter;
        buildControlFlowGraph(mainScript);


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
        System.out.println(visitor.asdot());
        // System.out.println(controlFlowGraph);

    }


    public PTreeNode stepOver() {
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
