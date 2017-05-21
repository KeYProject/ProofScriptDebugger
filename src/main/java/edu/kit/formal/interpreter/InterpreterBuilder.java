package edu.kit.formal.interpreter;

import de.uka.ilkd.key.macros.ProofMacro;
import de.uka.ilkd.key.macros.scripts.ProofScriptCommand;
import de.uka.ilkd.key.proof.Proof;
import edu.kit.formal.interpreter.funchdl.*;
import edu.kit.formal.proofscriptparser.Facade;
import edu.kit.formal.proofscriptparser.Visitor;
import edu.kit.formal.proofscriptparser.ast.ProofScript;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (21.05.17)
 */
public class InterpreterBuilder {
    private ProofScriptHandler psh = new ProofScriptHandler();
    private MacroCommandHandler pmh = new MacroCommandHandler();
    private RuleCommandHandler pmr = new RuleCommandHandler();
    private ProofScriptCommandBuilder pmc = new ProofScriptCommandBuilder();


    public Interpreter build(File file) throws IOException {
        return build(Facade.getAST(file));
    }

    private Interpreter build(List<ProofScript> ast) {
        psh.addScripts(ast);
        DefaultLookup lookup = new DefaultLookup(psh, pmh, pmr, pmc);
        return new Interpreter(lookup);
    }

    public InterpreterBuilder scriptSearchPath(File... paths) {

    }

    public InterpreterBuilder keyRules(Proof proof) {

    }

    public InterpreterBuilder defaultScriptCommands(List<ProofScriptCommand> commands) {

    }

    public InterpreterBuilder defaultKeyMacros(List<ProofMacro> macros) {

    }

    public InterpreterBuilder register(ProofScript... script) {
        psh.addScripts(Arrays.asList(script));
        return this;
    }

    public InterpreterBuilder onEntry(Visitor v) {
        return this;
    }


    public InterpreterBuilder onExit(Visitor v) {
        return this;
    }


    public InterpreterBuilder captureHistory() {
        return this;
    }


    public InterpreterBuilder log(String prefix) {
        return this;
    }
}
