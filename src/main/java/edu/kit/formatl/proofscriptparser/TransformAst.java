package edu.kit.formatl.proofscriptparser;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formal.proofscriptparser.ScriptLanguageVisitor;
import edu.kit.formatl.proofscriptparser.ast.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.*;

import java.util.*;

/**
 * @author Alexander Weigl
 * @version 1 (27.04.17)
 */
public class TransformAst implements ScriptLanguageVisitor<Object> {
    private List<ProofScript> scripts = new ArrayList<>(10);

    public List<ProofScript> getScripts() {
        return scripts;
    }

    @Override public ProofScript visitStart(ScriptLanguageParser.StartContext ctx) {
        ProofScript s = new ProofScript();
        s.setName(ctx.name.getText());
        s.setRuleContext(ctx);
        s.setParameters((Map<String, String>) ctx.paramters.accept(this));
        s.setBody((Statements) ctx.body.accept(this));
        scripts.add(s);
        return s;
    }

    @Override public Map<String, String> visitArgList(ScriptLanguageParser.ArgListContext ctx) {
        Map<String, String> signature = new LinkedHashMap<>();
        for (ScriptLanguageParser.VarDeclContext decl : ctx.varDecl()) {
            signature.put(decl.ID().getText(), decl.type().getText());
        }
        return signature;
    }

    @Override public Object visitVarDecl(ScriptLanguageParser.VarDeclContext ctx) {
        return null;
    }

    @Override public Object visitType(ScriptLanguageParser.TypeContext ctx) {
        return null;
    }

    @Override public Statements visitStmtList(ScriptLanguageParser.StmtListContext ctx) {
        Statements statements = new Statements();
        for (ScriptLanguageParser.StatementContext stmt : ctx.statement()) {
            statements.add((Statement) stmt.accept(this));
        }
        return statements;
    }

    @Override public Object visitStatement(ScriptLanguageParser.StatementContext ctx) {
        return ctx.getChild(0).accept(this);
    }

    @Override public Object visitAssignment(ScriptLanguageParser.AssignmentContext ctx) {
        AssignmentStatement assign = new AssignmentStatement();
        assign.setRuleContext(ctx);
        assign.setRhs(ctx.variable.getText());
        assign.setLhs((Expression) ctx.expression().accept(this));
        return assign;
    }

    @Override public BinaryExpression visitExprMultiplication(ScriptLanguageParser.ExprMultiplicationContext ctx) {
        return createBinaryExpression(ctx, ctx.expression(), Operator.MULTIPLY);
    }

    private BinaryExpression createBinaryExpression(ParserRuleContext ctx,
            List<ScriptLanguageParser.ExpressionContext> expression, Operator op) {
        BinaryExpression be = new BinaryExpression();
        be.setLeft((Expression) expression.get(0).accept(this));
        be.setRight((Expression) expression.get(1).accept(this));
        be.setOperator(op);
        return be;
    }

    @Override public Object visitExprMinus(ScriptLanguageParser.ExprMinusContext ctx) {
        return null;
    }

    @Override public Object visitExprNegate(ScriptLanguageParser.ExprNegateContext ctx) {
        return null;
    }

    @Override public Object visitExprComparison(ScriptLanguageParser.ExprComparisonContext ctx) {
        return createBinaryExpression(ctx, ctx.expression(), findOperator(ctx.op.getText()));
    }

    private Operator findOperator(String n) {
        for (Operator op : Operator.values()) {
            if (op.symbol().equals(n))
                return op;
        }
        return null;
    }

    @Override public Object visitExprEquality(ScriptLanguageParser.ExprEqualityContext ctx) {
        return createBinaryExpression(ctx, ctx.expression(), findOperator(ctx.op.getText()));

    }

    @Override public Object visitExprMatch(ScriptLanguageParser.ExprMatchContext ctx) {
        return ctx.matchPattern().accept(this);
    }

