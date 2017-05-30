package edu.kit.formal.interpreter;

import de.uka.ilkd.key.api.ScriptApi;
import de.uka.ilkd.key.api.VariableAssignments;
import de.uka.ilkd.key.logic.Term;
import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.KeyData;
import edu.kit.formal.interpreter.data.Value;
import edu.kit.formal.interpreter.data.VariableAssignment;
import edu.kit.formal.proofscriptparser.ast.Signature;
import edu.kit.formal.proofscriptparser.ast.TermLiteral;
import edu.kit.formal.proofscriptparser.ast.Type;
import edu.kit.formal.proofscriptparser.ast.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

        VariableAssignment interpreterAssignments = currentState.getAssignments();
        VariableAssignments keyAssignments = new VariableAssignments(null);
        interpreterAssignments.getTypes().forEach((k, v) -> keyAssignments.getTypeMap().put(k.getIdentifier(), interpreter.getTypeConversionBiMap().get(v)));
        interpreterAssignments.getValues().forEach((k, v) -> {
            try {
                keyAssignments.addAssignment(k.getIdentifier(), v);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Set<Map.Entry<Variable, Type>> sigEntrySet = signature.entrySet();
        sigEntrySet.forEach(e -> {
            try {
                keyAssignments.addType(e.getKey().getIdentifier(), interpreter.getTypeConversionBiMap().get(e.getValue()));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });


        List<VariableAssignments> keyMatchResult = scrapi.matchPattern(term, currentState.getData().getNode().sequent(), keyAssignments);
        //empty keyassigmnments
        System.out.println("Matched " + keyMatchResult.size() + " goals from " + currentState.toString() + " with pattern " + term);
        List<VariableAssignment> transformedMatchResults = new ArrayList<>();
        for (VariableAssignments mResult : keyMatchResult) {
            transformedMatchResults.add(from(mResult));
        }
        //keyMatchResult.forEach(r -> transformedMatchResults.add(from(r)));
        return transformedMatchResults;

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
        keyTypeMap.entrySet().forEach(e -> interpreterAssignments.declare(e.getKey(), interpreter.getTypeConversionBiMap().inverse().get(e.getValue())));
        keyTypeMap.keySet().forEach(k -> {
            try {
                interpreterAssignments.assign(k, Value.from(from((Term) keyAssignments.getVarValue(k))));
                //System.out.println(keyAssignments.getVarValue(k));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return interpreterAssignments;
    }

    private TermLiteral from(Term t) {
        return new TermLiteral(t.toString());
    }
}
