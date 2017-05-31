package edu.kit.formal.interpreter;

import de.uka.ilkd.key.api.KeYApi;
import de.uka.ilkd.key.api.ProjectedNode;
import de.uka.ilkd.key.api.ProofApi;
import de.uka.ilkd.key.api.ProofManagementApi;
import de.uka.ilkd.key.proof.init.ProofInputException;
import de.uka.ilkd.key.proof.io.ProblemLoaderException;
import de.uka.ilkd.key.speclang.Contract;
import edu.kit.formal.gui.model.RootModel;
import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.KeyData;
import edu.kit.formal.interpreter.data.State;
import edu.kit.formal.interpreter.exceptions.InterpreterRuntimeException;
import edu.kit.formal.proofscriptparser.Facade;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by sarah on 5/29/17.
 */
public class KeYProofFacade {

    @Getter
    private ProofManagementApi pma;
    @Getter
    private ProofApi pa;
    @Getter
    private ProjectedNode currentRoot;

    private RootModel model;

    @Getter
    private Interpreter<KeyData> interpreter;


    public KeYProofFacade(RootModel model) {
        this.model = model;
    }

    public State<KeyData> executeScriptWithKeYProblemFile(String currentScriptText, File keyProblemFile) {
        buildKeYInterpreter(keyProblemFile, false);
        currentRoot = pa.getFirstOpenGoal();

        KeyData keyData = new KeyData(currentRoot.getProofNode(), pa.getEnv(), pa.getProof());
        try {
            interpreter.interpret(Facade.getAST(currentScriptText), new GoalNode<>(null, keyData));
            return interpreter.getCurrentState();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public void prepareEnvWithKeYFile(File keYFile) {
        try {
            pma = KeYApi.loadFromKeyFile(keYFile);
            pa = pma.getLoadedProof();
            buildKeYInterpreter(keYFile, false);
        } catch (ProblemLoaderException e) {
            e.printStackTrace();
        }
    }

    public List<Contract> getContractsForJavaFile(File javaFile) {
        try {
            pma = KeYApi.loadFromKeyFile(javaFile);
            return pma.getProofContracts();
        } catch (ProblemLoaderException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void prepareEnvForContract(Contract c, File javaFile) {
        try {
            pa = pma.startProof(c);
            buildKeYInterpreter(javaFile, true);
        } catch (ProofInputException e) {
            e.printStackTrace();
        }

    }



    /**
     * Build the KeYInterpreter that handles the execution of the loaded key problem file
     *
     * @param keYFile
     */
    private void buildKeYInterpreter(File keYFile, Boolean isJavaFile) {
        InterpreterBuilder interpreterBuilder = new InterpreterBuilder();
        this.interpreter = null;
        interpreterBuilder.proof(pa).macros().scriptCommands();
        pa.getProof().getProofIndependentSettings().getGeneralSettings().setOneStepSimplification(false);
        this.interpreter = interpreterBuilder.build();


    }


    /**
     * Execute ScriptFile in the created interpreter
     *
     * @param
     */
    public void executeScript(String scriptText) throws InterpreterRuntimeException {
        if (interpreter == null) {
            throw new InterpreterRuntimeException("No interpreter created");
        }

        ProjectedNode root = pa.getFirstOpenGoal();

        KeyData keyData = new KeyData(root.getProofNode(), pa.getEnv(), pa.getProof());
        try {

            interpreter.interpret(Facade.getAST(scriptText), new GoalNode<KeyData>(null, keyData));
            this.model.setCurrentState(interpreter.getCurrentState());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
