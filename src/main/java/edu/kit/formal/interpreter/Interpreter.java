package edu.kit.formal.interpreter;

import edu.kit.formal.proofscriptparser.DefaultASTVisitor;
import edu.kit.formal.proofscriptparser.ast.*;

import java.util.*;

/**
 * Main Class for interpreter
 *
 * @author S.Grebing
 */
public class Interpreter<T> extends DefaultASTVisitor<T> {
    public Stack<AbstractState> stateStack;

    public HashMap<String, ProofScript> localCommands;

    public Interpreter() {
        localCommands = new LinkedHashMap<>();
    }

    //starting point is a statement list
    public void interpret(List<ProofScript> scripts, String sequent) {
        stateStack = new Stack<>();
        GoalNode startNode = new GoalNode(null, sequent);
        List<GoalNode> startNodes = new LinkedList<>();
        startNodes.add(startNode);
        //copy all local available proof script to hashmap for better lookup
        scripts.forEach(p -> localCommands.put(p.getName(), p));
        //execute first script (RULE: The first script in the file is main script)
        ProofScript m = scripts.get(0);
        //create new state
        State s = new State(startNodes, startNode);
        stateStack.push(s);
        //later through interface with getMainScript();
        visit(m);


    }


    @Override
    public T visit(ProofScript proofScript) {
        //AbstractState currentState = stateStack.pop();
        System.out.println("Visiting " + proofScript.getName());
        //add vars
        visit(proofScript.getSignature());
        Statements body = proofScript.getBody();
        visit(body);
        return null;
    }

    @Override
    public T visit(AssignmentStatement assignmentStatement) {
        System.out.println("Visiting Assignment " + assignmentStatement.toString());
        AbstractState state = stateStack.pop();
        GoalNode node = state.getSelectedGoalNode();
        Variable var = assignmentStatement.getLhs();
        Expression expr = assignmentStatement.getRhs();
        if (node != null) {
            Type t = node.lookUpType(var.getIdentifier());
            if (t != null) {
                Evaluator eval = new Evaluator((State) state);
                Value v = (Value) expr.accept(eval);
                node.getAssignments().setVar(var.getIdentifier(), v);
            } else {
                throw new RuntimeException("Assignment problem");
            }
        }
        stateStack.push(state);
        return null;
    }


    @Override
    public T visit(Statements statements) {
        Iterator<Statement> iterator = statements.iterator();

        while (iterator.hasNext()) {
            Statement s = iterator.next();
            s.accept(this);
        }
        return null;
    }

    @Override
    public T visit(CasesStatement casesStatement) {
        //neuerScope
        casesStatement.getCases();
        casesStatement.getDefaultCase();
        return null;
    }

    @Override
    public T visit(CaseStatement caseStatement) {
        return null;
    }

    @Override
    public T visit(CallStatement call) {
        //neuer scope
        State newState = stateStack.peek().copy();

        String commandName = call.getCommand();
        Parameters parameters = call.getParameters();

        ProofScript commandScript;
        //find body if local script command
        //TODO refactor into own interface/facade for proof commands
        if (localCommands.containsKey(commandName)) {
            commandScript = localCommands.get(commandName);
        } else {
            throw new RuntimeException("Command " + commandName + " is not known");
        }

        return null;

        //call.getCommand(); on signatur body suchen
        //state map mit commands die name nach body+parameter
        //nach schauen
        //call.getParameters();
        //ProofScript aktuell;
        //interpret(aktuell.getBody());
    }

    @Override
    public T visit(TheOnlyStatement theOnly) {
        //neuer scope?
        return null;
    }

    @Override
    public T visit(ForeachStatement foreach) {
        //neue zustände auf den stack mit jeweils anderem Ziel ausgewählt
        foreach.getBody();
        return null;
    }

    @Override
    public T visit(RepeatStatement repeatStatement) {
        return null;
    }

    @Override
    public T visit(Signature signature) {
        AbstractState state = stateStack.pop();
        GoalNode node = state.getSelectedGoalNode();
        node.enterNewVarScope();
        signature.forEach((v, t) -> {
            node.addVarDecl(v.getIdentifier(), t);
        });
        //state.toString();
        stateStack.push(state);

        return null;
    }

    @Override
    public T visit(Parameters parameters) {
        return null;
    }

}
