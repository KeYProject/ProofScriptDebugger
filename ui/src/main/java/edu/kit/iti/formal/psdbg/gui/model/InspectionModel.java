package edu.kit.iti.formal.psdbg.gui.model;

import edu.kit.iti.formal.psdbg.ChangeBean;
import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;
import lombok.Getter;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Model for the inspection view
 *
 * @author S.Grebing
 * @author Alexander Weigl
 */
@Getter
public class InspectionModel extends ChangeBean {
    private ASTNode node;

    //alle aktuellen nicht geschlossene Ziele -> alle leaves später (open+closed)
    private List<GoalNode<KeyData>> goals = new LinkedList<>();

    //sicht user selected
    private GoalNode<KeyData> selectedGoalNodeToShow;

    //aktuell im Interpreter aktives Goal
    private GoalNode<KeyData> currentInterpreterGoal;

    private Map<GoalNode, Color> colorofEachGoalNodeinListView = new HashMap<>();

    //private final StringProperty javaString = new SimpleStringProperty();
    private Set<Integer> highlightedJavaLines = new HashSet<>();

    private boolean closable;

    private boolean isInterpreterTab;

    private Mode mode = Mode.DEAD;

    public void setCurrentInterpreterGoal(GoalNode<KeyData> currentInterpreterGoal) {
        firePropertyChange("currentInterpreterGoal", this.currentInterpreterGoal, currentInterpreterGoal);
        this.currentInterpreterGoal = currentInterpreterGoal;
    }

    public void clearHighlightLines() {
        setHighlightedJavaLines(new HashSet<>());
    }

    public void setNode(ASTNode node) {
        firePropertyChange("node", this.node, node);
        this.node = node;
    }

    public void setGoals(List<GoalNode<KeyData>> goals) {
        firePropertyChange("goals", this.goals, goals);
        this.goals = goals;
    }

    public void setSelectedGoalNodeToShow(GoalNode<KeyData> selectedGoalNodeToShow) {
        firePropertyChange("selectedGoalNodeToShow", this.selectedGoalNodeToShow, selectedGoalNodeToShow);
        this.selectedGoalNodeToShow = selectedGoalNodeToShow;
    }

    public void setColorofEachGoalNodeinListView(Map<GoalNode, Color> colorofEachGoalNodeinListView) {
        firePropertyChange("colorofEachGoalNodeinListView", this.colorofEachGoalNodeinListView, colorofEachGoalNodeinListView);
        this.colorofEachGoalNodeinListView = colorofEachGoalNodeinListView;
    }

    public void setHighlightedJavaLines(Set<Integer> highlightedJavaLines) {
        firePropertyChange("highlightedJavaLines", this.highlightedJavaLines, highlightedJavaLines);
        this.highlightedJavaLines = highlightedJavaLines;
    }

    public void setClosable(boolean closable) {
        firePropertyChange("closeable", this.closable, closable);
        this.closable = closable;
    }

    public void setInterpreterTab(boolean interpreterTab) {
        firePropertyChange("interpreterTab", this.isInterpreterTab, interpreterTab);
        isInterpreterTab = interpreterTab;
    }

    public void setMode(Mode mode) {
        firePropertyChange("mode", this.mode, mode);
        this.mode = mode;
    }

    enum Mode {
        LIVING, DEAD, POSTMORTEM,
    }
}
