package edu.kit.formal.interpreter;

import edu.kit.formal.proofscriptparser.ast.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * Variable Assignments for each goal node
 *
 * @author S.Grebing
 */
public class VariableAssignment {

    private final VariableAssignment parent;
    Map<String, Value> values;
    Map<String, Type> types;

    public VariableAssignment(VariableAssignment parent) {
        values = new HashMap<>();
        types = new HashMap<>();
        this.parent = parent;
    }

    public VariableAssignment() {
        this(null);
    }

    public VariableAssignment getParent() {
        return parent;
    }

    public Map<String, Value> getValues() {
        return values;
    }

    public Map<String, Type> getTypes() {
        return types;
    }

    public VariableAssignment copy() {
        VariableAssignment copy;
        if (parent != null) {
            copy = new VariableAssignment(this.parent.copy());

        } else {
            copy = new VariableAssignment(null);
        }
        //TODO
        //copy.setValues(deepcopy of values);
        //deepcopy types
        return copy;
    }


    /**
     * Lookup value of variable also in parent assignments
     *
     * @param name
     * @return
     */
    //TODO throw exception
    public Value getValue(String name) {
        if (parent == null) {
            return values.getOrDefault(name, null);
        } else {
            return values.getOrDefault(name, parent.getValue(name));
        }
    }

    public VariableAssignment addVariable(String name, Type type) {
        this.types.put(name, type);
        return this;
    }

    /*
        public VariableAssignment peek(){
            TODO?
        }*/
    //enterscope
    public VariableAssignment push() {
        return new VariableAssignment(this);
    }

    //leavescope
    public VariableAssignment pop() {
        return getParent();
    }

    public VariableAssignment setVar(String name, Value v) {
        this.getValues().put(name, v);
        return this;
    }
}
