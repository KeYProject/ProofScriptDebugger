package edu.kit.iti.formal.psdbg.gui.actions.acomplete;

import java.util.stream.Stream;

/**
 * @author Alexander Weigl
 * @version 1 (13.03.18)
 */
public class ArgumentCompleter implements AutoCompleter {
    @Override
    public Stream<Suggestion> get(CompletionPosition cp) {
        return Stream.of(Suggestion.command("argument completer not implemented"));
    }
}
