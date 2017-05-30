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

import java.io.File;

/**
 * Model for the root window
 * Created by sarah on 5/26/17.
 */
public class RootModel {

    /**
     * Current laoded ScriptFile
     */
    private final SimpleObjectProperty<File> scriptFile;

    /**
     * Current loaded KeY File
     */
    private final SimpleObjectProperty<File> keYFile;

    /**
     * Current loaded script string
     */
    private final SimpleObjectProperty<String> currentScript;

    /**
     * The list of goal nodes in the current state (depending on interpreter state)
     */
    private ListProperty<GoalNode<KeyData>> currentGoalNodes;

    /**
     * Current SelectedGoalNode
     */
    private SimpleObjectProperty<GoalNode<KeyData>> currentSelectedGoalNode;

    @Getter
    private State<KeyData> currentState;


    public RootModel() {
        scriptFile = new SimpleObjectProperty<>();
        keYFile = new SimpleObjectProperty<>();
        currentScript = new SimpleObjectProperty<>("");
        currentSelectedGoalNode = new SimpleObjectProperty<>();
        currentGoalNodes = new SimpleListProperty<>(FXCollections.observableArrayList());

    }

    public GoalNode<KeyData> getCurrentSelectedGoalNode() {
        return currentSelectedGoalNode.get();
    }

    public void setCurrentSelectedGoalNode(GoalNode currentSelectedGoalNode) {
        this.currentSelectedGoalNode.set(currentSelectedGoalNode);
    }

    public SimpleObjectProperty<GoalNode<KeyData>> currentSelectedGoalNodeProperty() {
        return currentSelectedGoalNode;
    }

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


    public void setCurrentState(State<KeyData> currentState) {
        this.currentState = currentState;
    }
}
