package edu.kit.formal.interpreter;

import edu.kit.formal.proofscriptparser.ast.Type;

import java.util.HashMap;
import java.util.HashSet;
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
     * @return
     */
    public VariableAssignment joinWithoutCheck(VariableAssignment assignment) {
        this.getValues().putAll(assignment.getValues());
        this.getTypes().putAll(assignment.getTypes());
        return this;
    }

    /**
     * @param assignment
     * @return empty variable assignment if not possible to join conflictfree (i.e., if a variable name is present in both assignments with different types or dfferent values)
     * @throws RuntimeException
     */
    public VariableAssignment joinWithCheck(VariableAssignment assignment) throws RuntimeException {

        Set<String> namesV2 = assignment.getValues().keySet();
        Set<String> nonConflicting = new HashSet<>();
        Set<String> conflictingCand = new HashSet<>();

        //subtract V2 from V1 and add the nonconflicting varNames into the nonconflicting set
        nonConflicting = this.getValues().keySet().stream().filter(item -> !namesV2.contains(item)).collect(Collectors.toSet());
        //subtract V1 from V2 and add the nonconflicting varNames into the nonconflicting set
        nonConflicting.addAll(namesV2.stream().filter(item -> !this.getValues().keySet().contains(item)).collect(Collectors.toSet()));
        //create intersection
        conflictingCand = this.getValues().keySet().stream().filter(item -> namesV2.contains(item)).collect(Collectors.toSet());
        if (!conflictingCand.isEmpty()) {
            for (String s : conflictingCand) {
                if (!this.lookupVarValue(s).equals(assignment.lookupVarValue(s))) {
                    return new VariableAssignment(null);
                }
            }
        }

        return this.joinWithoutCheck(assignment);
    }
}
