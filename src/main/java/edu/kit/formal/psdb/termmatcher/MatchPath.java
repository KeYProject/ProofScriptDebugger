package edu.kit.formal.psdb.termmatcher;

import de.uka.ilkd.key.logic.Term;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.key_project.util.collection.ImmutableList;
import org.key_project.util.collection.ImmutableSLList;

/**
 * @author Alexander Weigl
 * @version 1 (24.08.17)
 */
@Data
@EqualsAndHashCode(exclude = {"parent","posInParent"})
public class MatchPath<T> {
    private final MatchPath<?> parent;
    private final T term;
    private final int posInParent;

    public MatchPath(MatchPath<?> parent, T unit, int pos) {
        posInParent = pos;
        term = unit;
        this.parent = parent;
    }

    public static MatchPath<Term> createTermPath(MatchPath<Term> path, int i) {
        return new MatchPath<>(path, path.getTerm().sub(i), i);
    }

    public ImmutableList<Integer> getPos() {
        if (parent == null) {
            return ImmutableSLList.singleton(posInParent);
        } else {
            return parent.getPos().append(posInParent);
        }
    }

    public static <T> MatchPath<T> createRoot(T keyTerm) {
        return new MatchPath<>(null, keyTerm, -1);
    }

    public String toString() {
        return term.toString();
    }

}
