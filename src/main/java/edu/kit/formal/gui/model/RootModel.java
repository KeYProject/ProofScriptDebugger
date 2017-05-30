package edu.kit.formal.gui.model;


import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.KeyData;
import edu.kit.formal.interpreter.data.State;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

/**
 * Model for the root window
 * @author S. Grebing
 */
public class RootModel {
    /**
     * Property: current loaded ScriptFile
     */
    private final SimpleObjectProperty<File> scriptFile;

    /**
     * Property: current loaded KeY File
     */
    private final SimpleObjectProperty<File> keYFile;

    /**
     * Property: current loaded script string
     */
    private final SimpleObjectProperty<String> currentScript;

    /**
     * ListProperty: list of goal nodes in the current state (depending on interpreter state)
     */
    private ListProperty<GoalNode<KeyData>> currentGoalNodes;

    /**
     * Current SelectedGoalNode
     */
    private SimpleObjectProperty<GoalNode<KeyData>> currentSelectedGoalNode;

    @Getter
    @Setter
    private State<KeyData> currentState;


    public RootModel() {
        scriptFile = new SimpleObjectProperty<>();
        keYFile = new SimpleObjectProperty<>();
        currentScript = new SimpleObjectProperty<>("");
        currentSelectedGoalNode = new SimpleObjectProperty<>();
        currentGoalNodes = new SimpleListProperty<>(FXCollections.observableArrayList());

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

    public String getCurrentScript() {
        return currentScript.get();
    }

    public void setCurrentScript(String currentScript) {
        this.currentScript.set(currentScript);
    }

    public SimpleObjectProperty<String> currentScriptProperty() {
        return currentScript;
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




}
