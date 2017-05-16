package edu.kit.formal.interpreter;

import edu.kit.formal.proofscriptparser.ast.Type;

/**
 * Objects of this class represent a GoalNode in a script state
 * If parent is null, this is the root
 *
 * @author S.Grebing
 */
public class GoalNode {

    //TODO this is only for testing, later Sequent object or similar
    private String sequent;

    private VariableAssignment assignments;

    private GoalNode parent;

    public GoalNode(GoalNode parent, String seq) {
        if (parent == null) {
            this.assignments = new VariableAssignment(null);
        }
        this.parent = parent;
        this.sequent = seq;
    }

    public VariableAssignment getAssignments() {
        return assignments;
    }

    public GoalNode setAssignments(VariableAssignment assignments) {
        this.assignments = assignments;
        return this;
    }

    public GoalNode getParent() {
        return parent;
    }

    public String toString() {
        return sequent;
    }

    /**
     * @param varname
     * @return value of variable if it exists
     */
    public Value lookupVarValue(String varname) {

        Value v = assignments.getValue(varname);
        if (v != null) {
            return v;
        } else {
            throw new RuntimeException("Value of variable " + varname + " is not defined in goal node " + this.toString());

        }

    }

    /**
     * Lookup the type of the variable in the type map
     *
     * @param id
     * @return
     */

    public Type lookUpType(String id) {
        Type t = this.getAssignments().getTypes().get(id);
        if (t == null) {
            //TODO lookup parent and outer Scope
            // this.getAssignments().
        } else {
            return t;
        }
        return null;
    }


    /**
     * Add a variable declaration to the type map
     * TODO default value in valuemap?
     *
     * @param name
     * @param t
     */
    public void addVarDecl(String name, Type t) {
        this.assignments = getAssignments().addVariable(name, t);

    }

    /**
     * Set the value of a variable in the value map
     *
     * @param name
     * @param v
     */
    public void setVarValue(String name, Value v) {
        VariableAssignment assignments = getAssignments();
        if (assignments.getTypes().containsKey(name)) {
            assignments.setVar(name, v);
        } else {
            throw new RuntimeException("Variable " + name + " has to be declared first");
        }
    }

    /**
     * Enter new variable scope and push onto stack
     */
    public void enterNewVarScope() {
        this.assignments = this.assignments.push();
    }

    public void exitNewVarScope() {
        this.assignments = this.assignments.pop();
    }

    public VariableAssignment peek() {
        return null;
    }

}
