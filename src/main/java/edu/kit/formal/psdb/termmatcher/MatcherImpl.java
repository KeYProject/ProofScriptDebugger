package edu.kit.formal.psdb.termmatcher;

import de.uka.ilkd.key.logic.Term;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Spliterator;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Alexander Weigl
 */
class MatcherImpl extends MatchPatternDualVisitor<Matchings, Term> {
    static final Matchings NO_MATCH = new Matchings();
    static final Matchings EMPTY_MATCH = Matchings.singleton("EMPTY_MATCH", null);

    private Stack<Term> termStack = new Stack<>();

    @Override
    public Matchings visitDontCare(MatchPatternParser.DontCareContext ctx, Term peek) {
        if (peek != null) {
            return EMPTY_MATCH;
        } else {
            return NO_MATCH;
        }
    }

    /*@Override
    protected Matchings visitStartDontCare(MatchPatternParser.StarDontCareContext ctx, Term peek) {
        if (peek != null) {
            return EMPTY_MATCH;
        } else {
            return NO_MATCH;
        }
    }*/

    @Override
    protected Matchings visitSchemaVar(MatchPatternParser.SchemaVarContext ctx, Term peek) {
        if (peek != null) {
            return Matchings.singleton(ctx.getText(), peek);
        } else {
            return NO_MATCH;
        }
    }

    @Override
    protected Matchings visitAnywhere(MatchPatternParser.AnywhereContext ctx, Term peek) {
        Matchings m = new Matchings();
        subTerms(peek).forEach(sub -> {
            Matchings s = accept(ctx.termPattern(), sub);
            m.addAll(s);
        });
        return m;
    }


    @Override
    protected Matchings visitFunction(MatchPatternParser.FunctionContext ctx, Term peek) {
        String expectedFunction = ctx.func.getText();

        if (peek.op().name().toString().equals(expectedFunction)  // same name
                && ctx.termPattern().size() == peek.arity()       // same arity
                ) {
            return IntStream.range(0, peek.arity())
                    .mapToObj(i -> (Matchings) accept(ctx.termPattern(i), peek.sub(i)))
                    .reduce(this::reduceConform)
                    .orElse(NO_MATCH);
        }
        return NO_MATCH;
    }


    @Override
    protected Matchings visitSemiSeqPattern(MatchPatternParser.SemiSeqPatternContext ctx, Term peek) {
        return null;
    }

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

    /**
     * @param m1
     * @param m2
     * @return
     */
    private Matchings reduceConform(Matchings m1, Matchings m2) {
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

    private HashMap<String, Term> reduceConform(HashMap<String, Term> h1, HashMap<String, Term> h2) {
        HashMap<String, Term> h3 = new HashMap<>(h1);
        for (String s1 : h3.keySet()) {
            if (h2.containsKey(s1) && !h2.get(s1).equals(h1.get(s1))) {
                return null;
            }
        }
        h3.putAll(h2);
        return h3;
    }
}


class Matchings extends ArrayList<HashMap<String, Term>> {
    public static Matchings singleton(String name, Term peek) {
        Matchings matchings = new Matchings();
        HashMap<String, Term> va = new HashMap<>();
        va.put(name, peek);
        matchings.add(va);
        return matchings;
    }
}