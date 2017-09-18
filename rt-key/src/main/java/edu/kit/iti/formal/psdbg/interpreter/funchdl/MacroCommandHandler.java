package edu.kit.iti.formal.psdbg.interpreter.funchdl;

import de.uka.ilkd.key.api.KeYApi;
import de.uka.ilkd.key.control.AbstractUserInterfaceControl;
import de.uka.ilkd.key.control.DefaultUserInterfaceControl;
import de.uka.ilkd.key.macros.ProofMacro;
import de.uka.ilkd.key.macros.ProofMacroFinishedInfo;
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
 * @author Alexander Weigl
 * @version 1 (21.05.17)
 */
@RequiredArgsConstructor
public class MacroCommandHandler implements CommandHandler<KeyData> {
    @Getter
    private final Map<String, ProofMacro> macros;

    public MacroCommandHandler() {
        macros = new HashMap<>();
    }

    public MacroCommandHandler(Collection<ProofMacro> macros) {
        this();
        macros.forEach(m -> this.macros.put(m.getScriptCommandName(), m));
    }


    @Override
    public boolean handles(CallStatement call) throws IllegalArgumentException {
        return macros.containsKey(call.getCommand());
    }

    @Override
    public void evaluate(Interpreter<KeyData> interpreter,
                         CallStatement call,
                         VariableAssignment params) {
        //ProofMacro m = macros.get(call.getCommand());
        ProofMacro macro = KeYApi.getMacroApi().getMacro(call.getCommand());
        ProofMacroFinishedInfo info = ProofMacroFinishedInfo.getDefaultInfo(macro,
                interpreter.getCurrentState().getSelectedGoalNode().getData().getProof());

        State<KeyData> state = interpreter.getCurrentState();
        GoalNode<KeyData> expandedNode = state.getSelectedGoalNode();


        try {
            //uiControl.taskStarted(new DefaultTaskStartedInfo(TaskStartedInfo.TaskKind.Macro, macro.getName(), 0));
            synchronized (macro) {
                AbstractUserInterfaceControl uiControl = new DefaultUserInterfaceControl();
                info = macro.applyTo(uiControl, expandedNode.getData().getNode(), null, uiControl);
                ImmutableList<Goal> ngoals = expandedNode.getData().getProof().getSubtreeGoals(expandedNode.getData().getNode());

                state.getGoals().remove(expandedNode);
                for (Goal g : ngoals) {
                    KeyData kdn = new KeyData(expandedNode.getData(), g.node());
                    state.getGoals().add(new GoalNode<>(expandedNode, kdn, kdn.isClosedNode()));
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
