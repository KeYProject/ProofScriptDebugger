package edu.kit.formal.interpreter.data;

import de.uka.ilkd.key.control.KeYEnvironment;
import de.uka.ilkd.key.proof.Goal;
import de.uka.ilkd.key.proof.Node;
import de.uka.ilkd.key.proof.Proof;
import lombok.*;

import java.util.function.Function;

/**
 * @author Alexander Weigl
 * @author S. Grebing
 * @version 2 (24.07.17)
 */
@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class KeyData {
    private static final String SEPARATOR = " // ";
    private final KeYEnvironment env;
    private final Proof proof;
    private Node node;

    private String branchingLabel,
            ruleLabel,
            programLinesLabel,
            programStatementsLabel,
            nameLabel;
    private Goal goal;

    public KeyData(KeyData data, Goal node) {
        env = data.env;
        //proofApi = data.proofApi;
        //scriptApi = data.scriptApi;
        this.proof = data.proof;
        this.goal = node;
        this.node = goal.node();
    }

    public KeyData(Goal g, KeYEnvironment environment, Proof proof) {
        goal = g;
        env = environment;
        this.proof = proof;
    }

    public KeyData(Node root, KeYEnvironment environment, Proof proof) {
        this(proof.getGoal(root), environment, proof);
        node = root;
    }

    public KeyData(KeyData kd, Node node) {
        this(node, kd.getEnv(),  kd.getProof());
    }

    /**
     * Return rule name of rule applied to this node
     *
     * @return String representation of applied rule name
     */
    public String getRuleLabel() {
        if (ruleLabel == null) {
            ruleLabel = constructLabel((Node n) -> n.getAppliedRuleApp().rule().name().toString());
        }
        return ruleLabel;
    }

    /**
     * Create Label for goalview according to function that is passed.
     * The following fucntions can be given:
     * <ul>
     *     <li>@see  #Method getRuleLabel()</li>
     *     <li>@see  #Method getBranchingLabel()</li>
     *     <li>@see  #Method getNameLabel()</li>
     *     <li>@see  #Method getProgramLinesLabel()</li>
     *     <li>@see  #Method getProgramStatementsLabel()</li>
     * </ul>
     * @param projection function determining which kind of label to construct
     * @return Label from this node to parent
     */
    private String constructLabel(Function<Node, String> projection) {
        StringBuilder sb = new StringBuilder();
        Node cur = getNode();
        do {
            try {
                String section = projection.apply(cur);
                //filter null elements and -1 elements
                if (section != null && !(section.equals(Integer.toString(-1)))) {
                    sb.append(section);
                    sb.append(SEPARATOR);

                }
            } catch (Exception e) {
            }
            cur = cur.parent();
        } while (cur != null);
        sb.append("$$");
        return sb.toString();
    }

    /**
     * Get branching label of node from KeY
     * @return label of this node determining the branching labels in KeY
     */
    public String getBranchingLabel() {
        if (branchingLabel == null) {
            branchingLabel = constructLabel(n -> n.getNodeInfo().getBranchLabel());
        }
        return branchingLabel;
    }

    /**
     * Get label with node name from KeY (often applied rulename)
     * @return name of this node
     */
    public String getNameLabel() {
        if (nameLabel == null) {
            nameLabel = constructLabel(Node::name);
        }
        return nameLabel;
    }

    /**
     * Get lines of active statement
     * @return line of active satetment in orginal file (-1 if rewritten by KeY or not applicable)
     */
    public String getProgramLinesLabel() {
        if (programLinesLabel == null) {
            programLinesLabel = constructLabel(n ->
                    Integer.toString(n.getNodeInfo().getActiveStatement().getPositionInfo().getStartPosition().getLine())
            );
        }
        return programLinesLabel;
    }

    /**
     * Get first activestatement for node
     * @return
     */
    public String getProgramStatementsLabel() {
        if (programStatementsLabel == null) {
            programStatementsLabel = constructLabel(n ->
                    n.getNodeInfo().getFirstStatementString());
        }

        return programStatementsLabel;
    }


    public Goal getGoal() {
        return goal;
    }

    public Node getNode() {
        return node;
        //return getGoal().node();
    }
}
