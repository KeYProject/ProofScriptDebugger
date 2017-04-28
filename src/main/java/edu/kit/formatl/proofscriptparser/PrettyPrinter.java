package edu.kit.formatl.proofscriptparser;

import edu.kit.formatl.proofscriptparser.ast.*;

import java.util.Iterator;
import java.util.Map;

/**
 * @author Alexander Weigl
 * @version 1 (28.04.17)
 */
public class PrettyPrinter extends DefaultASTVisitor<Void> {
    private final StringBuilder s = new StringBuilder();
    private int indentation = 0;

    @Override public String toString() {
        return s.toString();
    }

    @Override public Void visit(ProofScript proofScript) {
        s.append("script");
        s.append(proofScript.getName());
        s.append(" (");
        printArglist(proofScript.getParameters());
        s.append(") {");
        proofScript.getBody().accept(this);
        nl();
        s.append("}");
        return null;
    }

    private void printArglist(Map<String, String> parameters) {
        Iterator<Map.Entry<String, String>> iter = parameters.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> next = iter.next();
            s.append(next.getKey()).append(" : ").append(next.getValue());
            if (iter.hasNext())
                s.append(", ");
        }
    }

    @Override public Void visit(AssignmentStatement assign) {
        assign.getRhs().accept(this);
        s.append(" := ");
        assign.getLhs().accept(this);
        s.append(";");
        return null;
    }

    @Override public Void visit(BinaryExpression e) {
        e.getLeft().accept(this);
        s.append(e.getOperator().symbol());
        e.getRight().accept(this);
        return super.visit(e);
    }

    @Override public Void visit(MatchExpression matchExpression) {
        return super.visit(matchExpression);
    }

    @Override public Void visit(CasesStatement casesStatement) {
        s.append("cases {");
        incrementIndent();
        nl();
        for (CaseStatement c : casesStatement.getCases()) {
            c.accept(this);
            nl();
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

    @Override public Void visit(ScriptCallStatement call) {
        s.append(call.getCommand()).append(' ');
        call.getParameters().forEach((k, v) -> {
            s.append(k).append(" = ");
            v.accept(this);
            s.append(" ");
        });
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

}
