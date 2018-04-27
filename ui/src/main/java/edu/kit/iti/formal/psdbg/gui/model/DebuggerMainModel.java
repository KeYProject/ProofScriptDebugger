package edu.kit.iti.formal.psdbg.gui.model;

import de.uka.ilkd.key.speclang.Contract;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.dbg.DebuggerFramework;
import edu.kit.iti.formal.psdbg.interpreter.dbg.PTreeNode;
import javafx.beans.property.*;
import javafx.beans.value.ObservableBooleanValue;

import java.io.File;

public class DebuggerMainModel {
    /**
     * Property: current loaded javaFile
     */
    private final ObjectProperty<File> javaFile = new SimpleObjectProperty<>(this, "javaFile");

    /**
     * Property: current loaded KeY File
     */
    private final ObjectProperty<File> keyFile = new SimpleObjectProperty<>(this, "keyFile");

    /**
     * Chosen contract for problem
     */
    private final ObjectProperty<Contract> chosenContract = new SimpleObjectProperty<>(this, "chosenContract");

    /**
     * Properties for stepping enabling and disabling
     */
    private ObjectProperty<DebuggerFramework<KeyData>> debuggerFramework = new SimpleObjectProperty<>();

    private ObjectProperty<PTreeNode<KeyData>> statePointer = new SimpleObjectProperty<>();

    private BooleanProperty stepBackPossible = new SimpleBooleanProperty(this, "stepBackPossible", false);

    private BooleanProperty stepOverPossible = new SimpleBooleanProperty(this, "stepOverPossible", false);

    private BooleanProperty stepIntoPossible = new SimpleBooleanProperty(this, "stepIntoPossible", false);

    private BooleanProperty stepReturnPossible = new SimpleBooleanProperty(this, "stepReturnPossible", false);

    private ObjectProperty<InterpreterThreadState> interpreterState = new SimpleObjectProperty<>(this, "interpreterState",
            InterpreterThreadState.NO_THREAD);

    /**
     * True, iff the execution of script is not possible
     */
    private BooleanProperty executeNotPossible = new SimpleBooleanProperty(true);//proofTreeController.executeNotPossibleProperty().or(FACADE.readyToExecuteProperty().not());

    /**
     *
     */
    private ObjectProperty<File> initialDirectory = new SimpleObjectProperty<>(this, "initialDirectory");

    /**
     *
     */
    private StringProperty javaCode = new SimpleStringProperty(this, "javaCode");


    //private BooleanProperty debugMode = new SimpleBooleanProperty(this, "debugMode", false);

    public DebuggerFramework<KeyData> getDebuggerFramework() {
        return debuggerFramework.get();
    }

    public void setDebuggerFramework(DebuggerFramework<KeyData> debuggerFramework) {
        this.debuggerFramework.set(debuggerFramework);
    }

    public ObjectProperty<DebuggerFramework<KeyData>> debuggerFrameworkProperty() {
        return debuggerFramework;
    }

    public PTreeNode<KeyData> getStatePointer() {
        return statePointer.get();
    }

    public void setStatePointer(PTreeNode<KeyData> statePointer) {
        this.statePointer.set(statePointer);
    }

    public ObjectProperty<PTreeNode<KeyData>> statePointerProperty() {
        return statePointer;
    }

    public boolean isStepBackPossible() {
        return stepBackPossible.get();
    }

    public void setStepBackPossible(boolean stepBackPossible) {
        this.stepBackPossible.set(stepBackPossible);
    }

    public BooleanProperty stepBackPossibleProperty() {
        return stepBackPossible;
    }

    public boolean isStepOverPossible() {
        return stepOverPossible.get();
    }

    public void setStepOverPossible(boolean stepOverPossible) {
        this.stepOverPossible.set(stepOverPossible);
    }

    public BooleanProperty stepOverPossibleProperty() {
        return stepOverPossible;
    }

    public boolean isStepIntoPossible() {
        return stepIntoPossible.get();
    }

    public void setStepIntoPossible(boolean stepIntoPossible) {
        this.stepIntoPossible.set(stepIntoPossible);
    }

    public BooleanProperty stepIntoPossibleProperty() {
        return stepIntoPossible;
    }

    public boolean isStepReturnPossible() {
        return stepReturnPossible.get();
    }

    public void setStepReturnPossible(boolean stepReturnPossible) {
        this.stepReturnPossible.set(stepReturnPossible);
    }

    public BooleanProperty stepReturnPossibleProperty() {
        return stepReturnPossible;
    }

    public File getJavaFile() {
        return javaFile.get();
    }

    public void setJavaFile(File javaFile) {
        this.javaFile.set(javaFile);
    }

    public ObjectProperty<File> javaFileProperty() {
        return javaFile;
    }

    public File getKeyFile() {
        return keyFile.get();
    }

    public void setKeyFile(File keyFile) {
        this.keyFile.set(keyFile);
    }

    public ObjectProperty<File> keyFileProperty() {
        return keyFile;
    }

    public Contract getChosenContract() {
        return chosenContract.get();
    }

    public void setChosenContract(Contract chosenContract) {
        this.chosenContract.set(chosenContract);
    }

    public ObjectProperty<Contract> chosenContractProperty() {
        return chosenContract;
    }

    public Boolean getExecuteNotPossible() {
        return executeNotPossible.get();
    }

    public BooleanProperty executeNotPossibleProperty() {
        return executeNotPossible;
    }

    public File getInitialDirectory() {
        return initialDirectory.get();
    }

    public void setInitialDirectory(File initialDirectory) {
        this.initialDirectory.set(initialDirectory);
    }

    public ObjectProperty<File> initialDirectoryProperty() {
        return initialDirectory;
    }

    public String getJavaCode() {
        return javaCode.get();
    }

    public void setJavaCode(String javaCode) {
        this.javaCode.set(javaCode);
    }

    public StringProperty javaCodeProperty() {
        return javaCode;
    }

/*
    public boolean isDebugMode() {
        return debugMode.get();
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode.set(debugMode);
    }

    public BooleanProperty debugModeProperty() {
        return debugMode;
    }

*/
    public InterpreterThreadState getInterpreterState() {
        return interpreterState.get();
    }

    public ObjectProperty<InterpreterThreadState> interpreterStateProperty() {
        return interpreterState;
    }

    public void setInterpreterState(InterpreterThreadState interpreterState) {
        this.interpreterState.set(interpreterState);
    }


}
