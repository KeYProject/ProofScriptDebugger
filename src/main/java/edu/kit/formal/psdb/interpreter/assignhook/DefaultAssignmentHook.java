package edu.kit.formal.psdb.interpreter.assignhook;

import edu.kit.formal.psdb.interpreter.data.Value;
import edu.kit.formal.psdb.interpreter.data.VariableAssignment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Alexander Weigl
 * @version 1 (21.08.17)
 */
public abstract class DefaultAssignmentHook<T> implements VariableAssignmentHook<T> {
    private static Logger logger = LogManager.getLogger(KeyAssignmentHook.class);

    private Map<String, Function<T, Value>> initFunctions = new HashMap<>();
    private Map<String, BiFunction<T, Value, Boolean>> hooks = new HashMap<>();


    protected void register(String varName, BiFunction<T, Value, Boolean> setter, Function<T, Value> getter) {
        initFunctions.put(varName, getter);
        hooks.put(varName, setter);
    }

    @Override
    public <S> boolean handleAssignment(T data, String variableName, Value<S> value) {
        if (hooks.containsKey(variableName)) {
            return hooks.get(variableName).apply(data, value);
        }
        return true;
    }


    @Override
    public VariableAssignment getStartAssignment(T data) {
        VariableAssignment va = new VariableAssignment();
        for (Map.Entry<String, Function<T, Value>> s : initFunctions.entrySet()) {
            Value apply = s.getValue().apply(data);
            va.declare(s.getKey(), apply.getType());
            va.assign(s.getKey(), apply);
        }
        return va;
    }

}
