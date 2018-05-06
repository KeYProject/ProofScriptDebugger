package edu.kit.iti.formal.psdbg.interpreter.data;

import edu.kit.iti.formal.psdbg.parser.ast.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;

@Data
@RequiredArgsConstructor
public class SavePoint   {

    private final String savepointName;

    private int start = -1;

    private int end = -1;

    public SavePoint(CallStatement call){
        if(isSaveCommand(call)){
            Parameters p = call.getParameters();
            savepointName = ((StringLiteral) p.get(new Variable("#2"))).getText();
            start = call.getRuleContext().getStart().getStartIndex();
            end = call.getRuleContext().getStart().getStopIndex();
        } else {
        throw new IllegalArgumentException(call.getCommand()+" is not a save statement");
    }}

    public static boolean isSaveCommand(CallStatement call){
        return (call.getCommand().equals("save"));
    }

    public File getProofFile(File dir){
        return new File(dir, savepointName+".proof");
    }

    public static boolean isSaveCommand(Statement statement) {
        try{
            CallStatement c = (CallStatement) statement;
            return isSaveCommand(c);
        }catch (ClassCastException e) {
            return false;
        }
    }

    public boolean isThisStatement(Statement statement) {
        if(isSaveCommand(statement)){
            CallStatement c = (CallStatement) statement;
            return c.getCommand().equals(savepointName);

        }
        return false;
    }
}
