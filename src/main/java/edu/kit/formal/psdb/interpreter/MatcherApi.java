package edu.kit.formal.psdb.interpreter;

import edu.kit.formal.psdb.interpreter.data.GoalNode;
import edu.kit.formal.psdb.interpreter.data.VariableAssignment;
import edu.kit.formal.psdb.parser.ast.Signature;

import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (16.05.17)
 */
public interface MatcherApi<T> {
    List<VariableAssignment> matchLabel(GoalNode<T> currentState, String label);
    List<VariableAssignment> matchSeq(GoalNode<T> currentState, String data, Signature sig);

}
