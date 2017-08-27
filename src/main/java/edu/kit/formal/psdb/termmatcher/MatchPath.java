package edu.kit.formal.psdb.termmatcher;

import de.uka.ilkd.key.logic.Semisequent;
import de.uka.ilkd.key.logic.Sequent;
import de.uka.ilkd.key.logic.Term;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.key_project.util.collection.ImmutableList;
import org.key_project.util.collection.ImmutableSLList;

/**
 * @author Alexander Weigl
 * @version 1 (24.08.17)
 */
@Data
@EqualsAndHashCode(of = {"term"})
public abstract class MatchPath<T> {
    public static final int ROOT = -1;
    public static final int POSITION_ANTECEDENT = -2;
    public static final int POSITION_SUCCEDENT = -3;

    private final MatchPath<?> parent;
    private final T term;
    private final int posInParent;

    public MatchPath(MatchPath<?> parent, T unit, int pos) {
        posInParent = pos;
        term = unit;
        this.parent = parent;
    }

    public static MatchPath<Term> createTermPath(MatchPath<Term> path, int i) {
        return new MatchPathTerm(path, path.getTerm().sub(i), i);
    }

    public ImmutableList<Integer> getPos() {
        if (parent == null) {
            return ImmutableSLList.singleton(posInParent);
        } else {
            return parent.getPos().append(posInParent);
        }
    }

    public static MatchPath<Term> createRoot(Term keyTerm) {
        return new MatchPathTerm(null, keyTerm, -1);
    }

    public String toString() {
        return term.toString();
    }

    public static MatchPathSemiSequent createSemiSequent(Sequent s, boolean antec) {
        MatchPathSemiSequent mp = new MatchPathSemiSequent(
                createSequent(s), antec ? s.antecedent() : s.succedent(), antec);
        return mp;
    }

    private static MatchPathSequent createSequent(Sequent s) {
        return new MatchPathSequent(s);
    }

    public static MatchPathSemiSequent createSuccedent(Sequent sequent) {
        return createSemiSequent(sequent, false);
    }

    public static MatchPathSemiSequent createAntecedent(Sequent sequent) {
        return createSemiSequent(sequent, true);
    }

    public abstract int depth();

    public static class MatchPathTerm extends MatchPath<Term> {
        public MatchPathTerm(MatchPath<?> parent, Term unit, int pos) {
            super(parent, unit, pos);
        }

        @Override
        public int depth() {
            return getTerm().depth();
        }
    }


    public static class MatchPathSequent extends MatchPath<Sequent> {
        public MatchPathSequent(Sequent unit) {
            super(null, unit, ROOT);
        }

        @Override
        public int depth() {
            return 0;
        }
    }

    public static class MatchPathSemiSequent extends MatchPath<Semisequent> {
        public MatchPathSemiSequent(MatchPathSequent parent, Semisequent unit, boolean antec) {
            super(parent, unit, antec ? POSITION_ANTECEDENT : POSITION_SUCCEDENT);
        }

        @Override
        public int depth() {
            return 1;
        }
    }
}
