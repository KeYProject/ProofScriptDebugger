package edu.kit.iti.formal.psdbg.interpreter.dbg;

import edu.kit.iti.formal.psdbg.interpreter.Interpreter;
import edu.kit.iti.formal.psdbg.interpreter.NodeAddedEvent;
import edu.kit.iti.formal.psdbg.interpreter.StateAddedEvent;
import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.State;
import edu.kit.iti.formal.psdbg.interpreter.graphs.EdgeTypes;
import edu.kit.iti.formal.psdbg.parser.DefaultASTVisitor;
import edu.kit.iti.formal.psdbg.parser.Visitor;
import edu.kit.iti.formal.psdbg.parser.ast.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Alexander Weigl
 * @version 1 (27.10.17)
 */
@NoArgsConstructor
@AllArgsConstructor
public class StateWrapper<T> implements InterpreterObserver<T> {
    private static final Logger LOGGER = LogManager.getLogger(StateWrapper.class);

    @Getter
    @Setter
    private Interpreter<T> interpreter;

    @Getter
    private Visitor<Void> entryListener = new EntryListener();

    @Getter
    private Visitor<Void> exitListener = new ExitListener();

    @Getter
    private List<ASTNode> contextStack = new ArrayList<>(100);

    protected void emitNode(PTreeNode<T> node) {
    }

    public ASTNode[] getContextCopy() {
        return contextStack.toArray(new ASTNode[contextStack.size()])
    }

    /**
     * @param node
     */
    public PTreeNode<T> createNode(ProofScript node) {
        LOGGER.info("Creating Root for State graph with statement {}@{}", node.getNodeName(), node.getStartPosition());

        PTreeNode<T> newStateNode = new PTreeNode<>(node);
        newStateNode.setContext(getContextCopy());
        contextStack.add(node);
        State<T> currentInterpreterStateCopy = getInterpreterStateCopy();
        newStateNode.setStateBeforeStmt(currentInterpreterStateCopy);

        //add node to state graph
        return newStateNode;
    }

    public void createRoot(ProofScript node) {
        emitNode(createNode(node));
    }

    public void createRoot(ProofScript node) {
        emitNode(createNode(node));
    }


    public State<T> getInterpreterStateCopy() {
        return currentInterpreter.getCurrentState().copy();
    }

    /**
     * Create a new node for the state graph and add edges to already existing nodes
     *
     * @param node
     * @return null
     */
    private Void createNewNode(ASTNode node, boolean isCasesStmt) {
        LOGGER.info("Creating Node for State graph with statement {}@{}", node.getNodeName(), node.getStartPosition());
        //save old pointer of last node
        State<T> lastState;
        lastState = currentInterpreter.getCurrentState().copy();

        //create a new ProofTreeNode in graph with ast node as parameter
        PTreeNode<T> newStateNode = new PTreeNode<>(node);
        newStateNode.setContext(lastNode.getContext());
        //get the state before executing ast node to save it to the extended state
        InterpreterExtendedState<T> extState;

        if (isCasesStmt) {
            newStateNode.getContext().push(node);
            extState = new InterpreterExtendedState<>(lastNode.getExtendedState().copy());
            extState.setStmt(node);
            extState.setStateBeforeStmt(lastState.copy());
            Map<CaseStatement, List<GoalNode<T>>> mappingOfCaseToStates = extState.getMappingOfCaseToStates();
            CasesStatement cs = (CasesStatement) node;
            //initialize the cases in the mapping map
            cs.getCases().forEach(caseStatement -> {
                mappingOfCaseToStates.put(caseStatement, Collections.emptyList());
            });

            extState.setMappingOfCaseToStates(mappingOfCaseToStates);
            //TODO default case
            //newStateNode.setState(lastState.copy());

        } else {

            //set pointer to parent extended state
            extState = new InterpreterExtendedState<>(lastNode.getExtendedState().copy());
            extState.setStmt(node);
            //check whether case statement
            if (extState.getMappingOfCaseToStates().containsKey(node)) {
                extState.getMappingOfCaseToStates().get(node).add(lastState.getSelectedGoalNode().deepCopy());
                extState.setStateBeforeStmt(new State<T>(extState.getMappingOfCaseToStates().get(node), lastState.getSelectedGoalNode().deepCopy()));
            } else {
                extState.setStateBeforeStmt(lastState);
            }
        }

        newStateNode.setExtendedState(extState);

        stateGraph.addNode(newStateNode);

        stateGraph.putEdgeValue(lastNode, newStateNode, EdgeTypes.STATE_FLOW);

        //inform listeners about a new node in the graph
        fireNodeAdded(new NodeAddedEvent(lastNode, newStateNode));
        addedNodes.put(node, newStateNode);
        lastNode = newStateNode;
        return null;
    }

