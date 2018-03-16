package edu.kit.iti.formal.psdbg.gui.actions.inline;

import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;

import java.util.Collection;

/**
 * @author Alexander Weigl
 * @version 1 (12.03.18)
 */
public interface InlineActionSupplier {
    Collection<InlineAction> get(ASTNode node);
}
