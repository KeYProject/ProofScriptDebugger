package edu.kit.formal.interpreter.data;

import edu.kit.formal.interpreter.data.ScriptSequent;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Alexander Weigl
 * @version 1 (28.05.17)
 */
@Deprecated
public class StringScriptSequent implements ScriptSequent {
    @Getter
    private String sequent;

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public Set<List<String>> getLabels() {
        return Collections.singleton(Collections.singletonList(sequent));
    }
}
