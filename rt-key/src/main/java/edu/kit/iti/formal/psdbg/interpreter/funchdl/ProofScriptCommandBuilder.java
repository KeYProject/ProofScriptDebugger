package edu.kit.iti.formal.psdbg.interpreter.funchdl;

import de.uka.ilkd.key.control.AbstractUserInterfaceControl;
import de.uka.ilkd.key.control.DefaultUserInterfaceControl;
import de.uka.ilkd.key.macros.scripts.EngineState;
import de.uka.ilkd.key.macros.scripts.ProofScriptCommand;
import de.uka.ilkd.key.macros.scripts.meta.ProofScriptArgument;
import de.uka.ilkd.key.proof.Goal;
import de.uka.ilkd.key.proof.Node;
import edu.kit.iti.formal.psdbg.interpreter.Interpreter;
import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.data.State;
import edu.kit.iti.formal.psdbg.interpreter.data.VariableAssignment;
import edu.kit.iti.formal.psdbg.parser.ast.CallStatement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.key_project.util.collection.ImmutableList;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class handles the call of key's proof script commands, e.g. select or auto;
 *
 * @author Alexander Weigl
 * @version 1 (21.05.17)
 */
@RequiredArgsConstructor
public class ProofScriptCommandBuilder implements CommandHandler<KeyData> {
    protected static Logger LOGGER = LogManager.getLogger(ProofScriptCommandBuilder.class);

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
    public boolean handles(CallStatement call, KeyData data) {
        return commands.containsKey(call.getCommand());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void evaluate(Interpreter<KeyData> interpreter,
                         CallStatement call,
                         VariableAssignment params, KeyData data) {
        ProofScriptCommand c = commands.get(call.getCommand());
        State<KeyData> state = interpreter.getCurrentState();
        GoalNode<KeyData> expandedNode = state.getSelectedGoalNode();
        KeyData kd = expandedNode.getData();
        Map<String, String> map = new HashMap<>();
        params.asMap().forEach((k, v) -> {
                    System.out.println("v.getData().toString() = " + v.getData().toString());
                    map.put(k.getIdentifier(), v.getData().toString());
                }
        );
        try {
            EngineState estate = new EngineState(kd.getProof());
            estate.setGoal(kd.getNode());
            Object cc = c.evaluateArguments(estate, map); //exception?
            AbstractUserInterfaceControl uiControl = new DefaultUserInterfaceControl();
            c.execute(uiControl, cc, estate);
            //what happens if this is empty -> meaning proof is closed
            state.getGoals().remove(expandedNode);
            if (state.getSelectedGoalNode().equals(expandedNode)) {
                state.setSelectedGoalNode(null);
            }
            ImmutableList<Goal> ngoals = kd.getProof().getSubtreeGoals(kd.getNode());
            if (ngoals.isEmpty()) {
                Node start = expandedNode.getData().getNode();
                //start.leavesIterator()
//                Goal s = kd.getProof().getGoal(start);
                Iterator<Node> nodeIterator = start.leavesIterator();
                while (nodeIterator.hasNext()) {
                    Node n = nodeIterator.next();
                    LOGGER.error(n.isClosed());
                }

            } else {
                for (Goal g : ngoals) {
                    KeyData kdn = new KeyData(kd, g.node());
                    state.getGoals().add(new GoalNode<>(expandedNode, kdn, kdn.isClosedNode()));
                }
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getHelp(CallStatement call) {
        ProofScriptCommand c = commands.get(call.getCommand());
       /* URL res = getClass().getResource("/edu/kit/iti/formal/psdbg/commands/" + call.getCommand() + ".html");
        try {
            return IOUtils.toString(res.toURI(), "utf-8");
        } catch (NullPointerException | IOException | URISyntaxException e) {
            return "No Help found for " + call.getCommand();

        }*/



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
