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

    public Type lookupType(String name) {
        if (parent == null) {
            return types.getOrDefault(name, null);
        } else {
            return types.getOrDefault(name, parent.lookupType(name));
        }
    }

/*    public VariableAssignment copy() {
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
*/

    /**
     * Lookup value of variable also in parent assignments
     *
     * @param name
     * @return
     */

    public Value lookupVarValue(String name) {
        if (parent == null) {
            return values.getOrDefault(name, null);
        } else {
            return values.getOrDefault(name, parent.lookupVarValue(name));
        }
    }

    public VariableAssignment addVarDecl(String name, Type type) {
        if (lookupType(name) == null) {
            this.types.put(name, type);
            return this;
        } else {
            throw new RuntimeException("Variable " + name + " is already declared with type " + type.toString());
        }
    }


    //enterscope
    public VariableAssignment push() {
        return new VariableAssignment(this);
    }

    //leavescope
    public VariableAssignment pop() {
        return getParent();
    }

    public VariableAssignment setVarValue(String name, Value v) {
        VariableAssignment temp = this;
        if (this.getTypes().containsKey(name)) {
            this.values.put(name, v);
        } else {
            if (parent != null) {
                parent.setVarValue(name, v);
            } else {
                throw new RuntimeException("Variable " + name + " needs to be declared first");
            }
        }
        return temp;
    }

    @Override
    public String toString() {
        return "VariableAssignment{" +
                "parent=" + parent +
                ", values=" + values +
                ", types=" + types +
                '}';
    }

    public VariableAssignment deepCopy() {
        HashMap<String, Type> typeMapCopy = new HashMap<>();
        types.forEach((k, v) -> typeMapCopy.put(k, v));
        HashMap<String, Value> valueMap = new HashMap<>();
        values.forEach((k, v) -> valueMap.put(k, v));
        VariableAssignment copy = new VariableAssignment(parent == null ? null : parent.deepCopy());
        copy.values = valueMap;
        copy.types = typeMapCopy;
        return copy;
    }
}
