package edu.kit.iti.formal.psdbg.gui.actions.acomplete;

import java.util.stream.Stream;

/**
 * @author Alexander Weigl
 * @version 1 (13.03.18)
 */
public class ScriptCompleter implements AutoCompleter{
    @Override
    public boolean isActivated() {
        return false;
    }

    @Override
    public Stream<Suggestion> get(CompletionPosition cp) {
        return null;
    }
}
