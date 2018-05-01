package edu.kit.iti.formal.psdbg.interpreter.funchdl;

import edu.kit.iti.formal.psdbg.interpreter.Interpreter;
import edu.kit.iti.formal.psdbg.interpreter.data.VariableAssignment;
import edu.kit.iti.formal.psdbg.parser.ast.CallStatement;
import edu.kit.iti.formal.psdbg.parser.ast.Parameters;
import edu.kit.iti.formal.psdbg.parser.ast.Variable;
import edu.kit.iti.formal.psdbg.parser.data.Value;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Path;

public class SaveCommand extends BuiltinCommands.BuiltinCommand<String>{
        public SaveCommand() {
        super("save");
    }

    @Override
    public void evaluate(Interpreter<String> interpreter, CallStatement call, VariableAssignment params, String data) {

            //TODO: another param entry with id for the savepoint

            Value<Path> val = (Value<Path>) params.getValues().getOrDefault(
                new Variable("filepath"),
                Value.from(2));

            File savepoint = new File(val.toString());

    }

    private class Parameters extends edu.kit.iti.formal.psdbg.parser.ast.Parameters{

    }






}
