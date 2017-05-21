package edu.kit.formal.interpreter.funchdl;

import de.uka.ilkd.key.macros.ProofMacro;
import de.uka.ilkd.key.macros.scripts.MacroCommand;
import edu.kit.formal.interpreter.Interpreter;
import edu.kit.formal.interpreter.Value;
import edu.kit.formal.interpreter.VariableAssignment;
import edu.kit.formal.proofscriptparser.ast.CallStatement;
import edu.kit.formal.proofscriptparser.ast.Parameters;
import edu.kit.formal.proofscriptparser.ast.StringLiteral;
import edu.kit.formal.proofscriptparser.ast.Variable;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexander Weigl
 * @version 1 (21.05.17)
 */
@RequiredArgsConstructor
public class MacroCommandHandler implements CommandHandler {
    private final Map<String, ProofMacro> macros;

    public MacroCommandHandler() {
        macros = new HashMap<>();
    }


    @Override
    public boolean handles(CallStatement call) throws IllegalArgumentException {
        return macros.containsKey(call.getCommand());
    }

    @Override
    public void evaluate(Interpreter interpreter,
                         CallStatement call,
                         VariableAssignment params) {
        //ProofMacro m = macros.get(call.getCommand());
        Parameters p = new Parameters();
        p.put(new Variable("#2"), new StringLiteral(call.getCommand()));
        CallStatement macroCall = new CallStatement("macro", p);
        macroCall.setRuleContext(call.getRuleContext().get());
        //macro proofscript command
        interpreter.getFunctionLookup().callCommand(interpreter, call, params);
        //TODO change MacroCommand.Parameters to public
    }
}
