package edu.kit.formatl.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formatl.proofscriptparser.Visitor;
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
@EqualsAndHashCode(callSuper = false, of="sig")
@ToString
public class Signature extends ASTNode<ScriptLanguageParser.ArgListContext> implements Map<Variable, Type> {
    private final Map<Variable, Type> sig = new LinkedHashMap<>();

    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public Signature clone() {
        Signature signature = new Signature();
        forEach((k, v) -> signature.put(k.clone(), v));
        return signature;
    }

    public int size() {
        return sig.size();
    }

    public boolean isEmpty() {
        return sig.isEmpty();
    }

    public boolean containsKey(Object key) {
        return sig.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return sig.containsValue(value);
    }

    public Type get(Object key) {
        return sig.get(key);
    }


    public Type get(Variable key) {
        return sig.get(key);
    }

    public Type put(Variable key, Type value) {
        return sig.put(key, value);
    }

    public Type remove(Object key) {
        return sig.remove(key);
    }

    public void putAll(Map<? extends Variable, ? extends Type> m) {
        sig.putAll(m);
    }

    public void clear() {
        sig.clear();
    }

    public Set<Variable> keySet() {
        return sig.keySet();
    }

    public Collection<Type> values() {
        return sig.values();
    }

    public Set<Map.Entry<Variable, Type>> entrySet() {
        return sig.entrySet();
    }

    public Type getOrDefault(Object key, Type defaultValue) {
        return sig.getOrDefault(key, defaultValue);
    }

    public void forEach(BiConsumer<? super Variable, ? super Type> action) {
        sig.forEach(action);
    }

    public void replaceAll(BiFunction<? super Variable, ? super Type, ? extends Type> function) {
        sig.replaceAll(function);
    }

    public Type putIfAbsent(Variable key, Type value) {
        return sig.putIfAbsent(key, value);
    }

    public boolean remove(Object key, Object value) {
        return sig.remove(key, value);
    }

    public boolean replace(Variable key, Type oldValue, Type newValue) {
        return sig.replace(key, oldValue, newValue);
    }

    public Type replace(Variable key, Type value) {
        return sig.replace(key, value);
    }

    public Type computeIfAbsent(Variable key, Function<? super Variable, ? extends Type> mappingFunction) {
        return sig.computeIfAbsent(key, mappingFunction);
    }

    public Type computeIfPresent(Variable key,
            BiFunction<? super Variable, ? super Type, ? extends Type> remappingFunction) {
        return sig.computeIfPresent(key, remappingFunction);
    }

    public Type compute(Variable key,
            BiFunction<? super Variable, ? super Type, ? extends Type> remappingFunction) {
        return sig.compute(key, remappingFunction);
    }

    public Type merge(Variable key, Type value,
            BiFunction<? super Type, ? super Type, ? extends Type> remappingFunction) {
        return sig.merge(key, value, remappingFunction);
    }
}
