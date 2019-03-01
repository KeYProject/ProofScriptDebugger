package edu.kit.iti.formal.psdbg.gui.controls.ScriptTree;

import de.uka.ilkd.key.proof.Node;
import edu.kit.iti.formal.psdbg.gui.controls.TreeNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.dbg.PTreeNode;
import edu.kit.iti.formal.psdbg.parser.ast.CallStatement;
import edu.kit.iti.formal.psdbg.parser.ast.GuardedCaseStatement;
import edu.kit.iti.formal.psdbg.parser.ast.MatchExpression;
import edu.kit.iti.formal.psdbg.parser.ast.TermLiteral;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * This calss represents a node in the script tree, whcih can be of different kinds.
 * The scriptTreeNodes is the model calls for TreeNodes
 */

public class ForeachTreeNode extends AbstractTreeNode {
    @Getter
    private final PTreeNode<KeyData> scriptState;

    @Getter
    private final int linenr;

    private final boolean foreachstart;


    public ForeachTreeNode(Node node, PTreeNode<KeyData> scriptState, int linenr, boolean foreachstart) {
        super(node);
        this.scriptState = scriptState;
        this.linenr = linenr;
        this.foreachstart = foreachstart;
    }

    @Override
    public String toString(){
        return scriptState.getStatement().toString()+" with ID "+scriptState.getId();
    }

    @Override
    public TreeNode toTreeNode() {
        String label= (foreachstart)? "foreach in line " + linenr + " START": "foreach in line " + linenr + " END";

        return  new TreeNode(label, getNode());
    }
}