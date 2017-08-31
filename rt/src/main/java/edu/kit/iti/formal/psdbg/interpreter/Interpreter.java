package edu.kit.iti.formal.psdbg.interpreter;


import edu.kit.iti.formal.psdbg.interpreter.assignhook.VariableAssignmentHook;
import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.State;
import edu.kit.iti.formal.psdbg.interpreter.data.VariableAssignment;
import edu.kit.iti.formal.psdbg.interpreter.exceptions.InterpreterRuntimeException;
import edu.kit.iti.formal.psdbg.interpreter.funchdl.CommandLookup;
import edu.kit.iti.formal.psdbg.parser.DefaultASTVisitor;
import edu.kit.iti.formal.psdbg.parser.Visitor;
import edu.kit.iti.formal.psdbg.parser.ast.*;
import edu.kit.iti.formal.psdbg.parser.data.Value;
import edu.kit.iti.formal.psdbg.parser.types.Type;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Main Class for interpreter
 * Interpreter keeps a stack of states
 *
 * @author S.Grebing
 */
public class Interpreter<T> extends DefaultASTVisitor<Object>
        implements ScopeObservable {
    private static final int MAX_ITERATIONS = 5;

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
    @Setter
    private VariableAssignmentHook<T> variableAssignmentHook = null;

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

        //initialize environment variables
        if (variableAssignmentHook != null) {
            VariableAssignment va = variableAssignmentHook.getStartAssignment(getSelectedNode().getData());
            getSelectedNode().setAssignments(
                    getSelectedNode().getAssignments().push(va));
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
    public Object visit(ProofScript proofScript) {
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
    public Object visit(AssignmentStatement assignmentStatement) {
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
                throw new RuntimeException("SimpleType of Variable " + var + " is not declared yet");
            } else {
                Value v = evaluate(expr);
                if (fireVariableAssignmentHook(node, var.getIdentifier(), v)) {
                    node.setVariableValue(var, v);
                }
            }
        }
        exitScope(assignmentStatement);

        return null;
    }

    protected boolean fireVariableAssignmentHook(GoalNode<T> node, String identifier, Value v) {
        if (variableAssignmentHook != null) {
            return variableAssignmentHook.handleAssignment(node.getData(), identifier, v);
        }
        return true;
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
     *
     * @param casesStatement
     * @return
     */
    @Override
    public Object visit(CasesStatement casesStatement) {
        enterScope(casesStatement);
        State<T> beforeCases = peekState();

        List<GoalNode<T>> allGoalsBeforeCases = beforeCases.getGoals();
        //copy the list of goal nodes for keeping track of goals
        Set<GoalNode<T>> toMatch = new HashSet<>(allGoalsBeforeCases);

        //global List after all Case Statements
        List<GoalNode<T>> resultingGoals = new ArrayList<>();

        //handle cases
        List<CaseStatement> cases = casesStatement.getCases();

        for (GoalNode<T> goal : allGoalsBeforeCases) {
            newState(goal); //to allow the visit method for the case stmt to retrieve goal

            boolean result = false;
            for (CaseStatement aCase : cases) {
                result = (boolean) aCase.accept(this);
                if (result) {
                    //remove goal from set for default
                    toMatch.remove(goal);
                    //case statement matched and was executed
                    break;
                }
            }

            State<T> stateAfterCase = popState(); //remove state from stack

            if (result && stateAfterCase.getGoals() != null) {
                resultingGoals.addAll(stateAfterCase.getGoals());
            }
        }


        //for all remaining goals execute default
        if (!toMatch.isEmpty()) {
            VariableAssignment va = new VariableAssignment();
            Statements defaultCase = casesStatement.getDefaultCase();
            for (GoalNode<T> goal : toMatch) {
                resultingGoals.addAll(executeBody(defaultCase, goal, va).getGoals());
            }
        }

        //exit scope and create a new state using the union of all newly created goals

        State<T> newStateAfterCases;
        if (!resultingGoals.isEmpty()) {
            //goalsAfterCases.forEach(node -> node.exitScope());
            Stream<GoalNode<T>> goalNodeStream = resultingGoals.stream().filter(tGoalNode -> !(tGoalNode.isClosed()));
            List<GoalNode<T>> openGoalListAfterCases = goalNodeStream.collect(Collectors.toList());

            if (openGoalListAfterCases.size() == 1) {
                newStateAfterCases = new State<T>(openGoalListAfterCases, 0);
            } else {
                newStateAfterCases = new State<T>(openGoalListAfterCases, null);
            }
            stateStack.push(newStateAfterCases);
        }

        //stateStack.peek().getGoals().removeAll(beforeCases.getGoals());
        exitScope(casesStatement);
        return null;
    }

   /* @Override
    public Object visit(SimpleCaseStatement simpleCaseStatement) {
        Expression matchExpression = simpleCaseStatement.getGuard();
        State<T> currentStateToMatch = peekState();
        GoalNode<T> selectedGoal = currentStateToMatch.getSelectedGoalNode();
        VariableAssignment va = evaluateMatchInGoal(matchExpression, selectedGoal);
        if (va != null) {
            enterScope(simpleCaseStatement);
            executeBody(simpleCaseStatement.getBody(), selectedGoal, va);
            exitScope(simpleCaseStatement);
            return true;
        } else {
            return false;
        }
    }*/

    @Override
    public Object visit(TryCase TryCase) {
        enterScope(TryCase);

        exitScope(TryCase);
        return false;
    }

    /**
     * Computes which goals are handled by the different cases in the order the cases appear in the script
     * @param allGoalsBeforeCases
     * @param cases all cases as ordered list
     * @return a mapping of each goal to the matched case body together with variableassignments from the matching process
     */
  /*  private Map<GoalNode, Pair<VariableAssignment, Statements>> matchGoalsToCases(List<GoalNode<T>> allGoalsBeforeCases, CasesStatement cases) {
        //list of cases
        List<CaseStatement> caseStmts = cases.getCases();
        //remaining goals
        Set<GoalNode<T>> setOfRemainingGoals = Sets.newHashSet(allGoalsBeforeCases);

        //returnMap
        Map<GoalNode, Pair<VariableAssignment, Statements>> returnMap = new HashMap<>();

        for (CaseStatement caseStmt : caseStmts) {
            Map<GoalNode<T>, VariableAssignment> goalNodeVariableAssignmentMap = matchGoal(setOfRemainingGoals, caseStmt);
            //put all matched goals to returnMap for case
            goalNodeVariableAssignmentMap.keySet().forEach(tGoalNode ->
            {
                returnMap.put(tGoalNode, new Pair<VariableAssignment, Statements>(goalNodeVariableAssignmentMap.get(tGoalNode), caseStmt.getBody()));
            });
            setOfRemainingGoals.removeAll(goalNodeVariableAssignmentMap.keySet());
        }

        if (!setOfRemainingGoals.isEmpty()) {
            VariableAssignment va = new VariableAssignment();
            Statements defaultCase = cases.getDefaultCase();
            for (GoalNode<T> goal : setOfRemainingGoals) {
                returnMap.put(goal, new Pair<VariableAssignment, Statements>(va, defaultCase));
                // resultingGoals.addAll(executeBody(defaultCase, goal, va).getGoals());
            }
        }
        return returnMap;


    }*/

    /**
     * Match a set of goal nodes against a matchpattern of a case and return the matched goals together with instantiated variables
     *
     * @param allGoalsBeforeCases
     * @param aCase
     * @return
     */
  /*  private Map<GoalNode<T>, VariableAssignment> matchGoal(Set<GoalNode<T>> allGoalsBeforeCases, CaseStatement aCase) {

        HashMap<GoalNode<T>, VariableAssignment> matchedGoals = new HashMap<>();
        if (!aCase.isClosedStmt()) {
            SimpleCaseStatement caseStmt = (SimpleCaseStatement) aCase;
            Expression matchExpression = caseStmt.getGuard();
            for (GoalNode<T> goal : allGoalsBeforeCases) {
                VariableAssignment va = evaluateMatchInGoal(matchExpression, goal);
                if (va != null) {
                    matchedGoals.put(goal, va);
                }
            }
            return matchedGoals;
        } else {
            //handle try and closes
            if(aCase instanceof ClosesCase){
                ClosesCase closesCase = (ClosesCase) aCase;
                for (GoalNode<T> goal : allGoalsBeforeCases) {
                    //put state onto stack
                    newState(goal);
                    boolean matched = (Boolean) closesCase.accept(this);
                    if (matched) {
                        matchedGoals.put(goal, new VariableAssignment(null));
                    }
                }
                return matchedGoals;
            }else{
                TryCase tryCase = (TryCase) aCase;
                for (GoalNode<T> goal : allGoalsBeforeCases) {
                    //put state onto stack
                    newState(goal);
                    boolean matched = (Boolean) tryCase.accept(this);
                    if (matched) {
                        matchedGoals.put(goal, new VariableAssignment(null));
                    }
                }
                return matchedGoals;

            }


        }
    }*/

    /**
     * @param casesStatement
     * @return
     */
/*
    public Object visitOld(CasesStatement casesStatement) {
        enterScope(casesStatement);
        State<T> beforeCases = peekState();

        List<GoalNode<T>> allGoalsBeforeCases = beforeCases.getGoals();
        //copy the list of goal nodes for keeping track of goals
        Set<GoalNode<T>> toMatch = new HashSet<>(allGoalsBeforeCases);

        //global List after all Case Statements
        List<GoalNode<T>> resultingGoals = new ArrayList<>();

        //handle cases
        List<CaseStatement> cases = casesStatement.getCases();

        Map<GoalNode, Pair<VariableAssignment, Statements>> goalToCaseMapping = matchGoalsToCases(allGoalsBeforeCases, casesStatement);

        goalToCaseMapping.forEach((goalNode, variableAssignmentStatementsPair) -> {
            if(variableAssignmentStatementsPair.getValue().isEmpty()){
                //TODO this is the try case
            }else {
                State<T> createdState = newState(goalNode);
                executeBody(variableAssignmentStatementsPair.getValue(), goalNode, variableAssignmentStatementsPair.getKey());
                //stmts.accept(this);

                State<T> stateAfterCase = popState(); //remove state from stack
                if (stateAfterCase.getGoals() != null) {
                    resultingGoals.addAll(stateAfterCase.getGoals());
                }
            }
        });

        //exit scope and create a new state using the union of all newly created goals

        State<T> newStateAfterCases;
        if (!resultingGoals.isEmpty()) {
            //goalsAfterCases.forEach(node -> node.exitScope());
            Stream<GoalNode<T>> goalNodeStream = resultingGoals.stream().filter(tGoalNode -> !(tGoalNode.isClosed()));
            List<GoalNode<T>> openGoalListAfterCases = goalNodeStream.collect(Collectors.toList());

            if (openGoalListAfterCases.size() == 1) {
                newStateAfterCases = new State<T>(openGoalListAfterCases, 0);
            } else {
                newStateAfterCases = new State<T>(openGoalListAfterCases, null);
            }
            stateStack.push(newStateAfterCases);
        }

        //stateStack.peek().getGoals().removeAll(beforeCases.getGoals());
        exitScope(casesStatement);
        return null;
    }*/

    /**
     * Evaluate a match in a specific goal
     *
     * @param matchExpression
     * @param goal
     * @return null, if match was false, return the first Assignment when match was true
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

    protected State<T> executeBody(Statements caseStmts, GoalNode<T> goalNode, VariableAssignment va) {
        enterScope(caseStmts);
        goalNode.enterScope(va);
        State<T> s = newState(goalNode);
        caseStmts.accept(this);
        popState(s);
        exitScope(caseStmts);
        return s;
    }


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
    public Object visit(CallStatement call) {
        enterScope(call);
        //neuer VarScope
        //enter new variable scope
        VariableAssignment params = evaluateParameters(call.getParameters());
        GoalNode<T> g = getSelectedNode();
        g.enterScope();
        try {
            functionLookup.callCommand(this, call, params);
        } catch (RuntimeException e) {
            //TODO handling of error state for each visit
            State<T> newErrorState = newState(null, null);
            newErrorState.setErrorState(true);
            pushState(newErrorState);
        }
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
    public Object visit(TheOnlyStatement theOnly) {
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
    public Object visit(ForeachStatement foreach) {
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
    public Object visit(RepeatStatement repeatStatement) {
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
    public Object visit(Signature signature) {
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

    /**
     * Peek current state
     *
     * @return state on top of state stack
     */
    public State<T> getCurrentState() {
        try {
            return stateStack.peek();
        } catch (EmptyStackException e) {
            return new State<T>(null);
            //FIXME
        }
    }

    /**
     * Create new state containing goals and selected goal node an push to stack
     *
     * @param goals
     * @param selected
     * @return state that is pushed to stack
     */
    public State<T> newState(List<GoalNode<T>> goals, GoalNode<T> selected) {
        if (selected != null && !goals.contains(selected)) {
            throw new IllegalStateException("selected goal not in list of goals");
        }
        return pushState(new State<>(goals, selected));
    }

    /**
     * Cretae a ew state conatining the goals but without selected goal node and push to stack
     *
     * @param goals
     * @return
     */
    public State<T> newState(List<GoalNode<T>> goals) {
        return newState(goals, null);
    }

    /**
     * Cretae a new state containing only the selected goal node and push to stack
     *
     * @param selected
     * @return reference to state on stack
     */
    public State<T> newState(GoalNode<T> selected) {
        return newState(Collections.singletonList(selected), selected);
    }

    /**
     * Push state to stack and return reference to this state
     *
     * @param state
     * @return
     */
    public State<T> pushState(State<T> state) {
        if (stateStack.contains(state)) {
            throw new IllegalStateException("State is already on the stack!");
        }
        stateStack.push(state);
        return state;
    }

    /**
     * Remove top level state from stack and throw an Exception if state does not equal expected state
     *
     * @param expected
     * @return
     */
    public State<T> popState(State<T> expected) {
        State<T> actual = stateStack.pop();
        if (!expected.equals(actual)) {
            throw new IllegalStateException("Error on the stack!");
        }
        return actual;
    }

    /**
     * Remove top level state from stack
     *
     * @return
     */
    protected State<T> popState() {
        return stateStack.pop();
    }

    /**
     * Lookup state on the stack but do not remove it
     *
     * @return
     */
    public State<T> peekState() {
        return stateStack.peek();
    }

    /**
     * Get goalnodes from current state
     *
     * @return
     */
    public List<GoalNode<T>> getCurrentGoals() {
        return getCurrentState().getGoals();
    }


    //endregion
}
