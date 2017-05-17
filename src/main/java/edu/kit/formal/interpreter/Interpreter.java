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
    //TODO later also inclulde information about source line for each state (for debugging purposes and rewind purposes)
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

    /**
     * If new Block is entered, a new state has to be created (copy of current state) and opushed to the stack
     */
    private void enterScope() {

    }

    /**
     * If block is extied the top state on the stack has to be removed
     */
    private void exitScope() {

    }

    /**
     * Visit a proof script (context is handled by the call of the script noch by visiting the script itself)
     * 1) visit its signature
     * 2) visit its body
     *
     * @param proofScript
     * @return
     */
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

    /**
     * Visiting an assignment results in changing the variables of the current selected goalnode
     * @param assignmentStatement
     * @return
     */
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
                Evaluator eval = new Evaluator(state.getSelectedGoalNode());
                Value v = (Value) expr.accept(eval);
                node.getAssignments().setVar(var.getIdentifier(), v);
            } else {
                throw new RuntimeException("Assignment problem");
            }
        }
        stateStack.push(state);
        return null;
    }

    /**
     * Visiting a statement list results in visiting each statement
     * @param statements
     * @return
     */
    @Override
    public T visit(Statements statements) {
        for (Statement s : statements) {
            s.accept(this);
        }
        return null;
    }

    /**
     *
     * @param casesStatement
     * @return
     */
    @Override
    public T visit(CasesStatement casesStatement) {
        //neuerScope
        casesStatement.getCases();
        casesStatement.getDefaultCase();
        return null;
    }

    /**
     *
     * @param caseStatement
     * @return
     */
    @Override
    public T visit(CaseStatement caseStatement) {
        return null;
    }

    /**
     * Visiting a call statement results in:
     * 0) searching for the handler of the called command
     * 1) saving the context onto the stack and creating a copy of the state and push it onto the stack
     * 2) adding new Variable Assignments to te selected goal
     * 3) adding the assigned parameters to the variable assignments
     * 4) visting the body respec. letting the handler take over
     * 5) removing the top element form the stack
     * @param call
     * @return
     */
    @Override
    public T visit(CallStatement call) {
        //neuer scope
        State newState = stateStack.peek().copy();
        Evaluator eval = new Evaluator(newState.getSelectedGoalNode());

        String commandName = call.getCommand();
        Parameters parameters = call.getParameters();
        ProofScript commandScript;

        //TODO refactor into own interface/facade for proof commands
        if (localCommands.containsKey(commandName)) {
            commandScript = localCommands.get(commandName);
            Signature sig = commandScript.getSignature();
            Iterator<Map.Entry<Variable, Expression>> paramIterator = parameters.entrySet().iterator();
            //Iterator<Map.Entry<Variable, Type>> sigIter = sig.entrySet().iterator();
            while (paramIterator.hasNext()) {
                Map.Entry<Variable, Expression> nextP = paramIterator.next();
                Expression expr = nextP.getValue();
                Variable var = nextP.getKey();
                Value val = (Value) expr.accept(eval);
                newState.getSelectedGoalNode().setVarValue(var.getIdentifier(), val);

            }
            newState.getSelectedGoalNode().enterNewVarScope();
            stateStack.push(newState);
            visit(commandScript.getBody());
            stateStack.pop().getSelectedGoalNode().exitNewVarScope();
        } else {
            throw new RuntimeException("Command " + commandName + " is not known");
        }

        return null;

    }

    @Override
    public T visit(TheOnlyStatement theOnly) {
        //neuer scope?
        return null;
    }

    /**
     * Visiting foreach:
     * 1) foreach goal in state create a new state with exact this goal
     * 2) foreach of these goals visit body of foreach
     * 3) collect all results after foreach
     * @param foreach
     * @return
     */
    @Override
    public T visit(ForeachStatement foreach) {
        State currentState = (State) stateStack.pop();
        List<GoalNode> allGoals = currentState.getGoals();
        List<GoalNode> goalsAfterForeach = new ArrayList<>();
        Statements body = foreach.getBody();
        for (GoalNode goal : allGoals) {
            List<GoalNode> goals = new ArrayList<>();
            goals.add(goal);
            State single = new State(goals, goal);
            stateStack.push(single);
            visit(body);
            State afterForeach = (State) stateStack.pop();
            goalsAfterForeach.addAll(afterForeach.getGoals());
        }
        State afterForeach = new State(goalsAfterForeach, null);
        stateStack.push(afterForeach);
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
