package edu.kit.iti.formal.psdbg.termmatcher;

import edu.kit.formal.psdb.termmatcher.MatchPatternParser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

public class OwnFunctionContext extends MatchPatternParser.FunctionContext {

    public List<MatchPatternParser.TermPatternContext> termPattern = new ArrayList<>();

    public OwnFunctionContext(MatchPatternParser.TermPatternContext ctx) {
        super(ctx);
    }

    public void setFunc(Token func) {
        super.func = func;

    }

    public TerminalNode ID() {
        return super.ID();
    }

    @Override
    public List<MatchPatternParser.TermPatternContext> termPattern() {
        return termPattern;
    }

    @Override
    public MatchPatternParser.TermPatternContext termPattern(int i) {
        return termPattern.get(i);
    }

}
