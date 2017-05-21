package edu.kit.formal.interpreter.funchdl;

import edu.kit.formal.interpreter.*;
import edu.kit.formal.proofscriptparser.ast.CallStatement;
import edu.kit.formal.proofscriptparser.ast.Parameters;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * @author Alexander Weigl
 * @version 1 (21.05.17)
 */
public abstract class BuiltinCommands {
    @RequiredArgsConstructor
    public abstract static class BuiltinCommand implements CommandHandler {
        @Getter
        private final String name;

        @Override
        public boolean handles(CallStatement call) throws IllegalArgumentException {
            return name.equals(call.getCommand());

        }
    }

    public static class PrintCommand extends BuiltinCommand {
        public PrintCommand() {
            super("print_state");
        }

        @Override
        public void evaluate(Interpreter interpreter, CallStatement call, VariableAssignment params) {
            for (GoalNode gn : interpreter.getCurrentGoals()) {
                System.out.format("%s %s%n  %s%n", gn == interpreter.getSelectedNode() ? "*" : " ", gn.getSequent(), gn.getAssignments().asMap());
            }
        }
    }

    public static class SplitCommand extends BuiltinCommand {

        public SplitCommand() {
            super("split");
        }

        /**
         * Created by sarah on 5/17/17.
         */
        @Override
        public void evaluate(Interpreter interpreter, CallStatement call, VariableAssignment params) {
            Value<BigInteger> val = (Value<BigInteger>) params.getValues().getOrDefault("#1", Value.from(2));
            int num = val.getData().intValue();
            GoalNode g = interpreter.getSelectedNode();
            AbstractState s = interpreter.getCurrentState();
            State state = new State(new ArrayList<>(s.getGoals()), null);
            state.getGoals().remove(s.getSelectedGoalNode());
            for (char i = 0; i < num; i++) {
                state.getGoals().add(new GoalNode(g, g.getSequent() + "." + (char) ('a' + i)));
            }
            interpreter.pushState(state);
        }
    }
}
