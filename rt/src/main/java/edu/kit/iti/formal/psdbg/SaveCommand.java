package edu.kit.iti.formal.psdbg;

import edu.kit.iti.formal.psdbg.interpreter.Interpreter;
import edu.kit.iti.formal.psdbg.interpreter.data.SavePoint;
import edu.kit.iti.formal.psdbg.interpreter.data.VariableAssignment;
import edu.kit.iti.formal.psdbg.interpreter.funchdl.BuiltinCommands;
import edu.kit.iti.formal.psdbg.parser.ast.CallStatement;
import lombok.Getter;
import sun.security.ssl.Debug;

import java.io.File;
import java.io.IOException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;

public class SaveCommand extends BuiltinCommands.BuiltinCommand<String> {

    @Getter
    private final List<SavePoint> splist;

    public SaveCommand() {
        super("save");
        splist = new ArrayList<>();
    }

    @Override
    public void evaluate(Interpreter<String> interpreter, CallStatement call, VariableAssignment params, String data) {

        SavePoint sp = new SavePoint(call);

        // check if Savepoint already in list
        boolean inlist = false;
        if (splist.size() != 0) {
            for (int i = 0; i < splist.size(); i++) {
                if (splist.get(i).getSavepointName().equals(sp.getSavepointName())) {
                    inlist = true;

                    //TODO: force replace?!
                    splist.add(i, sp);
                }
            }
        }

        if(!inlist) {
            splist.add(sp);
        }

    }
}







