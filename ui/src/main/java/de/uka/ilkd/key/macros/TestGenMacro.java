package de.uka.ilkd.key.macros;

import de.uka.ilkd.key.control.UserInterfaceControl;
import de.uka.ilkd.key.logic.PosInOccurrence;
import de.uka.ilkd.key.macros.ProofMacro;
import de.uka.ilkd.key.proof.Goal;
import de.uka.ilkd.key.proof.Node;
import de.uka.ilkd.key.proof.Proof;
import de.uka.ilkd.key.proof.ProverTaskListener;
import org.key_project.util.collection.ImmutableList;

public class TestGenMacro implements ProofMacro {
    @Override
    public String getName() {
        return "dummy test gen macro";
    }

    @Override
    public String getScriptCommandName() {
        return null;
    }

    @Override
    public String getCategory() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public boolean canApplyTo(Proof proof, ImmutableList<Goal> immutableList, PosInOccurrence posInOccurrence) {
        return false;
    }

    @Override
    public boolean canApplyTo(Node node, PosInOccurrence posInOccurrence) {
        return false;
    }

    @Override
    public ProofMacroFinishedInfo applyTo(UserInterfaceControl userInterfaceControl, Proof proof, ImmutableList<Goal> immutableList, PosInOccurrence posInOccurrence, ProverTaskListener proverTaskListener) throws InterruptedException, Exception {
        return null;
    }

    @Override
    public ProofMacroFinishedInfo applyTo(UserInterfaceControl userInterfaceControl, Node node, PosInOccurrence posInOccurrence, ProverTaskListener proverTaskListener) throws InterruptedException, Exception {
        return null;
    }
}
