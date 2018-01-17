package edu.kit.iti.formal.psdbg.termmatcher;

import com.google.common.collect.Sets;
import de.uka.ilkd.key.logic.Semisequent;
import de.uka.ilkd.key.logic.SequentFormula;
import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.logic.op.QuantifiableVariable;
import edu.kit.formal.psdb.termmatcher.MatchPatternLexer;
import edu.kit.formal.psdb.termmatcher.MatchPatternParser;
import edu.kit.iti.formal.psdbg.termmatcher.mp.MatchPath;
import static edu.kit.iti.formal.psdbg.termmatcher.mp.MatchPathFacade.*;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Token;
import org.key_project.util.collection.ImmutableArray;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Matchpattern visitor visits the matchpatterns of case-statements
 *
 * @author Alexander Weigl
 * @author S. Grebing
 */
class MatcherImpl extends MatchPatternDualVisitor<Matchings, MatchPath> {
    static final Matchings NO_MATCH = new Matchings();

    static final Matchings EMPTY_MATCH = Matchings.singleton("EMPTY_MATCH", null);

    static final Map<String, MatchPath> EMPTY_VARIABLE_ASSIGNMENT = EMPTY_MATCH.first();

    Random random = new Random(42L);

    /*
     * Reduce two matchinfos
     *
     * @param m1
     * @param m2
     * @return
     *
    protected static List<MatchInfo> reduceConform(List<MatchInfo> m1, List<MatchInfo> m2) {
        if (m1 == null || m2 == null) return null; //"null" is equivalent to NO_MATCH

        List<MatchInfo> res = new ArrayList<>();
        boolean oneMatch = false;
        for (MatchInfo minfo1 : m1) {
            for (MatchInfo minfo2 : m2) {

                Set<SequentFormula> intersection = new HashSet<>(minfo1.matchedForms);
                intersection.retainAll(minfo2.matchedForms);
                if (!intersection.isEmpty()) continue;
                HashMap<String, MatchPath> h3 = reduceConform(minfo1.matching, minfo2.matching);

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
    */
    private List<Integer> currentPosition = new ArrayList<>();

    /**
     * If true, we assume every term in the pattern has a binder.
     * The binding names are generated.
     *
     * @see #handleBindClause(MatchPatternParser.BindClauseContext, MatchPath, Matchings)
     */
    @Getter
    @Setter
    private boolean catchAll = false;

