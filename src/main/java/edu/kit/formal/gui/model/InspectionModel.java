package edu.kit.formal.gui.model;

import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.KeyData;
import edu.kit.formal.proofscriptparser.ast.ASTNode;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.scene.paint.Color;

/**
 * Model for the inspection view
 *
 * @author S.Grebing
 * @author Alexander Weigl
 */
public class InspectionModel {
    enum Mode {
        LIVING, DEAD, POSTMORTEM,
    }

    private final ObjectProperty<ASTNode> node = new SimpleObjectProperty<>();
    private final ListProperty<GoalNode<KeyData>> goals = new SimpleListProperty<>();
    private final ObjectProperty<GoalNode<KeyData>> selectedGoalNodeToShow = new SimpleObjectProperty<>();
    private final ObjectProperty<GoalNode<KeyData>> currentInterpreterGoal = new SimpleObjectProperty<>();

    private final MapProperty<GoalNode, Color> colorofEachGoalNodeinListView = new SimpleMapProperty<>(FXCollections.observableHashMap());
    //private final StringProperty javaString = new SimpleStringProperty();
    private final SetProperty<Integer> highlightedJavaLines = new SimpleSetProperty<>(FXCollections.observableSet());
    private final BooleanProperty closable = new SimpleBooleanProperty();
    private final BooleanProperty isInterpreterTab = new SimpleBooleanProperty();
    private ObjectProperty<Mode> mode = new SimpleObjectProperty<>();


    public GoalNode<KeyData> getCurrentInterpreterGoal() {
        return currentInterpreterGoal.get();
    }

    public ObjectProperty<GoalNode<KeyData>> currentInterpreterGoalProperty() {
        return currentInterpreterGoal;
    }

    public void setCurrentInterpreterGoal(GoalNode<KeyData> currentInterpreterGoal) {
        this.currentInterpreterGoal.set(currentInterpreterGoal);
    }

    public Mode getMode() {
        return mode.get();
    }

    public ObjectProperty<Mode> modeProperty() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode.set(mode);
    }

    public ASTNode getNode() {
        return node.get();
    }

    public ObjectProperty<ASTNode> nodeProperty() {
        return node;
    }

    public void setNode(ASTNode node) {
        this.node.set(node);
    }

    public ObservableList<GoalNode<KeyData>> getGoals() {
        return goals.get();
    }

    public ListProperty<GoalNode<KeyData>> goalsProperty() {
        return goals;
    }

    public void setGoals(ObservableList<GoalNode<KeyData>> goals) {
        this.goals.set(goals);
    }

    public GoalNode getSelectedGoalNodeToShow() {
        return selectedGoalNodeToShow.get();
    }

    public ObjectProperty<GoalNode<KeyData>> selectedGoalNodeToShowProperty() {
        return selectedGoalNodeToShow;
    }

    public void setSelectedGoalNodeToShow(GoalNode selectedGoalNodeToShow) {
        this.selectedGoalNodeToShow.set(selectedGoalNodeToShow);
    }

    public ObservableMap<GoalNode, Color> getColorofEachGoalNodeinListView() {
        return colorofEachGoalNodeinListView.get();
    }

    public MapProperty<GoalNode, Color> colorofEachGoalNodeinListViewProperty() {
        return colorofEachGoalNodeinListView;
    }

    public void setColorofEachGoalNodeinListView(ObservableMap<GoalNode, Color> colorofEachGoalNodeinListView) {
        this.colorofEachGoalNodeinListView.set(colorofEachGoalNodeinListView);
    }

    /*
        public String getJavaString() {
            return javaString.get();
        }

        public StringProperty javaStringProperty() {
            return javaString;
        }

        public void setJavaString(String javaString) {
            this.javaString.set(javaString);
        }
    */
    public ObservableSet<Integer> getHighlightedJavaLines() {
        return highlightedJavaLines.get();
    }

    public SetProperty<Integer> highlightedJavaLinesProperty() {
        return highlightedJavaLines;
    }

    public void setHighlightedJavaLines(ObservableSet<Integer> highlightedJavaLines) {
        this.highlightedJavaLines.set(highlightedJavaLines);
    }

    public boolean isClosable() {
        return closable.get();
    }

    public BooleanProperty closableProperty() {
        return closable;
    }

    public void setClosable(boolean closable) {
        this.closable.set(closable);
    }

    public boolean isIsInterpreterTab() {
        return isInterpreterTab.get();
    }

    public BooleanProperty isInterpreterTabProperty() {
        return isInterpreterTab;
    }

    public void setIsInterpreterTab(boolean isInterpreterTab) {
        this.isInterpreterTab.set(isInterpreterTab);
    }
}
