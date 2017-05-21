package edu.kit.formal.dbg;

import edu.kit.formal.interpreter.*;
import edu.kit.formal.interpreter.funchdl.BuiltinCommands;
import edu.kit.formal.interpreter.funchdl.DefaultLookup;
import edu.kit.formal.proofscriptparser.*;
import edu.kit.formal.proofscriptparser.ast.*;
import org.antlr.v4.runtime.CharStreams;

import java.io.*;
import java.math.BigInteger;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (21.05.2017)
 */
public class Debugger {
    Interpreter interpreter;
    private List<ProofScript> scripts;
    private Blocker blocker = new Blocker();
    private DefaultLookup debuggerFunctions = new DefaultLookup();
    private HistoryListener history;

    private Thread interpreterThread;

    public Debugger(String file) throws IOException {
        DefaultLookup lookup = new DefaultLookup();
        lookup.getBuilders().add(new BuiltinCommands.SplitCommand());
        interpreter = new Interpreter(lookup);

        history = new HistoryListener(interpreter);
        scripts = Facade.getAST(new File(file));
        interpreter.getEntryListeners().add(history);
        interpreter.getEntryListeners().add(blocker);
        interpreter.getEntryListeners().add(new CommandLogger());
        //TODO install debugger functions

        debuggerFunctions.getBuilders().add(new Step());
        debuggerFunctions.getBuilders().add(new Printer());
        debuggerFunctions.getBuilders().add(new ChgSel());
        debuggerFunctions.getBuilders().add(new Start());
        debuggerFunctions.getBuilders().add(new Pause());
        debuggerFunctions.getBuilders().add(new BrkPnt());
        debuggerFunctions.getBuilders().add(new Status());

    }

    public static void main(String[] args) throws IOException {
        Debugger d = new Debugger("src/test/resources/edu/kit/formal/interpreter/dbg.kps");
        d.run();
    }

    private void run() throws IOException {
        blocker.stepUntilBlock.set(2);
        interpreterThread = new Thread(() -> {
            interpreter.interpret(scripts, "start");
        });
        interpreterThread.setName("interpreter");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int counter = 0;
        while (true) {
            System.out.printf("dbg (%03d)> ", ++counter);
            String input = br.readLine();
            execuate(input);
        }
    }

    private void execuate(String input) {
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


    private class Step implements edu.kit.formal.interpreter.funchdl.CommandHandler {
        @Override
        public boolean handles(CallStatement call) throws IllegalArgumentException {
            return call.getCommand().equals("step");
        }

        @Override
        public void evaluate(Interpreter interpreter, CallStatement call, VariableAssignment params) {
            IntegerLiteral il = (IntegerLiteral) call.getParameters().get(new Variable("#1"));
            int steps = 1;
            if (il != null)
                steps = il.getValue().intValue();
            blocker.stepUntilBlock.set(steps * 2); //FIXME times two is strange, something wrong on sync
            //LockSupport.unpark(interpreterThread);
            blocker.unlock();
        }
    }

    private class Printer implements edu.kit.formal.interpreter.funchdl.CommandHandler {
        @Override
        public boolean handles(CallStatement call) throws IllegalArgumentException {
            return call.getCommand().equals("psel");
        }

        @Override
        public void evaluate(Interpreter interpreter, CallStatement call, VariableAssignment params) {
            AbstractState state = interpreter.getCurrentState();
            int i = 0;
            GoalNode sel;
            try {
                sel = state.getSelectedGoalNode();
            } catch (IllegalStateException e) {
                sel = null;
            }

            for (GoalNode g : state.getGoals()) {
                System.out.printf("%2d %s %s\n     %s\n",
                        i++,
                        g == sel ? "*" : " ",
                        g.getSequent(),
                        g.getAssignments().asMap());
            }
        }
    }


    private class Start implements edu.kit.formal.interpreter.funchdl.CommandHandler {
        @Override
        public boolean handles(CallStatement call) throws IllegalArgumentException {
            return call.getCommand().equals("start");
        }

        @Override
        public void evaluate(Interpreter interpreter, CallStatement call, VariableAssignment params) {
            interpreterThread.start();
        }
    }


    private class Pause implements edu.kit.formal.interpreter.funchdl.CommandHandler {
        @Override
        public boolean handles(CallStatement call) throws IllegalArgumentException {
            return call.getCommand().equals("pause");
        }

        @Override
        public void evaluate(Interpreter interpreter, CallStatement call, VariableAssignment params) {
            blocker.stepUntilBlock.set(1); // block at next statement
        }
    }

    private class BrkPnt implements edu.kit.formal.interpreter.funchdl.CommandHandler {
        @Override
        public boolean handles(CallStatement call) throws IllegalArgumentException {
            return call.getCommand().equals("b");
        }

        @Override
        public void evaluate(Interpreter interpreter, CallStatement call, VariableAssignment params) {
            IntegerLiteral il = (IntegerLiteral) call.getParameters().get(new Variable("#1"));
            blocker.addBreakpoint(il.getValue().intValue());

            System.out.println("brkpnts: " + blocker.brkpnts);

        }
    }


    private class Status implements edu.kit.formal.interpreter.funchdl.CommandHandler {
        @Override
        public boolean handles(CallStatement call) throws IllegalArgumentException {
            return call.getCommand().equals("status");
        }

        @Override
        public void evaluate(Interpreter interpreter, CallStatement call, VariableAssignment params) {
            System.out.format("name: %s, p: %s, state: %s, alive:%s, daemon:%s, interrupted:%s %n",
                    interpreterThread.getName(),
                    interpreterThread.getPriority(),
                    interpreterThread.getState(),
                    interpreterThread.isAlive(),
                    interpreterThread.isDaemon(),
                    interpreterThread.isInterrupted());

            List<ASTNode> nodes = history.getQueueNode();
            List<AbstractState> states = history.getQueueState();
            CommandLogger cmd = new CommandLogger();
            for (int i = 0; i < nodes.size(); i++) {
                ASTNode node = nodes.get(i);
                node.accept(cmd);
                // System.out.format("\t\t\t>>> %d states: [%s]%n", states.get(i).getGoals().size(),
                //         states.get(i).getSelectedGoalNode().getAssignments().asMap());
            }
        }
    }

    private class ChgSel implements edu.kit.formal.interpreter.funchdl.CommandHandler {
        @Override
        public boolean handles(CallStatement call) throws IllegalArgumentException {
            return call.getCommand().equals("chgsel");
        }

        @Override
        public void evaluate(Interpreter interpreter, CallStatement call, VariableAssignment params) {
            AbstractState state = interpreter.getCurrentState();
            params = interpreter.evaluateParameters(call.getParameters());
            Value<BigInteger> v = params.getValues().get("#1");

            interpreter.newState(state.getGoals(),
                    state.getGoals().get(v.getData().intValue()));

        }
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
