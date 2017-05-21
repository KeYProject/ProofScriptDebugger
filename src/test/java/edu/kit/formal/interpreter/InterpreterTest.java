package edu.kit.formal.interpreter;

import edu.kit.formal.ScopeLogger;
import edu.kit.formal.interpreter.funchdl.BuiltinCommands;
import edu.kit.formal.interpreter.funchdl.CommandLookup;
import edu.kit.formal.interpreter.funchdl.DefaultLookup;
import edu.kit.formal.interpreter.funchdl.ProofScriptHandler;
import edu.kit.formal.proofscriptparser.Facade;
import edu.kit.formal.proofscriptparser.ast.CallStatement;
import edu.kit.formal.proofscriptparser.ast.ProofScript;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author Alexander Weigl
 * @version 1 (21.05.17)
 */
public class InterpreterTest {

    public Interpreter execute(InputStream is) throws IOException {
        List<ProofScript> scripts = Facade.getAST(CharStreams.fromStream(is));
        Interpreter i = new Interpreter(createTestLookup(scripts));
        i.setMatcherApi(new EvaluatorTest.PseudoMatcher());
        //i.getEntryListeners().add(new ScopeLogger("scope:"));
        i.interpret(scripts, "abc");
        return i;
    }

    private CommandLookup createTestLookup(List<ProofScript> scripts) {
        DefaultLookup defaultLookup = new DefaultLookup();
        defaultLookup.getBuilders().add(new BuiltinCommands.PrintCommand());
        defaultLookup.getBuilders().add(new BuiltinCommands.SplitCommand());
        defaultLookup.getBuilders().add(new AssertionCommand());
        defaultLookup.getBuilders().add(new AssertionEqCommand());
        ProofScriptHandler scriptHandler = new ProofScriptHandler(scripts);
        scriptHandler.getSearchPath().add(new File("src/test/resources/edu/kit/formal/interpreter/"));
        defaultLookup.getBuilders().add(scriptHandler);
        return defaultLookup;
    }


    @Test
    public void testSimple() throws IOException {
        Interpreter i = execute(getClass().getResourceAsStream("simple1.txt"));
        Assert.assertEquals(10, i.getCurrentState().getGoals().size());
    }


    @Test
    public void testInclude() throws IOException {
        Interpreter i = execute(getClass().getResourceAsStream("includetest.kps"));
    }

    private class AssertionEqCommand extends BuiltinCommands.BuiltinCommand {

        public AssertionEqCommand() {
            super("assertEq");
        }

        @Override
        public void evaluate(Interpreter interpreter, CallStatement call, VariableAssignment params) {
            Map<String, Value> m = params.asMap();
            Value exp = get(m, "exp", "expected", "#1");
            Value act = get(m, "act", "actual", "#2");
            Value msg = get(m, "msg", "#4");
            if (msg == null)
                Assert.assertEquals(exp, act);
            else
                Assert.assertEquals((String) msg.getData(), exp, act);
        }

        private Value get(Map<String, Value> m, String... keys) {
            for (String k : keys) {
                if (m.containsKey(k)) {
                    return m.get(k);
                }
            }
            return null;
        }
    }


    private class AssertionCommand extends BuiltinCommands.BuiltinCommand {

        public AssertionCommand() {
            super("assert");
        }

        @Override
        public void evaluate(Interpreter interpreter, CallStatement call, VariableAssignment params) {
            Map<String, Value> m = params.asMap();
            Value<Boolean> exp = get(m, "val", "#1");
            Value<String> msg = get(m, "msg", "#2");
            if (msg == null)
                Assert.assertTrue(exp.getData());
            else
                Assert.assertTrue(msg.getData(), exp.getData());
        }

        private Value get(Map<String, Value> m, String... keys) {
            for (String k : keys) {
                if (m.containsKey(k)) {
                    return m.get(k);
                }
            }
            return null;
        }
    }
}
