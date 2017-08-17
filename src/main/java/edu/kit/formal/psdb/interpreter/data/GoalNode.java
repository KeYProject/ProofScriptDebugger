package edu.kit.formal.psdb.interpreter.data;

import edu.kit.formal.psdb.parser.ast.Variable;
import edu.kit.formal.psdb.parser.types.Type;
import lombok.Getter;
import lombok.ToString;

/**
 * Objects of this class represent a GoalNode in a script state
 * If parent is null, this is the root
 *
 * @author S.Grebing
 */
@ToString
public class GoalNode<T> {
    private VariableAssignment assignments;

    @Getter
    private GoalNode<T> parent;

    @Getter
    private T data;

    @Getter
    private boolean isClosed;

    /**
     * This conctructur will be replaced with concrete one that uses projectedNode
     *
     * @param parent
     * @param data
     */
    public GoalNode(GoalNode<T> parent, T data, boolean isClosed) {
        //BUG: Hier muesste deepcopy der assignments passieren
        this.assignments = new VariableAssignment(parent == null ? null : parent.deepCopyAssignments());
        this.parent = parent;
        this.data = data;
    }

    private VariableAssignment deepCopyAssignments() {
        return assignments.deepCopy();
    }

    public VariableAssignment getAssignments() {
        return assignments;
    }

    /**
     * @param varname
     * @return value of variable if it exists
     */
    public Value getVariableValue(Variable varname) {
        return assignments.getValue(varname);

    }

    /**
     * Lookup the type of the variable in the type map
     *
     * @param varname
     * @return
     */
    public Type getVariableType(Variable varname) {
        Type t = this.getAssignments().getType(varname);
        if (t == null) {
            throw new RuntimeException("Variable " + varname + " must be declared first");
        } else {

            return t;
        }
    }


    /**
     * Add a variable declaration to the type map (TODO Default value in map?)
     *
     * @param name
     * @param type
     * @throws NullPointerException
     */
    public void declareVariable(Variable name, Type type) {
        this.getAssignments().declare(name, type);
    }

    /**
     * Set the value of a variable in the value map
     *
     * @param name
     * @param value
     */
    public void setVariableValue(Variable name, Value value) {
        getAssignments().assign(name, value);
    }

    /**
     * Enter new variable scope and push onto stack
     */
    public VariableAssignment enterScope() {
        assignments = assignments.push();
        return assignments;
    }


    public VariableAssignment exitScope() {
        this.assignments = this.assignments.pop();
        return assignments;
    }

    public GoalNode<T> deepCopy() {
        //TODO method does nothing helpful atm
        return new GoalNode<T>(parent, data, isClosed);
    }

    public VariableAssignment enterScope(VariableAssignment va) {
        assignments = assignments.push(va);
        return assignments;
    }

    public String toCellTextForKeYData() {
        KeyData kd = (KeyData) this.data;
        return kd.getNode().sequent().toString();


    }

    public String toListLabelForKeYData() {
        KeyData kd = (KeyData) this.data;
        return Integer.toString(kd.getNode().serialNr());

    }
}
