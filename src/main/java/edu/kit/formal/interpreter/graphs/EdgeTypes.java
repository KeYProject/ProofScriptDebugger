package edu.kit.formal.interpreter.graphs;

/**
 * Edge Types a state graph and control flow graph may have
 */
public enum EdgeTypes {
    STEP_INTO, STEP_OVER, STEP_BACK, STEP_RETURN, STEP_OVER_COND, STATE_FLOW;
}
