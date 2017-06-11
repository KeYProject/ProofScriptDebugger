package edu.kit.formal.gui.model;


import de.uka.ilkd.key.speclang.Contract;
import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.KeyData;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;

/**
 * Model for the root window
 *
 * @author S. Grebing
 */
public class RootModel {

    /**
     * Property: current loaded ScriptFile
     */
    private final SimpleObjectProperty<File> scriptFile = new SimpleObjectProperty<>();


    /**
     * Property: current loaded javaFile
     */
    private final SimpleObjectProperty<File> javaFile = new SimpleObjectProperty<>();

    /**
     * Property: current loaded KeY File
     */
    private final SimpleObjectProperty<File> keYFile = new SimpleObjectProperty<>();

    /**
     * Property: current loaded script string
     */
    //private ObservableValue<String> currentScript;

    /**
     * ListProperty: list of goal nodes in the current state (depending on interpreter state)
     */
    private final ListProperty<GoalNode<KeyData>> currentGoalNodes = new SimpleListProperty<>(FXCollections.observableArrayList());

    /**
     * Current SelectedGoalNode
     */
    private final SimpleObjectProperty<GoalNode<KeyData>> currentSelectedGoalNode = new SimpleObjectProperty<>();

    private final SimpleListProperty<Contract> loadedContracts = new SimpleListProperty<>(FXCollections.observableArrayList());

    private final SimpleObjectProperty<Contract> chosenContract = new SimpleObjectProperty<>();

    public RootModel() {

    }


    /*****************************************************************************************************************
     *                                          Property Getter and Setter
     ***************************************************************************************************************/
    public File getScriptFile() {
        return scriptFile.get();
    }

    public void setScriptFile(File scriptFile) {
        this.scriptFile.set(scriptFile);
    }

    public SimpleObjectProperty<File> scriptFileProperty() {
        return scriptFile;
    }

    public File getKeYFile() {
        return keYFile.get();
    }

    public void setKeYFile(File keYFile) {
        this.keYFile.set(keYFile);
    }

    public SimpleObjectProperty<File> keYFileProperty() {
        return keYFile;
    }

    public ObservableList<GoalNode<KeyData>> getCurrentGoalNodes() {
        return currentGoalNodes.get();
    }

    public void setCurrentGoalNodes(ObservableList<GoalNode<KeyData>> currentGoalNodes) {
        this.currentGoalNodes.set(currentGoalNodes);
    }

    public ListProperty<GoalNode<KeyData>> currentGoalNodesProperty() {
        return currentGoalNodes;
    }

    public GoalNode<KeyData> getCurrentSelectedGoalNode() {
        return currentSelectedGoalNode.get();
    }

    public void setCurrentSelectedGoalNode(GoalNode<KeyData> currentSelectedGoalNode) {
        this.currentSelectedGoalNode.set(currentSelectedGoalNode);
    }

    public SimpleObjectProperty<GoalNode<KeyData>> currentSelectedGoalNodeProperty() {
        return currentSelectedGoalNode;
    }

    public File getJavaFile() {
        return javaFile.get();
    }

    public void setJavaFile(File javaFile) {
        this.javaFile.set(javaFile);
    }

    public SimpleObjectProperty<File> javaFileProperty() {
        return javaFile;
    }

    public ObservableList<Contract> getLoadedContracts() {
        return loadedContracts.get();
    }

    public void setLoadedContracts(ObservableList<Contract> loadedContracts) {
        this.loadedContracts.set(loadedContracts);
    }

    public SimpleListProperty<Contract> loadedContractsProperty() {
        return loadedContracts;
    }

    public Contract getChosenContract() {
        return chosenContract.get();
    }

    public void setChosenContract(Contract chosenContract) {
        this.chosenContract.set(chosenContract);
    }

    public SimpleObjectProperty<Contract> chosenContractProperty() {
        return chosenContract;
    }


}
