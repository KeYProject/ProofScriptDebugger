package edu.kit.formal.proofscriptparser.lint;

import edu.kit.formal.proofscriptparser.ast.ASTNode;
import edu.kit.formal.proofscriptparser.ast.GoalSelector;
import edu.kit.formal.proofscriptparser.ast.Statement;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (23.05.17)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LintProblem {
    @Getter
    @Setter(AccessLevel.MODULE)
    private String message;

    @Getter
    @Setter(AccessLevel.MODULE)
    private char level;

    @Getter
    @Setter(AccessLevel.MODULE)
    private int number;

    @Getter
    @Setter(AccessLevel.MODULE)
    private String lintRule;

    @Getter
    private List<ASTNode> affectedNodes = new ArrayList<>();

    public static LintProblem create(String ruleName) {
        LintProblem lp = new LintProblem();
        lp.setLintRule(ruleName);
        return lp;
    }

    public LintProblem level(char w) {
        setLevel(w);
        return this;
    }

    public LintProblem message(String s) {
        setMessage(s);
        return this;
    }

    public LintProblem nodes(ASTNode... nodes) {
        getAffectedNodes().addAll(Arrays.asList(nodes));
        return this;
    }
}
