package edu.kit.formal.psdb.termmatcher;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class Matching contains a hashmap of string to term
 */
public class Matchings extends ArrayList<Map<String, MatchPath>> {
    public static Matchings singleton(String name, MatchPath term) {
        Matchings matchings = new Matchings();
        Map<String, MatchPath> va = new TreeMap<>();
        va.put(name, term);
        matchings.add(va);
        return matchings;
    }
}