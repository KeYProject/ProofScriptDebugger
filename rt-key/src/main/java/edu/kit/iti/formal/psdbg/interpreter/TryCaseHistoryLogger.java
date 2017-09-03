package edu.kit.iti.formal.psdbg.interpreter;

import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.data.State;
import edu.kit.iti.formal.psdbg.parser.DefaultASTVisitor;
import edu.kit.iti.formal.psdbg.parser.Visitor;
import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Logger for Interpreting a try case. If the case was successful,
 * the logged Events can be replayed back to the parent interpreter listeners
 */
public class TryCaseHistoryLogger extends HistoryListener {

    private Interpreter currentInterpreter;

    private EntryListener entryListener = new EntryListener();

    private ExitListener exitListener = new ExitListener();

    /**
     * Mapping of ASTNodes and their entry and exit states
     */
    private Map<ASTNode, EntryExitPair> mapOfNodesAndStates = new HashMap<>();
    /**
     * List containing of pairs which contain an ASTNode and whether it was visited by entry or exit
     */

    private List<Pair<EntryName, ASTNode>> sequenceOfEvents = new LinkedList<>();

    public TryCaseHistoryLogger(Interpreter interpreter) {
        super(interpreter);
        this.currentInterpreter = interpreter;
        install(interpreter);
    }

    public void install(Interpreter interpreter) {
        if (currentInterpreter != null) deinstall(interpreter);
        interpreter.getEntryListeners().add(entryListener);
        interpreter.getExitListeners().add(exitListener);
        this.currentInterpreter = interpreter;
    }

    public void deinstall(Interpreter interpreter) {
        if (interpreter != null) {
            interpreter.getEntryListeners().remove(entryListener);
            interpreter.getExitListeners().remove(exitListener);
        }
    }

    public void printSequenceOfEvents() {
        for (int i = 0; i < sequenceOfEvents.size(); i++) {
            Pair<EntryName, ASTNode> entryNameASTNodePair = sequenceOfEvents.get(i);
            System.out.println(entryNameASTNodePair.getKey() + " " + entryNameASTNodePair.getValue().getStartPosition());

        }
    }

    /**
     * Create a new entry in maps. This is done when entering a node
     *
     * @param node
     * @param state
     * @return
     */
    private Void createNewEntry(ASTNode node, State state) {
        this.sequenceOfEvents.add(new Pair<>(EntryName.ENTRY, node));
        this.mapOfNodesAndStates.put(node, new EntryExitPair(state));
        return null;
    }

    /**
     * Update the antrx after exiting the node
     * @param node
     * @param state
     * @return
     */
    private Void updateEntry(ASTNode node, State state) {
        this.sequenceOfEvents.add(new Pair<>(EntryName.EXIT, node));
        this.mapOfNodesAndStates.get(node).setExitState(state);
        return null;
    }

    /**
     * Play events back to interpreter that listeners are informed about computation
     *
     * @param interpreter
     */
    public void replayEvents(Interpreter interpreter) {
        assert sequenceOfEvents.get(0).getKey().equals(EntryName.ENTRY);
        //get entry and exit listeners to inform about events
        List<Visitor> entryListeners = interpreter.getEntryListeners();
        List<Visitor> exitListeners = interpreter.getExitListeners();
        //assert start state is equal to first entry state
        State<GoalNode<KeyData>> entryState = mapOfNodesAndStates.get(sequenceOfEvents.get(0).getValue()).entryState;
        assert interpreter.peekState().equals(entryState);
        for (Pair<EntryName, ASTNode> nextevent : sequenceOfEvents) {
            ASTNode node = nextevent.getValue();
            if (nextevent.getKey().equals(EntryName.ENTRY)) {

                if (entryListeners.size() != 0) {
                    entryListeners.forEach(node::accept);
                }
                interpreter.popState();
                interpreter.pushState(mapOfNodesAndStates.get(node).entryState);
            } else {
                if (exitListeners.size() != 0) {
                    exitListeners.forEach(node::accept);
                }
                interpreter.popState();
                interpreter.pushState(mapOfNodesAndStates.get(node).exitState);

            }

        }


        //interpreter.popState()
    }

    private enum EntryName {ENTRY, EXIT}

    private class EntryExitPair {

        @Getter
        @Setter
        private State<GoalNode<KeyData>> entryState;
        @Getter
        @Setter
        private State<GoalNode<KeyData>> exitState;

        public EntryExitPair(State<GoalNode<KeyData>> entry, State<GoalNode<KeyData>> exit) {
            this.entryState = entry;
            this.exitState = exit;

        }

        public EntryExitPair(State<GoalNode<KeyData>> entryState) {
            this.entryState = entryState;
        }


    }

    private class EntryListener extends DefaultASTVisitor<Void> {
        @Override
        public Void defaultVisit(ASTNode node) {
            return createNewEntry(node, currentInterpreter.peekState());

        }
    }

    private class ExitListener extends DefaultASTVisitor<Void> {
        @Override
        public Void defaultVisit(ASTNode node) {
            return updateEntry(node, currentInterpreter.peekState());

        }
    }

}
