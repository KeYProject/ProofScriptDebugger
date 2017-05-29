package edu.kit.formal.interpreter.dbg;

import edu.kit.formal.interpreter.HistoryListener;
import edu.kit.formal.interpreter.Interpreter;
import edu.kit.formal.interpreter.State;
import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.Value;
import edu.kit.formal.interpreter.data.VariableAssignment;
import edu.kit.formal.interpreter.funchdl.BuiltinCommands;
import edu.kit.formal.interpreter.funchdl.CommandHandler;
import edu.kit.formal.interpreter.funchdl.DefaultLookup;
import edu.kit.formal.proofscriptparser.DefaultASTVisitor;
import edu.kit.formal.proofscriptparser.Facade;
import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formal.proofscriptparser.TransformAst;
import edu.kit.formal.proofscriptparser.ast.*;
import org.antlr.v4.runtime.CharStreams;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author Alexander Weigl
 * @version 1 (21.05.2017)
 */
public class Debugger {
    private Interpreter<String> interpreter;
    private List<ProofScript> scripts;
    private Blocker blocker = new Blocker();
    private DefaultLookup debuggerFunctions = new DefaultLookup();
    private HistoryListener history;

    private Thread interpreterThread;

    public Debugger(String file) throws IOException {
        DefaultLookup lookup = new DefaultLookup();
        lookup.getBuilders().add(new BuiltinCommands.SplitCommand());
        interpreter = new Interpreter<>(lookup);
        interpreter.setMatcherApi(new PseudoMatcher());
        history = new HistoryListener(interpreter);
        scripts = Facade.getAST(new File(file));
        interpreter.getEntryListeners().add(history);
        interpreter.getEntryListeners().add(blocker);
        interpreter.getEntryListeners().add(new CommandLogger());

        registerDebuggerFunction("step", this::step);
        registerDebuggerFunction("b", this::setBreakpoint);
        registerDebuggerFunction("start", this::start);
        registerDebuggerFunction("pause", this::pause);
        registerDebuggerFunction("chgsel", this::changeSelected);
        registerDebuggerFunction("psel", this::psel);
        registerDebuggerFunction("status", this::status);
    }

    public static void main(String[] args) throws IOException {
        Debugger d = new Debugger("src/test/resources/edu/kit/formal/interpreter/dbg.kps");
        d.run();
    }

    private void registerDebuggerFunction(final String step,
                                          BiConsumer<CallStatement, VariableAssignment> func) {
        debuggerFunctions.getBuilders().add(new CommandHandler() {
            @Override
            public boolean handles(CallStatement call) throws IllegalArgumentException {
                return step.equals(call.getCommand());
            }

            @Override
            public void evaluate(Interpreter i, CallStatement call, VariableAssignment params) {
                func.accept(call, params);
            }
        });
    }

    private void run() throws IOException {
        blocker.stepUntilBlock.set(2);
        interpreterThread = new Thread(() -> {
            interpreter.interpret(scripts, null);
        });
        interpreterThread.setName("interpreter");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int counter = 0;
        while (true) {
            System.out.printf("dbg (%03d)> ", ++counter);
            String input = br.readLine();
            execute(input);
        }
    }

    private void execute(String input) {
        input = input.trim();
        if (input.isEmpty()) {
            return;
        }

        if (!input.endsWith(";"))
            input += ';';

        ScriptLanguageParser parser = Facade.getParser(CharStreams.fromString(input));
        ScriptLanguageParser.ScriptCommandContext ctx = parser.scriptCommand();
        CallStatement call = (CallStatement) ctx.accept(new TransformAst());
        try {
            debuggerFunctions.callCommand(interpreter, call, new VariableAssignment());
        } catch (IllegalStateException e) {
            System.out.println("No function for " + input + " defined! ");
            e.printStackTrace();

        }
    }


    public void step(CallStatement call, VariableAssignment params) {
        IntegerLiteral il = (IntegerLiteral) call.getParameters().get(new Variable("#1"));
        int steps = 1;
        if (il != null)
            steps = il.getValue().intValue();
        blocker.stepUntilBlock.set(steps); //FIXME times two is strange, something wrong on sync
        //LockSupport.unpark(interpreterThread);
        blocker.unlock();
    }

