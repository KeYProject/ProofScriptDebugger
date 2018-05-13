package edu.kit.iti.formal.psdbg.interpreter.funchdl;

import edu.kit.iti.formal.psdbg.interpreter.Interpreter;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.data.SavePoint;
import edu.kit.iti.formal.psdbg.interpreter.data.VariableAssignment;
import edu.kit.iti.formal.psdbg.parser.ast.CallStatement;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;


public class BuiltInCommandHandler implements CommandHandler<KeyData> {

    @Getter @Setter
    private Map<String, CommandHandler<KeyData>> builtins;

    @Getter @Setter
    private SaveCommand sc;

    public BuiltInCommandHandler() {
        builtins = new HashMap<>();
        builtins.put("save", sc);
    }


    @Override
    public boolean handles(CallStatement call, @Nullable KeyData data) throws IllegalArgumentException {
        // handler only knows SaveCommand for now
        return builtins.containsKey(call.getCommand());
    }

    @Override
    public void evaluate(Interpreter<KeyData> interpreter, CallStatement call, VariableAssignment params, KeyData data) {
        builtins.get(call.getCommand()).evaluate(interpreter,call,params, data);
    }
}
