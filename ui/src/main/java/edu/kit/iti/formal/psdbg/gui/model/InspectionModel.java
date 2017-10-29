package edu.kit.iti.formal.psdbg.gui.model;

import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;
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

    private final ObjectProperty<ASTNode> node = new SimpleObjectProperty<>(this, "node");

    //alle aktuellen nicht geschlossene Ziele -> alle leaves später (open+closed)
    private final ListProperty<GoalNode<KeyData>> goals = new SimpleListProperty<>(this, "goals");

    //sicht user selected
    private final ObjectProperty<GoalNode<KeyData>> selectedGoalNodeToShow = new SimpleObjectProperty<>(this, "selectedGoalNodeToShow");

    //aktuell im Interpreter aktives Goal
    private final ObjectProperty<GoalNode<KeyData>> currentInterpreterGoal = new SimpleObjectProperty<>(this, "currentInterpreterGoal");

    private final MapProperty<GoalNode, Color> colorofEachGoalNodeinListView = new SimpleMapProperty<>(FXCollections.observableHashMap());

    //private final StringProperty javaString = new SimpleStringProperty();
    private final SetProperty<Integer> highlightedJavaLines = new SimpleSetProperty<>(FXCollections.observableSet(), "highlightedJavaLines");

    private final BooleanProperty closable = new SimpleBooleanProperty(this, "InspectionViewClosableProperty");

    private final BooleanProperty isInterpreterTab = new SimpleBooleanProperty();

    private ObjectProperty<Mode> mode = new SimpleObjectProperty<>();


    public GoalNode<KeyData> getCurrentInterpreterGoal() {
        return currentInterpreterGoal.get();
    }

    public void setCurrentInterpreterGoal(GoalNode<KeyData> currentInterpreterGoal) {
        this.currentInterpreterGoal.set(currentInterpreterGoal);
    }

    public ObjectProperty<GoalNode<KeyData>> currentInterpreterGoalProperty() {
        return currentInterpreterGoal;
    }

    public Mode getMode() {
        return mode.get();
    }

    public void setMode(Mode mode) {
        this.mode.set(mode);
    }

    public ObjectProperty<Mode> modeProperty() {
        return mode;
    }

    public ASTNode getNode() {
        return node.get();
    }

    public void setNode(ASTNode node) {
        this.node.set(node);
    }

    public ObjectProperty<ASTNode> nodeProperty() {
        return node;
    }

    public ObservableList<GoalNode<KeyData>> getGoals() {
        return goals.get();
    }

    public void setGoals(ObservableList<GoalNode<KeyData>> goals) {
        this.goals.set(goals);
    }

    public ListProperty<GoalNode<KeyData>> goalsProperty() {
        return goals;
    }

    public GoalNode getSelectedGoalNodeToShow() {
        return selectedGoalNodeToShow.get();
    }

    public void setSelectedGoalNodeToShow(GoalNode selectedGoalNodeToShow) {
        this.selectedGoalNodeToShow.set(selectedGoalNodeToShow);
    }

    public ObjectProperty<GoalNode<KeyData>> selectedGoalNodeToShowProperty() {
        return selectedGoalNodeToShow;
    }

    public ObservableMap<GoalNode, Color> getColorofEachGoalNodeinListView() {
        return colorofEachGoalNodeinListView.get();
    }

    public void setColorofEachGoalNodeinListView(ObservableMap<GoalNode, Color> colorofEachGoalNodeinListView) {
        this.colorofEachGoalNodeinListView.set(colorofEachGoalNodeinListView);
    }

    public MapProperty<GoalNode, Color> colorofEachGoalNodeinListViewProperty() {
        return colorofEachGoalNodeinListView;
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

    public void setHighlightedJavaLines(ObservableSet<Integer> highlightedJavaLines) {
        this.highlightedJavaLines.set(highlightedJavaLines);
    }

    public SetProperty<Integer> highlightedJavaLinesProperty() {
        return highlightedJavaLines;
    }

    public boolean isClosable() {
        return closable.get();
    }

    public void setClosable(boolean closable) {
        this.closable.set(closable);
    }

    public BooleanProperty closableProperty() {
        return closable;
    }

    public boolean isIsInterpreterTab() {
        return isInterpreterTab.get();
    }

    public void setIsInterpreterTab(boolean isInterpreterTab) {
        this.isInterpreterTab.set(isInterpreterTab);
    }

    public BooleanProperty isInterpreterTabProperty() {
        return isInterpreterTab;
    }


    enum Mode {
        LIVING, DEAD, POSTMORTEM,
    }
}
