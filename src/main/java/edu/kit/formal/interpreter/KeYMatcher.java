package edu.kit.formal.interpreter;

import de.uka.ilkd.key.api.ScriptApi;
import de.uka.ilkd.key.api.VariableAssignments;
import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.KeyData;
import edu.kit.formal.interpreter.data.VariableAssignment;
import edu.kit.formal.proofscriptparser.ast.Signature;

import java.util.List;
import java.util.Map;

/**
 * Interface to KeY Matcher Api
 *
 * @author S. Grebing
 */
public class KeYMatcher implements MatcherApi<KeyData> {
    private ScriptApi scrapi;
    private Interpreter<KeyData> interpreter;

    public KeYMatcher(ScriptApi scrapi, Interpreter<KeyData> interpreter) {
        this.scrapi = scrapi;
        this.interpreter = interpreter;
    }

    @Override
    public List<VariableAssignment> matchLabel(GoalNode<KeyData> currentState,
                                               String label) {
        return null;
    }

    @Override
    public List<VariableAssignment> matchSeq(GoalNode<KeyData> currentState,
                                             String term,
                                             Signature signature) {
        VariableAssignment assignments = currentState.getAssignments();
        //ProjectedNode pNode = currentState.getData();
        //Gemeinsame VariableAssignments
        //scrapi.matchPattern(data, pNode.getData(), assignments);
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
        /* weigl: getKeYDeclarationPrefix is missing in latest jar
            keyTypeMap.forEach((k, v) -> interpreterAssignments.declare(
                new Variable(k),
                interpreter.transKeYFormType(v.getKeYDeclarationPrefix())));
        */
        /*interpreterAssignments.getTypes().forEach((k, v) -> {
            try {
                //TODO cast is not valid
                interpreterAssignments.setVariableValue(k, (Value) keyAssignments.getVarValue(k));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        */
        return interpreterAssignments;
    }
}
