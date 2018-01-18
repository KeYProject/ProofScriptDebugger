package edu.kit.iti.formal.psdbg.gui.controller;

import com.google.common.eventbus.EventBus;
import de.uka.ilkd.key.logic.PosInOccurrence;
import de.uka.ilkd.key.proof.Goal;
import de.uka.ilkd.key.proof.Node;
import de.uka.ilkd.key.rule.TacletApp;
import edu.kit.iti.formal.psdbg.gui.controls.ScriptArea;
import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.data.State;
import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;
import edu.kit.iti.formal.psdbg.parser.ast.CallStatement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * See http://codingjunkie.net/guava-eventbus/ for an introduction.
 * <p>
 * Created by weigl on 7/11/17.
 */
public class Events {
    public static final EventBus GLOBAL_EVENT_BUS = new EventBus();

    public static void register(Object object) {
        GLOBAL_EVENT_BUS.register(object);
    }

    public static void unregister(Object object) {
        GLOBAL_EVENT_BUS.unregister(object);
    }

    public static void fire(Object focusScriptArea) {
        GLOBAL_EVENT_BUS.post(focusScriptArea);
    }

    @Data
    @RequiredArgsConstructor
    public static class FocusScriptArea {
        private final ScriptArea scriptArea;

    }

    @Data
    @RequiredArgsConstructor
    public static class TacletApplicationEvent {
        private final TacletApp app;
        private final PosInOccurrence pio;

        private final Goal currentGoal;


    }

    @Data
    @RequiredArgsConstructor
    public static class ScriptModificationEvent {
        private final int line;

        private final CallStatement cs;
    }

    @Data
    @RequiredArgsConstructor
    /**
     * A new AST node of existing script was executed
     */
    public static class NewNodeExecuted {
        private final ASTNode correspondingASTNode;
    }


    @Data
    @RequiredArgsConstructor
    /**
     * Event that should be fired when a new goal node was created to inform view
     * components s.t. they can updateTarget their view
     */
    public static class EventForNewGoalView {
        private final ASTNode correspodingASTNode;

        private final State<KeyData> newState;

        private final List<GoalNode> listOfNotExecutedNodes;

        private final List<GoalNode> listOfAlreadyExecutedGoalNodes;

        private final List<GoalNode> closedNodes;

        private final List<GoalNode> openNodes;
    }

    /**
     *
     */
    @Data
    @RequiredArgsConstructor
    public static class SelectNodeInGoalList {
        private final Node node;
    }

    /**
     * @author weigl
     */
    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class PublishMessage {
        private final String message;

        private int flash = 0;
    }

    /**
     *
     */
    @Data
    @RequiredArgsConstructor
    public static class ShowSequent {
        private final Node node;
    }

    /**
     * Der Gipfel an doofen Eventnamen
     *
     * @author weigl
     */
    @Data
    @RequiredArgsConstructor
    public static class InsertAtTheEndOfMainScript {
        private final String text;
    }
}
