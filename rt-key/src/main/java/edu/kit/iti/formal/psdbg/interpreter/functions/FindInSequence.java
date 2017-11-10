package edu.kit.iti.formal.psdbg.interpreter.functions;

import edu.kit.iti.formal.psdbg.interpreter.Evaluator;
import edu.kit.iti.formal.psdbg.interpreter.KeYMatcher;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.data.VariableAssignment;
import edu.kit.iti.formal.psdbg.parser.NotWelldefinedException;
import edu.kit.iti.formal.psdbg.parser.Visitor;
import edu.kit.iti.formal.psdbg.parser.ast.FunctionCall;
import edu.kit.iti.formal.psdbg.parser.ast.MatchExpression;
import edu.kit.iti.formal.psdbg.parser.ast.Signature;
import edu.kit.iti.formal.psdbg.parser.ast.TermLiteral;
import edu.kit.iti.formal.psdbg.parser.data.Value;
import edu.kit.iti.formal.psdbg.parser.function.ScriptFunction;
import edu.kit.iti.formal.psdbg.parser.types.SimpleType;
import edu.kit.iti.formal.psdbg.parser.types.TermType;
import edu.kit.iti.formal.psdbg.parser.types.Type;
import edu.kit.iti.formal.psdbg.parser.types.TypeFacade;

import java.util.Collections;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (10.11.17)
 */
public class FindInSequence implements ScriptFunction {
    public static final List<Type> TYPES =
            Collections.singletonList(SimpleType.PATTERN);

    @Override
    public String getName() {
        return "find";
    }

    @Override
    public Type getType(List<Type> types) throws NotWelldefinedException {
        if (!TYPES.equals(types))
            throw new NotWelldefinedException("Wrong type of parameter for " + getName(), null);
        return new TermType();
    }

    @Override
    public Value eval(Visitor<Value> val, FunctionCall call)
            throws IllegalArgumentException {
        Evaluator<KeyData> e = (Evaluator<KeyData>) val;
        try {
            MatchExpression match = (MatchExpression) call.getArguments().get(0);
            Signature sig = match.getSignature();
            Value pattern = e.eval(match.getPattern());
            List<VariableAssignment> va = null;
            KeYMatcher matcher = (KeYMatcher) e.getMatcher();
            if (pattern.getType() == SimpleType.STRING) {
                va = matcher.matchLabel(e.getGoal(), (String) pattern.getData());
                //TODO extract the results form the matcher in order to retrieve the selection results
            } else if (TypeFacade.isTerm(pattern.getType())) {
                va = matcher.matchSeq(e.getGoal(), (String) pattern.getData(), sig);
            }

            //TODO capture top level term
            return Value.from(new TermLiteral(""));
        } catch (ClassCastException exc) {
            throw new IllegalStateException("Excepted a match expression as first argument found: " + call.getArguments().get(0),
                    exc);
        }
    }

}
