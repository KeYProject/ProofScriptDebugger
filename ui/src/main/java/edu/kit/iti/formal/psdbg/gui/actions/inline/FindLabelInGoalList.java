package edu.kit.iti.formal.psdbg.gui.actions.inline;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;
import edu.kit.iti.formal.psdbg.parser.ast.StringLiteral;
import javafx.event.ActionEvent;

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
            setName(getClass().getSimpleName());
            setGraphics(new MaterialDesignIconView(MaterialDesignIcon.ACCOUNT));
            setPriority(2);
            setEventHandler(this::handle);
        }

        private void handle(ActionEvent event) {
            System.out.println("FindLabelInGoalListAction.handle");
        }
    }
}
