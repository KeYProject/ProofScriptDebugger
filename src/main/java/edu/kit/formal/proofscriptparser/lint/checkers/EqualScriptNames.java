package edu.kit.formal.proofscriptparser.lint.checkers;

import edu.kit.formal.proofscriptparser.ast.ASTNode;
import edu.kit.formal.proofscriptparser.ast.ProofScript;
import edu.kit.formal.proofscriptparser.lint.Issue;
import edu.kit.formal.proofscriptparser.lint.IssuesRepository;
import edu.kit.formal.proofscriptparser.lint.LintProblem;
import edu.kit.formal.proofscriptparser.lint.LintRule;
import org.antlr.v4.runtime.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alexander Weigl
 * @version 1 (23.05.17)
 */
public class EqualScriptNames implements LintRule {
    private Issue ISSUE = IssuesRepository.getIssue(IssuesId.EQUAL_SCRIPT_NAMES);

    @Override
    public void check(List<? extends ASTNode> node, List<LintProblem> problems) {
        Map<String, Token> scripts = new HashMap<>();
        for (ASTNode s : node) {
            if (s instanceof ProofScript) {
                ProofScript proofScript = (ProofScript) s;
                if (scripts.containsKey(proofScript.getName())) {
                    problems.add(LintProblem.create(ISSUE,
                            scripts.get(proofScript.getName()),
                            proofScript.getRuleContext().get().name));
                }
                scripts.put(proofScript.getName(),
                        proofScript.getRuleContext().get().name);
            }
        }
    }

    @Override
    public String getName() {
        return ISSUE.getRulename();
    }
}