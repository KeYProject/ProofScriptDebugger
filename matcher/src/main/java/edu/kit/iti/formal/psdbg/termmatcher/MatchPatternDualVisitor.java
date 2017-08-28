package edu.kit.iti.formal.psdbg.termmatcher;

import edu.kit.formal.psdb.termmatcher.MatchPatternBaseVisitor;
import edu.kit.formal.psdb.termmatcher.MatchPatternParser;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Stack;

public abstract class MatchPatternDualVisitor<T, S> extends MatchPatternBaseVisitor<T> {
    private Stack<S> stack = new Stack<>();

    public final T accept(ParserRuleContext ctx, S arg) {
        stack.push(arg);
        T t = ctx.accept(this);
        stack.pop();
        return t;
    }

    @Override
    public T visitSequentAnywhere(MatchPatternParser.SequentAnywhereContext ctx) {
        return visitSequentAnywhere(ctx, stack.peek());
    }

    public abstract T visitSequentAnywhere(MatchPatternParser.SequentAnywhereContext ctx, S peek);


    @Override
    public T visitSequentArrow(MatchPatternParser.SequentArrowContext ctx) {
        return visitSequentArrow(ctx, stack.peek());
    }

    public abstract T visitSequentArrow(MatchPatternParser.SequentArrowContext ctx, S peek);

    @Override
    public final T visitSemiSeqPattern(MatchPatternParser.SemiSeqPatternContext ctx) {
        return visitSemiSeqPattern(ctx, stack.peek());
    }

    protected abstract T visitSemiSeqPattern(MatchPatternParser.SemiSeqPatternContext ctx, S peek);

    @Override
    public T visitNumber(MatchPatternParser.NumberContext ctx) {
        return visitNumber(ctx, stack.peek());
    }

    @Override
    public final T visitDontCare(MatchPatternParser.DontCareContext ctx) {
        return visitDontCare(ctx, stack.peek());
    }

    public abstract T visitDontCare(MatchPatternParser.DontCareContext ctx, S peek);

    /*@Override
    public final T visitStartDontCare(MatchPatternParser.StarDontCareContext ctx) {
        return visitStartDontCare(ctx, stack.peek());
    }

    protected abstract T visitStartDontCare(MatchPatternParser.StartDontCareContext ctx, S peek);
*/
    @Override
    public final T visitSchemaVar(MatchPatternParser.SchemaVarContext ctx) {
        return visitSchemaVar(ctx, stack.peek());
    }

    protected abstract T visitSchemaVar(MatchPatternParser.SchemaVarContext ctx, S peek);

    @Override
    public final T visitFunction(MatchPatternParser.FunctionContext ctx) {
        return visitFunction(ctx, stack.peek());
    }

    @Override
    public final T visitAnywhere(MatchPatternParser.AnywhereContext ctx) {
        return visitAnywhere(ctx, stack.peek());
    }

    protected abstract T visitAnywhere(MatchPatternParser.AnywhereContext ctx, S peek);

    protected abstract T visitFunction(MatchPatternParser.FunctionContext ctx, S peek);

    protected abstract T visitNumber(MatchPatternParser.NumberContext ctx, S peek);

    @Override
    public T visitPlusMinus(MatchPatternParser.PlusMinusContext ctx) {
        return visitPlusMinus(ctx, stack.peek());
    }

    protected abstract T visitPlusMinus(MatchPatternParser.PlusMinusContext ctx, S peek);

    @Override
    public T visitMult(MatchPatternParser.MultContext ctx) {
        return visitMult(ctx, stack.peek());
    }

    protected abstract T visitMult(MatchPatternParser.MultContext ctx, S peek);

    @Override
    public T visitComparison(MatchPatternParser.ComparisonContext ctx) {
        return visitComparison(ctx, stack.peek());
    }

    protected abstract T visitComparison(MatchPatternParser.ComparisonContext ctx, S peek);

    @Override
    public T visitOr(MatchPatternParser.OrContext ctx) {
        return visitOr(ctx, stack.peek());
    }

    protected abstract T visitOr(MatchPatternParser.OrContext ctx, S peek);

    @Override
    public T visitExprNot(MatchPatternParser.ExprNotContext ctx) {
        return visitExprNot(ctx, stack.peek());
    }

    public abstract T visitExprNot(MatchPatternParser.ExprNotContext ctx, S peek);

    @Override
    public T visitExprNegate(MatchPatternParser.ExprNegateContext ctx) {
        return visitExprNegate(ctx, stack.peek());
    }

    public abstract T visitExprNegate(MatchPatternParser.ExprNegateContext ctx, S peek);

    @Override
    public T visitExprParen(MatchPatternParser.ExprParenContext ctx) {
        return visitExprParen(ctx, stack.peek());
    }

    public abstract T visitExprParen(MatchPatternParser.ExprParenContext ctx, S peek);


    @Override
    public T visitImpl(MatchPatternParser.ImplContext ctx) {
        return visitImpl(ctx, stack.peek());
    }

    protected abstract T visitImpl(MatchPatternParser.ImplContext ctx, S peek);

    @Override
    public T visitDivMod(MatchPatternParser.DivModContext ctx) {
        return visitDivMod(ctx, stack.peek());
    }

    protected abstract T visitDivMod(MatchPatternParser.DivModContext ctx, S peek);

    @Override
    public T visitAnd(MatchPatternParser.AndContext ctx) {
        return visitAnd(ctx, stack.peek());
    }

    protected abstract T visitAnd(MatchPatternParser.AndContext ctx, S peek);

    @Override
    public T visitXor(MatchPatternParser.XorContext ctx) {
        return visitXor(ctx, stack.peek());
    }

    protected abstract T visitXor(MatchPatternParser.XorContext ctx, S peek);

    @Override
    public T visitEquality(MatchPatternParser.EqualityContext ctx) {
        return visitEquality(ctx, stack.peek());
    }

    protected abstract T visitEquality(MatchPatternParser.EqualityContext ctx, S peek);
}
    


