package edu.kit.formal.interpreter.data;

import de.uka.ilkd.key.control.KeYEnvironment;
import de.uka.ilkd.key.proof.Node;
import de.uka.ilkd.key.proof.Proof;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Alexander Weigl
 * @version 1 (28.05.17)
 */
@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class KeyData {
    private Node node;
    private KeYEnvironment env;
    private Proof proof;

    public KeyData(KeyData data, Node node) {
        env = data.env;
        //proofApi = data.proofApi;
        //scriptApi = data.scriptApi;
        this.node = node;
    }


}
