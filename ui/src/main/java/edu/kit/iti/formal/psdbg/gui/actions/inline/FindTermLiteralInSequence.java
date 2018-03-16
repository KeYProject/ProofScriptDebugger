package edu.kit.iti.formal.psdbg.gui.actions.inline;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;
import edu.kit.iti.formal.psdbg.parser.ast.TermLiteral;
import javafx.event.ActionEvent;

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
            setName(getClass().getSimpleName());
            setGraphics(new MaterialDesignIconView(MaterialDesignIcon.MAGNIFY_PLUS));
            setEventHandler(this::handle);
        }

        private void handle(ActionEvent event) {
            System.out.println("FindTermLiteralInSequenceAction.handle");
        }
    }
}
