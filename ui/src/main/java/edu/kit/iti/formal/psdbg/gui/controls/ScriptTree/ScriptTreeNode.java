package edu.kit.iti.formal.psdbg.gui.controls.ScriptTree;

import de.uka.ilkd.key.proof.Node;
import edu.kit.iti.formal.psdbg.gui.controls.TreeNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.dbg.PTreeNode;
import edu.kit.iti.formal.psdbg.parser.ast.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * This calss represents a node in the script tree, whcih can be of different kinds.
 * The scriptTreeNodes is the model calls for TreeNodes
 */

public class ScriptTreeNode extends AbstractTreeNode {
    @Getter
    private final PTreeNode<KeyData> scriptState;

    @Getter @Setter
    private final int linenr;


    public ScriptTreeNode(Node node, PTreeNode<KeyData> scriptState, int linenr) {
        super(node);
        this.scriptState = scriptState;
        this.linenr = linenr;

    }

    @Override
    public String toString(){
        return scriptState.getStatement().toString()+" with ID "+scriptState.getId();
    }

    @Override
    public TreeNode toTreeNode() {
        String label;
        if (isMatchEx()) {

            String matchexpression = "";
            if (scriptState.getStatement() instanceof GuardedCaseStatement) {
                MatchExpression me = (MatchExpression) ((GuardedCaseStatement) scriptState.getStatement()).getGuard();
                try {
                    matchexpression = ((TermLiteral) me.getPattern()).getContent();
                } catch (ClassCastException cce) {
                    //not a Termliteral but a Stringliteral
                    matchexpression = ((StringLiteral) me.getPattern()).getText();
                }

            } else { //default case statement
                matchexpression = "default";
            }

            label = "match ("+ matchexpression +") in line " + linenr;
        } else {
            label = ((CallStatement)scriptState.getStatement()).getCommand() + " (line " + linenr + ")";

        }

        if (!isSucc()) {
            label += " (failed)";
        }
        return  new TreeNode(label, getNode());
    }
}