    @Override public Object visitExprIMP(ScriptLanguageParser.ExprIMPContext ctx) {
        return createBinaryExpression(ctx, ctx.expression(), Operator.IMPLICATION);

    }

    @Override public Object visitExprParen(ScriptLanguageParser.ExprParenContext ctx) {
        return ctx.expression().accept(this);
    }

    @Override public Object visitExprOr(ScriptLanguageParser.ExprOrContext ctx) {
        return createBinaryExpression(ctx, ctx.expression(), Operator.OR);

    }

    @Override public Object visitExprLineOperators(ScriptLanguageParser.ExprLineOperatorsContext ctx) {
        return createBinaryExpression(ctx, ctx.expression(), findOperator(ctx.op.getText()));

    }

    @Override public Object visitExprAnd(ScriptLanguageParser.ExprAndContext ctx) {
        return createBinaryExpression(ctx, ctx.expression(), Operator.AND);

    }

    @Override public Object visitExprLiterals(ScriptLanguageParser.ExprLiteralsContext ctx) {
        return ctx.literals().accept(this);
    }

    @Override public Object visitExprDivision(ScriptLanguageParser.ExprDivisionContext ctx) {
        return createBinaryExpression(ctx, ctx.expression(), Operator.DIVISION);

    }

    @Override public Object visitLiteralID(ScriptLanguageParser.LiteralIDContext ctx) {
        return null;
    }

    @Override public Object visitLiteralDigits(ScriptLanguageParser.LiteralDigitsContext ctx) {
        return null;
    }

    @Override public Object visitLiteralTerm(ScriptLanguageParser.LiteralTermContext ctx) {
        return new TermLiteral(ctx.getText());
    }

    @Override public Object visitLiteralString(ScriptLanguageParser.LiteralStringContext ctx) {
        return new StringLiteral(ctx.getText());
    }

    @Override public Object visitLiteralTrue(ScriptLanguageParser.LiteralTrueContext ctx) {
        return new BooleanLiteral(false, ctx.TRUE().getSymbol());
    }

    @Override public Object visitLiteralFalse(ScriptLanguageParser.LiteralFalseContext ctx) {
        return new BooleanLiteral(false, ctx.FALSE().getSymbol());
    }

    @Override public Object visitMatchPattern(ScriptLanguageParser.MatchPatternContext ctx) {
        MatchExpression match = new MatchExpression();
        match.setRuleContext(ctx);
        match.setSignature((Map<String, String>) ctx.argList().accept(this));
        if (ctx.TERM_LITERAL() != null) {
            match.setTerm(new TermLiteral(ctx.TERM_LITERAL().getText()));
        }
        else {
            match.setVariable(ctx.ID().getText());
        }
        return match;
    }

    @Override public Object visitScriptVar(ScriptLanguageParser.ScriptVarContext ctx) {
        return null;
    }

    @Override public Object visitRepeatStmt(ScriptLanguageParser.RepeatStmtContext ctx) {
        return null;
    }

    @Override public Object visitCasesStmt(ScriptLanguageParser.CasesStmtContext ctx) {
        return null;
    }

    @Override public Object visitCasesList(ScriptLanguageParser.CasesListContext ctx) {
        return null;
    }

    @Override public Object visitForEachStmt(ScriptLanguageParser.ForEachStmtContext ctx) {
        return null;
    }

    @Override public Object visitTheOnlyStmt(ScriptLanguageParser.TheOnlyStmtContext ctx) {
        return null;
    }

    @Override public Object visitScriptCommand(ScriptLanguageParser.ScriptCommandContext ctx) {
        return null;
    }

    @Override public Object visitCallStmt(ScriptLanguageParser.CallStmtContext ctx) {
        return null;
    }

    @Override public Object visit(ParseTree parseTree) {
        return null;
    }

    @Override public Object visitChildren(RuleNode ruleNode) {
        return null;
    }

    @Override public Object visitTerminal(TerminalNode terminalNode) {
        return null;
    }

    @Override public Object visitErrorNode(ErrorNode errorNode) {
        return null;
    }
}
