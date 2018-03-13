package edu.kit.iti.formal.psdbg.gui.actions.acomplete;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author Alexander Weigl
 * @version 1 (13.03.18)
 */
public interface AutoCompleter {
    default boolean isActivated() {
        return true;
    }

    public Stream<Suggestion> get(CompletionPosition cp);
}
