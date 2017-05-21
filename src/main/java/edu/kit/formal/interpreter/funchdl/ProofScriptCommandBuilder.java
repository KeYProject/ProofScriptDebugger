package edu.kit.formal.interpreter.funchdl;

import de.uka.ilkd.key.api.ProofScriptCommandCall;
import de.uka.ilkd.key.macros.scripts.EngineState;
import de.uka.ilkd.key.macros.scripts.ProofScriptCommand;
import edu.kit.formal.interpreter.Interpreter;
import edu.kit.formal.interpreter.VariableAssignment;
import edu.kit.formal.proofscriptparser.ast.CallStatement;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexander Weigl
 * @version 1 (21.05.17)
 */
@RequiredArgsConstructor
public class ProofScriptCommandBuilder implements CommandHandler {
    private final Map<String, ProofScriptCommand> scripts;

    public ProofScriptCommandBuilder() {
        this(new HashMap<>());
    }

    @Override
    public boolean handles(CallStatement call) {
        return scripts.containsKey(call.getCommand());
    }

    @Override
    public void evaluate(Interpreter interpreter,
                         CallStatement call,
                         VariableAssignment params) {
        ProofScriptCommand c = scripts.get(call.getCommand());
        EngineState state = null;
        Map<String, String> map;
        //ProofScriptCommandCall cc = c.evaluateArguments(state, map);
    }
}
