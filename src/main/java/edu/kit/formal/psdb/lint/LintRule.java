package edu.kit.formal.psdb.lint;

import edu.kit.formal.psdb.parser.ast.ASTNode;

import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (23.05.17)
 */
public interface LintRule {
    void check(List<? extends ASTNode> node, List<LintProblem> problems);
    String getName();
}
