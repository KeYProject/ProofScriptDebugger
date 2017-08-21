package edu.kit.formal.psdb.termmatcher;

import java.util.Stack;

public abstract class MatchPatternDualVisitor<T, S> extends MatchPatternBaseVisitor<T> {
    private Stack<S> stack = new Stack<>();

    public final T accept(MatchPatternParser.TermPatternContext ctx, S arg) {
        stack.push(arg);
        T t = ctx.accept(this);
        stack.pop();
        return t;
    }

    @Override
    public final T visitSequentPattern(MatchPatternParser.SequentPatternContext ctx) {
        return visitSequentPattern(ctx, stack.peek());
    }

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
    public T visitBinaryOperation(MatchPatternParser.BinaryOperationContext ctx) {
        return visitBinaryOperation(ctx, stack.peek());
    }

    @Override
    public final T visitAnywhere(MatchPatternParser.AnywhereContext ctx) {
        return visitAnywhere(ctx, stack.peek());
    }

    protected abstract T visitAnywhere(MatchPatternParser.AnywhereContext ctx, S peek);

    protected abstract T visitBinaryOperation(MatchPatternParser.BinaryOperationContext ctx, S peek);

    protected abstract T visitFunction(MatchPatternParser.FunctionContext ctx, S peek);

    protected abstract T visitNumber(MatchPatternParser.NumberContext ctx, S peek);

    protected abstract T visitSequentPattern(MatchPatternParser.SequentPatternContext ctx, S peek);
}
