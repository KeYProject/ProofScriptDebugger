package edu.kit.formal.interpreter.funchdl;

import de.uka.ilkd.key.api.ProjectedNode;
import de.uka.ilkd.key.api.ProofScriptCommandCall;
import de.uka.ilkd.key.api.ScriptApi;
import de.uka.ilkd.key.api.VariableAssignments;
import de.uka.ilkd.key.macros.scripts.EngineState;
import de.uka.ilkd.key.macros.scripts.RuleCommand;
import de.uka.ilkd.key.macros.scripts.ScriptException;
import de.uka.ilkd.key.rule.Rule;
import edu.kit.formal.interpreter.Interpreter;
import edu.kit.formal.interpreter.VariableAssignment;
import edu.kit.formal.proofscriptparser.ast.CallStatement;
import edu.kit.formal.proofscriptparser.ast.Parameters;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexander Weigl
 * @version 1 (21.05.17)
 */
@RequiredArgsConstructor
public class RuleCommandHandler implements CommandHandler {
    @Getter
    private final Map<String, Rule> rules;

    public RuleCommandHandler() {
        this(new HashMap<>());
    }

    @Override
    public boolean handles(CallStatement call) throws IllegalArgumentException {
        return rules.containsKey(call.getCommand());
    }

    @Override
    public void evaluate(Interpreter interpreter,
                         CallStatement call,
                         VariableAssignment params) {
        Rule r = rules.get(call.getCommand());
        RuleCommand rc = new RuleCommand();
        RuleCommand.Parameters rcp = new RuleCommand.Parameters();
        //TODO fill in rcp
        EngineState es = interpreter.getEngineState();
        ScriptApi sa = interpreter.getScriptApi();
        VariableAssignments assignments = new VariableAssignments();
        ProjectedNode onNode = null;
        try {
            sa.executeScriptCommand(
                    new ProofScriptCommandCall<RuleCommand.Parameters>(rc, rcp),
                    onNode, assignments);
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
