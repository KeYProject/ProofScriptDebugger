package edu.kit.formal.psdb.termmatcher;

import de.uka.ilkd.key.logic.SequentFormula;
import de.uka.ilkd.key.logic.Term;
import org.antlr.v4.runtime.Token;
import org.key_project.util.collection.ImmutableArray;

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
            if (!s1.equals("EMPTY_MATCH") && (h2.containsKey(s1) && !h2.get(s1).equals(h1.get(s1)))) {
                return null;
            }
        }

        h3.putAll(h2);
        return h3;
    }

    /**
     * Visit a semisequent pattern f(x), f(y)
     * @param ctx
     * @param peek
     * @return
     */
    @Override
    protected Matchings visitSemiSeqPattern(MatchPatternParser.SemiSeqPatternContext ctx, Term peek) {
        return null;
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

    @Override
    protected Matchings visitBinaryOperation(MatchPatternParser.BinaryOperationContext ctx, Term peek) {
        Token op = ctx.op;
        String opToKeYName = convert(op.getType());

        MatchPatternParser.TermPatternContext left = ctx.left;
        MatchPatternParser.TermPatternContext right = ctx.right;

        if (peek.op().name().toString().equals(opToKeYName)  // same name
                && peek.arity() == 2   // we know that we are in binary term
                ) {
            Matchings lM = accept(left, peek.sub(0));
            Matchings rM = accept(right, peek.sub(1));
            Matchings ret = reduceConform(lM, rM);
            return ret;
            /*return IntStream.range(0, peek.arity())
                    .mapToObj(i -> (Matchings) accept(ctx.termPattern(i), peek.sub(i)))
                    .reduce(MatcherImpl::reduceConform)
                    .orElse(NO_MATCH);*/
        }
        return NO_MATCH;


    }

    private String convert(int op) {

        switch (op) {

            case MatchPatternParser.PLUS:
                return "add";

            case MatchPatternParser.MINUS:
                return "sub";

            case MatchPatternParser.MUL:
                return "mul";


            case MatchPatternParser.DIV:
                return "div";


            case MatchPatternParser.LE:
                return "lt";


            case MatchPatternParser.LEQ:
                return "leq";


            case MatchPatternParser.EQ:
                return "equals";

            case MatchPatternParser.GE:
                return "gt";

            case MatchPatternParser.GEQ:
                return "geq";

            case MatchPatternParser.IMP:
                return "imp";


            case MatchPatternParser.AND:
                return "and";

            case MatchPatternParser.OR:
                return "or";

            default:
                throw new UnsupportedOperationException("The operator " + op + "is not known");
        }


    }

    /**
     * Reduce the matchings by eliminating non-compatible matchings.
     * For example:
     * m1: <X, f(y)>, <Y,g> and m2: <X, g> <Y, f(x)>
     *
     * @param m1
     * @param m2
     * @return
     */
    protected static Matchings reduceConform(Matchings m1, Matchings m2) {
        //shortcuts
        if (m1 == NO_MATCH || m2 == NO_MATCH) return NO_MATCH;
        if (m1 == EMPTY_MATCH) return m2;
        if (m2 == EMPTY_MATCH) return m1;

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
                    .orElse(EMPTY_MATCH);
        }
        return NO_MATCH;
    }

    @Override
    protected Matchings visitNumber(MatchPatternParser.NumberContext ctx, Term peek) {
        //we are at a natural number
        if (peek.op().name().toString().equals("Z")) {
            ImmutableArray<Term> subs = peek.subs();
            int transformedString = transformToNumber(peek.sub(0));

            return EMPTY_MATCH;
        } else {
            return NO_MATCH;
        }


    }

    private int transformToNumber(Term sub) {
        int number = 0;

        return 0;
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