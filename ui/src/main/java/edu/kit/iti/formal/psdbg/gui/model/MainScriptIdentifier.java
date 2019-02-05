package edu.kit.iti.formal.psdbg.gui.model;

import edu.kit.iti.formal.psdbg.parser.ast.ProofScript;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.Collection;
import java.util.Optional;

/**
 * This class holds information to identify the main script (entry point) in a
 * set of {@link edu.kit.iti.formal.psdbg.parser.ast.ProofScript}s.
 *
 * @author Alexander Weigl
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class MainScriptIdentifier {
    private String sourceName;
    private int lineNumber;
    private String scriptName;
    //TODO private ScriptArea scriptArea;
    private JTextArea scriptArea;


    /**
     * Returns the main proof script identified by this instance in the given collection of {@link ProofScript}s
     * <p>
     * Strategy: Find by name, then by line number.
     * Update name, if line number triggers.
     *
     * @param scripts
     * @return
     */
    @Nonnull
    public Optional<ProofScript> find(Collection<ProofScript> scripts) {
        Optional<ProofScript> byName = byName(scripts);
        Optional<ProofScript> byLine = byLine(scripts);
        ProofScript script = byName.orElseGet(() -> byLine.orElse(null));
        if (script == null) return Optional.empty();
        lineNumber = script.getStartPosition().getLineNumber();
        scriptName = script.getName();
        return Optional.of(script);
    }

    public Optional<ProofScript> byLine(Collection<ProofScript> scripts) {
        if (lineNumber < 0)
            return Optional.empty();

        return scripts.stream()
                .filter(ps -> ps.getOrigin().equals(sourceName))
                .filter(ps -> ps.getStartPosition().getLineNumber() == lineNumber)
                .findFirst();
    }

    public Optional<ProofScript> byName(Collection<ProofScript> scripts) {
        if (scriptName == null)
            return Optional.empty();

        return scripts.stream()
                .filter(ps -> ps.getOrigin().equals(sourceName))
                .filter(ps -> ps.getName().equals(scriptName))
                .findFirst();
    }
}