    private static HashMap<String, MatchPath> reduceConform(Map<String, MatchPath> h1, Map<String, MatchPath> h2) {
        HashMap<String, MatchPath> listOfElementsofH1 = new HashMap<>(h1);

        for (String s1 : listOfElementsofH1.keySet()) {

            if (!s1.equals("EMPTY_MATCH") && h2.containsKey(s1)) {
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
        listOfElementsofH1.putAll(h2);
        return listOfElementsofH1;
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
        for (Map<String, MatchPath> h1 : m1) {
            for (Map<String, MatchPath> h2 : m2) {
                Map<String, MatchPath> h3 = reduceConform(h1, h2);
                if (h3 != null) {
                    //m3.add(h3);
                    if (!m3.contains(h3)) m3.add(h3);
                    oneMatch = true;
                }
            }
        }
        return oneMatch ? m3 : NO_MATCH;
    }


    /**
     * Transform a number term into an int value.
     * <p>
     * i.e. Z(1(2(3(#)))) ==> 123
     *
     * @param zTerm
     * @return
     */
    public static int transformToNumber(Term zTerm) {
        List<Integer> integ = MatcherImpl.transformHelper(new ArrayList<>(), zTerm);
        int dec = 10;
        int output = integ.get(0);
        for (int i = 1; i < integ.size(); i++) {
            Integer integer = integ.get(i);
            output += integer * dec;
            dec = dec * 10;
        }
        return output;
    }

    private static List<Integer> transformHelper(List<Integer> l, Term sub) {
        if (sub.op().name().toString().equals("#")) {
            return l;
        } else {
            l.add(Integer.parseUnsignedInt(sub.op().name().toString()));
            return transformHelper(l, sub.sub(0));
        }
    }

    /**
     * Visit '_'
     *
     * @param ctx
     * @param peek
     * @return
     */
    @Override
    public Matchings visitDontCare(MatchPatternParser.DontCareContext ctx, MatchPath peek) {
        if (peek != null) {
            return handleBindClause(ctx.bindClause(), peek, EMPTY_MATCH);
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
    protected Matchings visitSchemaVar(MatchPatternParser.SchemaVarContext ctx, MatchPath peek) {
        if (peek != null) {
            return Matchings.singleton(ctx.getText(), peek);
        } else {
            return NO_MATCH;
        }
    }

    /**
     * Visit a '...'MatchPath'...' structure
     *
     * @param ctx
     * @param peek
     * @return
     */
    @Override
    protected Matchings visitAnywhere(MatchPatternParser.AnywhereContext ctx, MatchPath peek) {
        Matchings m = new Matchings();
        subTerms((MatchPath.MPTerm) peek).forEach(sub -> {
            Matchings s = accept(ctx.termPattern(), sub);
            m.addAll(s);
        });
        return m;
    }

    /**
     * Visit a function and predicate symbol without a sequent arrow
     *
     * @param ctx
     * @param path
     * @return
     */
    @Override
    protected Matchings visitFunction(MatchPatternParser.FunctionContext ctx, MatchPath path) {
        //System.out.format("Match: %25s with %s %n", peek, ctx.toInfoString(new MatchPatternParser(null)));
        String expectedFunction = ctx.func.getText();
        Term peek = ((MatchPath.MPTerm) path).getUnit();
        boolean sameName = expectedFunction.equals("?")
                || expectedFunction.equals("_")
                || peek.op().name().toString().equals(expectedFunction);
        boolean sameArity = ctx.termPattern().size() == peek.arity();
        if (sameName && sameArity) {
            Matchings m = IntStream.range(0, peek.arity())
                    .mapToObj(i -> (Matchings)
                            accept(ctx.termPattern(i), create(path, i)))
                    .reduce(MatcherImpl::reduceConform)
                    .orElse(EMPTY_MATCH);
            return handleBindClause(ctx.bindClause(), path, m);
        }
        return NO_MATCH;
    }

    /**
     * @param ctx
     * @param t
     * @param m
     * @return
     */
    private Matchings handleBindClause(MatchPatternParser.BindClauseContext ctx, MatchPath t, Matchings m) {
        if (!catchAll && ctx == null) {
            return m;
        }

        if (m.equals(NO_MATCH)) {
            return m;
        }

        // double ? for anonymous matchings
        final String name = ctx != null
                ? ctx.SID().getText()
                : "??_" + getRandomNumber();

        Matchings mNew = Matchings.singleton(name, t);
        return this.reduceConform(m, mNew);
    }


    @Override
    protected Matchings visitNumber(MatchPatternParser.NumberContext ctx, MatchPath path) {
        //we are at a number
        Term peek = ((MatchPath.MPTerm) path).getUnit();
        if (peek.op().name().toString().equals("Z")) {
            ImmutableArray<Term> subs = peek.subs();
            int transformedString = transformToNumber(peek.sub(0));
            if (Integer.parseUnsignedInt(ctx.DIGITS().getText()) == transformedString) {
                return EMPTY_MATCH;
            } else {
                return NO_MATCH;
            }
        } else {
            return NO_MATCH;
        }


    }

    /**
     * Visit a sequent pattern 'f(x) ==> f(x)', 'f(x) ==>' or '==> f(x)'
     *
     * @param ctx
     * @param peek
     * @return
     */
    @Override
    public Matchings visitSequentAnywhere(MatchPatternParser.SequentAnywhereContext ctx, MatchPath peek) {
        MatchPath.MPSequent seq = (MatchPath.MPSequent) peek;
        MatchPatternParser.SemiSeqPatternContext patternCtx = ctx.anywhere;

        Matchings ret = new Matchings();
        Matchings antecMatches = accept(patternCtx, createAntecedent(seq));
        Matchings succMatches = accept(patternCtx, createSuccedent(seq));

        //if(!antecMatches.equals(EMPTY_MATCH))
        ret.addAll(antecMatches);
        //if(!succMatches.equals(EMPTY_MATCH))
        ret.addAll(succMatches);

        //if (ret.contains(EMPTY_VARIABLE_ASSIGNMENT) && ret.size() > 1)
        //    ret.remove(EMPTY_VARIABLE_ASSIGNMENT); // remove empty match if there is an other match

        return ret;
    }

    @Override
    public Matchings visitSequentArrow(MatchPatternParser.SequentArrowContext ctx, MatchPath peek) {
        MatchPath.MPSequent seq = (MatchPath.MPSequent) peek;

        //NPE
        Matchings mAntec = ctx.antec != null
                ? accept(ctx.antec, createAntecedent(seq))
                : EMPTY_MATCH;

        Matchings mSucc = ctx.succ != null
                ? accept(ctx.succ, createSuccedent(seq))
                : EMPTY_MATCH;

        return MatcherImpl.reduceConform(mAntec, mSucc);
    }

    /**
     * Visit a semisequent pattern f(x), f(y)
     *
     * @param ctx
     * @param peek
     * @return
     */
    @Override
    protected Matchings visitSemiSeqPattern(MatchPatternParser.SemiSeqPatternContext ctx, MatchPath peek) {
        MatchPath.MPSemiSequent sseq = (MatchPath.MPSemiSequent) peek;
        Semisequent ss = (Semisequent) peek.getUnit();
        if (ss.isEmpty()) {
            return ctx.termPattern().size() == 0
                    ? EMPTY_MATCH
                    : NO_MATCH;
        }

        List<MatchPath.MPSequentFormula> sequentFormulas =
                IntStream.range(0, ss.size())
                        .mapToObj(pos -> create(sseq, pos))
                        .collect(Collectors.toList());

        List<MatchPatternParser.TermPatternContext> patterns = ctx.termPattern();

        HashMap<MatchPatternParser.TermPatternContext, Map<SequentFormula, Matchings>>
                map = new HashMap<>();

        //cartesic product of pattern and top-level terms
        for (MatchPatternParser.TermPatternContext tctx : patterns) {
            map.put(tctx, new HashMap<>());
            for (MatchPath.MPSequentFormula sf : sequentFormulas) {
                Matchings temp = accept(tctx, create(sf));
                if (!temp.equals(NO_MATCH))
                    map.get(tctx).put(sf.getUnit(), temp);
            }
        }

        List<Matchings> matchings = new ArrayList<>();
        reduceDisjoint(map, patterns, matchings);
        Matchings ret = new Matchings();
        //.filter(x -> !x.equals(EMPTY_MATCH))
        matchings.forEach(ret::addAll);
        return ret;
    }

    /**
     * @param map
     * @param patterns
     * @param matchings
     */
    private void reduceDisjoint(HashMap<MatchPatternParser.TermPatternContext, Map<SequentFormula, Matchings>> map,
                                List<MatchPatternParser.TermPatternContext> patterns,
                                List<Matchings> matchings) {
        reduceDisjoint(map, patterns, matchings, 0, EMPTY_MATCH, new HashSet<>());
    }

    /**
     * @param map
     * @param patterns
     * @param matchings
     * @param currentPatternPos
     * @param ret
     * @param chosenSequentFormulas
     */
    private void reduceDisjoint(HashMap<MatchPatternParser.TermPatternContext, Map<SequentFormula, Matchings>> map,
                                List<MatchPatternParser.TermPatternContext> patterns,
                                List<Matchings> matchings,
                                int currentPatternPos,
                                Matchings ret,
                                Set<SequentFormula> chosenSequentFormulas) {
        if (currentPatternPos >= patterns.size()) { // end of selection process is reached
            matchings.add(ret);
            return;
        }

        MatchPatternParser.TermPatternContext currentPattern = patterns.get(currentPatternPos);
        Sets.SetView<SequentFormula> topLevelFormulas =
                Sets.difference(map.get(currentPattern).keySet(), chosenSequentFormulas);

        if (topLevelFormulas.size() == 0) {
            return; // all top level formulas has been chosen, we have no matches left
        }

        for (SequentFormula formula : topLevelFormulas) { // chose a toplevel formula
            // the matchings for current pattern against the toplevel
            Matchings m = map.get(currentPattern).get(formula);
            //join them with the current Matchings
            Matchings mm = reduceConform(m, ret);
            chosenSequentFormulas.add(formula); // adding the formula, so other matchings can not choose it

            // recursion: choose the next matchings for the next pattern
            reduceDisjoint(map, patterns, matchings,
                    currentPatternPos + 1, mm, chosenSequentFormulas);

            chosenSequentFormulas.remove(formula); // delete the formula, so it is free to choose, again
        }
    }

    @Override
    protected Matchings visitPlusMinus(MatchPatternParser.PlusMinusContext ctx, MatchPath peek) {
        return visitBinaryOperation(convert(ctx.op.getType()),
                ctx.termPattern(0), ctx.termPattern(1), peek);
    }

    protected Matchings visitBinaryOperation(String keyOpName, MatchPatternParser.TermPatternContext right, MatchPatternParser.TermPatternContext left, MatchPath peek) {
        //create new functioncontext object and set fields accordingly
        OwnFunctionContext func = new OwnFunctionContext(left);
        //MatchPatternParser.FunctionContext func = new MatchPatternParser.FunctionContext(left);
        //func.func = new CommonToken(MatchPatternLexer.ID, keyOpName);

        func.func = new CommonToken(MatchPatternLexer.ID, keyOpName);
        //right is added as first argument, left as second
        func.termPattern().add(right);
        func.termPattern().add(left);

        return accept(func, peek);
    }

    @Override
    public Matchings visitQuantForm(MatchPatternParser.QuantFormContext ctx, MatchPath peek) {
        Term toMatch = (Term) peek.getUnit();
        if (!toMatch.op().toString().equals(convert(ctx.quantifier.getType()))) {
            return NO_MATCH;
        }
        if (toMatch.boundVars().size() != ctx.boundVars.size()) {
            return NO_MATCH;
        }

        Matchings match = EMPTY_MATCH;

        for (int i = 0; i < ctx.boundVars.size(); i++) {
            Token qfPattern = ctx.boundVars.get(i);
            QuantifiableVariable qv = toMatch.boundVars().get(i);

            if (qfPattern.getType() == MatchPatternLexer.DONTCARE) {
                //match = reduceConform(match, Matchings.singleton(qfPattern.getText(), new MatchPath.MPQuantifiableVarible(peek, qv, i)));
                match = reduceConform(match, EMPTY_MATCH);
                continue;
            }
            if (qfPattern.getType() == MatchPatternLexer.SID) {
                match = reduceConform(match, Matchings.singleton(qfPattern.getText(), new MatchPath.MPQuantifiableVariable(peek, qv, i)));
            } else {
                if (!qv.name().toString().equals(qfPattern.getText())) {
                    return NO_MATCH;
                }
                match = reduceConform(match, EMPTY_MATCH);
            }
        }


        Matchings fromTerm = accept(ctx.skope, create(peek, 0));
        Matchings retM = reduceConform(fromTerm, match);
        retM.forEach(stringMatchPathMap -> {
            stringMatchPathMap.forEach((s, matchPath) -> {
                        if (matchPath instanceof MatchPath.MPQuantifiableVariable) {

                            //create term from variablename and put instead into map
                        }
                    }

            );
        });
        return handleBindClause(ctx.bindClause(), peek, retM);
    }

    @Override
    protected Matchings visitMult(MatchPatternParser.MultContext ctx, MatchPath peek) {
        return visitBinaryOperation("mul", ctx.termPattern(0), ctx.termPattern(1), peek);
    }

    @Override
    protected Matchings visitComparison(MatchPatternParser.ComparisonContext ctx, MatchPath peek) {
        return visitBinaryOperation(convert(ctx.op.getType()), ctx.termPattern(0), ctx.termPattern(1), peek);
    }

    @Override
    protected Matchings visitOr(MatchPatternParser.OrContext ctx, MatchPath peek) {
        return visitBinaryOperation("or", ctx.termPattern(0), ctx.termPattern(1), peek);
    }

    @Override
    public Matchings visitExprNot(MatchPatternParser.ExprNotContext ctx, MatchPath peek) {
        return visitBinaryOperation("not", ctx.termPattern(), ctx, peek);
    }

    @Override
    public Matchings visitExprNegate(MatchPatternParser.ExprNegateContext ctx, MatchPath peek) {
        return visitUnaryOperation("sub", ctx.termPattern(), peek);
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
            case MatchPatternParser.FORALL:
                return "all";
            case MatchPatternParser.EXISTS:
                return "exists";

            default:
                throw new UnsupportedOperationException("The operator " + op + "is not known");
        }


    }




    @Override
    public Matchings visitExprParen(MatchPatternParser.ExprParenContext ctx, MatchPath peek) {
        return handleBindClause(ctx.bindClause(), peek, accept(ctx.termPattern(), peek));
    }

    private Matchings visitUnaryOperation(String unaryOp,
                                          MatchPatternParser.TermPatternContext ctx,
                                          MatchPath peek) {
        MatchPatternParser.FunctionContext func = new MatchPatternParser.FunctionContext(ctx);
        func.termPattern().add(ctx);
        func.func = new CommonToken(MatchPatternLexer.ID, unaryOp);
        return accept(func, peek);
    }

    @Override
    protected Matchings visitImpl(MatchPatternParser.ImplContext ctx, MatchPath peek) {
        return visitBinaryOperation("imp", ctx.termPattern(0), ctx.termPattern(1), peek);
    }

    @Override
    protected Matchings visitDivMod(MatchPatternParser.DivModContext ctx, MatchPath peek) {
        return visitBinaryOperation(convert(ctx.op.getType()), ctx.termPattern(0), ctx.termPattern(1), peek);
    }

    @Override
    protected Matchings visitAnd(MatchPatternParser.AndContext ctx, MatchPath peek) {
        return visitBinaryOperation("and", ctx.termPattern(0), ctx.termPattern(1), peek);
    }

    @Override
    protected Matchings visitXor(MatchPatternParser.XorContext ctx, MatchPath peek) {
        return visitBinaryOperation("xor", ctx.termPattern(0), ctx.termPattern(1), peek);
    }

    @Override
    protected Matchings visitEquality(MatchPatternParser.EqualityContext ctx, MatchPath peek) {
        return visitBinaryOperation("equals", ctx.termPattern(0), ctx.termPattern(1), peek);
    }

    @Override
    protected Matchings visitEquivalence(MatchPatternParser.EquivalenceContext ctx, MatchPath peek) {
        return visitBinaryOperation("equiv", ctx.termPattern(0), ctx.termPattern(1), peek);
    }

    private Stream<MatchPath.MPTerm> subTerms(MatchPath.MPTerm peek) {
        ArrayList<MatchPath.MPTerm> list = new ArrayList<>();
        subTerms(list, peek);
        return list.stream();
    }

    private void subTerms(ArrayList<MatchPath.MPTerm> list, MatchPath.MPTerm peek) {
        list.add(peek);
        for (int i = 0; i < peek.getUnit().arity(); i++) {
            subTerms(list, create(peek, i));
        }
    }

    public int getRandomNumber() {
        return random.nextInt();
    }
}
/*    private Matchings reduceConformQuant(Matchings fromTerm, Matchings match) {
        Matchings ret = new Matchings();
        Map<String, MatchPath> quantifiedVarMap = match.first();

        System.out.println("quantifiedVarMap = " + quantifiedVarMap);

      List<Map<String, MatchPath>> list = fromTerm.stream().filter(
        map -> map.entrySet().stream().allMatch(
                entry -> {
                    System.out.println("entry = " + entry);
                    if (entry.getValue() != null) {
                        MatchPath mp = (MatchPath) entry.getValue();
                        Term mterm = (Term) mp.getUnit();
                        if (quantifiedVarMap.containsKey(entry.getKey())) {
                            QuantifiableVariable unit = (QuantifiableVariable) quantifiedVarMap.get(entry.getKey()).getUnit();
                            return unit.name().toString().
                                    equals(mterm.op().name().toString());
                        } else {

                            return true;
                        }
                    } else {
                        //in this case we have an empty match, however, we may have bound quantVars, we need to add them
                        System.out.println("entry.getKey() = " + entry.getKey());
                        return true;
                    }
                }
        )
).collect(Collectors.toList());

        ret.addAll(list);
                return ret;
                }*/