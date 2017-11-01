package edu.kit.iti.formal.psdbg.interpreter;

import de.uka.ilkd.key.api.KeYApi;
import de.uka.ilkd.key.api.ProofApi;
import de.uka.ilkd.key.api.ProofManagementApi;
import de.uka.ilkd.key.control.KeYEnvironment;
import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.proof.Proof;
import de.uka.ilkd.key.proof.init.ProofInputException;
import de.uka.ilkd.key.proof.io.ProblemLoaderException;
import de.uka.ilkd.key.speclang.Contract;
import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Facade to KeY. Build part of the interpreter
 *
 * @author S. Grebing
 * @author A. Weigl
 */
public class KeYProofFacade {
    private static final Logger LOGGER = LogManager.getLogger(KeYProofFacade.class);

    /**
     * Current Proof
     */
    private SimpleObjectProperty<Proof> proof = new SimpleObjectProperty<>();

    /**
     * Current KeYEnvironment
     */
    private SimpleObjectProperty<KeYEnvironment> environment = new SimpleObjectProperty<>();

    /**
     * Chosen Contract; Null if only key file was loaded
     */
    private SimpleObjectProperty<Contract> contract = new SimpleObjectProperty<>();

    /**
     * BooleanProperty inidcating whether KeY finished loading
     */
    private BooleanBinding readyToExecute = proof.isNotNull();

    //Workaround until api is relaxed
    private ProofManagementApi pma;

    /**
     *
     */
    public ProofState getProofState() {
        Proof p = proof.get();
        if (p == null)
            return ProofState.EMPTY;

        if (p.root().childrenCount() == 0)
            return ProofState.VIRGIN;

        return ProofState.DIRTY;
    }

    /**
     * reload the current proof. synchronously because only the first load is slow.
     */
    public void reload() throws ProofInputException, ProblemLoaderException {
        if (contract.get() != null) {// reinstante the contract
            setProof(getEnvironment().createProof(
                    contract.get().getProofObl(getEnvironment().getServices())));
        } else {
            setProof(KeYApi.loadFromKeyFile(proof.get().getProofFile()).getLoadedProof().getProof());
        }
    }

    //region loading
    public Task<ProofApi> loadKeyFileTask(File keYFile) {
        Task<ProofApi> task = new Task<ProofApi>() {
            @Override
            protected ProofApi call() throws Exception {
                ProofApi pa = loadKeyFile(keYFile);
                return pa;
            }

            @Override
            protected void succeeded() {
                System.out.println("KeYProofFacade.succeeded");
                environment.set(getValue().getEnv());
                proof.set(getValue().getProof());
                contract.set(null);
            }
        };

        return task;
    }

    /**
     * This method does not set the environment or proof property, because of threading reason
     *
     * @param keYFile
     * @return
     * @throws ProblemLoaderException
     */
    ProofApi loadKeyFile(File keYFile) throws ProblemLoaderException {
        ProofManagementApi pma = KeYApi.loadFromKeyFile(keYFile);
        ProofApi pa = pma.getLoadedProof();
        return pa;
    }

    public ProofApi loadKeyFileSync(File keyFile) throws ProblemLoaderException {
        ProofApi pa = loadKeyFile(keyFile);
        environment.set(pa.getEnv());
        proof.set(pa.getProof());
        contract.set(null);
        return pa;
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

    /**
     * Build the KeYInterpreter that handles the execution of the loaded key problem sourceName
     */
    public InterpreterBuilder buildInterpreter() {
        assert readyToExecute.getValue();
        assert getEnvironment() != null && getProof() != null : "No proof loaded";
        InterpreterBuilder interpreterBuilder = new InterpreterBuilder();
        interpreterBuilder
                .proof(environment.get(), proof.get())
                .startState()
                .macros()
                .scriptCommands()
                .scriptSearchPath(new File("."));
        //getProof().getProofIndependentSettings().getGeneralSettings().setOneStepSimplification(false);

        return interpreterBuilder;
    }
    //endregion

    /**
     * Reload all KeY structure if proof should be reloaded
     */
    public void reloadEnvironment() {
        setProof(null);
        setEnvironment(null);
        setContract(null);
        pma = null;
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

    public enum ProofState {
        /**
         * There is no KeY Proof actually loaded.
         */
        EMPTY,
        /**
         * The loaded KeY Proof is untouched iff there is only one node --- the root.
         */
        VIRGIN,
        /**
         * The KeY Proof was ravished by Taclets and other stuff.
         */
        DIRTY;
    }
    //endregion
}

