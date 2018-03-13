package edu.kit.iti.formal.psdbg.gui.actions.acomplete;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AutoCompletionController {
    @Getter
    @Setter
    protected List<AutoCompleter> completers = new ArrayList<>();

    public List<Suggestion> getSuggestions(CompletionPosition cp) {
        return completers.stream()
                .filter(AutoCompleter::isActivated)
                .flatMap(c -> c.get(cp))
                .sorted()
                .collect(Collectors.toList());
    }

}