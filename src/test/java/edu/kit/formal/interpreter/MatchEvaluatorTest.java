package edu.kit.formal.interpreter;

import edu.kit.formal.proofscriptparser.ast.Type;
import org.junit.Before;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sarah on 5/23/17.
 */

public class MatchEvaluatorTest {

    MatchEvaluator mEval;

    public MatchEvaluatorTest() {

    }

    @Before
    public void setup() {


        GoalNode parent = new GoalNode(null, "pa");
        parent.addVarDecl("a", Type.INT);
        parent.addVarDecl("b", Type.INT);
        VariableAssignment va = parent.getAssignments();
        va.setVarValue("a", Value.from(1));
        va.setVarValue("b", Value.from(1));
        GoalNode selected = new GoalNode(parent, "selg");
        mEval = new MatchEvaluator(va, selected, new PseudoMatcher());
        //eval = new Evaluator(selected.getAssignments(), selected);
        //eval.setMatcher(new PseudoMatcher());
    }


    static class PseudoMatcher implements MatcherApi {
        @Override
        public List<VariableAssignment> matchLabel(GoalNode currentState, String label) {
            Pattern p = Pattern.compile(label, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(currentState.getSequent());
            return m.matches()
                    ? Collections.singletonList(new VariableAssignment())
                    : Collections.emptyList();
        }

        @Override
        public List<VariableAssignment> matchSeq(GoalNode currentState, String data) {
            return Collections.emptyList();
        }
    }
}
