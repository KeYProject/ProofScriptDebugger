package edu.kit.iti.formal.psdbg.gui.actions.inline;

import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;
import edu.kit.iti.formal.psdbg.parser.ast.TermLiteral;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Alexander Weigl
 * @version 1 (12.03.18)
 */
public class FindTermLiteralInSequence implements InlineActionSupplier {
    @Override
    public Collection<InlineAction> get(ASTNode node) {
        if (node instanceof TermLiteral) {
            return Collections.singletonList(
                    new FindTermLiteralInSequenceAction()
            );
        }
        return Collections.emptyList();
    }

    public static class FindTermLiteralInSequenceAction extends InlineAction {
        public FindTermLiteralInSequenceAction() {
            super("Find Term", 0, null);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("FindTermLiteralInSequenceAction.handle");
        }
    }
}
