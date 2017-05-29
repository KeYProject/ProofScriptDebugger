package edu.kit.formal.interpreter;

import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.ScriptSequent;
import edu.kit.formal.interpreter.data.VariableAssignment;
import edu.kit.formal.proofscriptparser.ast.Signature;

import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (16.05.17)
 */
public interface MatcherApi<T> {
    List<VariableAssignment> matchLabel(GoalNode<T> currentState, String label);
    List<VariableAssignment> matchSeq(GoalNode<T> currentState, String data, Signature sig);
}
