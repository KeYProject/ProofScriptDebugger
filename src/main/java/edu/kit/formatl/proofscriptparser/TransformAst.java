package edu.kit.formatl.proofscriptparser;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formal.proofscriptparser.ScriptLanguageVisitor;
import edu.kit.formatl.proofscriptparser.ast.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.*;
import sun.net.idn.Punycode;

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
        if (ctx.signature != null)
            s.setSignature((Signature) ctx.signature.accept(this));
        s.setBody((Statements) ctx.body.accept(this));
        scripts.add(s);
        return s;
    }

    @Override public Signature visitArgList(ScriptLanguageParser.ArgListContext ctx) {
        Signature signature = new Signature();
        for (ScriptLanguageParser.VarDeclContext decl : ctx.varDecl()) {
            signature.put(new Variable(decl.name), decl.type.getText());
        }
        return signature;
    }

    @Override public Object visitVarDecl(ScriptLanguageParser.VarDeclContext ctx) {
        throw new IllegalStateException("not implemented");
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
        assign.setRhs(new Variable(ctx.variable));
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

    private UnaryExpression createUnaryExpression(ParserRuleContext ctx, ScriptLanguageParser.ExpressionContext expression, Operator op){
        UnaryExpression ue = new UnaryExpression();
        ue.setExpression((Expression) expression.accept(this));
        ue.setOperator(op);
        return ue;
    }
    @Override public Object visitExprMinus(ScriptLanguageParser.ExprMinusContext ctx) {
       return createUnaryExpression(ctx, ctx.expression(), Operator.MINUS);
    }

    @Override public Object visitExprNegate(ScriptLanguageParser.ExprNegateContext ctx) {
        return createUnaryExpression(ctx, ctx.expression(), Operator.NEG);

    }

    @Override public Object visitExprComparison(ScriptLanguageParser.ExprComparisonContext ctx) {
        return createBinaryExpression(ctx, ctx.expression(), findOperator(ctx.op.getText()));
    }

    private Operator findOperator(String n) {
        for (Operator op : Operator.values()) {
            if (op.symbol().equals(n))
                return op;
        }
        throw new IllegalStateException("Operator " + n + " not defined");
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
        return new Variable(ctx.ID().getSymbol());
    }

    @Override public Object visitLiteralDigits(ScriptLanguageParser.LiteralDigitsContext ctx) {
        return new IntegerLiteral(ctx.DIGITS().getSymbol());
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
        throw new IllegalStateException("not implemented");
    }

    @Override public Object visitRepeatStmt(ScriptLanguageParser.RepeatStmtContext ctx) {
        RepeatStatement rs = new RepeatStatement();
        rs.setRuleContext(ctx);
        rs.setBody((Statements) ctx.stmtList().accept(this));
        return rs;
    }

    @Override public Object visitCasesStmt(ScriptLanguageParser.CasesStmtContext ctx) {
        CasesStatement cases = new CasesStatement();
        ctx.casesList().forEach(c -> cases.getCases().add((CaseStatement) c.accept(this)));
        return cases;
    }

    @Override public Object visitCasesList(ScriptLanguageParser.CasesListContext ctx) {
        CaseStatement caseStatement = new CaseStatement();
        caseStatement.setRuleContext(ctx);
        caseStatement.setGuard((Expression) ctx.expression().accept(this));
        caseStatement.setBody((Statements) ctx.stmtList().accept(this));
        return caseStatement;
    }

    @Override public Object visitForEachStmt(ScriptLanguageParser.ForEachStmtContext ctx) {
        ForeachStatement f = new ForeachStatement();
        f.setRuleContext(ctx);
        f.setBody((Statements) ctx.stmtList().accept(this));
        return f;
    }

    @Override public Object visitTheOnlyStmt(ScriptLanguageParser.TheOnlyStmtContext ctx) {
        TheOnlyStatement f = new TheOnlyStatement();
        f.setRuleContext(ctx);
        f.setBody((Statements) ctx.stmtList().accept(this));
        return f;
    }

    @Override public Object visitScriptCommand(ScriptLanguageParser.ScriptCommandContext ctx) {
        ScriptCallStatement scs = new ScriptCallStatement();
        scs.setRuleContext(ctx);
        scs.setCommand(ctx.cmd.getText());
        int i = 1;
        if (ctx.parameters() != null) {
            ctx.parameters().accept(this);
        }
        return scs;
    }

    @Override public Object visitParameters(ScriptLanguageParser.ParametersContext ctx) {
        Parameters params = new Parameters();
        int i = 1;
        for (ScriptLanguageParser.ParameterContext pc : ctx.parameter()) {
            Expression expr = (Expression) pc.expr.accept(this);
            Variable key = pc.pname != null ? new Variable(pc.pname) : new Variable("#" + (i++));
            params.put(key, expr);
        }
        return params;
    }

    @Override public Object visitParameter(ScriptLanguageParser.ParameterContext ctx) {
        return null;
    }

    @Override public Object visit(ParseTree parseTree) {
        throw new IllegalStateException("not implemented");
    }

    @Override public Object visitChildren(RuleNode ruleNode) {
        throw new IllegalStateException("not implemented");
    }

    @Override public Object visitTerminal(TerminalNode terminalNode) {
        throw new IllegalStateException("not implemented");
    }

    @Override public Object visitErrorNode(ErrorNode errorNode) {
        throw new IllegalStateException("not implemented");
    }
}
