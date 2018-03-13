package edu.kit.iti.formal.psdbg.gui.actions.acomplete;

import lombok.Data;

/**
 * @author Alexander Weigl
 * @version 1 (13.03.18)
 */
@Data
public class DefaultAutoCompletionController extends AutoCompletionController {
    KeywordCompleter kwCompleter = new KeywordCompleter();
    RuleCompleter ruleCompleter = new RuleCompleter();
    CommandCompleter commandCompleter = new CommandCompleter();
    MacroCompleter macroCompleter = new MacroCompleter();
    ScriptCompleter scriptCompleter = new ScriptCompleter();
    ArgumentCompleter argumentCompleter = new ArgumentCompleter();

    public DefaultAutoCompletionController() {
        completers.add(kwCompleter);
        completers.add(ruleCompleter);
        completers.add(commandCompleter);
        completers.add(macroCompleter);
        completers.add(scriptCompleter);
        completers.add(argumentCompleter);
    }
}
