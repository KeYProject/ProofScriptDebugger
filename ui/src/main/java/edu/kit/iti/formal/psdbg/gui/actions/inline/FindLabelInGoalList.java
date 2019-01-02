package edu.kit.iti.formal.psdbg.gui.actions.inline;

import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;
import edu.kit.iti.formal.psdbg.parser.ast.StringLiteral;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Alexander Weigl
 * @version 1 (12.03.18)
 */
public class FindLabelInGoalList implements InlineActionSupplier {
    @Override
    public Collection<InlineAction> get(ASTNode node) {
        if (node instanceof StringLiteral) {
            return Collections.singletonList(new FindLabelInGoalListAction());
        }
        return Collections.emptyList();
    }

    public static class FindLabelInGoalListAction extends InlineAction {
        public FindLabelInGoalListAction() {
            super("Find Label in Goals", 2, null);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("FindLabelInGoalListAction.handle");
        }
    }
}
