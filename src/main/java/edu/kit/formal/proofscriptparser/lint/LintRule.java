package edu.kit.formal.proofscriptparser.lint;

import edu.kit.formal.proofscriptparser.ast.ASTNode;
import edu.kit.formal.proofscriptparser.ast.ProofScript;

import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (23.05.17)
 */
public interface LintRule {
    void check(List<? extends ASTNode> node, List<LintProblem> problems);
    String getName();
}
