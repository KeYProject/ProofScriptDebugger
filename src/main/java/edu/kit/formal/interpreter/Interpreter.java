package edu.kit.formal.interpreter;

import de.uka.ilkd.key.api.ScriptApi;
import de.uka.ilkd.key.macros.scripts.EngineState;
import edu.kit.formal.interpreter.funchdl.CommandCall;
import edu.kit.formal.interpreter.funchdl.CommandLookup;
import edu.kit.formal.proofscriptparser.DefaultASTVisitor;
import edu.kit.formal.proofscriptparser.Visitor;
import edu.kit.formal.proofscriptparser.ast.*;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.ParserRuleContext;

import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.*;
import java.util.logging.Logger;

/**
 * Main Class for interpreter
 *
 * @author S.Grebing
 */
public class Interpreter extends DefaultASTVisitor<Void>
        implements ScopeObservable {
    //TODO later also include information about source line for each state (for debugging purposes and rewind purposes)
    private Stack<AbstractState> stateStack = new Stack<>();
    private static Logger logger = Logger.getLogger("interpreter");

    @Getter
    private List<Visitor> entryListeners = new ArrayList<>(),
            exitListeners = new ArrayList<>();

    @Getter
    @Setter
    private MatcherApi matcherApi;

    @Getter
    private CommandLookup functionLookup;

    @Getter
    @Setter
    private boolean scrictSelectedGoalMode = false;
    private EngineState engineState;
    private ScriptApi scriptApi;

    public Interpreter(CommandLookup lookup) {
        functionLookup = lookup;
    }

    //starting point is a statement list
    public void interpret(List<ProofScript> scripts, String sequent) {
        newState(new GoalNode(null, sequent));
        //execute first script (RULE: The first script in the file is main script)
        ProofScript m = scripts.get(0);
        //later through interface with getMainScript();
        m.accept(this);
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

        GoalNode node = getSelectedNode();
        Type t = assignmentStatement.getType();
        Variable var = assignmentStatement.getLhs();
        Expression expr = assignmentStatement.getRhs();
        if (t != null) {
            node.addVarDecl(var.getIdentifier(), t);
        }

        if (expr != null) {
            Type type = node.lookUpType(var.getIdentifier());
            if (type == null) {
                throw new RuntimeException("Type of Variable " + var.getIdentifier() + " is not declared yet");
            } else {
                Value v = evaluate(expr);
                node.setVarValue(var.getIdentifier(), v);
            }
        }
        exitScope(assignmentStatement);

        return null;
    }

    private Value evaluate(Expression expr) {
        return evaluate(getSelectedNode(), expr);
    }

    private Value evaluate(GoalNode g, Expression expr) {
        enterScope(expr);
        Evaluator evaluator = new Evaluator(g.getAssignments(),g);
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
        State beforeCases = (State) stateStack.pop();
        List<GoalNode> allGoalsBeforeCases = beforeCases.getGoals();
        for (GoalNode node : allGoalsBeforeCases) {
            node.enterNewVarScope();
        }
        List<GoalNode> goalsAfterCases = new ArrayList<>();
        //copy the list of goal nodes for keeping track of goals
        Set<GoalNode> copiedList = new HashSet<>();
        for (GoalNode goalNode : allGoalsBeforeCases) {
            copiedList.add(goalNode);
        }

        //handle cases
        List<CaseStatement> cases = casesStatement.getCases();
        Iterator<CaseStatement> casesIter = cases.iterator();
        while (casesIter.hasNext()) {
            //get information for case
            CaseStatement currentCase = casesIter.next();
            Expression guard = currentCase.getGuard();
            Statements body = currentCase.getBody();


            Iterator<GoalNode> goalIter = copiedList.iterator();
            Set<GoalNode> forCase = new HashSet<>();
            //iterate over all available goals and select those that evaluate to true with the guard
            //assumption, matchpattern handles varAssignments
            while (goalIter.hasNext()) {
                GoalNode g = goalIter.next();
                Value eval = evaluate(g, guard);
                System.out.println();
                if (eval.getData().equals(true)) {
                    forCase.add(g);
                }
            }
            copiedList.removeAll(forCase);

            //for each selected goal put a state onto tze stack and visit the body of the case
            Iterator<GoalNode> caseGoals = forCase.iterator();
            while (caseGoals.hasNext()) {
                GoalNode current = caseGoals.next();
                List<GoalNode> goalList = new ArrayList<>();
                goalList.add(current);
                State s = new State(goalList, current);
                stateStack.push(s);
                visit(body);
                //after executing the body collect the newly created goals form the stack and remove the state
                State aftercase = (State) stateStack.pop();
                goalsAfterCases.addAll(aftercase.getGoals());
            }

        }
        //for all remaining goals execute default
        if (!copiedList.isEmpty()) {
            Statements defaultCase = casesStatement.getDefaultCase();
            Iterator<GoalNode> remainingGoalsIter = copiedList.iterator();
            while (remainingGoalsIter.hasNext()) {
                GoalNode next = remainingGoalsIter.next();
                List<GoalNode> goalList = new ArrayList<>();
                goalList.add(next);
                State s = new State(goalList, next);
                stateStack.push(s);
                visit(defaultCase);
                State aftercase = (State) stateStack.pop();
                goalsAfterCases.addAll(aftercase.getGoals());

            }


        }

        //exit scope and create a new state using the union of all newly created goals

        State newStateAfterCases;
        if (!goalsAfterCases.isEmpty()) {
            for (GoalNode goalAfterCases : goalsAfterCases) {
                goalAfterCases.exitNewVarScope();
            }
            if (goalsAfterCases.size() == 1) {
                newStateAfterCases = new State(goalsAfterCases, goalsAfterCases.get(0));
            } else {
                newStateAfterCases = new State(goalsAfterCases, null);
            }
            stateStack.push(newStateAfterCases);
        }

        exitScope(casesStatement);
        return null;
    }


    /**
     * @param caseStatement
     * @return
     */
    @Override
    public Void visit(CaseStatement caseStatement) {
        enterScope(caseStatement);
        exitScope(caseStatement);
        return null;
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
    public Void visit(CallStatement call) {
        enterScope(call);
        //neuer VarScope
        //enter new variable scope
        VariableAssignment params = evaluateParameters(call.getParameters());
        GoalNode g = getSelectedNode();
        g.enterNewVarScope();
        functionLookup.callCommand(this, call, params);
        g.exitNewVarScope();
        exitScope(call);
        return null;
    }


    public VariableAssignment evaluateParameters(Parameters parameters) {
        VariableAssignment va = new VariableAssignment();
        parameters.entrySet().forEach(entry -> {
            Value val = evaluate(entry.getValue());
            va.addVarDecl(entry.getKey().getIdentifier(), val.getType());
            va.setVarValue(entry.getKey().getIdentifier(), val);
        });
        return va;
    }

    @Override
    public Void visit(TheOnlyStatement theOnly) {
        List<GoalNode> goals = getCurrentState().getGoals();
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
        List<GoalNode> allGoals = getCurrentGoals();
        List<GoalNode> goalsAfterForeach = new ArrayList<>();
        Statements body = foreach.getBody();
        for (GoalNode goal : allGoals) {
            newState(goal);
            visit(body);
            AbstractState s = popState();
            goalsAfterForeach.addAll(s.getGoals());
        }
        State afterForeach = new State(goalsAfterForeach, null);
        stateStack.push(afterForeach);
        exitScope(foreach);
        return null;
    }

    @Override
    public Void visit(RepeatStatement repeatStatement) {
        enterScope(repeatStatement);
        boolean b = false;
        do {
            AbstractState prev = getCurrentState();
            repeatStatement.getBody().accept(this);
            AbstractState end = getCurrentState();

            Set<GoalNode> prevNodes = new HashSet<>(prev.getGoals());
            Set<GoalNode> endNodes = new HashSet<>(end.getGoals());
            b = prevNodes.equals(endNodes);
        } while (b);
        exitScope(repeatStatement);
        return null;
    }

    @Override
    public Void visit(Signature signature) {
        exitScope(signature);
        GoalNode node = getSelectedNode();
        node.enterNewVarScope();
        signature.forEach((v, t) -> {
            node.addVarDecl(v.getIdentifier(), t);
        });
        enterScope(signature);
        return null;
    }

    //region State Handling
    public GoalNode getSelectedNode() {
        try {
            return stateStack.peek().getSelectedGoalNode();
        } catch (IllegalStateException e) {
            if (scrictSelectedGoalMode)
                throw e;

            logger.warning("No goal selected. Returning first goal!");
            return getCurrentGoals().get(0);
        }
    }

    public AbstractState getCurrentState() {
        return stateStack.peek();
    }

    public AbstractState newState(List<GoalNode> goals, GoalNode selected) {
        if (selected != null && !goals.contains(selected)) {
            throw new IllegalStateException("selected goal not in list of goals");
        }
        return pushState(new State(goals, selected));
    }

    public AbstractState newState(List<GoalNode> goals) {
        return newState(goals, null);
    }

    public AbstractState newState(GoalNode selected) {
        return newState(Collections.singletonList(selected), selected);
    }

    public AbstractState pushState(AbstractState state) {
        if (stateStack.contains(state)) {
            throw new IllegalStateException("State is already on the stack!");
        }
        stateStack.push(state);
        return state;
    }

    public void popState(AbstractState expected) {
        AbstractState actual = stateStack.pop();
        if (!expected.equals(actual)) {
            throw new IllegalStateException("Error on the stack!");
        }
    }

    private AbstractState popState() {
        return stateStack.pop();
    }

    public List<GoalNode> getCurrentGoals() {
        return getCurrentState().getGoals();
    }

    public EngineState getEngineState() {
        return engineState;
    }

    public ScriptApi getScriptApi() {
        return scriptApi;
    }
    //endregion
}
