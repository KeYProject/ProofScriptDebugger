package edu.kit.iti.formal.psdbg.interpreter.funchdl;

import de.uka.ilkd.key.control.AbstractUserInterfaceControl;
import de.uka.ilkd.key.control.DefaultUserInterfaceControl;
import de.uka.ilkd.key.macros.scripts.EngineState;
import de.uka.ilkd.key.macros.scripts.ProofScriptCommand;
import de.uka.ilkd.key.macros.scripts.meta.ProofScriptArgument;
import de.uka.ilkd.key.proof.Goal;
import edu.kit.iti.formal.psdbg.interpreter.Interpreter;
import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.data.State;
import edu.kit.iti.formal.psdbg.interpreter.data.VariableAssignment;
import edu.kit.iti.formal.psdbg.parser.ast.CallStatement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.key_project.util.collection.ImmutableList;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This class handle the call of key's proof script commands, e.g. select or auto;
 *
 * @author Alexander Weigl
 * @version 1 (21.05.17)
 */
@RequiredArgsConstructor
public class ProofScriptCommandBuilder implements CommandHandler<KeyData> {
    @Getter
    private final Map<String, ProofScriptCommand> commands;

    public ProofScriptCommandBuilder() {
        this(new HashMap<>());
    }

    public ProofScriptCommandBuilder(Collection<ProofScriptCommand> scriptCommands) {
        this();
        scriptCommands.forEach(c -> commands.put(c.getName(), c));
    }

    @Override
    public boolean handles(CallStatement call) {
        return commands.containsKey(call.getCommand());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void evaluate(Interpreter<KeyData> interpreter,
                         CallStatement call,
                         VariableAssignment params) {
        ProofScriptCommand c = commands.get(call.getCommand());
        State<KeyData> state = interpreter.getCurrentState();
        GoalNode<KeyData> expandedNode = state.getSelectedGoalNode();
        KeyData kd = expandedNode.getData();
        Map<String, String> map = new HashMap<>();
        params.asMap().forEach((k, v) -> map.put(k.getIdentifier(), v.getData().toString()));
        //System.out.println(map);
        try {
            EngineState estate = new EngineState(kd.getProof());
            estate.setGoal(kd.getNode());
            Object cc = c.evaluateArguments(estate, map);
            AbstractUserInterfaceControl uiControl = new DefaultUserInterfaceControl();
            c.execute(uiControl, cc, estate);

            ImmutableList<Goal> ngoals = kd.getProof().getSubtreeGoals(kd.getNode());
            state.getGoals().remove(expandedNode);
            for (Goal g : ngoals) {
                KeyData kdn = new KeyData(kd, g.node());
                state.getGoals().add(new GoalNode<>(expandedNode, kdn, kdn.isClosedNode()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getHelp(CallStatement call) {
        ProofScriptCommand c = commands.get(call.getCommand());
        StringBuilder html = new StringBuilder();


        html.append("<html><head><style>body {font-family: monospace;}</style></head><body>");
        html.append("Synopsis: <strong>")
                .append(c.getName())
                .append("</strong> ");

        for (Object arg : c.getArguments()) {
            // weigl: something is wrong, my compiler does not like me...!
            ProofScriptArgument a = (ProofScriptArgument) arg;
            html.append(' ');
            if (a.isFlag()) {
                html.append("[").append(a.getName()).append("]");
            } else {
                if (!a.isRequired())
                    html.append("[");
                if (a.getName().startsWith("#"))
                    html.append("&lt;").append(a.getType().getSimpleName().toUpperCase()).append("&gt;");
                else
                    html.append(a.getName()).append("=&lt;").append(a.getType().getSimpleName().toUpperCase()).append("&gt;");
                if (!a.isRequired())
                    html.append("]");
            }
        }


        html.append("<p>").append("short description").append("</p>");

        html.append("<dl>");
        for (Object arg : c.getArguments()) {
            // weigl: something is wrong, my compiler does not like me...!
            ProofScriptArgument a = (ProofScriptArgument) arg;


            html.append("<dt>");
            html.append(a.getName()).append(" : ").append(a.getType().getSimpleName().toUpperCase());
            html.append("</dt>");
            html.append("<dd>");
            if (a.isRequired()) {
                html.append("<em>REQUIRED</em> ");
            }
            html.append("argument description");
            html.append("</dd>");

        }
        html.append("</dl>");

        html.append("</body></html>");
        return html.toString();
    }
}