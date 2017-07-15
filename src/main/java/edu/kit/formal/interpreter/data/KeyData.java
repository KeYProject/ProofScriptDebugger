package edu.kit.formal.interpreter.data;

import de.uka.ilkd.key.control.KeYEnvironment;
import de.uka.ilkd.key.proof.Goal;
import de.uka.ilkd.key.proof.Node;
import de.uka.ilkd.key.proof.Proof;
import lombok.*;

import java.util.function.Function;

/**
 * @author Alexander Weigl
 * @version 1 (28.05.17)
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

    private String branchingLabel,
            ruleLabel,
            programLinesLabel,
            programStatementsLabel,
            nameLabel;

    private Node node;
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

    public String getRuleLabel() {
        if (ruleLabel == null) {
            ruleLabel = constructLabel((Node n) -> n.getAppliedRuleApp().rule().name().toString());
        }
        return ruleLabel;
    }

    private String constructLabel(Function<Node, String> projection) {
        StringBuilder sb = new StringBuilder();
        Node cur = getNode();
        do {
            try {
                String section = projection.apply(getNode());
                if (section != null) {
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

    public String getBranchingLabel() {
        if (branchingLabel == null) {
            branchingLabel = constructLabel(n -> n.getNodeInfo().getBranchLabel());
        }
        return branchingLabel;
    }

    public String getNameLabel() {
        if (nameLabel == null) {
            nameLabel = constructLabel(Node::name);
        }
        return nameLabel;
    }


    public String getProgramLinesLabel() {
        if (programLinesLabel == null) {
            programLinesLabel = constructLabel(n ->
                    Integer.toString(n.getNodeInfo().getExecStatementPosition().getLine()));
        }
        return programLinesLabel;
    }

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
        return getGoal().node();
    }
}
