package edu.kit.formal.psdb.interpreter.funchdl;

import de.uka.ilkd.key.macros.ProofMacro;
import edu.kit.formal.psdb.interpreter.Interpreter;
import edu.kit.formal.psdb.interpreter.data.Value;
import edu.kit.formal.psdb.interpreter.data.VariableAssignment;
import edu.kit.formal.psdb.parser.ast.*;
import edu.kit.formal.psdb.parser.types.SimpleType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexander Weigl
 * @version 1 (21.05.17)
 */
@RequiredArgsConstructor
public class MacroCommandHandler implements CommandHandler {
    @Getter private final Map<String, ProofMacro> macros;

    public MacroCommandHandler() {
        macros = new HashMap<>();
    }

    public MacroCommandHandler(Collection<ProofMacro> macros) {
        this();
        macros.forEach(m -> this.macros.put(m.getScriptCommandName(), m));
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
        macroCall.setRuleContext(call.getRuleContext());
        VariableAssignment newParam = new VariableAssignment(null);
        newParam.declare("#2", SimpleType.STRING);
        newParam.assign("#2", Value.from(call.getCommand()));
        //macro proofscript command
        interpreter.getFunctionLookup().callCommand(interpreter, macroCall, newParam);

        //TODO change MacroCommand.Parameters to public
    }
}