package edu.kit.formal.interpreter;

import com.google.common.graph.MutableValueGraph;
import edu.kit.formal.proofscriptparser.ast.ProofScript;

/**
 * Class controlling proof tree structure for debugger
 *
 * @author S. Grebing
 */
public class ProofTreeController {

    private MutableValueGraph<PTreeNode, EdgeTypes> graph;

    private Interpreter currentInterpreter;


    private PTreeNode statePointer;

    public ProofTreeController(Interpreter inter, ProofScript mainScript) {
        this.currentInterpreter = inter;
        buildEmptyGraph(mainScript);


    }

    private void buildEmptyGraph(ProofScript mainScript) {
        ProgramFlowVisitor visitor = new ProgramFlowVisitor();

        mainScript.accept(visitor);
        System.out.println(visitor.getGraph());

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
