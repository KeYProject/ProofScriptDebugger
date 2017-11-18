package edu.kit.iti.formal.psdbg.interpreter.assignhook;

import edu.kit.iti.formal.psdbg.interpreter.Interpreter;
import edu.kit.iti.formal.psdbg.parser.data.Value;
import lombok.Getter;
import lombok.Setter;

public class InterpreterOptionsHook<T> extends DefaultAssignmentHook<T> {
    @Getter
    @Setter
    private Interpreter<T> interpreter;

    public InterpreterOptionsHook(Interpreter<T> interpreter) {
        this.interpreter = interpreter;


        register("__MAX_ITERATIONS_REPEAT",
                (T data, Value v) -> {
                    interpreter.setMaxIterationsRepeat((Integer) v.getData());
                    return true;
                },
                (T data) -> Value.from(interpreter.getMaxIterationsRepeat())
        );


        register("__STRICT_MODE",
                (T data, Value v) -> {
                    interpreter.setStrictMode((boolean) v.getData());
                    return true;
                },
                (T data) -> Value.from(interpreter.isStrictMode())
        );

    }
}
