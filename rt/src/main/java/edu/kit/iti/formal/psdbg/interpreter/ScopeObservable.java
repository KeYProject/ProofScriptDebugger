package edu.kit.iti.formal.psdbg.interpreter;

import edu.kit.iti.formal.psdbg.parser.Visitor;
import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (21.05.17)
 */
public interface ScopeObservable {
    /**
     * If new Block is entered, a new state has to be created
     * (copy of current state) and pushed to the stack
     */
    default <T extends ParserRuleContext> void enterScope(ASTNode<T> node) {
        callListeners(getEntryListeners(), node);
    }

    /**
     * If block is extied the top state on the
     * stack has to be removed
     */
    default <T extends ParserRuleContext> void exitScope(ASTNode<T> node) {
        callListeners(getExitListeners(), node);
    }

    List<Visitor> getExitListeners();
    List<Visitor> getEntryListeners();

    default <T extends ParserRuleContext> void callListeners(List<Visitor> listeners, ASTNode<T> node) {
        if (listeners.size() != 0) {
            listeners.forEach(node::accept);
        }
    }
}