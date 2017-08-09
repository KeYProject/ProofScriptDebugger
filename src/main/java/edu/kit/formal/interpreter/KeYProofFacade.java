package edu.kit.formal.interpreter;

import de.uka.ilkd.key.api.KeYApi;
import de.uka.ilkd.key.api.ProofApi;
import de.uka.ilkd.key.api.ProofManagementApi;
import de.uka.ilkd.key.control.KeYEnvironment;
import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.proof.Proof;
import de.uka.ilkd.key.proof.init.ProofInputException;
import de.uka.ilkd.key.proof.io.ProblemLoaderException;
import de.uka.ilkd.key.speclang.Contract;
import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.KeyData;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Facade to KeY. Build part of the interpreter
 * @author S. Grebing
 * @author A. Weigl
 */
public class KeYProofFacade {
    private SimpleObjectProperty<Proof> proof = new SimpleObjectProperty<>();
    private SimpleObjectProperty<KeYEnvironment> environment = new SimpleObjectProperty<>();

    private SimpleObjectProperty<Contract> contract = new SimpleObjectProperty<>();

    private BooleanBinding readyToExecute = proof.isNotNull();
    //Workaround until api is relaxed
    private ProofManagementApi pma;

    //region loading
    public Task<Void> loadKeyFileTask(File keYFile) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                loadKeyFile(keYFile);
                return null;
            }
        };
        return task;
    }

    public void loadKeyFile(File keYFile) throws ProblemLoaderException {
        ProofManagementApi pma = KeYApi.loadFromKeyFile(keYFile);
        ProofApi pa = pma.getLoadedProof();
        environment.set(pa.getEnv());
        proof.set(pa.getProof());
        contract.set(null);
    }

    public Task<List<Contract>> getContractsForJavaFileTask(File javaFile) {
        return new Task<List<Contract>>() {
            @Override
            protected List<Contract> call() throws Exception {
                return getContractsForJavaFile(javaFile);
            }
        };
    }

    public List<Contract> getContractsForJavaFile(File javaFile)
            throws ProblemLoaderException {
        pma = KeYApi.loadFromKeyFile(javaFile);
        //TODO relax key api setEnvironment(pma.getEnvironment());
        return pma.getProofContracts();
    }

    public void activateContract(Contract c) throws ProofInputException {
        ProofApi pa = pma.startProof(c);
        environment.set(pa.getEnv());
        proof.set(pa.getProof());
        contract.set(null);
    }
    //endregion

    /**
     * Build the KeYInterpreter that handles the execution of the loaded key problem sourceName
     *
     * @param keYFile
     * @param scriptText
     */
    public InterpreterBuilder buildInterpreter() {
        assert readyToExecute.getValue();

        InterpreterBuilder interpreterBuilder = new InterpreterBuilder();
        interpreterBuilder
                .proof(environment.get(), proof.get())
                .startState()
                .macros()
                .scriptCommands()
                .scriptSearchPath(new File("."));
        getProof().getProofIndependentSettings().getGeneralSettings().setOneStepSimplification(false);

        return interpreterBuilder;
    }


    //region Getter and Setters
    public Services getService() {
        //FIXME if key api relaxed
        return pma != null ? pma.getServices() : getEnvironment().getServices();
    }

    public Proof getProof() {
        return proof.get();
    }

    public void setProof(Proof proof) {
        this.proof.set(proof);
    }

    public SimpleObjectProperty<Proof> proofProperty() {
        return proof;
    }

    public KeYEnvironment getEnvironment() {
        return environment.get();
    }

    public void setEnvironment(KeYEnvironment environment) {
        this.environment.set(environment);
    }

    public SimpleObjectProperty<KeYEnvironment> environmentProperty() {
        return environment;
    }

    public Contract getContract() {
        return contract.get();
    }

    public void setContract(Contract contract) {
        this.contract.set(contract);
    }

    public SimpleObjectProperty<Contract> contractProperty() {
        return contract;
    }

    public Boolean getReadyToExecute() {
        return readyToExecute.getValue();
    }

    public BooleanBinding readyToExecuteProperty() {
        return readyToExecute;
    }

    public Collection<GoalNode<KeyData>> getPseudoGoals() {
        KeyData data = new KeyData(proof.get().root(), getEnvironment(), getProof());
        return Collections.singleton(new GoalNode<>(null, data, data.getNode().isClosed()));
    }
    //endregion
}

