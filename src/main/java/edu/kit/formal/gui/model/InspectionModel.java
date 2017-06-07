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
}
