package edu.kit.formal.interpreter;


import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import de.uka.ilkd.key.api.ScriptApi;
import de.uka.ilkd.key.api.VariableAssignments;
import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.State;
import edu.kit.formal.interpreter.data.Value;
import edu.kit.formal.interpreter.data.VariableAssignment;
import edu.kit.formal.interpreter.exceptions.InterpreterRuntimeException;
import edu.kit.formal.interpreter.funchdl.CommandLookup;
import edu.kit.formal.proofscriptparser.DefaultASTVisitor;
import edu.kit.formal.proofscriptparser.Visitor;
import edu.kit.formal.proofscriptparser.ast.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.logging.Logger;

/**
 * Main Class for interpreter
 *
 * @author S.Grebing
 */
public class Interpreter<T> extends DefaultASTVisitor<Void>
        implements ScopeObservable {
    private static final int MAX_ITERATIONS = 5;
    @Getter
    private static final BiMap<Type, VariableAssignments.VarType> typeConversionBiMap =
            new ImmutableBiMap.Builder<Type, VariableAssignments.VarType>()
                    .put(Type.ANY, VariableAssignments.VarType.ANY)
                    .put(Type.BOOL, VariableAssignments.VarType.BOOL)
                    .put(Type.TERM, VariableAssignments.VarType.FORMULA) //TODO: parametrisierte Terms
                    .put(Type.INT, VariableAssignments.VarType.INT)
                    .put(Type.STRING, VariableAssignments.VarType.OBJECT)
                    .build();
    private static Logger logger = Logger.getLogger("interpreter");
    //TODO later also include information about source line for each state (for debugging purposes and rewind purposes)
    private Stack<State<T>> stateStack = new Stack<>();
    @Getter
    private List<Visitor> entryListeners = new ArrayList<>(),
            exitListeners = new ArrayList<>();
    @Getter
    @Setter
    private MatcherApi<T> matcherApi;
    @Getter
    private CommandLookup functionLookup;
    @Getter
    @Setter
    private boolean scrictSelectedGoalMode = false;
    @Getter
    private ScriptApi scriptApi;

    public Interpreter(CommandLookup lookup) {
        functionLookup = lookup;
    }

    /**
     * starting point is a statement list
     */
    public void interpret(ProofScript script) {
        if (stateStack.empty()) {
            throw new InterpreterRuntimeException("no state on stack. call newState before interpret");
        }
        script.accept(this);
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
    public Void visit(ProofScript proofScript) {
        enterScope(proofScript);
        //add vars
        visit(proofScript.getSignature());
        proofScript.getBody().accept(this);
        exitScope(proofScript);
        return null;
    }

    /**
     * Visiting an assignment results in changing the variables of the current selected goalnode
     *
     * @param assignmentStatement
     * @return
     */
    @Override
    public Void visit(AssignmentStatement assignmentStatement) {
        enterScope(assignmentStatement);

        GoalNode<T> node = getSelectedNode();
        Type t = assignmentStatement.getType();
        Variable var = assignmentStatement.getLhs();
        Expression expr = assignmentStatement.getRhs();
        if (t != null) {
            node.declareVariable(var, t);
        }

        if (expr != null) {
            Type type = node.getVariableType(var);
            if (type == null) {
                throw new RuntimeException("Type of Variable " + var + " is not declared yet");
            } else {
                Value v = evaluate(expr);
                node.setVariableValue(var, v);
            }
        }
        exitScope(assignmentStatement);

        return null;
    }


    private Value evaluate(Expression expr) {
        return evaluate(getSelectedNode(), expr);
    }

    private Value evaluate(GoalNode<T> g, Expression expr) {
        enterScope(expr);
        Evaluator evaluator = new Evaluator(g.getAssignments(), g);
        evaluator.setMatcher(matcherApi);
        evaluator.getEntryListeners().addAll(entryListeners);
        evaluator.getExitListeners().addAll(exitListeners);
        exitScope(expr);
        return evaluator.eval(expr);
    }


    /**
     * Visiting a statement list results in visiting each statement
     *
     * @param statements
     * @return
     */
    @Override
    public Void visit(Statements statements) {
        enterScope(statements);
        for (Statement s : statements) {
            s.accept(this);
        }
        exitScope(statements);
        return null;
    }

    /**
     * @param casesStatement
     * @return
     */
    @Override
    public Void visit(CasesStatement casesStatement) {
        enterScope(casesStatement);
        State<T> beforeCases = peekState();


        List<GoalNode<T>> allGoalsBeforeCases = beforeCases.getGoals();

        //global List after all Case Statements
        List<GoalNode<T>> goalsAfterCases = new ArrayList<>();
        //copy the list of goal nodes for keeping track of goals
        Set<GoalNode<T>> remainingGoalsSet = new HashSet<>(allGoalsBeforeCases);

        //handle cases
        List<CaseStatement> cases = casesStatement.getCases();
        for (CaseStatement aCase : cases) {
            Map<GoalNode<T>, VariableAssignment> matchedGoals =
                    matchGoal(remainingGoalsSet, aCase);
            if (matchedGoals != null) {
                remainingGoalsSet.removeAll(matchedGoals.keySet());
                goalsAfterCases.addAll(executeCase(aCase.getBody(), matchedGoals));
            }

        }

        //for all remaining goals execute default
        if (!remainingGoalsSet.isEmpty()) {
            VariableAssignment va = new VariableAssignment();
            Statements defaultCase = casesStatement.getDefaultCase();
            for (GoalNode<T> goal : remainingGoalsSet) {

                goalsAfterCases.addAll(executeBody(defaultCase, goal, va).getGoals());
            }


        }

        //exit scope and create a new state using the union of all newly created goals

        State<T> newStateAfterCases;
        if (!goalsAfterCases.isEmpty()) {
            //goalsAfterCases.forEach(node -> node.exitScope());

            if (goalsAfterCases.size() == 1) {
                newStateAfterCases = new State<T>(goalsAfterCases, 0);
            } else {
                newStateAfterCases = new State<T>(goalsAfterCases, null);
            }
            stateStack.push(newStateAfterCases);
        }

        //stateStack.peek().getGoals().removeAll(beforeCases.getGoals());
        exitScope(casesStatement);
        return null;
    }

    /**
     * Match a set of goal nodes against a matchpattern of a case and return the matched goals together with instantiated variables
     *
     * @param allGoalsBeforeCases
     * @param aCase
     * @return
     */
    private Map<GoalNode<T>, VariableAssignment> matchGoal(Set<GoalNode<T>> allGoalsBeforeCases, CaseStatement aCase) {

        HashMap<GoalNode<T>, VariableAssignment> matchedGoals = new HashMap<>();
        Expression matchExpression = aCase.getGuard();

        for (GoalNode<T> goal : allGoalsBeforeCases) {
            VariableAssignment va = evaluateMatchInGoal(matchExpression, goal);
            if (va != null) {
                matchedGoals.put(goal, va);
            }

        }
        return matchedGoals;
    }

    /**
     * Evaluate a match in a specific goal
     *
     * @param matchExpression
     * @param goal
     * @return null, if match was false, return teh first Assignment when match was true
     */
    private VariableAssignment evaluateMatchInGoal(Expression matchExpression, GoalNode<T> goal) {
        enterScope(matchExpression);
        MatchEvaluator mEval = new MatchEvaluator(goal.getAssignments(), goal, matcherApi);
        mEval.getEntryListeners().addAll(entryListeners);
        mEval.getExitListeners().addAll(exitListeners);
        exitScope(matchExpression);

        List<VariableAssignment> matchResult = mEval.eval(matchExpression);
        if (matchResult.isEmpty()) {
            return null;
        } else {
            return matchResult.get(0);
        }

        /*Evaluator eval = new Evaluator(goal.getAssignments(), goal);
        eval.setMatcher(matcherApi);
        eval.getEntryListeners().addAll(entryListeners);
        eval.getExitListeners().addAll(exitListeners);
        exitScope(matchExpression);

        Value v = eval.eval(matchExpression);
        if (v.getData().equals(Value.TRUE)) {
            if (eval.getMatchedVariables().size() == 0) {
                return new VariableAssignment();
            } else {
                return eval.getMatchedVariables().get(0);
            }
        }
        return null;*/
    }

    /**
     * For each selected goal put a state onto the stack and visit the body of the case
     *
     * @param
     * @param caseStmts
     * @param goalsToApply @return
     */
    private List<GoalNode<T>> executeCase(Statements caseStmts,
                                          Map<GoalNode<T>, VariableAssignment> goalsToApply) {
        enterScope(caseStmts);
        List<GoalNode<T>> goalsAfterCases = new ArrayList<>();

        for (Map.Entry<GoalNode<T>, VariableAssignment> next : goalsToApply.entrySet()) {
            State<T> s = executeBody(caseStmts, next.getKey(), next.getValue());
            goalsAfterCases.addAll(s.getGoals());
        }
        exitScope(caseStmts);
        return goalsAfterCases;


    }

    private State<T> executeBody(Statements caseStmts, GoalNode<T> goalNode, VariableAssignment va) {

        goalNode.enterScope(va);
        State<T> s = newState(goalNode);
        caseStmts.accept(this);
        popState(s);
        return s;
    }

    /**
     * @param caseStatement
     * @return
     */
  /*  @Override
    public Void visit(CaseStatement caseStatement) {
        enterScope(caseStatement);
        exitScope(caseStatement);
        return null;
    }*/

    /**
     * Visiting a call statement results in:
     * 0) searching for the handler of the called command
     * 1) saving the context onto the stack and creating a copy of the state and push it onto the stack
     * 2) adding new Variable Assignments to te selected goal
     * 3) adding the assigned parameters to the variable assignments
     * 4) visiting the body respec. letting the handler take over
     * 5) removing the top element form the stack
     *
     * @param call
     * @return
     */
    @Override
    public Void visit(CallStatement call) {
        enterScope(call);
        //neuer VarScope
        //enter new variable scope
        VariableAssignment params = evaluateParameters(call.getParameters());
        GoalNode<T> g = getSelectedNode();
        g.enterScope();
        functionLookup.callCommand(this, call, params);
        g.exitScope();
        exitScope(call);
        return null;
    }


    public VariableAssignment evaluateParameters(Parameters parameters) {
        VariableAssignment va = new VariableAssignment();
        parameters.entrySet().forEach(entry -> {
            Value val = evaluate(entry.getValue());
            va.declare(entry.getKey(), val.getType());
            va.assign(entry.getKey(), val);
        });
        return va;
    }

    @Override
    public Void visit(TheOnlyStatement theOnly) {
        List<GoalNode<T>> goals = getCurrentState().getGoals();
        if (goals.size() > 1) {
            throw new IllegalArgumentException(
                    String.format("TheOnly at line %d: There are %d goals!",
                            theOnly.getStartPosition().getLineNumber(),
                            goals.size()));
        }
        enterScope(theOnly);
        theOnly.getBody().accept(this);
        exitScope(theOnly);
        return null;
    }

    /**
     * Visiting foreach:
     * 1) foreach goal in state create a new state with exact this goal
     * 2) foreach of these goals visit body of foreach
     * 3) collect all results after foreach
     *
     * @param foreach
     * @return
     */
    @Override
    public Void visit(ForeachStatement foreach) {
        enterScope(foreach);
        List<GoalNode<T>> allGoals = getCurrentGoals();
        List<GoalNode<T>> goalsAfterForeach = new ArrayList<>();
        Statements body = foreach.getBody();
        for (GoalNode<T> goal : allGoals) {
            newState(goal);
            visit(body);
            State<T> s = popState();
            goalsAfterForeach.addAll(s.getGoals());
        }
        State<T> afterForeach = new State<T>(goalsAfterForeach, null);
        stateStack.push(afterForeach);
        exitScope(foreach);
        return null;
    }

    @Override
    public Void visit(RepeatStatement repeatStatement) {
        enterScope(repeatStatement);
        int counter = 0;
        boolean b = false;
        do {
            counter++;
            State<T> prev = getCurrentState();
            repeatStatement.getBody().accept(this);
            State<T> end = getCurrentState();

            Set<GoalNode<T>> prevNodes = new HashSet<>(prev.getGoals());
            Set<GoalNode<T>> endNodes = new HashSet<>(end.getGoals());
            b = prevNodes.equals(endNodes);
            b = b && counter <= MAX_ITERATIONS;
        } while (b);
        exitScope(repeatStatement);
        return null;
    }

    @Override
    public Void visit(Signature signature) {
        exitScope(signature);
        GoalNode<T> node = getSelectedNode();
        node.enterScope();
        signature.forEach(node::declareVariable);
        enterScope(signature);
        return null;
    }

    //region State Handling
    public GoalNode<T> getSelectedNode() {
        try {
            return stateStack.peek().getSelectedGoalNode();
        } catch (IllegalStateException e) {
            if (scrictSelectedGoalMode)
                throw e;

            logger.warning("No goal selected. Returning first goal!");
            return getCurrentGoals().get(0);
        }
    }


    public State<T> getCurrentState() {
        try {
            return stateStack.peek();
        } catch (EmptyStackException e) {
            return new State<T>(null);
            //FIXME
        }
    }

    public State<T> newState(List<GoalNode<T>> goals, GoalNode<T> selected) {
        if (selected != null && !goals.contains(selected)) {
            throw new IllegalStateException("selected goal not in list of goals");
        }
        return pushState(new State<>(goals, selected));
    }

    public State<T> newState(List<GoalNode<T>> goals) {
        return newState(goals, null);
    }

    public State<T> newState(GoalNode<T> selected) {
        return newState(Collections.singletonList(selected), selected);
    }

    public State<T> pushState(State<T> state) {
        if (stateStack.contains(state)) {
            throw new IllegalStateException("State is already on the stack!");
        }
        stateStack.push(state);
        return state;
    }

    public State<T> popState(State<T> expected) {
        State<T> actual = stateStack.pop();
        if (!expected.equals(actual)) {
            throw new IllegalStateException("Error on the stack!");
        }
        return actual;
    }

    private State<T> popState() {
        return stateStack.pop();
    }


    public State<T> peekState() {
        return stateStack.peek();
    }

    public List<GoalNode<T>> getCurrentGoals() {
        return getCurrentState().getGoals();
    }


    //endregion
}
