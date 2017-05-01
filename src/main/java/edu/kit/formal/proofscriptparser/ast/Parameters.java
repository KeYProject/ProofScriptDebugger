package edu.kit.formal.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formal.proofscriptparser.Visitor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Alexander Weigl
 * @version 1 (29.04.17)
 */
@EqualsAndHashCode
@ToString
public class Parameters extends ASTNode<ScriptLanguageParser.ParametersContext> {
    private final Map<Variable, Expression> parameters = new LinkedHashMap<>();

    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public Parameters clone() {
        Parameters p = new Parameters();
        forEach((k, v) -> p.put(k.clone(), v.clone()));
        return p;
    }

    public int size() {
        return parameters.size();
    }

    public boolean isEmpty() {
        return parameters.isEmpty();
    }

    public boolean containsKey(Object key) {
        return parameters.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return parameters.containsValue(value);
    }

    public Expression get(Object key) {
        return parameters.get(key);
    }

    public Expression put(Variable key, Expression value) {
        return parameters.put(key, value);
    }

    public Expression remove(Object key) {
        return parameters.remove(key);
    }

    public void putAll(Map<? extends Variable, ? extends Expression> m) {
        parameters.putAll(m);
    }

    public void clear() {
        parameters.clear();
    }

    public Set<Variable> keySet() {
        return parameters.keySet();
    }

    public Collection<Expression> values() {
        return parameters.values();
    }

    public Set<Map.Entry<Variable, Expression>> entrySet() {
        return parameters.entrySet();
    }

    public Expression getOrDefault(Object key, Expression defaultValue) {
        return parameters.getOrDefault(key, defaultValue);
    }

    public void forEach(BiConsumer<? super Variable, ? super Expression> action) {
        parameters.forEach(action);
    }

    public void replaceAll(BiFunction<? super Variable, ? super Expression, ? extends Expression> function) {
        parameters.replaceAll(function);
    }

    public Expression putIfAbsent(Variable key, Expression value) {
        return parameters.putIfAbsent(key, value);
    }

    public boolean remove(Object key, Object value) {
        return parameters.remove(key, value);
    }

    public boolean replace(Variable key, Expression oldValue, Expression newValue) {
        return parameters.replace(key, oldValue, newValue);
    }

    public Expression replace(Variable key, Expression value) {
        return parameters.replace(key, value);
    }

    public Expression computeIfAbsent(Variable key, Function<? super Variable, ? extends Expression> mappingFunction) {
        return parameters.computeIfAbsent(key, mappingFunction);
    }

    public Expression computeIfPresent(Variable key,
            BiFunction<? super Variable, ? super Expression, ? extends Expression> remappingFunction) {
        return parameters.computeIfPresent(key, remappingFunction);
    }

    public Expression compute(Variable key,
            BiFunction<? super Variable, ? super Expression, ? extends Expression> remappingFunction) {
        return parameters.compute(key, remappingFunction);
    }

    public Expression merge(Variable key, Expression value,
            BiFunction<? super Expression, ? super Expression, ? extends Expression> remappingFunction) {
        return parameters.merge(key, value, remappingFunction);
    }

}
