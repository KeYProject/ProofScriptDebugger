package edu.kit.formal.psdb.termmatcher;

import de.uka.ilkd.key.logic.Semisequent;
import de.uka.ilkd.key.logic.Sequent;
import de.uka.ilkd.key.logic.SequentFormula;
import de.uka.ilkd.key.logic.Term;
import edu.kit.formal.psdb.termmatcher.MatchPatternParser.SemiSeqPatternContext;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.key_project.util.collection.ImmutableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static edu.kit.formal.psdb.termmatcher.MatchPatternParser.SequentPatternContext;
import static edu.kit.formal.psdb.termmatcher.MatchPatternParser.TermPatternContext;
import static edu.kit.formal.psdb.termmatcher.MatcherImpl.MatchInfo;
import static edu.kit.formal.psdb.termmatcher.MatcherImpl.NO_MATCH;

/**
 * A facade for capturing everthing we want to do with matchers.
 *
 * @author Alexander Weigl
 * @author S.Grebing
 */
public class MatcherFacade {
    public static Matchings matches(String pattern, Term keyTerm) {
        MatcherImpl matcher = new MatcherImpl();
        MatchPatternParser mpp = getParser(pattern);
        TermPatternContext ctx = mpp.termPattern();
        return matcher.accept(ctx, keyTerm);
    }

    /**
     * Returns a {@link MatchPatternParser} for the given input pattern.
     *
     * @param pattern
     * @return
     */
    public static MatchPatternParser getParser(String pattern) {
        return getParser(CharStreams.fromString(pattern));
    }

    public static MatchPatternParser getParser(CharStream stream) {
        return new MatchPatternParser(new CommonTokenStream(new MatchPatternLexer(stream)));
    }

    /**
     * Match a semisequent against a sequent
     *
     * @param pattern Semisequent pattern e.g. f(x), f(x)
     * @param semiSeq Concrete KeY Semisequent
     * @return Matchings
     */
    public static Matchings matches(String pattern, Semisequent semiSeq) {

        MatchPatternParser mpp = getParser(pattern);
        SemiSeqPatternContext ctx = mpp.semiSeqPattern();
        return matches(ctx, semiSeq);

    }


    public static Matchings matches(SemiSeqPatternContext pattern, Semisequent semiSeq) {
        MatcherImpl matcher = new MatcherImpl();
        ImmutableList<SequentFormula> allSequentFormulas = semiSeq.asList();

        List<TermPatternContext> termPatternContexts = pattern.termPattern();

        List<List<MatcherImpl.MatchInfo>> allMatches = new ArrayList<>();

        for (TermPatternContext termPatternContext : termPatternContexts) {
            List<MatchInfo> m = new ArrayList<>();
            for (SequentFormula form : allSequentFormulas) {
                Matchings temp = matcher.accept(termPatternContext, form.formula());

                for (HashMap<String, Term> match : temp) {
                    m.add(new MatchInfo(match, Collections.singleton(form)));
                }
            }

            allMatches.add(m);
        }

        List<MatchInfo> res = reduceCompatibleMatches(allMatches);

        if (res == null)
            return NO_MATCH;

        List<HashMap<String, Term>> resMap = res.stream().map(el -> el.matching).collect(Collectors.toList());
        Matchings resMatchings = new Matchings();
        resMatchings.addAll(resMap);

        return resMatchings;
    }

    //BiMap<Pair<SequentFormula, MatchPatternParser.TermPatternContext>, Matchings> formulaToMatchingInfo,

    /**
     * Reduce all matches to only comaptible matchings
     * @param allMatches
     * @return
     */
    private static List<MatchInfo> reduceCompatibleMatches(List<List<MatchInfo>> allMatches) {
        if (allMatches.size() == 1) {
            return allMatches.get(0);
        }

        if (allMatches.size() == 2) {
            return MatcherImpl.reduceConform(allMatches.get(0), allMatches.get(1));
        } else {
            List<MatchInfo> tmp = MatcherImpl.reduceConform(allMatches.get(0), allMatches.get(1));
            List<List<MatchInfo>> list = new ArrayList<>();
            list.add(tmp);
            list.addAll(allMatches.subList(2, allMatches.size()));
            return reduceCompatibleMatches(list);
        }
    }



    /**
     * Match a sequent pattern against a concrete sequent
     *
     * @param pattern e.g., f(x) ==> f(y)
     * @param sequent
     * @return
     */
    public static Matchings matches(String pattern, Sequent sequent) {
        MatcherImpl matcher = new MatcherImpl();
        MatchPatternParser mpp = getParser(pattern);
        SequentPatternContext ctx = mpp.sequentPattern();

        Semisequent antec = sequent.antecedent();
        Semisequent succ = sequent.succedent();

        SemiSeqPatternContext antecPattern = ctx.antec;
        SemiSeqPatternContext succPattern = ctx.succ;

        Matchings mAntec = antecPattern == null ? MatcherImpl.EMPTY_MATCH : matches(antecPattern, antec);
        Matchings mSucc = succPattern == null ? MatcherImpl.EMPTY_MATCH : matches(succPattern, succ);

        Matchings newMatching = MatcherImpl.reduceConform(mAntec, mSucc);

        return newMatching;
    }
}
