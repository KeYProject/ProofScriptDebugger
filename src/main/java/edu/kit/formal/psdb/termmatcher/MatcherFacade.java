package edu.kit.formal.psdb.termmatcher;

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
}
