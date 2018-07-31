package edu.kit.iti.formal.psdbg.gui.controls.ScriptTree;

import de.uka.ilkd.key.proof.Node;
import edu.kit.iti.formal.psdbg.gui.controls.TreeNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.dbg.PTreeNode;
import edu.kit.iti.formal.psdbg.parser.ast.CallStatement;
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
    @Getter
    private final Node keyNode;
    @Getter @Setter
    private final int linenr;

    @Override
    public String toString(){
        return scriptState.getStatement().toString()+" with ID "+scriptState.getId();
    }

    @Override
    public TreeNode toTreeNode() {
        String label;
        if (isMatchEx()) {
            label = "match in line " + linenr;
        } else {
            label = ((CallStatement) scriptState.getStatement()).getCommand() + " (line " + linenr + ")";
        }

        if (!isSucc()) {
            label += " (failed)";
        }
        return  new TreeNode(label, keyNode);
    }
}
