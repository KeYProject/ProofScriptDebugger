package edu.kit.iti.formal.psdbg.interpreter.funchdl;

import edu.kit.iti.formal.psdbg.SaveCommand;
import edu.kit.iti.formal.psdbg.interpreter.Interpreter;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.data.SavePoint;
import edu.kit.iti.formal.psdbg.interpreter.data.VariableAssignment;
import edu.kit.iti.formal.psdbg.parser.ast.CallStatement;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BuiltInCommandHandler implements CommandHandler<KeyData> {

    @Getter
    private final Map<String, BuiltinCommands.BuiltinCommand> builtins;

    @Getter
    private final SaveCommand sc = new SaveCommand();

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

        if(SavePoint.isSaveCommand(call)) {
            //TODO: interpreter ist ast visitor, so not needed?
            sc.evaluate(null,call,params,null);



        }

    }
}
