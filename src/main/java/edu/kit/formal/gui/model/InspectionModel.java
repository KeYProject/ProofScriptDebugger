package edu.kit.formal.gui.model;

import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.proofscriptparser.ast.ASTNode;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import java.util.Map;
import java.util.Set;

/**
 * Model for the inspection view
 *
 * @author S.Grebing
 */
public class InspectionModel {

    private ASTNode node;

    private SimpleListProperty<GoalNode> currentGoals;

    private SimpleObjectProperty<GoalNode> selectedGoalNodeToShow;

    private Map<GoalNode, Color> colorofEachGoalNodeinListView;

    private boolean showJavaView;

    private String javaString;

    private Set<Integer> highlightedJavaLines;

    private boolean closable;

    private boolean isInterpreterTab;

    public InspectionModel() {
    }

    /***************************************************************************************************************
     *   Getter and Setter
     *
     ***************************************************************************************************************/
    public ASTNode getNode() {
        return node;
    }

    public void setNode(ASTNode node) {
        this.node = node;
    }

    public ObservableList<GoalNode> getCurrentGoals() {
        return currentGoals.get();
    }

    public void setCurrentGoals(ObservableList<GoalNode> currentGoals) {
        this.currentGoals.set(currentGoals);
    }

    public SimpleListProperty<GoalNode> currentGoalsProperty() {
        return currentGoals;
    }

    public GoalNode getSelectedGoalNodeToShow() {
        return selectedGoalNodeToShow.get();
    }

    public void setSelectedGoalNodeToShow(GoalNode selectedGoalNodeToShow) {
        this.selectedGoalNodeToShow.set(selectedGoalNodeToShow);
    }

    public SimpleObjectProperty<GoalNode> selectedGoalNodeToShowProperty() {
        return selectedGoalNodeToShow;
    }

    public Map<GoalNode, Color> getColorofEachGoalNodeinListView() {
        return colorofEachGoalNodeinListView;
    }

    public void setColorofEachGoalNodeinListView(Map<GoalNode, Color> colorofEachGoalNodeinListView) {
        this.colorofEachGoalNodeinListView = colorofEachGoalNodeinListView;
    }

    public boolean isShowJavaView() {
        return showJavaView;
    }

    public void setShowJavaView(boolean showJavaView) {
        this.showJavaView = showJavaView;
    }

    public String getJavaString() {
        return javaString;
    }

    public void setJavaString(String javaString) {
        this.javaString = javaString;
    }

    public Set<Integer> getHighlightedJavaLines() {
        return highlightedJavaLines;
    }

    public void setHighlightedJavaLines(Set<Integer> highlightedJavaLines) {
        this.highlightedJavaLines = highlightedJavaLines;
    }

    public boolean isClosable() {
        return closable;
    }

    public void setClosable(boolean closable) {
        this.closable = closable;
    }

    public boolean isInterpreterTab() {
        return isInterpreterTab;
    }

    public void setInterpreterTab(boolean interpreterTab) {
        isInterpreterTab = interpreterTab;
    }
}
