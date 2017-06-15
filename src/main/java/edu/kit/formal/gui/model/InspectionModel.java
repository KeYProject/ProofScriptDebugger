package edu.kit.formal.gui.model;

import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.proofscriptparser.ast.ASTNode;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Model for the inspection view
 *
 * @author S.Grebing
 */
public class InspectionModel {

    private ASTNode node;

    private List<GoalNode> currentGoals;

    private GoalNode selectedGoalNodeToShow;

    private Map<GoalNode, Color> colorofEachGoalNodeinListView;

    private boolean showJavaView;

    private String javaString;

    private Set<Integer> highlightedJavaLines;

    private boolean closable;
    private boolean isInterpreterTab;

    public ASTNode getNode() {
        return node;
    }

    public void setNode(ASTNode node) {
        this.node = node;
    }

    public List<GoalNode> getCurrentGoals() {
        return currentGoals;
    }

    public void setCurrentGoals(List<GoalNode> currentGoals) {
        this.currentGoals = currentGoals;
    }

    public GoalNode getSelectedGoalNodeToShow() {
        return selectedGoalNodeToShow;
    }

    public void setSelectedGoalNodeToShow(GoalNode selectedGoalNodeToShow) {
        this.selectedGoalNodeToShow = selectedGoalNodeToShow;
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
