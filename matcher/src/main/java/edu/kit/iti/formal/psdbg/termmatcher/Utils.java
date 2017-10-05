package edu.kit.iti.formal.psdbg.termmatcher;

import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.logic.op.Equality;
import de.uka.ilkd.key.logic.op.Junctor;
import de.uka.ilkd.key.logic.op.Operator;

public class Utils {
    /**
     * Rewrite toString() representation of Term to a parsable version
     *
     * @param formula
     * @return parsable Stringversion of Term
     */
    @Deprecated
    public static String toPrettyTerm(Term formula) {
        StringBuilder sb = new StringBuilder();

        Operator op = formula.op();

        //ugly if/else
        if (op.equals(Junctor.IMP)) {
            sb.append("(" + toPrettyTerm(formula.sub(0)) + ") -> (" + toPrettyTerm(formula.sub(1)) + ")");
        } else {
            if (op.equals(Junctor.AND)) {
                sb.append("(" + toPrettyTerm(formula.sub(0)) + ") && (" + toPrettyTerm(formula.sub(1)) + ")");
            } else {
                if (op.equals(Junctor.OR)) {
                    sb.append("(" + toPrettyTerm(formula.sub(0)) + ") || (" + toPrettyTerm(formula.sub(1)) + ")");
                } else {
                    if (op.equals(Equality.EQV)) {
                        sb.append("(" + toPrettyTerm(formula.sub(0)) + ") <-> (" + toPrettyTerm(formula.sub(1)) + ")");
                    } else {
                        if (op.equals(Equality.EQUALS)) {
                            sb.append("(" + toPrettyTerm(formula.sub(0)) + ") == (" + toPrettyTerm(formula.sub(1)) + ")");
                        } else {
                            if (op.equals(Junctor.NOT)) {
                                sb.append("(!" + toPrettyTerm(formula.sub(0)) + ")");
                            } else {
                                sb.append(formula.toString());
                            }
                        }
                    }
                }
            }
        }

        return sb.toString();
    }
}
