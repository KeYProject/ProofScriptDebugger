package edu.kit.formal.interpreter;

import edu.kit.formal.proofscriptparser.ast.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    private Map<String, Value> asMap(Map<String, Value> map) {
        if (parent != null) {
            parent.asMap(map);
        }
        map.putAll(this.values);
        return map;
    }

    public Map<String, Value> asMap() {
        return asMap(new HashMap<>());
    }

    /**
     * Method joins two variable assignments without checking their compatibility
     *
     * @param assignment
     * @return a new Variable Assignment
     */
    public VariableAssignment joinWithoutCheck(VariableAssignment assignment) {
        VariableAssignment va = new VariableAssignment(null);
        va.getValues().putAll(assignment.getValues());
        va.getTypes().putAll(assignment.getTypes());
        return va;
    }

    /**
     * @param assignment
     * @return empty variable assignment if not possible to join conflictfree (i.e., if a variable name is present
     * in both assignments with different types or dfferent values) the join otherwise
     * @throws RuntimeException
     */
    public VariableAssignment joinWithCheck(VariableAssignment assignment) throws RuntimeException {

        Set<String> namesV2 = assignment.getValues().keySet();
        //create intersection
        Set<String> conflictingCand = this.getValues().keySet().stream().filter(item -> namesV2.contains(item)).collect(Collectors.toSet());

        if (!conflictingCand.isEmpty()) {
            for (String s : conflictingCand) {
                if (!this.lookupVarValue(s).equals(assignment.lookupVarValue(s))) {
                    return new VariableAssignment(null);
                }
            }
        }

        return this.joinWithoutCheck(assignment);
    }

    /**
     * checks whether an assignment is empty i.e. does not contain type declarations and values
     *
     * @return
     */
    public boolean isEmpty() {
        if (this.getValues().isEmpty() && this.parent != null) {
            return this.getParent().isEmpty();
        } else {
            return this.getValues().isEmpty();
        }
    }

    public VariableAssignment push(VariableAssignment va) {
        VariableAssignment nva = push();
        nva.types.putAll(va.types);
        nva.values.putAll(va.values);
        return nva;
    }
}
