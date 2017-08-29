package edu.kit.iti.formal.psdbg.parser.types;

import edu.kit.iti.formal.psdbg.parser.data.Value;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alexander Weigl
 * @version 1 (29.08.17)
 */
public class FunctionExtractor {
    public <T> Collection<Function> getFunctions(Class<T> clazz) {
        List<Function> rt = new ArrayList<>(clazz.getMethods().length);
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.isAnnotationPresent(ScriptFunction.class)
                    && m.isAccessible()
                    && Modifier.isStatic(m.getModifiers())) {
                ScriptFunction sf = m.getAnnotation(ScriptFunction.class);
                Type returnType = TypeFacade.findType(sf.rtype());
                List<Type> args = Arrays.stream(sf.atypes())
                        .map(TypeFacade::findType)
                        .collect(Collectors.toList());
                Function func = new FunctionByReflection(
                        sf.value(), returnType, args, m);
                rt.add(func);
            }
        }
        return rt;
    }

    /**
     *
     */
    public @interface ScriptFunction {
        String value();

        String rtype();

        String[] atypes();
    }

    @AllArgsConstructor
    private static class FunctionByReflection implements Function {
        @Getter
        private final String name;
        @Getter
        private final Type returnType;
        @Getter
        private final List<Type> args;
        @Getter
        private final Method m;

        @Nonnull
        @Override
        public Type[] getArgumentTypes() {
            return (Type[]) args.toArray();
        }

        @Override
        public Value apply(Value[] arguments) {
            try {
                return (Value) m.invoke(arguments);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
