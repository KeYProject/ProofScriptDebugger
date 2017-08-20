package edu.kit.formal.psdb.termmatcher;

import de.uka.ilkd.key.logic.SequentFormula;
import de.uka.ilkd.key.logic.Term;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Matchpattern visitor visits the matchpatterns of case-statements
 * @author Alexander Weigl
 * @author S. Grebing
 */
class MatcherImpl extends MatchPatternDualVisitor<Matchings, Term> {
    static final Matchings NO_MATCH = new Matchings();
    static final Matchings EMPTY_MATCH = Matchings.singleton("EMPTY_MATCH", null);

    private Stack<Term> termStack = new Stack<>();

    protected static List<MatchInfo> reduceConform(List<MatchInfo> m1, List<MatchInfo> m2) {
        if (m1 == null || m2 == null) return null; //"null" is equivalent to NO_MATCH

        List<MatchInfo> res = new ArrayList<>();
        boolean oneMatch = false;
        for (MatchInfo minfo1 : m1) {
            for (MatchInfo minfo2 : m2) {

                Set<SequentFormula> intersection = new HashSet<>(minfo1.matchedForms);
                intersection.retainAll(minfo2.matchedForms);
                if (!intersection.isEmpty()) continue;

                HashMap<String, Term> h3 = reduceConform(minfo1.matching, minfo2.matching);
                if (h3 != null) {
                    Set<SequentFormula> union = new HashSet<>(minfo1.matchedForms);
                    union.addAll(minfo2.matchedForms);
                    res.add(new MatchInfo(h3, union));
                    oneMatch = true;
                }
            }
        }
        return oneMatch ? res : null;
    }

    private static HashMap<String, Term> reduceConform(HashMap<String, Term> h1, HashMap<String, Term> h2) {
        HashMap<String, Term> h3 = new HashMap<>(h1);
        for (String s1 : h3.keySet()) {
            if (h2.containsKey(s1) && !h2.get(s1).equals(h1.get(s1))) {
                return null;
            }
        }
        h3.putAll(h2);
        return h3;
    }

    /**
     * Reduce the matchings by eliminating non-compatible matchings.
     * For example:
     * m1: <X, f(y)>, <Y,g> and m2: <X, g> <Y, f(x)>
     * @param m1
     * @param m2
     * @return
     */
    protected static Matchings reduceConform(Matchings m1, Matchings m2) {
        if (m1 == NO_MATCH || m2 == NO_MATCH) return NO_MATCH;

        Matchings m3 = new Matchings();
        boolean oneMatch = false;
        for (HashMap<String, Term> h1 : m1) {
            for (HashMap<String, Term> h2 : m2) {
                HashMap<String, Term> h3 = reduceConform(h1, h2);
                if (h3 != null) {
                    m3.add(h3);
                    oneMatch = true;
                }
            }
        }
        return oneMatch ? m3 : NO_MATCH;
    }

    /*@Override
    protected Matchings visitStartDontCare(MatchPatternParser.StarDontCareContext ctx, Term peek) {
        if (peek != null) {
            return EMPTY_MATCH;
        } else {
            return NO_MATCH;
        }
    }*/

    /**
     * Visit '_'
     *
     * @param ctx
     * @param peek
     * @return
     */
    @Override
    public Matchings visitDontCare(MatchPatternParser.DontCareContext ctx, Term peek) {
        if (peek != null) {
            return EMPTY_MATCH;
        } else {
            return NO_MATCH;
        }
    }

    /**
     * Visit a Schema Variable
     *
     * @param ctx
     * @param peek
     * @return
     */
    @Override
    protected Matchings visitSchemaVar(MatchPatternParser.SchemaVarContext ctx, Term peek) {
        if (peek != null) {
            return Matchings.singleton(ctx.getText(), peek);
        } else {
            return NO_MATCH;
        }
    }

    /**
     * Visit a '...'Term'...' structure
     * @param ctx
     * @param peek
     * @return
     */
    @Override
    protected Matchings visitAnywhere(MatchPatternParser.AnywhereContext ctx, Term peek) {
        Matchings m = new Matchings();
        subTerms(peek).forEach(sub -> {
            Matchings s = accept(ctx.termPattern(), sub);
            m.addAll(s);
        });
        return m;
    }

    /**
     * Visit a function and predicate symbol without a sequent arrow
     * @param ctx
     * @param peek
     * @return
     */
    @Override
    protected Matchings visitFunction(MatchPatternParser.FunctionContext ctx, Term peek) {
        String expectedFunction = ctx.func.getText();
        if (peek.op().name().toString().equals(expectedFunction)  // same name
                && ctx.termPattern().size() == peek.arity()       // same arity
                ) {
            return IntStream.range(0, peek.arity())
                    .mapToObj(i -> (Matchings) accept(ctx.termPattern(i), peek.sub(i)))
                    .reduce(MatcherImpl::reduceConform)
                    .orElse(NO_MATCH);
        }
        return NO_MATCH;
    }

    /**
     * Visit a semisequent pattern f(x), f(y)
     * @param ctx
     * @param peek
     * @return
     */
    @Override
    protected Matchings visitSemiSeqPattern(MatchPatternParser.SemiSeqPatternContext ctx, Term peek) {
        /*Matchings m = new Matchings();
        List<MatchPatternParser.TermPatternContext> termPatterns = ctx.termPattern();
        for (MatchPatternParser.TermPatternContext termPattern : termPatterns) {
            System.out.println("Searching for "+termPattern.getText()+" and "+peek.toString());
            Matchings m1 = accept(termPattern,peek);

        }*/
        return null;
    }

    /**
     * Visit a sequent pattern 'f(x) ==> f(x)', 'f(x) ==>' or '==> f(x)'
     * @param ctx
     * @param peek
     * @return
     */
    @Override
    protected Matchings visitSequentPattern(MatchPatternParser.SequentPatternContext ctx, Term peek) {
        return null;
    }

    private Stream<Term> subTerms(Term peek) {
        ArrayList<Term> list = new ArrayList<>();
        subTerms(list, peek);
        return list.stream();
    }

    private void subTerms(ArrayList<Term> list, Term peek) {
        list.add(peek);
        for (Term s : peek.subs()) {
            subTerms(list, s);
        }
    }

    public static class MatchInfo {
        public HashMap<String, Term> matching;
        public Set<SequentFormula> matchedForms;

        public MatchInfo(HashMap<String, Term> m, Set<SequentFormula> f) {
            matching = m;
            matchedForms = f;
        }
    }
}

/**
 * Class Matching contains a hashmap of string to term
 */
class Matchings extends ArrayList<HashMap<String, Term>> {
    public static Matchings singleton(String name, Term peek) {
        Matchings matchings = new Matchings();
        HashMap<String, Term> va = new HashMap<>();
        va.put(name, peek);
        matchings.add(va);
        return matchings;
    }
}