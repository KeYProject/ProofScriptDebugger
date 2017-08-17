package edu.kit.formal.psdb.interpreter;

import de.uka.ilkd.key.api.ScriptApi;
import de.uka.ilkd.key.api.VariableAssignments;
import de.uka.ilkd.key.logic.Name;
import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.logic.op.SchemaVariable;
import de.uka.ilkd.key.proof.ApplyStrategy;
import de.uka.ilkd.key.proof.Goal;
import de.uka.ilkd.key.proof.Proof;
import de.uka.ilkd.key.proof.init.Profile;
import de.uka.ilkd.key.rule.NoPosTacletApp;
import de.uka.ilkd.key.rule.Taclet;
import de.uka.ilkd.key.rule.TacletApp;
import edu.kit.formal.psdb.interpreter.data.GoalNode;
import edu.kit.formal.psdb.interpreter.data.KeyData;
import edu.kit.formal.psdb.interpreter.data.Value;
import edu.kit.formal.psdb.interpreter.data.VariableAssignment;
import edu.kit.formal.psdb.parser.ast.Signature;
import edu.kit.formal.psdb.parser.ast.TermLiteral;
import edu.kit.formal.psdb.parser.types.SimpleType;
import edu.kit.formal.psdb.parser.ast.Variable;
import edu.kit.formal.psdb.parser.types.Type;
import org.key_project.util.collection.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Interface to KeY Matcher Api
 *
 * @author S. Grebing
 */
public class KeYMatcher implements MatcherApi<KeyData> {
    private static final Name CUT_TACLET_NAME = new Name("cut");
    private ScriptApi scrapi;
    private Interpreter<KeyData> interpreter;
    private List<MatchResult> resultsFromLabelMatch;

    public KeYMatcher(ScriptApi scrapi, Interpreter<KeyData> interpreter) {
        this.scrapi = scrapi;
        this.interpreter = interpreter;
    }

    /**
     * @param proof
     * @param kd
     * @param term
     * @return null if the term is not derivable or the new state
     */
    public static GoalNode<KeyData> isDerivable(Proof proof, GoalNode<KeyData> kd, Term term) {
        Taclet cut = proof.getEnv().getInitConfigForEnvironment().lookupActiveTaclet(CUT_TACLET_NAME);
        TacletApp app = NoPosTacletApp.createNoPosTacletApp(cut);
        SchemaVariable sv = (SchemaVariable) app.uninstantiatedVars().iterator().next();
        TacletApp cutApp = app.addCheckedInstantiation(sv, term, proof.getServices(), true);
        ImmutableList<Goal> goalList = kd.getData().getGoal().apply(cutApp);

        if (goalList.size() != 2) {
            throw new IllegalStateException("Cut created more than two sub goals!");
        }

        // apply auto on the first (hope that is the "to show" case.
        Goal toShow = goalList.head();

        //apply auto to close this branch, from AutoCommand.java
        Profile profile = proof.getServices().getProfile();
        ApplyStrategy applyStrategy = new ApplyStrategy(profile.getSelectedGoalChooserBuilder().create());
        applyStrategy.start(proof, toShow);

        //check if there are no goals under toShow
        boolean isDerivable = proof.getSubtreeGoals(toShow.node()).size() == 0;

        if (isDerivable) {
            KeyData kdataNew = new KeyData(kd.getData(), goalList.head());
            GoalNode<KeyData> newGoalNode = new GoalNode<KeyData>(kd, kdataNew, kdataNew.isClosedNode());
            return newGoalNode;
        } else {
            proof.pruneProof(kd.getData().getNode(), false);
            return null;
        }
    }

    /**
     * If teh label matcher was successful the list contains all match results
     *
     * @return
     */
    public List<MatchResult> getResultsFromLabelMatch() {
        return resultsFromLabelMatch;
    }

    /**
     * Match the label of a goal node
     *
     * @param currentState goal node as possible match cancidate
     * @param label        String representation for regualr expression for label to match
     * @return List of matches if match was sucessful, empty list otherwise
     */
    @Override
    public List<VariableAssignment> matchLabel(GoalNode<KeyData> currentState,
                                               String label) {
        List<VariableAssignment> assignments = new ArrayList<>();
        resultsFromLabelMatch = new ArrayList<>();
        //compile pattern
        Pattern regexpForLabel = Pattern.compile(label);


        String branchLabel = currentState.getData().getBranchingLabel();
        Matcher branchLabelMatcher = regexpForLabel.matcher(branchLabel);

        if (branchLabelMatcher.matches()) {
            VariableAssignment va = new VariableAssignment(null);
            va.declare("$$branchLabel_", SimpleType.STRING);
            va.assign("$$branchLabel_", Value.from(branchLabelMatcher.group()));
            assignments.add(va);
            resultsFromLabelMatch.add(branchLabelMatcher.toMatchResult());
        }

        String controlFlowLines = currentState.getData().getProgramLinesLabel();
        Matcher linesMatcher = regexpForLabel.matcher(controlFlowLines);
        if (linesMatcher.matches()) {
            VariableAssignment va = new VariableAssignment(null);
            va.declare("$$CtrlLinesLabel_", SimpleType.STRING);
            va.assign("$$CtrlLinesLabel_", Value.from(linesMatcher.group()));
            assignments.add(va);
            resultsFromLabelMatch.add(linesMatcher.toMatchResult());
        }

        String controlFlowStmts = currentState.getData().getProgramStatementsLabel();
        Matcher flowStmtsMatcher = regexpForLabel.matcher(controlFlowLines);
        if (flowStmtsMatcher.matches()) {
            VariableAssignment va = new VariableAssignment(null);
            va.declare("$$FlowStmtsLabel_", SimpleType.STRING);
            va.assign("$$FlowStmtsLabel_", Value.from(flowStmtsMatcher.group()));
            assignments.add(va);
            resultsFromLabelMatch.add(flowStmtsMatcher.toMatchResult());
        }

        String ruleLabel = currentState.getData().getRuleLabel();
        Matcher ruleMatcher = regexpForLabel.matcher(ruleLabel);
        if (ruleMatcher.matches()) {
            VariableAssignment va = new VariableAssignment(null);
            va.declare("$$RuleLabel_", SimpleType.STRING);
            va.assign("$$RuleLabel_", Value.from(ruleMatcher.group()));
            assignments.add(va);
            resultsFromLabelMatch.add(ruleMatcher.toMatchResult());
        }


        assignments.forEach(variableAssignment -> System.out.println(variableAssignment));
        return assignments.isEmpty()? null: assignments;
    }

    /**
     * Match against a sequent of a goal node
     * @param currentState
     * @param term
     * @param signature
     * @return
     */
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

        //   System.out.println("Matching: "+term.toString()+"\n================\n"+currentState.getData().getNode().sequent()+"\n================\n");
        List<VariableAssignments> keyMatchResult = scrapi.matchPattern(term, currentState.getData().getNode().sequent(), keyAssignments);
        //empty keyassignments
        // System.out.println("Matched " + keyMatchResult.size() + " goals from " + currentState.toString() + " with pattern " + term);
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
