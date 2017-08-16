package edu.kit.formal.psdb.interpreter.dbg;

import edu.kit.formal.psdb.interpreter.MatcherApi;
import edu.kit.formal.psdb.interpreter.data.GoalNode;
import edu.kit.formal.psdb.interpreter.data.Value;
import edu.kit.formal.psdb.interpreter.data.VariableAssignment;
import edu.kit.formal.psdb.parser.ast.Signature;
import edu.kit.formal.psdb.parser.ast.Variable;
import edu.kit.formal.psdb.parser.types.Type;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PseudoMatcher implements MatcherApi<String> {
    @Override
    public List<VariableAssignment> matchLabel(GoalNode<String> currentState, String label) {
        Pattern p = Pattern.compile(label, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(currentState.getData());
        return m.matches()
                ? Collections.singletonList(new VariableAssignment())
                : Collections.emptyList();
    }

    @Override
    public List<VariableAssignment> matchSeq(GoalNode<String> currentState, String data, Signature sig) {
        Pattern p = Pattern.compile(data, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(currentState.getData());
        if (!m.matches())
            return Collections.emptyList();

        VariableAssignment va = new VariableAssignment();

        for (Map.Entry<Variable, Type> s : sig.entrySet()) {
            va.declare(s.getKey(), s.getValue());
            va.assign(s.getKey(),
                    Value.from(m.group(s.getKey().getIdentifier())));
        }
        return Collections.singletonList(va);
    }
}
   