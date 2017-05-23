package edu.kit.formal.proofscriptparser.lint;

import edu.kit.formal.proofscriptparser.ast.ASTNode;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (23.05.17)
 */
@Data
@RequiredArgsConstructor
public class LintProblem {
    @Getter
    private final Issue issue;

    @Getter
    private List<Token> markTokens = new ArrayList<>();

    public Token getFirstToken() {
        if (markTokens.size() == 0)
            return null;
        return markTokens.get(0);
    }

    public int getLineNumber() {
        if (getFirstToken() == null)
            return -1;
        return getFirstToken().getLine();
    }

    public static LintProblem create(Issue issue, Token... markTokens) {
        LintProblem lp = new LintProblem(issue);
        return lp.tokens(markTokens);
    }

    public String getMessage() {
        return String.format(getIssue().getValue(), markTokens.toArray());
    }

    public static <T extends ParserRuleContext>
    LintProblem create(Issue issue, ASTNode<T>... nodes) {
        return new LintProblem(issue).nodes(nodes);
    }

    public <T extends ParserRuleContext> LintProblem nodes(ASTNode<T>... nodes) {
        for (ASTNode n : nodes) {
            ParserRuleContext ctx = n.getRuleCtx();
            if (ctx != null)
                markTokens.add(ctx.getStart());
        }
        return this;
    }

    public LintProblem tokens(Token... toks) {
        getMarkTokens().addAll(Arrays.asList(toks));
        return this;
    }
}