    /**
     * Add a new state to an existing PTreeNode
     *
     * @param node
     * @param fireStateAdded
     * @return
     */
    private Void addState(ASTNode node, boolean fireStateAdded) {
        LOGGER.info("Adding a new state for statement {}@{}", node.getNodeName(), node.getStartPosition());
        //get node from addedNodes Map
        PTreeNode<T> newStateNode = addedNodes.get(node);
        //copy Current Interpreter state
        State<T> currentState = currentInterpreter.getCurrentState().copy();
        //set the state
        if (node != this.root.get().getScriptStmt()) {
            //newStateNode.setState(currentState);
            newStateNode.getExtendedState().setStateAfterStmt(currentState);
            if (newStateNode.getContext().peek().equals(node)) {
                newStateNode.getContext().pop();
            }
        } else {
            //  newStateNode.setState(currentState);
            newStateNode.getExtendedState().setStateAfterStmt(currentState);
            if (newStateNode.getContext().peek().equals(node)) {
                newStateNode.getContext().pop();
            }
        }
        if (fireStateAdded) {
            fireStateAdded(new StateAddedEvent(newStateNode, currentState, newStateNode.getExtendedState()));
        }
        LOGGER.debug("Extended state for {} updated with {} \n", node.getStartPosition(), newStateNode.getExtendedState().toString());
        return null;
    }


    private class EntryListener extends DefaultASTVisitor<Void> {
        @Override
        public Void visit(ProofScript proofScript) {
            if (root.get() == null) {
                createRootNode(proofScript);
            } else {
                if (!root.get().getScriptstmt().equals(proofScript)) {
                    createNewNode(proofScript);
                }
            }

            return null;
        }

        @Override
        public Void visit(AssignmentStatement assignment) {
            return createNewNode(assignment);
        }


        @Override
        public Void visit(CasesStatement casesStatement) {
            return createNewNode(casesStatement, true);
        }

        /* @Override
         public Void visit(CaseStatement caseStatement) {
             return createNewNode(caseStatement);
         }
         */
        @Override
        public Void visit(CallStatement call) {
            return createNewNode(call);
        }

        @Override
        public Void visit(TheOnlyStatement theOnly) {
            return createNewNode(theOnly);
        }

        @Override
        public Void visit(ForeachStatement foreach) {
            return createNewNode(foreach);
        }

        @Override
        public Void visit(RepeatStatement repeatStatement) {
            return createNewNode(repeatStatement);
        }

        @Override
        public Void visit(MatchExpression matchExpression) {
            return createNewNode(matchExpression);
        }

        @Override
        public Void visit(TryCase tryCase) {
            return createNewNode(tryCase);
        }

        @Override
        public Void visit(SimpleCaseStatement simpleCaseStatement) {
            return createNewNode(simpleCaseStatement);
        }

        @Override
        public Void visit(ClosesCase closesCase) {
            return createNewNode(closesCase);
        }

        @Override
        public Void visit(DefaultCaseStatement defCase) {
            return createNewNode(defCase);
        }
    }

    private class ExitListener extends DefaultASTVisitor<Void> {

        @Override
        public Void visit(AssignmentStatement assignment) {
            return addState(assignment);
        }

        @Override
        public Void visit(ProofScript proofScript) {
            return addState(proofScript, false);
        }

        @Override
        public Void visit(CasesStatement casesStatement) {
            return addState(casesStatement, false);
        }

        /*@Override
        public Void visit(CaseStatement caseStatement) {
            return addState(caseStatement, false);
        }*/

        @Override
        public Void visit(DefaultCaseStatement defCase) {
            return addState(defCase);
        }

        @Override
        public Void visit(CallStatement call) {
            //  System.out.println("Call "+call.getCommand()+" "+currentInterpreter.getCurrentState());
            return addState(call);
        }

        @Override
        public Void visit(TheOnlyStatement theOnly) {
            return addState(theOnly);
        }

        @Override
        public Void visit(ForeachStatement foreach) {
            return addState(foreach);
        }

        @Override
        public Void visit(RepeatStatement repeatStatement) {
            return addState(repeatStatement);
        }

        @Override
        public Void visit(TryCase tryCase) {
            return addState(tryCase);
        }

        @Override
        public Void visit(SimpleCaseStatement simpleCaseStatement) {
            return addState(simpleCaseStatement, false);
        }

        @Override
        public Void visit(ClosesCase closesCase) {
            return addState(closesCase);
        }
    }
}
