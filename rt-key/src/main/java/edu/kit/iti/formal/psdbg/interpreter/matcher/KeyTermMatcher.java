package edu.kit.iti.formal.psdbg.interpreter.matcher;

import com.google.common.collect.Sets;
import de.uka.ilkd.key.logic.*;
import de.uka.ilkd.key.logic.op.*;
import org.antlr.v4.runtime.Token;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class KeyTermMatcher extends  KeyTermBaseVisitor<Matchings, MatchPath> {
    static final Matchings NO_MATCH = new Matchings();

    static final Matchings EMPTY_MATCH = Matchings.singleton("EMPTY_MATCH", null);

    static final Map<String, MatchPath> EMPTY_VARIABLE_ASSIGNMENT = EMPTY_MATCH.first();

    private String randomName;

   // MatchPath peek;

    private List<Integer> currentposition = new ArrayList<>();

    private boolean catchAll = false;

    public Matchings matchesToplevel(Sequent sequent, List<Term> patternTerms) {
        MatchPath.MPSequent seq = MatchPathFacade.create(sequent);

        Matchings ret = new Matchings();
        Matchings antecMatches = matchesSemisequent(MatchPathFacade.createAntecedent(seq), patternTerms);
        Matchings succMatches = matchesSemisequent(MatchPathFacade.createSuccedent(seq), patternTerms);

        //if(!antecMatches.equals(EMPTY_MATCH))
        ret.addAll(antecMatches);
        //if(!succMatches.equals(EMPTY_MATCH))
        ret.addAll(succMatches);
        return ret;
    }


    public Matchings matchesSequent(Sequent sequent, Sequent pattern) {
        MatchPath.MPSequent mps = MatchPathFacade.create(sequent);
        Matchings ms = matchesSemisequent(
                MatchPathFacade.createSuccedent(mps),
                pattern.succedent());
        Matchings ma = matchesSemisequent(
                MatchPathFacade.createAntecedent(mps),
                pattern.antecedent());

        return reduceConform(ms, ma);
    }

    private Matchings matchesSemisequent(MatchPath.MPSemiSequent peek, Semisequent pattern) {
        List<Term> patterns = pattern.asList().stream().map(SequentFormula::formula).collect(Collectors.toList());
        return matchesSemisequent(peek, patterns);

    }


    private Matchings matchesSemisequent(MatchPath.MPSemiSequent peek, List<Term> patterns) {

        Semisequent semiSeq = peek.getUnit();
        if (semiSeq.isEmpty()) {
            return patterns.isEmpty()
                    ? EMPTY_MATCH
                    : NO_MATCH;
        }
        HashMap<Term, Map<SequentFormula, Matchings>> map = new HashMap<>();
        List<MatchPath.MPSequentFormula> sequentFormulas =
                IntStream.range(0, semiSeq.size())
                        .mapToObj(pos -> MatchPathFacade.create(peek, pos))
                        .collect(Collectors.toList());

        //cartesic product of pattern and top-level terms
        for (Term subPatternTerm : patterns) {
            map.put(subPatternTerm, new HashMap<>());
            for (MatchPath.MPSequentFormula sf : sequentFormulas) {
                Matchings temp = visit(subPatternTerm, MatchPathFacade.create(sf));
                if (!temp.equals(NO_MATCH))
                    map.get(subPatternTerm).put(sf.getUnit(), temp);
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
     * Visit a '...'MatchPath'...' structure
     * @param pattern
     * @param subject
     * @return
     */
    @DispatchOn(EllipsisOp.class)
    public Matchings visitEllipsisOp(Term pattern, MatchPath subject) {
        Matchings matchings = new Matchings();
        subTerms((MatchPath.MPTerm) subject).forEach(sub -> {
            Matchings s = visit(pattern.sub(0), sub);
            matchings.addAll(s);
        });
        return matchings;
    }


    /**
     * Visit a MatchIdentifierOP e.g., ?X or ?
     * @param pattern
     * @param subject
     * @return
     */
    @DispatchOn(MatchIdentifierOp.class)
    public Matchings visitMatchIdentifierOp(Term pattern, MatchPath subject) {
        if(subject != null) {
            String name = pattern.op().name().toString();
            if (name.equalsIgnoreCase("?")) {
                name = getRandomName();
            }
            Matchings mSingleton = Matchings.singleton(name, subject);
            return mSingleton;
        } else {
            return NO_MATCH;
        }

    }


    @DispatchOn(Quantifier.class)
    public Matchings visitQuantifier(Term pattern, MatchPath subject) {
        Term toMatch = (Term) subject.getUnit();
        if (!toMatch.op().equals(pattern.op())) {
            return NO_MATCH;
        }
        if (toMatch.boundVars().size() != pattern.boundVars().size()) {
            return NO_MATCH;
        }

        Matchings match = EMPTY_MATCH;

     /*   for (int i = 0; i < pattern.boundVars().size(); i++) {

            QuantifiableVariable qfPattern = pattern.boundVars().get(i);
            QuantifiableVariable qv = toMatch.boundVars().get(i);

           if (qfPattern.getType() == MatchPatternLexer.DONTCARE) {
                //match = reduceConform(match, Matchings.singleton(qfPattern.getText(), new MatchPath.MPQuantifiableVarible(peek, qv, i)));
                match = reduceConform(match, EMPTY_MATCH);
                continue;
            }
            if (qfPattern.getType() == MatchPatternLexer.SID) {
                TermFactory tf = new TermFactory(new HashMap<>());
                TermBuilder tb = new TermBuilder(tf, services);
                Term termQVariable = tb.var(qv);

                match = reduceConform(match, Matchings.singleton(qfPattern.getText(),
                        new MatchPath.MPTerm(peek, termQVariable, -i)));
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

      /*  // Decision: Order of bounded vars
        Matchings mm = new Matchings();
        if (term.getUnit().boundVars().size() != pattern.boundVars().size()) {
            return NO_MATCH;
        }

        Map<String, MatchPath> mPaths = new HashMap<>();
        mm.add(mPaths);

        for (int i = 0; i < term.getUnit().boundVars().size(); i++) {
            QuantifiableVariable bv = term.getUnit().boundVars().get(i);
            QuantifiableVariable pv = pattern.boundVars().get(i);

            if (pv instanceof MatchIdentifierOp) {
                //Namensbehandlung
                String name;
                if (pv.name().toString().equalsIgnoreCase("?")) {
                    name = getRandomName();
                } else {
                    name = getBindingName(pv.name());
                }

                mPaths.put(name, term);
            } else if (!pv.equals(bv)) {
                return NO_MATCH;
            }
        }
        return mm;*/
      return null;

    }

    @DispatchOn(MatchBinderOp.class)
    public Matchings visitMatchBinderOp(Term pattern, MatchPath subject) {
        Matchings matchings = visit(pattern.sub(1), subject);
        if (matchings != NO_MATCH) {
            String name = pattern.sub(0).op().name().toString();
            for (Map<String, MatchPath> a : matchings) {
                a.put(name, subject);
            }
        }
        return matchings;
    }



    @Override
    protected Matchings defaultVisit(Term pattern, MatchPath subject1) {
        Matchings m = new Matchings();
        MatchPath<Term, Object> subject = (MatchPath<Term, Object>) subject1;
        if (subject.getUnit().subs().size() != pattern.subs().size()) {
            return NO_MATCH;
        }
        if(pattern.equals(subject1.getUnit()))
            return EMPTY_MATCH;
        for (int i = 0; i < subject.getUnit().subs().size(); i++) {
            Term tt = subject.getUnit().sub(i);
            Term pt = pattern.sub(i);
            Matchings msub = visit(pt, MatchPathFacade.create(subject, i));

            if (msub.equals(NO_MATCH)) {
                return NO_MATCH;
            }
            m = reduceConform(m, msub);
        }

        return m;
    }

    //region helper
    private String getBindingName(Name name) {

        if (name.toString().equals("?"))
            return getRandomName();
        return name.toString();
    }

    public String getRandomName() {
        return randomName;
    }

    private Stream<MatchPath.MPTerm> subTerms(MatchPath.MPTerm peek) {
        ArrayList<MatchPath.MPTerm> list = new ArrayList<>();
        subTerms(list, peek);
        return list.stream();
    }

    private void subTerms(ArrayList<MatchPath.MPTerm> list, MatchPath.MPTerm peek) {
        list.add(peek);
        for (int i = 0; i < peek.getUnit().arity(); i++) {
            subTerms(list, MatchPathFacade.create(peek, i));
        }
    }
    //endregion

    //region Reductions


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
        if (m1 == EMPTY_MATCH || m1.size()==0) return m2;
        if (m2 == EMPTY_MATCH || m2.size()==0) return m1;

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


    /* @param map
     * @param patterns
     * @param matchings
     */
    private void reduceDisjoint(HashMap<Term, Map<SequentFormula, Matchings>> map,
                                List<Term> patterns,
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
    private void reduceDisjoint(HashMap<Term, Map<SequentFormula, Matchings>> map,
                                List<Term> patterns,
                                List<Matchings> matchings,
                                int currentPatternPos,
                                Matchings ret,
                                Set<SequentFormula> chosenSequentFormulas) {
        if (currentPatternPos >= patterns.size()) { // end of selection process is reached
            matchings.add(ret);
            return;
        }

        Term currentPattern = patterns.get(currentPatternPos);
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


//endregion

}
