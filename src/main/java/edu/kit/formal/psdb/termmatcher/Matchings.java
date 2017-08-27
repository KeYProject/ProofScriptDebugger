package edu.kit.formal.psdb.termmatcher;

import com.google.common.collect.Sets;

import java.util.*;

/**
 * Class Matching contains a hashmap of string to term
 */
public class Matchings extends TreeSet<Map<String, MatchPath>> {
    public Matchings() {
        super(new VariableAssignmentComparator());
    }

    public static Matchings singleton(String name, MatchPath term) {
        Matchings matchings = new Matchings();
        Map<String, MatchPath> va = new TreeMap<>();
        va.put(name, term);
        matchings.add(va);
        return matchings;
    }

}

class VariableAssignmentComparator implements Comparator<Map<String, MatchPath>> {
    /**
     * <ol>
     * <li>both maps contains the same keys</li>
     * <li>foreach key in lexi-order, the depth has to be greater</li>
     * </ol>
     *
     * @return
     */
    @Override
    public int compare(Map<String, MatchPath> o1, Map<String, MatchPath> o2) {
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

    private int compareVariableName(Map<String, MatchPath> o1, Map<String, MatchPath> o2) {
        return variableNames(o1).compareTo(variableNames(o2));
    }

    private String variableNames(Map<String, MatchPath> va) {
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