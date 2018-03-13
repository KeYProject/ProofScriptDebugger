package edu.kit.iti.formal.psdbg.gui.actions.acomplete;

import de.uka.ilkd.key.control.KeYEnvironment;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Alexander Weigl
 * @version 1 (13.03.18)
 */
public class RuleCompleter implements AutoCompleter {
    @Getter
    @Setter
    private KeYEnvironment environment;
    private List<Suggestion> suggestions = new ArrayList<>(2048);

    @Override
    public boolean isActivated() {
        return environment != null;
    }

    @Override
    public Stream<Suggestion> get(CompletionPosition cp) {
        return cp.filterByPrefix(suggestions);
    }

    public void setEnvironment(KeYEnvironment environment) {
        if (this.environment != environment) {
            suggestions.clear();
            environment.getInitConfig().activatedTaclets().forEach(
                    t -> suggestions.add(Suggestion.rule(t.name().toString())));
        }
        this.environment = environment;
    }
}
