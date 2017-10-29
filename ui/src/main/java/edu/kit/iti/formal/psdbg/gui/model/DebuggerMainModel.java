package edu.kit.iti.formal.psdbg.gui.model;

import de.uka.ilkd.key.speclang.Contract;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.dbg.DebuggerFramework;
import edu.kit.iti.formal.psdbg.interpreter.dbg.PTreeNode;
import javafx.beans.property.*;
import javafx.beans.value.ObservableBooleanValue;

import java.io.File;

public class DebuggerMainModel {
    private ObjectProperty<DebuggerFramework<KeyData>> debuggerFramework = new SimpleObjectProperty<>();
    private ObjectProperty<PTreeNode<KeyData>> statePointer = new SimpleObjectProperty<>();

    private BooleanProperty stepBackPossible = new SimpleBooleanProperty(this, "stepBackPossible", false);
    private BooleanProperty stepOverPossible = new SimpleBooleanProperty(this, "stepOverPossible", false);
    private BooleanProperty stepIntoPossible = new SimpleBooleanProperty(this, "stepIntoPossible", false);
    private BooleanProperty stepReturnPossible = new SimpleBooleanProperty(this, "stepReturnPossible", false);


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
     * True, iff the execution is not possible
     */
    private ObservableBooleanValue executeNotPossible = new SimpleBooleanProperty();//proofTreeController.executeNotPossibleProperty().or(FACADE.readyToExecuteProperty().not());

    /**
     *
     */
    private ObjectProperty<File> initialDirectory = new SimpleObjectProperty<>(this, "initialDirectory");

    /**
     *
     */
    private StringProperty javaCode = new SimpleStringProperty(this, "javaCode");


    private BooleanProperty debugMode = new SimpleBooleanProperty(this, "debugMode", false);

    public DebuggerFramework<KeyData> getDebuggerFramework() {
        return debuggerFramework.get();
    }

    public ObjectProperty<DebuggerFramework<KeyData>> debuggerFrameworkProperty() {
        return debuggerFramework;
    }

    public void setDebuggerFramework(DebuggerFramework<KeyData> debuggerFramework) {
        this.debuggerFramework.set(debuggerFramework);
    }

    public PTreeNode<KeyData> getStatePointer() {
        return statePointer.get();
    }

    public ObjectProperty<PTreeNode<KeyData>> statePointerProperty() {
        return statePointer;
    }

    public void setStatePointer(PTreeNode<KeyData> statePointer) {
        this.statePointer.set(statePointer);
    }

    public boolean isStepBackPossible() {
        return stepBackPossible.get();
    }

    public BooleanProperty stepBackPossibleProperty() {
        return stepBackPossible;
    }

    public void setStepBackPossible(boolean stepBackPossible) {
        this.stepBackPossible.set(stepBackPossible);
    }

    public boolean isStepOverPossible() {
        return stepOverPossible.get();
    }

    public BooleanProperty stepOverPossibleProperty() {
        return stepOverPossible;
    }

    public void setStepOverPossible(boolean stepOverPossible) {
        this.stepOverPossible.set(stepOverPossible);
    }

    public boolean isStepIntoPossible() {
        return stepIntoPossible.get();
    }

    public BooleanProperty stepIntoPossibleProperty() {
        return stepIntoPossible;
    }

    public void setStepIntoPossible(boolean stepIntoPossible) {
        this.stepIntoPossible.set(stepIntoPossible);
    }

    public boolean isStepReturnPossible() {
        return stepReturnPossible.get();
    }

    public BooleanProperty stepReturnPossibleProperty() {
        return stepReturnPossible;
    }

    public void setStepReturnPossible(boolean stepReturnPossible) {
        this.stepReturnPossible.set(stepReturnPossible);
    }

    public File getJavaFile() {
        return javaFile.get();
    }

    public ObjectProperty<File> javaFileProperty() {
        return javaFile;
    }

    public void setJavaFile(File javaFile) {
        this.javaFile.set(javaFile);
    }

    public File getKeyFile() {
        return keyFile.get();
    }

    public ObjectProperty<File> keyFileProperty() {
        return keyFile;
    }

    public void setKeyFile(File keyFile) {
        this.keyFile.set(keyFile);
    }

    public Contract getChosenContract() {
        return chosenContract.get();
    }

    public ObjectProperty<Contract> chosenContractProperty() {
        return chosenContract;
    }

    public void setChosenContract(Contract chosenContract) {
        this.chosenContract.set(chosenContract);
    }

    public Boolean getExecuteNotPossible() {
        return executeNotPossible.get();
    }

    public ObservableBooleanValue executeNotPossibleProperty() {
        return executeNotPossible;
    }

    public File getInitialDirectory() {
        return initialDirectory.get();
    }

    public ObjectProperty<File> initialDirectoryProperty() {
        return initialDirectory;
    }

    public void setInitialDirectory(File initialDirectory) {
        this.initialDirectory.set(initialDirectory);
    }

    public String getJavaCode() {
        return javaCode.get();
    }

    public StringProperty javaCodeProperty() {
        return javaCode;
    }

    public void setJavaCode(String javaCode) {
        this.javaCode.set(javaCode);
    }

    public boolean isDebugMode() {
        return debugMode.get();
    }

    public BooleanProperty debugModeProperty() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode.set(debugMode);
    }

    public void release() {
    }
}
