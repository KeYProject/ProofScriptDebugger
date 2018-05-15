package edu.kit.iti.formal.psdbg.interpreter.funchdl;

import edu.kit.iti.formal.psdbg.interpreter.Interpreter;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.data.SavePoint;
import edu.kit.iti.formal.psdbg.interpreter.data.VariableAssignment;
import edu.kit.iti.formal.psdbg.interpreter.funchdl.BuiltinCommands;
import edu.kit.iti.formal.psdbg.interpreter.funchdl.CommandHandler;
import edu.kit.iti.formal.psdbg.parser.ast.CallStatement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import sun.security.ssl.Debug;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;

public class SaveCommand implements CommandHandler<KeyData>{

    @Getter @Setter
    private File path;

    public SaveCommand(File path) {
        this.path = path;
    }


    @Override
    public boolean handles(CallStatement call, @Nullable KeyData data) throws IllegalArgumentException {
        return call.getCommand().equals("save");
    }

    @Override
    public void evaluate(Interpreter<KeyData> interpreter, CallStatement call, VariableAssignment params, KeyData data) {
        SavePoint sp = new SavePoint(call);

        //Not via Parentpath -> dependency on OS
        String parentpath = path.getAbsolutePath();
        parentpath = parentpath.substring(0, parentpath.length() - path.getName().length());

        File newfile = new File(parentpath + sp.getSavepointName() + ".key");
        System.out.println("(Safepoint) Location to be saved to = " + newfile.getAbsolutePath());
        try {
            interpreter.getSelectedNode().getData().getProof().saveToFile(newfile);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}







