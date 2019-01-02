package edu.kit.iti.formal.psdbg.gui.model;

import de.uka.ilkd.key.speclang.Contract;
import edu.kit.iti.formal.psdbg.ChangeBean;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.dbg.DebuggerFramework;
import edu.kit.iti.formal.psdbg.interpreter.dbg.PTreeNode;
import lombok.Data;

import java.io.File;

@Data
public class DebuggerMainModel extends ChangeBean {
    /**
     * Property: current loaded KeY File
     */
    private File keyFile;
    /**
     * Chosen contract for problem
     */
    private Contract chosenContract;
    /**
     * Property: current loaded javaFile
     */
    private File javaFile;
    /**
     * Properties for stepping enabling and disabling
     */
    private DebuggerFramework<KeyData> debuggerFramework;

    private PTreeNode<KeyData> statePointer;

    private boolean stepBackPossible;

    private boolean stepOverPossible;

    private boolean stepIntoPossible;

    private boolean stepReturnPossible;

    private InterpreterThreadState interpreterState = InterpreterThreadState.NO_THREAD;

    /**
     * True, iff the execution of script is not possible
     */
    private boolean executeNotPossible = true;

    /**
     *
     */
    private File initialDirectory;

    /**
     *
     */
    private String javaCode;


    public void setKeyFile(File keyFile) {
        this.keyFile = keyFile;
    }

    public void setChosenContract(Contract chosenContract) {
        this.chosenContract = chosenContract;
    }

    public void setJavaFile(File javaFile) {
        this.javaFile = javaFile;
    }

    public void setDebuggerFramework(DebuggerFramework<KeyData> debuggerFramework) {
        this.debuggerFramework = debuggerFramework;
    }

    public void setStatePointer(PTreeNode<KeyData> statePointer) {
        this.statePointer = statePointer;
    }

    public void setStepBackPossible(boolean stepBackPossible) {
        this.stepBackPossible = stepBackPossible;
    }

    public void setStepOverPossible(boolean stepOverPossible) {
        this.stepOverPossible = stepOverPossible;
    }

    public void setStepIntoPossible(boolean stepIntoPossible) {
        this.stepIntoPossible = stepIntoPossible;
    }

    public void setStepReturnPossible(boolean stepReturnPossible) {
        this.stepReturnPossible = stepReturnPossible;
    }

    public void setInterpreterState(InterpreterThreadState interpreterState) {
        this.interpreterState = interpreterState;
    }

    public void setExecuteNotPossible(boolean executeNotPossible) {
        this.executeNotPossible = executeNotPossible;
    }

    public void setInitialDirectory(File initialDirectory) {
        this.initialDirectory = initialDirectory;
    }

    public void setJavaCode(String javaCode) {
        this.javaCode = javaCode;
    }
}
