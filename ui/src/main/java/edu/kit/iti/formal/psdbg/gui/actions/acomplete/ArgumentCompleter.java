package edu.kit.iti.formal.psdbg.gui.actions.acomplete;

import de.uka.ilkd.key.api.KeYApi;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.exceptions.NoCallHandlerException;
import edu.kit.iti.formal.psdbg.interpreter.funchdl.*;
import edu.kit.iti.formal.psdbg.parser.ast.CallStatement;
import edu.kit.iti.formal.psdbg.parser.ast.Parameters;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

/**
 * @author Alexander Weigl
 * @version 1 (13.03.18)
 */
@AllArgsConstructor

public class ArgumentCompleter implements AutoCompleter {
    @Getter
    private final ProofScriptHandler psh = new ProofScriptHandler();
    @Getter
    private final MacroCommandHandler pmh = new MacroCommandHandler();
    @Getter
    private final RuleCommandHandler pmr = new RuleCommandHandler();
    @Getter
    private ProofScriptCommandBuilder pmc = new ProofScriptCommandBuilder();
    @Getter
    private DefaultLookup<KeyData> lookup = new DefaultLookup(psh, pmh, pmr, pmc);
    private KeyData keyData;

    public ArgumentCompleter() {
        KeYApi.getMacroApi().getMacros().forEach(m ->
                pmh.getMacros().put(m.getScriptCommandName(), m));
        KeYApi.getScriptCommandApi().getScriptCommands().forEach(m ->
                pmc.getCommands().put(m.getName(), m));
    }

    public void setDefaultKeyData(KeyData data) {
        this.keyData = data;
    }

    @Override
    public Stream<Suggestion> get(CompletionPosition cp) {
        String cmd = cp.getCommand();
        if (!cmd.isEmpty()) {
            CallStatement call = new CallStatement(cmd, new Parameters());
            try {
                return lookup.getBuilder(call, keyData).getArguments(cmd)
                        .filter(s -> s.startsWith(cp.getPrefix()))
                        .map(Suggestion::argument);
            } catch (NoCallHandlerException e) {
            }
        }
        return Stream.of();
    }
}
