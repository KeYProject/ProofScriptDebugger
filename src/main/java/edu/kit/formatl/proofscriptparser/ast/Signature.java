package edu.kit.formatl.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formatl.proofscriptparser.Visitor;

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
public class Signature extends ASTNode<ScriptLanguageParser.ArgListContext> implements Map<Variable, String> {
    private final Map<Variable, String> sig = new LinkedHashMap<>();

    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public ASTNode<ScriptLanguageParser.ArgListContext> clone() {
        return null;
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

    public String get(Object key) {
        return sig.get(key);
    }

    public String put(Variable key, String value) {
        return sig.put(key, value);
    }

    public String remove(Object key) {
        return sig.remove(key);
    }

    public void putAll(Map<? extends Variable, ? extends String> m) {
        sig.putAll(m);
    }

    public void clear() {
        sig.clear();
    }

    public Set<Variable> keySet() {
        return sig.keySet();
    }

    public Collection<String> values() {
        return sig.values();
    }

    public Set<Map.Entry<Variable, String>> entrySet() {
        return sig.entrySet();
    }

    public String getOrDefault(Object key, String defaultValue) {
        return sig.getOrDefault(key, defaultValue);
    }

    public void forEach(BiConsumer<? super Variable, ? super String> action) {
        sig.forEach(action);
    }

    public void replaceAll(BiFunction<? super Variable, ? super String, ? extends String> function) {
        sig.replaceAll(function);
    }

    public String putIfAbsent(Variable key, String value) {
        return sig.putIfAbsent(key, value);
    }

    public boolean remove(Object key, Object value) {
        return sig.remove(key, value);
    }

    public boolean replace(Variable key, String oldValue, String newValue) {
        return sig.replace(key, oldValue, newValue);
    }

    public String replace(Variable key, String value) {
        return sig.replace(key, value);
    }

    public String computeIfAbsent(Variable key, Function<? super Variable, ? extends String> mappingFunction) {
        return sig.computeIfAbsent(key, mappingFunction);
    }

    public String computeIfPresent(Variable key,
            BiFunction<? super Variable, ? super String, ? extends String> remappingFunction) {
        return sig.computeIfPresent(key, remappingFunction);
    }

    public String compute(Variable key,
            BiFunction<? super Variable, ? super String, ? extends String> remappingFunction) {
        return sig.compute(key, remappingFunction);
    }

    public String merge(Variable key, String value,
            BiFunction<? super String, ? super String, ? extends String> remappingFunction) {
        return sig.merge(key, value, remappingFunction);
    }
}
