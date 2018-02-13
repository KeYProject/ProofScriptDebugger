package edu.kit.iti.formal.psdbg.termmatcher;

import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.logic.Semisequent;
import de.uka.ilkd.key.logic.Sequent;
import de.uka.ilkd.key.logic.Term;
import edu.kit.iti.formal.psdbg.termmatcher.MatchPatternLexer;
import edu.kit.iti.formal.psdbg.termmatcher.MatchPatternParser;
import edu.kit.iti.formal.psdbg.termmatcher.mp.MatchPathFacade;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A facade for capturing everything we want to do with matchers.
 *
 * @author Alexander Weigl
 * @author S.Grebing
 */
public class MatcherFacade {
    private static Logger logger = LogManager.getLogger(MatcherFacade.class);

    public static Matchings matches(String pattern, Term keyTerm, Services services) {
        MatcherImpl matcher = new MatcherImpl(services);
        matcher.setCatchAll(false);
        MatchPatternParser mpp = getParser(pattern);
        MatchPatternParser.TermPatternContext ctx = mpp.termPattern();
        return matcher.accept(ctx, MatchPathFacade.createRoot(keyTerm));
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
     * @param pattern  Semisequent pattern e.g. f(x), f(x)
     * @param semiSeq  Concrete KeY Semisequent
     * @param catchAll
     * @param services
     * @return Matchings
     */
    public static Matchings matches(String pattern, Semisequent semiSeq, boolean catchAll, Services services) {
        MatchPatternParser mpp = getParser(pattern);
        MatchPatternParser.SemiSeqPatternContext ctx = mpp.semiSeqPattern();
        MatcherImpl matcher = new MatcherImpl(services);
        matcher.setCatchAll(catchAll);
        Matchings m = matcher.accept(ctx, MatchPathFacade.createRoot(semiSeq));
        return m;
    }


    /**
     * Match a sequent pattern against a concrete sequent
     *
     * @param pattern e.g., f(x) ==> f(y)
     * @param sequent
     * @param services
     * @return
     */
    public static Matchings matches(String pattern, Sequent sequent, boolean catchAll, Services services) {
        MatchPatternParser mpp = getParser(pattern);
        MatchPatternParser.SequentPatternContext ctx = mpp.sequentPattern();
        logger.info("Matching \n" + pattern + "\n" + sequent.toString());
        if (mpp.getNumberOfSyntaxErrors() != 0) {
            logger.info("Invalid pattern syntax '{}' no matches returned.", pattern);
            return new Matchings();
        }

        MatcherImpl matcher = new MatcherImpl(services);
        matcher.setCatchAll(catchAll);
        Matchings m = matcher.accept(ctx, MatchPathFacade.create(sequent));
        return m;
    }

    /**
     * Like {@link #matches(String, Sequent, Services)} but allows to use
     * MatcherImpl#isCatchAll.
     *
     * @param pattern
     * @param sequent
     * @param services
     * @return
     */
    public static Matchings matches(String pattern, Sequent sequent, Services services) {
        return matches(pattern, sequent, true, services);
    }
}
