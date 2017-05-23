package edu.kit.formal.interpreter;

import de.uka.ilkd.key.api.KeYApi;
import de.uka.ilkd.key.api.ProofApi;
import de.uka.ilkd.key.api.ProofManagementApi;
import de.uka.ilkd.key.control.KeYEnvironment;
import de.uka.ilkd.key.macros.ProofMacro;
import de.uka.ilkd.key.macros.scripts.ProofScriptCommand;
import de.uka.ilkd.key.proof.Proof;
import edu.kit.formal.ScopeLogger;
import edu.kit.formal.interpreter.funchdl.*;
import edu.kit.formal.proofscriptparser.Facade;
import edu.kit.formal.proofscriptparser.Visitor;
import edu.kit.formal.proofscriptparser.ast.ProofScript;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (21.05.17)
 */

public class InterpreterBuilder {
    @Getter
    private final ProofScriptHandler psh = new ProofScriptHandler();
    @Getter
    private final MacroCommandHandler pmh = new MacroCommandHandler();
    @Getter
    private final RuleCommandHandler pmr = new RuleCommandHandler();
    @Getter
    private ProofScriptCommandBuilder pmc = new ProofScriptCommandBuilder();
    @Getter
    private ProofScript entryPoint;
    @Getter
    private Proof proof;
    @Getter
    private KeYEnvironment keyEnvironment;
    @Getter
    private HistoryListener historyLogger;
    @Getter
    private ScopeLogger logger;
    @Getter
    private DefaultLookup lookup = new DefaultLookup(psh, pmh, pmr, pmc);

    private Interpreter interpreter = new Interpreter(lookup);

    public InterpreterBuilder addProofScripts(File file) throws IOException {
        return addProofScripts(Facade.getAST(file));
    }

    public InterpreterBuilder addProofScripts(List<ProofScript> ast) {
        psh.addScripts(ast);
        return this;
    }

    public InterpreterBuilder startWith(ProofScript entryPoint) {
        this.entryPoint = entryPoint;
        return this;
    }

    public Interpreter build() {
        return interpreter;
    }

    public InterpreterBuilder scriptSearchPath(File... paths) {
        psh.getSearchPath().addAll(Arrays.asList(paths));
        return this;
    }

    //TODO
    public InterpreterBuilder proof(KeYEnvironment env, Proof proof) {
        this.proof = proof;
        this.keyEnvironment = env;
        return this;
    }

    public InterpreterBuilder scriptCommands() {
        return scriptCommands(KeYApi.getScriptCommandApi().getScriptCommands());
    }

    public InterpreterBuilder scriptCommands(Collection<ProofScriptCommand> commands) {
        commands.forEach(m -> pmc.getCommands().put(m.getName(), m));
        return this;
    }

    public InterpreterBuilder macros() {
        return macros(KeYApi.getMacroApi().getMacros());
    }

    public InterpreterBuilder macros(Collection<ProofMacro> macros) {
        macros.forEach(m -> pmh.getMacros().put(m.getName(), m));
        return this;
    }

    public InterpreterBuilder register(ProofScript... script) {
        psh.addScripts(Arrays.asList(script));
        return this;
    }

    public InterpreterBuilder onEntry(Visitor v) {
        interpreter.getEntryListeners().add(v);
        return this;
    }


    public InterpreterBuilder onExit(Visitor v) {
        interpreter.getExitListeners().add(v);
        return this;
    }


    public InterpreterBuilder captureHistory() {
        if (historyLogger != null)
            historyLogger = new HistoryListener(interpreter);
        return onEntry(historyLogger);
    }


    public InterpreterBuilder log(String prefix) {
        if (logger == null)
            logger = new ScopeLogger("interpreter");
        return onEntry(logger);
    }
}