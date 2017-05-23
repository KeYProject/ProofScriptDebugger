package edu.kit.formal.interpreter;

import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (16.05.17)
 */
public interface MatcherApi {
    List<VariableAssignment> matchLabel(GoalNode currentState, String label);
    List<VariableAssignment> matchSeq(GoalNode currentState, String data);
}