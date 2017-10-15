package edu.kit.iti.formal.psdbg.interpreter.data;

import edu.kit.iti.formal.psdbg.parser.ast.CaseStatement;

import java.util.*;
import java.util.stream.Collectors;


public class InterpreterExtendedState<T> extends State<T> {

    private Map<CaseStatement, List<GoalNode<T>>> mappingOfCaseToStates = new HashMap<>();

    public InterpreterExtendedState(Collection<GoalNode<T>> goals, GoalNode selected) {
        super(goals, selected);
    }

    public InterpreterExtendedState(Collection<GoalNode<T>> goals, GoalNode selected, Map<CaseStatement, List<GoalNode<T>>> casesMapping) {
        super(goals, selected);
        this.mappingOfCaseToStates = casesMapping;
    }

    public Map<CaseStatement, List<GoalNode<T>>> getMappingOfCaseToStates() {
        return mappingOfCaseToStates;
    }

    public void setMappingOfCaseToStates(Map<CaseStatement, List<GoalNode<T>>> mappingOfCaseToStates) {
        this.mappingOfCaseToStates = mappingOfCaseToStates;
    }

    public List<GoalNode<T>> getClosedNodes() {
        return super.getGoals().stream().filter(nodes -> nodes.isClosed()).collect(Collectors.toList());
    }

    public List<GoalNode<T>> getopenNodes() {
        return super.getGoals().stream().filter(nodes -> !nodes.isClosed()).collect(Collectors.toList());
    }

    public List<GoalNode<T>> getActiveGoalsForCase(CaseStatement caseStmt) {
        return mappingOfCaseToStates.getOrDefault(caseStmt, Collections.emptyList());

    }
    //getuserSelected/
    //verfuegbar im case
    //map<case, listgoal>
    //regel anwendbar oder nicht
}
