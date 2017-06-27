package edu.kit.formal.interpreter;

import edu.kit.formal.gui.controller.PuppetMaster;
import edu.kit.formal.interpreter.data.KeyData;
import edu.kit.formal.proofscriptparser.Visitor;
import edu.kit.formal.proofscriptparser.ast.ProofScript;

/**
 * Class controlling and maintaining proof tree structure for debugger and handling step functions for the debugger
 *
 * @author S. Grebing
 */
public class ProofTreeController {

    /**
     * To control stepping
     */
    private final PuppetMaster blocker = new PuppetMaster();
    /**
     * Visitor to retrieve state graph
     */
    private StateGraphVisitor stateGraphVisitor;
    /**
     * Graph that is computed on the fly in order to allow stepping
     */

    private ProgramFlowVisitor controlFlowGraphVisitor;
    /**
     * Current interpreter
     */
    private Interpreter<KeyData> currentInterpreter;
    /**
     * Current State in graph
     */
    private PTreeNode statePointer;

    public ProofTreeController(Interpreter<KeyData> inter, ProofScript mainScript) {
        blocker.deinstall();

        this.currentInterpreter = inter;
        buildControlFlowGraph(mainScript);
        this.stateGraphVisitor = new StateGraphVisitor(inter, mainScript, controlFlowGraphVisitor);
        statePointer = stateGraphVisitor.getRootNode();

        blocker.getStepUntilBlock().set(1);
        blocker.install(currentInterpreter);
    }

    /**
     * Build the control flow graph for looking up step-edges for the given script inligning called script commands
     *
     * @param mainScript
     */
    private void buildControlFlowGraph(ProofScript mainScript) {
        this.controlFlowGraphVisitor = new ProgramFlowVisitor(currentInterpreter.getFunctionLookup());
        mainScript.accept(controlFlowGraphVisitor);

        System.out.println("CFG\n" + controlFlowGraphVisitor.asdot());

    }

    public PTreeNode stepOver() {
        blocker.getStepUntilBlock().addAndGet(1);
        blocker.unlock();



        if (statePointer == null) {
            this.statePointer = stateGraphVisitor.getLastNode();
        }
        //PTreeNode current = statePointer;
        //ASTNode stmt = current.getScriptstmt();
        //System.out.println("CurrentPointer: " + stmt.getNodeName() + "@" + stmt.getStartPosition());

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


    public Visitor getStateVisitor() {
        return this.stateGraphVisitor;
    }
}
