package edu.kit.formal.interpreter;

import edu.kit.formal.proofscriptparser.ast.Type;
import lombok.Getter;

/**
 * Objects of this class represent a GoalNode in a script state
 * If parent is null, this is the root
 *
 * @author S.Grebing
 */
public class GoalNode {
    //TODO this is only for testing, later Sequent object or similar
    @Getter
    private String sequent;

    private VariableAssignment assignments;

    private GoalNode parent;

    public GoalNode(GoalNode parent, String seq) {
        this.assignments = new VariableAssignment(parent == null ? null : parent.assignments);
        this.parent = parent;
        this.sequent = seq;
    }

    public VariableAssignment getAssignments() {
        return assignments;
    }

    public GoalNode getParent() {
        return parent;
    }

    public String toString() {
        String s = "Seq: " + sequent + "\n" +
                assignments.toString();
        return s;
    }

    /**
     * @param varname
     * @return value of variable if it exists
     */
    public Value lookupVarValue(String varname) {
        Value v = assignments.lookupVarValue(varname);
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
        Type t = this.getAssignments().lookupType(id);
        if (t == null) {
            throw new RuntimeException("Variable " + id + " must be declared first");
        } else {

            return t;
        }
    }


    /**
     * Add a variable declaration to the type map (TODO Default value in map?)
     * @param name
     * @param t
     */
    public void addVarDecl(String name, Type t) {
        VariableAssignment assignments = this.getAssignments().addVarDecl(name, t);
        if (assignments == null) {
            throw new RuntimeException("Could not add var decl " + name);
        } else {
            this.assignments = assignments;
        }
    }

    /**
     * Set the value of a variable in the value map
     *
     * @param name
     * @param v
     */
    public void setVarValue(String name, Value v) {
        VariableAssignment assignments = getAssignments();
        assignments.setVarValue(name, v);

    }

    /**
     * Enter new variable scope and push onto stack
     */
    public VariableAssignment enterNewVarScope() {
        assignments = assignments.push();
        return assignments;
    }

    public VariableAssignment exitNewVarScope() {
        this.assignments = this.assignments.pop();
        return assignments;
    }

    public GoalNode deepCopy() {
        //TODO method does nothing helpful atm
        return new GoalNode(this.getParent(), sequent);
    }
}
