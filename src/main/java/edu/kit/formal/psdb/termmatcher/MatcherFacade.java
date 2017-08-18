package edu.kit.formal.psdb.termmatcher;

import de.uka.ilkd.key.logic.Semisequent;
import de.uka.ilkd.key.logic.Sequent;
import de.uka.ilkd.key.logic.SequentFormula;
import de.uka.ilkd.key.logic.Term;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.key_project.util.collection.ImmutableList;

import java.util.ArrayList;
import java.util.List;

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
        MatchPatternParser.TermPatternContext ctx = mpp.termPattern();
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
        MatchPatternParser.SemiSeqPatternContext ctx = mpp.semiSeqPattern();
        return matches(ctx, semiSeq);

    }

    public static Matchings matches(MatchPatternParser.SemiSeqPatternContext pattern, Semisequent semiSeq) {
        MatcherImpl matcher = new MatcherImpl();
        ImmutableList<SequentFormula> allSequentFormulas = semiSeq.asList();

        List<MatchPatternParser.TermPatternContext> termPatternContexts = pattern.termPattern();

        List<Matchings> allMatches = new ArrayList<>();

        for (MatchPatternParser.TermPatternContext termPatternContext : termPatternContexts) {
            Matchings m = new Matchings();
            for (SequentFormula form : allSequentFormulas) {
                Matchings temp = matcher.accept(termPatternContext, form.formula());
                m.addAll(temp);
            }
            allMatches.add(m);
        }

        Matchings res = reduceCompatibleMatches(allMatches);
        System.out.println("res = " + res);
        return res;
    }

    /**
     * Reduce all matches to only comaptible matchings
     * @param allMatches
     * @return
     */
    private static Matchings reduceCompatibleMatches(List<Matchings> allMatches) {
        if (allMatches.size() == 2) {
            return MatcherImpl.reduceConform(allMatches.get(0), allMatches.get(1));
        } else {
            Matchings tmp = MatcherImpl.reduceConform(allMatches.get(0), allMatches.get(1));
            List<Matchings> list = new ArrayList<>();
            list.add(tmp);
            list.addAll(allMatches.subList(2, allMatches.size()));
            return reduceCompatibleMatches(list);
        }
    }

    /**
     * Filter matchings s.t. only those remain, that fit the pattern
     *
     * @param allCompatibelMatchings
     * @param pattern
     * @return
     */
    private static List<Matchings> filterMatchings(List<Matchings> allCompatibelMatchings, MatchPatternParser.SemiSeqPatternContext pattern) {
        List<Matchings> ret = new ArrayList<>();
        List<MatchPatternParser.TermPatternContext> termPatternContexts = pattern.termPattern();


        return ret;
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
        MatchPatternParser.SequentPatternContext ctx = mpp.sequentPattern();
        Semisequent antec = sequent.antecedent();
        Semisequent succ = sequent.succedent();


        return matches(pattern,antec);
    }
}
