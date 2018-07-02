package edu.kit.iti.formal.psdbg.gui.controls.ScriptTree;

import de.uka.ilkd.key.proof.Node;
import edu.kit.iti.formal.psdbg.gui.controls.ScriptTree.AbstractTreeNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.dbg.PTreeNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * This calss represents a node in the script tree, whcih can be of different kinds.
 * The scriptTreeNodes is the model calls for TreeNodes
 */
@RequiredArgsConstructor
public class ScriptTreeNode extends AbstractTreeNode {
    @Getter
    private final PTreeNode<KeyData> scriptState;
    @Getter @Setter
    private Node keyNode;

}
