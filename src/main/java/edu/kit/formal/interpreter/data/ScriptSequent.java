package edu.kit.formal.interpreter.data;

import java.util.List;
import java.util.Set;

/**
 * @author Alexander Weigl
 * @version 1 (28.05.17)
 */
@Deprecated
public interface ScriptSequent {
    Set<List<String>> getLabels();
}
