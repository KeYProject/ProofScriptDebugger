package edu.kit.formal.interpreter;

import de.uka.ilkd.key.api.KeYApi;
import de.uka.ilkd.key.api.ProjectedNode;
import de.uka.ilkd.key.api.ProofApi;
import de.uka.ilkd.key.api.ProofManagementApi;
import de.uka.ilkd.key.proof.io.ProblemLoaderException;
import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.KeyData;
import edu.kit.formal.interpreter.data.State;
import edu.kit.formal.proofscriptparser.Facade;

import java.io.File;
import java.io.IOException;

/**
 * Created by sarah on 5/29/17.
 */
public class KeYInterpreterFacade {

    private ProofManagementApi pma;
    private ProofApi pa;
    private ProjectedNode currentRoot;


    public State<KeyData> executeScriptWithKeYProblemFile(String currentScriptText, File keyProblemFile) {
        Interpreter<KeyData> inter = buildKeYInterpreter(keyProblemFile);
        ProjectedNode root = pa.getFirstOpenGoal();

        KeyData keyData = new KeyData(root.getProofNode(), pa.getEnv(), pa.getProof());
        try {
            inter.interpret(Facade.getAST(currentScriptText), new GoalNode<>(null, keyData));
            return inter.getCurrentState();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    //TODO bei Aufruf ausfuehren
    private Interpreter<KeyData> buildKeYInterpreter(File keYFile) {
        InterpreterBuilder interpreterBuilder = new InterpreterBuilder();
        Interpreter<KeyData> interpreter = null;
        try {
            pma = KeYApi.loadFromKeyFile(keYFile);
            pa = pma.getLoadedProof();
            interpreterBuilder.proof(pa).macros().scriptCommands();
            pa.getProof().getProofIndependentSettings().getGeneralSettings().setOneStepSimplification(false);
            interpreter = interpreterBuilder.build();

        } catch (ProblemLoaderException e) {
            e.printStackTrace();
        }
        return interpreter;
    }

}
