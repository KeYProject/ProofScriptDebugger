package edu.kit.formatl.proofscriptparser.ast;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * @author Alexander Weigl
 * @version 1 (27.04.17)
 */
public class Statements {
    private List<Statement> statements = new ArrayList<>();

    public int size() {
        return statements.size();
    }

    public boolean isEmpty() {
        return statements.isEmpty();
    }

    public boolean contains(Object o) {
        return statements.contains(o);
    }

    public Iterator<Statement> iterator() {
        return statements.iterator();
    }

    public Object[] toArray() {
        return statements.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return statements.toArray(a);
    }

    public boolean add(Statement statement) {
        return statements.add(statement);
    }

    public boolean remove(Object o) {
        return statements.remove(o);
    }

    public boolean containsAll(Collection<?> c) {
        return statements.containsAll(c);
    }

    public boolean addAll(Collection<? extends Statement> c) {
        return statements.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends Statement> c) {
        return statements.addAll(index, c);
    }

    public boolean removeAll(Collection<?> c) {
        return statements.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return statements.retainAll(c);
    }

    public void replaceAll(UnaryOperator<Statement> operator) {
        statements.replaceAll(operator);
    }

    public void sort(Comparator<? super Statement> c) {
        statements.sort(c);
    }

    public void clear() {
        statements.clear();
    }

    public Statement get(int index) {
        return statements.get(index);
    }

    public Statement set(int index, Statement element) {
        return statements.set(index, element);
    }

    public void add(int index, Statement element) {
        statements.add(index, element);
    }

    public Statement remove(int index) {
        return statements.remove(index);
    }

    public int indexOf(Object o) {
        return statements.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return statements.lastIndexOf(o);
    }

    public ListIterator<Statement> listIterator() {
        return statements.listIterator();
    }

    public ListIterator<Statement> listIterator(int index) {
        return statements.listIterator(index);
    }

    public List<Statement> subList(int fromIndex, int toIndex) {
        return statements.subList(fromIndex, toIndex);
    }

    public Spliterator<Statement> spliterator() {
        return statements.spliterator();
    }

    public boolean removeIf(Predicate<? super Statement> filter) {
        return statements.removeIf(filter);
    }

    public Stream<Statement> stream() {
        return statements.stream();
    }

    public Stream<Statement> parallelStream() {
        return statements.parallelStream();
    }

    public void forEach(Consumer<? super Statement> action) {
        statements.forEach(action);
    }
}
