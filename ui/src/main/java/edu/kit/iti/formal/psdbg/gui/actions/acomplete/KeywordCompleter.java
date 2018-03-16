package edu.kit.iti.formal.psdbg.gui.actions.acomplete;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Alexander Weigl
 * @version 1 (13.03.18)
 */
public class KeywordCompleter implements AutoCompleter {
    private static final List<Suggestion> suggestions = new ArrayList<>(32);
    static {
        suggestions.add(Suggestion.keyword("foreach"));
        suggestions.add(Suggestion.keyword("repeat"));
        suggestions.add(Suggestion.keyword("cases"));
        suggestions.add(Suggestion.keyword("case"));
        suggestions.add(Suggestion.keyword("while"));
        suggestions.add(Suggestion.keyword("if"));
        suggestions.add(Suggestion.keyword("theonly"));
        suggestions.add(Suggestion.keyword("script"));
    }

    @Override
    public Stream<Suggestion> get(CompletionPosition cp) {
        return cp.filterByPrefix(suggestions);
    }
}
