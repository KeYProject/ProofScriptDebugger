package edu.kit.formal.psdb.interpreter.data;

import de.uka.ilkd.key.api.ProjectedNode;
import lombok.Getter;

import java.util.List;
import java.util.Set;

/**
 * @author Alexander Weigl
 * @version 1 (28.05.17)
 */
public class KeyScriptSequent {
    @Getter
    private ProjectedNode projectedNode;


    public Set<List<String>> getLabels() {
        //TODO
        return null;
    }
}