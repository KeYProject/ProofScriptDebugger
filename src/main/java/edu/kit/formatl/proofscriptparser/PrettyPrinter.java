package edu.kit.formatl.proofscriptparser;

import edu.kit.formatl.proofscriptparser.ast.*;

import java.util.Iterator;
import java.util.Map;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
public class PrettyPrinter extends DefaultASTVisitor<Void> {
    private static final int MAX_WIDTH = 80;
    private final StringBuilder s = new StringBuilder();
    private int indentation = 0;
    private int currentLineLength;

    @Override public String toString() {
        return s.toString();
    }

    @Override public Void visit(ProofScript proofScript) {
        s.append("script");
        s.append(proofScript.getName());
        s.append(" (");
        proofScript.getSignature().accept(this);
        s.append(") {");
        proofScript.getBody().accept(this);
        nl();
        s.append("}");
        return null;
    }

    @Override public Void visit(Signature sig) {
        Iterator<Map.Entry<Variable, String>> iter = sig.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Variable, String> next = iter.next();
            next.getKey().accept(this);
            s.append(" : ").append(next.getValue());
            if (iter.hasNext())
                s.append(", ");
        }
        return null;
    }

    @Override public Void visit(AssignmentStatement assign) {
        assign.getRhs().accept(this);
        s.append(" := ");
        assign.getLhs().accept(this);
        s.append(";");
        return null;
    }

    @Override public Void visit(BinaryExpression e) {
        boolean left = e.getPrecedence() < e.getLeft().getPrecedence();
        boolean right = e.getPrecedence() < e.getRight().getPrecedence();

        if (left) {
            s.append("(");
        }
        e.getLeft().accept(this);
        if (left) {
            s.append(")");
        }

        s.append(e.getOperator().symbol());

        if (right) {
            s.append("(");
        }
        e.getRight().accept(this);
        if (right) {
            s.append(")");
        }

        return super.visit(e);
    }

    @Override public Void visit(MatchExpression match) {
        s.append("match ");
        String prefix = getWhitespacePrefix();
        if (match.getTerm() != null) {
            match.getTerm().accept(this);
        }

        if (!match.getSignature().isEmpty()) {

            if (getCurrentLineLength() > MAX_WIDTH) {
                s.append("\n").append(prefix);
            }
            else {
                s.append(" ");
            }

            s.append("using [");
            match.getSignature().accept(this);
            s.append("]");
        }

        return null;
    }

    @Override public Void visit(CasesStatement casesStatement) {
        s.append("cases {");
        incrementIndent();
        nl();
        for (CaseStatement c : casesStatement.getCases()) {
            c.accept(this);
            nl();
        }
        if(casesStatement.getDefaultCase()!=null) {
            s.append("default {");
            casesStatement.getDefaultCase().accept(this);
            cl();
            s.append("}");
        }
        decrementIndent();
        cl();
        s.append("}");
        return null;
    }

    /**
     * clear line
     */
    private void cl() {
        int i = s.length() - 1;
        while (Character.isWhitespace(s.charAt(i))) {
            s.deleteCharAt(i--);
        }
        nl();
    }

    @Override public Void visit(CaseStatement caseStatement) {
        s.append("case ");
        caseStatement.getGuard().accept(this);
        s.append(" {");
        caseStatement.getBody().accept(this);
        nl();
        s.append("}");
        return super.visit(caseStatement);
    }

    @Override public Void visit(CallStatement call) {
        s.append(call.getCommand()).append(' ');
        call.getParameters().accept(this);
        s.append(";");
        return null;
    }

    @Override public Void visit(TermLiteral termLiteral) {
        s.append(String.format("`%s`", termLiteral.getText()));
        return super.visit(termLiteral);
    }

    @Override public Void visit(StringLiteral stringLiteral) {
        s.append(String.format("\"%s\"", stringLiteral.getText()));
        return super.visit(stringLiteral);
    }

    @Override public Void visit(Variable variable) {
        s.append(variable.getIdentifier());
        return super.visit(variable);
    }

    @Override public Void visit(BooleanLiteral booleanLiteral) {
        s.append(booleanLiteral.getValue());
        return super.visit(booleanLiteral);
    }

    @Override public Void visit(Statements statements) {
        if (statements.size() == 0)
            return null;
        incrementIndent();
        for (Statement s : statements) {
            nl();
            s.accept(this);
        }
        decrementIndent();
        return super.visit(statements);
    }

    @Override public Void visit(IntegerLiteral integer) {
        s.append(integer.getValue().toString());
        return null;
    }

    @Override public Void visit(TheOnlyStatement theOnly) {
        s.append("theonly {");
        theOnly.getBody().accept(this);
        cl();
        s.append("}");
        return null;
    }

    @Override public Void visit(ForeachStatement foreach) {
        s.append("foreach {");
        foreach.getBody().accept(this);
        cl();
        s.append("}");
        return null;
    }

    @Override public Void visit(RepeatStatement repeat) {
        s.append("repeat");
        s.append("{");
        repeat.getBody().accept(this);
        cl();
        s.append("}");
        return null;

    }

    @Override public Void visit(Parameters parameters) {
        int nl = getLastNewline();
        String indention = getWhitespacePrefix();
        Iterator<Map.Entry<Variable, Expression>> iter = parameters.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry<Variable, Expression> entry = iter.next();
            entry.getKey().accept(this);
            s.append("=");
            entry.getValue().accept(this);
            if (iter.hasNext()) {
                int currentLineLength = getCurrentLineLength();
                if (currentLineLength > 80) {
                    s.append("\n").append(indention);
                }
                else {
                    s.append(" ");
                }
            }
        }
        return null;
    }

    private int getLastNewline() {
        int posnewline = s.length() - 1;
        while (s.charAt(posnewline) != '\n') {
            posnewline--;
        }
        return posnewline;
    }

    private String getWhitespacePrefix() {
        return s.substring(getLastNewline() + 1).replaceAll("\\w", " ");
    }

    @Override public Void visit(UnaryExpression unaryExpression) {
        return super.visit(unaryExpression);
    }

    private void nl() {
        s.append('\n');
        for (int i = 0; i < indentation; i++)
            s.append('\t');
    }

    private void decrementIndent() {
        indentation--;
    }

    private void incrementIndent() {
        indentation++;
    }

    public int getCurrentLineLength() {
        return s.length() - getLastNewline();
    }
}
