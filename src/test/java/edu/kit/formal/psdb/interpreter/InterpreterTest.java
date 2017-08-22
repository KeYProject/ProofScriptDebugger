package edu.kit.formal.psdb.interpreter;

import edu.kit.formal.psdb.interpreter.data.GoalNode;
import edu.kit.formal.psdb.interpreter.data.Value;
import edu.kit.formal.psdb.interpreter.data.VariableAssignment;
import edu.kit.formal.psdb.interpreter.dbg.PseudoMatcher;
import edu.kit.formal.psdb.interpreter.funchdl.BuiltinCommands;
import edu.kit.formal.psdb.interpreter.funchdl.CommandLookup;
import edu.kit.formal.psdb.interpreter.funchdl.DefaultLookup;
import edu.kit.formal.psdb.interpreter.funchdl.ProofScriptHandler;
import edu.kit.formal.psdb.parser.Facade;
import edu.kit.formal.psdb.parser.ast.CallStatement;
import edu.kit.formal.psdb.parser.ast.ProofScript;
import edu.kit.formal.psdb.parser.ast.Variable;
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

    private static <T> T get(Map<Variable, T> m, String... keys) {
        for (String k : keys) {
            if (m.containsKey(new Variable(k))) {
                return m.get(new Variable(k));
            }
        }
        return null;
    }

    public Interpreter<String> execute(InputStream is) throws IOException {
        List<ProofScript> scripts = Facade.getAST(CharStreams.fromStream(is));
        Interpreter<String> i = new Interpreter<>(createTestLookup(scripts));
        i.setMatcherApi(new PseudoMatcher());
        //i.getEntryListeners().add(new ScopeLogger("scope:"));
        i.newState(new GoalNode<>(null, "abc", false));
        i.interpret(scripts.get(0));
        return i;
    }

    private CommandLookup createTestLookup(List<ProofScript> scripts) {
        DefaultLookup defaultLookup = new DefaultLookup();
        defaultLookup.getBuilders().add(new BuiltinCommands.PrintCommand());
        defaultLookup.getBuilders().add(new BuiltinCommands.SplitCommand());
        defaultLookup.getBuilders().add(new AssertionCommand());
        defaultLookup.getBuilders().add(new AssertionEqCommand());
        ProofScriptHandler scriptHandler = new ProofScriptHandler(scripts);
        scriptHandler.getSearchPath().add(new File("src/test/resources/edu/kit/formal/psdb/interpreter/"));
        defaultLookup.getBuilders().add(scriptHandler);
        return defaultLookup;
    }

    @Test
    public void testSimple() throws IOException {
        Interpreter<String> i = execute(getClass().getResourceAsStream("simple1.txt"));
        Assert.assertEquals(10, i.getCurrentState().getGoals().size());
    }

    @Test
    public void testSimple2() throws IOException {
        // Interpreter inter = execute(getClass().getResourceAsStream("testSimple2.txt"));
        // Assert.assertSame(0, ((BigInteger) inter.getCurrentGoals().getGoals().get(0).getAssignments().lookupVarValue("j").getData()).intValue());
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
            Map<Variable, Value> m = params.asMap();
            Value exp = get(m, "exp", "expected", "#1");
            Value act = get(m, "act", "actual", "#2");
            Value msg = get(m, "msg", "#4");
            if (msg == null)
                Assert.assertEquals(exp, act);
            else
                Assert.assertEquals((String) msg.getData(), exp, act);
        }
    }

    private class AssertionCommand extends BuiltinCommands.BuiltinCommand {

        public AssertionCommand() {
            super("assert");
        }

        @Override
        public void evaluate(Interpreter interpreter, CallStatement call, VariableAssignment params) {
            Map<Variable, Value> m = params.asMap();
            Value<Boolean> exp = get(m, "val", "#1");
            Value<String> msg = get(m, "msg", "#2");
            if (msg == null)
                Assert.assertTrue(exp.getData());
            else
                Assert.assertTrue(msg.getData(), exp.getData());
        }
    }
}
