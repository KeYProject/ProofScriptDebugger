package edu.kit.iti.formal.psdbg.interpreter.matcher;

import com.google.common.collect.Sets;
import de.uka.ilkd.key.logic.SequentFormula;
import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.logic.op.QuantifiableVariable;

import java.util.*;

public class MutableMatchings implements Matchings {
    public Set<Match> inner = new TreeSet<>(new VariableAssignmentComparator());

    public static Matchings singleton(String name, MatchPath term) {
        Matchings matchings = new MutableMatchings();
        matchings.add(name,term);
        return matchings;
    }

    public static Matchings emptySingleton() {
        Matchings matchings = new MutableMatchings();
        matchings.add(Match.empty());
        return matchings;
    }

    @Override
    public boolean isNoMatch() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void add(String binder, MatchPath term) {
        add(Match.singleton(binder, term));
    }

    @Override
    public void add(Match matching) {
        inner.add(matching);
    }

    @Override
    public void addAll(Collection<Match> matchings) {
        inner.addAll(matchings);
    }

    @Override
    public Collection<Match> getMatchings() {
        return inner;
    }

    @Override
    public String toString() {
        return inner.toString();
    }

    /**
     * Reduce the matchings by eliminating non-compatible matchings.
     * For example:
     * m1: <X, f(y)>, <Y,g> and m2: <X, g> <Y, f(x)>
     *
     * @param other
     * @return
     */
    @Override
    public Matchings reduceConform(Matchings other) {
        //shortcuts
        if(other.isNoMatch()) return other.reduceConform(this);
        if(other.isEmpty()) return other.reduceConform(this);
        if(this.inner.size() == 0) return other;
        MutableMatchings mother = (MutableMatchings) other;

        MutableMatchings m3 = new MutableMatchings();
        boolean oneMatch = false;

        for (Match h1 : inner) {
            for (Match h2 : mother.inner) {
                Match h3 = reduceConform(h1, h2);
                if (h3 != null) {
                    if (!m3.inner.contains(h3))
                        m3.add(h3);
                    oneMatch = true;
                }
            }
        }
        return oneMatch ? m3 : NoMatch.INSTANCE;
    }


    /**
     *
     * @param h1
     * @param h2
     * @return
     */
    public static Match reduceConform(Match h1, Match h2) {
        Match newMatch = new Match(h1);

        for (String s1 : newMatch.keySet()) {
            if (h2.containsKey(s1)) {
                if (h2.get(s1) instanceof MatchPath.MPQuantifiableVariable &&
                        !((QuantifiableVariable) h2.get(s1).getUnit()).name().toString().equals(h1.get(s1).toString())) {
                    return null;
                }
                if (h1.get(s1) instanceof MatchPath.MPQuantifiableVariable &&
                        !((QuantifiableVariable) h1.get(s1).getUnit()).name().toString().equals(h2.get(s1).toString())) {
                    return null;
                }

                if (!h2.get(s1).equals(h1.get(s1))) {
                    return null;
                }
            }
        }
        newMatch.putAll(h2);
        return newMatch;
    }



class VariableAssignmentComparator implements Comparator<Match> {
    /**
     * <ol>
     * <li>both maps contains the same keys</li>
     * <li>foreach key in lexi-order, the depth has to be greater</li>
     * </ol>
     *
     * @return
     */
    @Override
    public int compare(Match o1, Match o2) {
        if (isTrueSubset(o1.keySet(), o2.keySet())) {
            return 1;
        }
        if (isTrueSubset(o2.keySet(), o1.keySet())) {
            return -1;
        }

        if (!o1.keySet().equals(o2.keySet())) {
            // different domains,
            // there exists at least one variable that is not assign in the other
            int cmp = Integer.compare(o1.size(), o2.size());
            if (cmp != 0) {
                return cmp;
            } else {
                return compareVariableName(o1, o2);
            }
        }

        ArrayList<String> keys = new ArrayList<>(Sets.intersection(o1.keySet(), o2.keySet()));
        keys.sort(String::compareTo); // order of the traversal
        keys.remove("EMPTY_MATCH");

        for (String k : keys) {
            int depthA = o1.get(k).depth();
            int depthB = o2.get(k).depth();
            int cmp = Integer.compare(depthA, depthB);
            if (cmp != 0)
                return cmp;
        }
        // all terms same depth: now let the lexi-order decide
        for (String k : keys) {
            int cmp = o1.get(k).toString().compareTo(o2.get(k).toString());
            if (cmp != 0)
                return cmp;
        }
        return 0;
    }

    private int compareVariableName(Match o1, Match o2) {
        return variableNames(o1).compareTo(variableNames(o2));
    }

    private String variableNames(Match va) {
        return va.keySet().stream().reduce((a, b) -> a + '#' + b).orElse("#");
    }

    /**
     * @param a
     * @param b
     * @return
     */
    private boolean isTrueSubset(Set<String> a, Set<String> b) {
        return b.containsAll(a) && !a.containsAll(b);
    }

}
}
