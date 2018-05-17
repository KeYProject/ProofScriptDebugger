package edu.kit.iti.formal.psdbg.interpreter.funchdl;

import edu.kit.iti.formal.psdbg.interpreter.Interpreter;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.data.SavePoint;
import edu.kit.iti.formal.psdbg.interpreter.data.VariableAssignment;
import edu.kit.iti.formal.psdbg.parser.ast.CallStatement;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

public class SaveCommand implements CommandHandler<KeyData> {
    private static final String SAVE_COMMAND_NAME = "#save";
    private static Logger logger = LogManager.getLogger(SaveCommand.class);
    private static Logger consoleLogger = LogManager.getLogger("console");

    @Getter
    @Setter
    private File path;

    public SaveCommand(File path) {
        this.path = path;
    }


    @Override
    public boolean handles(CallStatement call, @Nullable KeyData data) throws IllegalArgumentException {
        return call.getCommand().equals(SAVE_COMMAND_NAME);
    }

    @Override
    public void evaluate(Interpreter<KeyData> interpreter, CallStatement call, VariableAssignment params, KeyData data) {
        //be careful parameters are uninterpreted
        SavePoint sp = new SavePoint(call);
        //Not via Parentpath -> dependency on OS
           /*     String parentPath = path.getAbsolutePath();
        parentPath = parentPath.substring(0, parentPath.length() - path.getName().length());*/

        File parent = path.getParentFile();
        File newFile = sp.getProofFile(parent);

        consoleLogger.info("(Safepoint) Location to be saved to = " + newFile.getAbsolutePath());

        try {
            interpreter.getSelectedNode().getData().getProof().saveToFile(newFile);
            //TODO Call to key persistend facade
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean isUninterpretedParams(CallStatement call) {
        return true;
    }
}







