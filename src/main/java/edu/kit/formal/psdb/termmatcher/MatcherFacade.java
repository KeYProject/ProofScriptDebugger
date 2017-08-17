package edu.kit.formal.psdb.termmatcher;

import de.uka.ilkd.key.logic.Semisequent;
import de.uka.ilkd.key.logic.Sequent;
import de.uka.ilkd.key.logic.Term;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 * A facade for capturing everthing we want to do with matchers.
 *
 * @author Alexander Weigl
 */
public class MatcherFacade {
    public static Matchings matches(String pattern, Term keyTerm) {
        MatcherImpl matcher = new MatcherImpl();
        MatchPatternParser mpp = getParser(pattern);
        MatchPatternParser.TermPatternContext ctx = mpp.termPattern();
        return matcher.accept(ctx, keyTerm);
    }

    public static MatchPatternParser getParser(String pattern) {
        return getParser(CharStreams.fromString(pattern));
    }

    public static MatchPatternParser getParser(CharStream stream) {
        return new MatchPatternParser(new CommonTokenStream(new MatchPatternLexer(stream)));
    }

    /**
     * Match a semisequent against a sequent
     *
     * @param pattern Semiseqeuent pattern e.g. f(x), f(x)
     * @param semiSeq Concrete KeY Semisequent
     * @return Matchings
     */
    public static Matchings matches(String pattern, Semisequent semiSeq) {
        MatcherImpl matcher = new MatcherImpl();
        MatchPatternParser mpp = getParser(pattern);
        MatchPatternParser.SemiSeqPatternContext ctx = mpp.semiSeqPattern();
        return matches(ctx, semiSeq);

    }

    public static Matchings matches(MatchPatternParser.SemiSeqPatternContext pattern, Semisequent semiSeq) {
        //semiSeq.iterator()
        // List<MatchPatternParser.TermPatternContext> termPatternContexts = ctx.termPattern();
        // for (MatchPatternParser.TermPatternContext termpattern: termPatternContexts) {
        //for all terms in seq
        // Matchings s = accept(termpattern, peek);
        // }
        return null;
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


        return null;
    }
}
