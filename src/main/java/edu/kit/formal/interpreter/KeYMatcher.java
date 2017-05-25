package edu.kit.formal.interpreter;

import de.uka.ilkd.key.api.ProjectedNode;
import de.uka.ilkd.key.api.ScriptApi;
import de.uka.ilkd.key.api.VariableAssignments;
import edu.kit.formal.proofscriptparser.ast.Signature;

import java.util.List;
import java.util.Map;

/**
 * Interface to KeY Matcher Api
 *
 * @author S. Grebing
 */
public class KeYMatcher implements MatcherApi {

    ScriptApi scrapi;
    Interpreter interpreter;

    public KeYMatcher(ScriptApi scrapi, Interpreter interpreter) {
        this.scrapi = scrapi;
        this.interpreter = interpreter;
    }

    @Override
    public List<VariableAssignment> matchLabel(GoalNode currentState, String label) {

        return null;
    }

    @Override
    public List<VariableAssignment> matchSeq(GoalNode currentState, String data, Signature signature) {
        VariableAssignment assignments = currentState.getAssignments();
        ProjectedNode pNode = currentState.getActualKeYGoalNode();
        //Gemeinsame VariableAssignments
        //scrapi.matchPattern(data, pNode.getSequent(), assignments);

        return null;
    }

    /**
     * Transforms a KeY Variable Assignment into an assignment for the interpreter
     *
     * @param keyAssignments
     * @return
     */
    public VariableAssignment from(VariableAssignments keyAssignments) {
        VariableAssignment interpreterAssignments = new VariableAssignment(null);

        Map<String, VariableAssignments.VarType> keyTypeMap = keyAssignments.getTypeMap();
        //find type needs to be rewritten
        keyTypeMap.forEach((k, v) -> interpreterAssignments.addVarDecl(k, interpreter.transKeYFormType(v.getKeYDeclarationPrefix())));

        /*interpreterAssignments.getTypes().forEach((k, v) -> {
            try {
                //TODO cast is not valid
                interpreterAssignments.setVarValue(k, (Value) keyAssignments.getVarValue(k));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        */
        return interpreterAssignments;
    }
}
