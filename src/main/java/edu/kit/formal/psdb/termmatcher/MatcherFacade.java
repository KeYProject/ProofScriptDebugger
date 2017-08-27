package edu.kit.formal.psdb.termmatcher;

import de.uka.ilkd.key.logic.Semisequent;
import de.uka.ilkd.key.logic.Sequent;
import de.uka.ilkd.key.logic.Term;
import edu.kit.formal.psdb.termmatcher.MatchPatternParser.SemiSeqPatternContext;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import static edu.kit.formal.psdb.termmatcher.MatchPatternParser.SequentPatternContext;
import static edu.kit.formal.psdb.termmatcher.MatchPatternParser.TermPatternContext;

/**
 * A facade for capturing everthing we want to do with matchers.
 *
 * @author Alexander Weigl
 * @author S.Grebing
 */
public class MatcherFacade {
    public static Matchings matches(String pattern, Term keyTerm) {
        MatcherImpl matcher = new MatcherImpl();
        matcher.setCatchAll(false);
        MatchPatternParser mpp = getParser(pattern);
        TermPatternContext ctx = mpp.termPattern();
        return matcher.accept(ctx, MatchPath.createRoot(keyTerm));
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
     * @param catchAll
     * @return Matchings
     */
    public static Matchings matches(String pattern, Semisequent semiSeq, boolean catchAll) {
        MatchPatternParser mpp = getParser(pattern);
        SemiSeqPatternContext ctx = mpp.semiSeqPattern();
        MatcherImpl matcher = new MatcherImpl();
        matcher.setCatchAll(catchAll);
        Matchings m = matcher.accept(ctx, new MatchPath.MatchPathSemiSequent(null, semiSeq, true));
        return m;
    }


    /**
     * Match a sequent pattern against a concrete sequent
     *
     * @param pattern e.g., f(x) ==> f(y)
     * @param sequent
     * @return
     */
    public static Matchings matches(String pattern, Sequent sequent, boolean catchAll) {
        MatchPatternParser mpp = getParser(pattern);
        SequentPatternContext ctx = mpp.sequentPattern();
        MatcherImpl matcher = new MatcherImpl();
        matcher.setCatchAll(catchAll);
        Matchings m = matcher.accept(ctx, new MatchPath.MatchPathSequent(sequent));
        return m;
    }

    /**
     * Like {@link #matches(String, Sequent)} but allows to use
     * MatcherImpl#isCatchAll.
     *
     * @param pattern
     * @param sequent
     * @return
     */
    public static Matchings matches(String pattern, Sequent sequent) {
        return matches(pattern, sequent,false);
    }
}
