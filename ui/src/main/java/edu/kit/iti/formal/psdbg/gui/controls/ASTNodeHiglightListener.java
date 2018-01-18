package edu.kit.iti.formal.psdbg.gui.controls;

import edu.kit.iti.formal.psdbg.interpreter.Interpreter;
import edu.kit.iti.formal.psdbg.parser.DefaultASTVisitor;
import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;
import edu.kit.iti.formal.psdbg.parser.ast.Parameters;
import edu.kit.iti.formal.psdbg.parser.ast.Statements;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Sets a property that can be used to highlight ASTNodes in the SScriptArea according to current interpreterstate
 * Contains a listener that listens on entry of an ASTNode
 *
 * @param <T>
 */
public class ASTNodeHiglightListener<T> {

    private SimpleObjectProperty<ASTNode> currentHighlightNode = new SimpleObjectProperty<>();
    private Interpreter<T> currentInterpreter;
    private HighlightEntryListener list = new HighlightEntryListener();
    private ASTNode lastHighlightedNode;

    public ASTNodeHiglightListener(Interpreter<T> inter) {

    }

    public ASTNode getCurrentHighlightNode() {
        return currentHighlightNode.get();
    }

    public void setCurrentHighlightNode(ASTNode currentHighlightNode) {
        this.currentHighlightNode.set(currentHighlightNode);
    }

    public SimpleObjectProperty<ASTNode> currentHighlightNodeProperty() {
        return currentHighlightNode;
    }

    public void install(Interpreter<T> interpreter) {
        if (currentInterpreter != null) deinstall(interpreter);
        int i = interpreter.getEntryListeners().size();
        interpreter.getEntryListeners().add(0, list);
        //    interpreter.getExitListeners().addCell(0, exitListener);
        currentInterpreter = interpreter;
    }

    private void deinstall(Interpreter<T> interpreter) {
        if (interpreter != null) {
            interpreter.getEntryListeners().remove(list);
            // interpreter.getExitListeners().remove(exitListener);
        }
    }

    public class HighlightEntryListener extends DefaultASTVisitor<Void> {
        @Override
        public Void visit(Parameters parameters) {
            return null;
        }

        @Override
        public Void visit(Statements statements) {
            return null;
        }

        @Override
        public Void defaultVisit(ASTNode node) {
            if (node != null) {
                lastHighlightedNode = node;
                Platform.runLater(() -> {
                    currentHighlightNode.setValue(lastHighlightedNode);

                });
            }
            return null;
        }
    }
}
