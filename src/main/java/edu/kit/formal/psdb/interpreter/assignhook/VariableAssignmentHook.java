package edu.kit.formal.psdb.interpreter.assignhook;

import edu.kit.formal.psdb.interpreter.data.Value;
import edu.kit.formal.psdb.interpreter.data.VariableAssignment;

/**
 * @author Alexander Weigl
 * @version 1 (21.08.17)
 */
public interface VariableAssignmentHook<T> {
    /**
     * @param data
     * @param variableName
     * @param value
     * @param <S>
     */
    <S> boolean handleAssignment(T data, String variableName, Value<S> value);

    /**
     * @param data
     * @return
     */
    VariableAssignment getStartAssignment(T data);
}