    public void psel(CallStatement call, VariableAssignment params) {
        State<String> state = interpreter.getCurrentState();
        int i = 0;
        GoalNode sel;
        try {
            sel = state.getSelectedGoalNode();
        } catch (IllegalStateException e) {
            sel = null;
        }

        for (GoalNode<String> g : state.getGoals()) {
            System.out.printf("%2d %s %s\n     %s\n",
                    i++,
                    g == sel ? "*" : " ",
                    g.getData(),
                    g.getAssignments().asMap());
        }
    }

    public void start(CallStatement call, VariableAssignment params) {
        interpreterThread.start();
    }


    public void pause(CallStatement call, VariableAssignment params) {
        blocker.stepUntilBlock.set(1); // block at next statement
    }

    public void setBreakpoint(CallStatement call, VariableAssignment params) {
        IntegerLiteral il = (IntegerLiteral) call.getParameters().get(new Variable("#1"));
        blocker.addBreakpoint(il.getValue().intValue());

        System.out.println("brkpnts: " + blocker.brkpnts);

    }

    public void status(CallStatement call, VariableAssignment params) {
        System.out.format("name: %s, p: %s, state: %s, alive:%s, daemon:%s, interrupted:%s %n",
                interpreterThread.getName(),
                interpreterThread.getPriority(),
                interpreterThread.getState(),
                interpreterThread.isAlive(),
                interpreterThread.isDaemon(),
                interpreterThread.isInterrupted());

        List<ASTNode> nodes = history.getQueueNode();
        List<State> states = history.getQueueState();
        CommandLogger cmd = new CommandLogger();
        for (int i = 0; i < nodes.size(); i++) {
            ASTNode node = nodes.get(i);
            node.accept(cmd);
            // System.out.format("\t\t\t>>> %d states: [%s]%n", states.get(i).getGoals().size(),
            //         states.get(i).getSelectedGoalNode().getAssignments().asMap());
        }
    }

    public void changeSelected(CallStatement call, VariableAssignment params) {
        State<String> state = interpreter.getCurrentState();
        params = interpreter.evaluateParameters(call.getParameters());
        Value<BigInteger> v = params.getValues().get("#1");

        interpreter.newState(state.getGoals(),
                state.getGoals().get(v.getData().intValue()));
    }

    private class CommandLogger extends DefaultASTVisitor<Void> {
        public void suffix(ASTNode node) {
            System.out.format("%02d: ", node.getStartPosition().getLineNumber());
        }

        @Override
        public Void visit(ProofScript proofScript) {
            suffix(proofScript);
            System.out.printf("script %s (%s) {%n",
                    proofScript.getName(),
                    Facade.prettyPrint(proofScript.getSignature())
            );
            return null;
        }

        @Override
        public Void visit(AssignmentStatement assignment) {
            suffix(assignment);
            System.out.println(Facade.prettyPrint(assignment));
            return super.visit(assignment);
        }

        @Override
        public Void visit(CasesStatement casesStatement) {
            suffix(casesStatement);
            System.out.println("cases {");
            return super.visit(casesStatement);
        }

        @Override
        public Void visit(CaseStatement caseStatement) {
            suffix(caseStatement);
            System.out.println("case " + Facade.prettyPrint(caseStatement.getGuard()));
            return super.visit(caseStatement);
        }

        @Override
        public Void visit(CallStatement call) {
            suffix(call);
            System.out.println(Facade.prettyPrint(call));
            return super.visit(call);
        }

        @Override
        public Void visit(TheOnlyStatement theOnly) {
            suffix(theOnly);
            System.out.println("theonly {");
            return super.visit(theOnly);
        }

        @Override
        public Void visit(ForeachStatement foreach) {
            suffix(foreach);
            System.out.println("foreach {");
            return super.visit(foreach);
        }

        @Override
        public Void visit(RepeatStatement repeatStatement) {
            suffix(repeatStatement);
            System.out.println("repeat {");
            return super.visit(repeatStatement);
        }
    }
}
