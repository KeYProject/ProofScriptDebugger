package edu.kit.iti.formal.psdbg;

import de.uka.ilkd.key.api.KeYApi;
import de.uka.ilkd.key.api.ProofManagementApi;
import de.uka.ilkd.key.proof.io.ProblemLoaderException;
import edu.kit.iti.formal.psdbg.interpreter.InterpreterBuilder;
import edu.kit.iti.formal.psdbg.interpreter.KeyInterpreter;

import java.io.File;
import java.io.IOException;

/**
 * @author Alexander Weigl
 * @version 1 (27.10.17)
 */
public class CommandLineDebugger {
    public static void main(String[] args) throws ProblemLoaderException, IOException {
        ProofManagementApi pma = KeYApi.loadFromKeyFile(new File(args[1]));

        KeyInterpreter ib = new InterpreterBuilder()
                .addProofScripts(new File(args[0]))
                .proof(pma.getLoadedProof())
                .build();

    }
}
