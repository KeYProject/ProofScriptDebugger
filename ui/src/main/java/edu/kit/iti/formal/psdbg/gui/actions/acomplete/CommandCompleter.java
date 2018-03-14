package edu.kit.iti.formal.psdbg.gui.actions.acomplete;

import de.uka.ilkd.key.api.KeYApi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author Alexander Weigl
 * @version 1 (13.03.18)
 */
public class CommandCompleter implements AutoCompleter {
    private static Collection<Suggestion> suggestions = new ArrayList<>();
    static {
        KeYApi.getScriptCommandApi().getScriptCommands().forEach(proofMacro -> {
            suggestions.add(Suggestion.command(proofMacro.getName()));
        });
    }

    @Override
    public Stream<Suggestion> get(CompletionPosition cp) {
        return cp.filterByPrefix(suggestions);
    }
}

