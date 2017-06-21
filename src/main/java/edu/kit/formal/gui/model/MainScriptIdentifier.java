package edu.kit.formal.gui.model;

import edu.kit.formal.gui.controls.ScriptArea;
import edu.kit.formal.proofscriptparser.ast.ProofScript;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Collection;
import java.util.Optional;

/**
 * This class holds information to identify the main script (entry point) in a
 * set of {@link edu.kit.formal.proofscriptparser.ast.ProofScript}s.
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
    private ScriptArea scriptArea;


    /**
     * Returns the main proof script identified by this instance in the given collection of {@link ProofScript}s
     * <p>
     * Strategy: Find by name, then by line number.
     * Update name, if line number triggers.
     *
     * @param scripts
     * @return
     */
    public Optional<ProofScript> find(Collection<ProofScript> scripts) {
        Optional<ProofScript> byName = byName(scripts);
        Optional<ProofScript> byLine = byLine(scripts);
        ProofScript script = byName.orElseGet(() -> byLine.orElse(null));
        if (script == null) return null;
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