package edu.kit.iti.formal.psdbg.parser.types;

import edu.kit.iti.formal.psdbg.parser.data.Value;

import javax.annotation.Nonnull;

/**
 * @author Alexander Weigl
 * @version 1 (29.08.17)
 */
public interface Function {
    @Nonnull
    String getName();

    @Nonnull
    Type getReturnType();

    default int arity() {
        return getArgumentTypes().length;
    }

    @Nonnull
    Type[] getArgumentTypes();


    Value apply(Value... arguments);
}
