package edu.kit.iti.formal.psdbg.lint.checkers;

/**
 * Should correspond to <code>issues-list-en.xml</code>
 *
 * @author Alexander Weigl
 * @version 1 (23.05.17)
 */
public enum IssuesId {
    EMPTY_BLOCKS, EQUAL_SCRIPT_NAMES, NEGATED_MATCH_WITH_USING, SUCC_SAME_BLOCK,
    FOREACH_AFTER_THEONLY,
    REDECLARE_VARIABLE, REDECLARE_VARIABLE_TYPE_MISMATCH,
    VARIABLE_NOT_DECLARED,
    VARIABLE_NOT_USED, THEONLY_AFTER_FOREACH
}
