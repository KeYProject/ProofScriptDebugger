package edu.kit.iti.formal.psdbg.interpreter;

import de.uka.ilkd.key.api.KeYApi;
import de.uka.ilkd.key.api.ProofApi;
import de.uka.ilkd.key.api.ProofManagementApi;
import de.uka.ilkd.key.control.KeYEnvironment;
import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.proof.Goal;
import de.uka.ilkd.key.proof.Proof;
import de.uka.ilkd.key.proof.init.ProofInputException;
import de.uka.ilkd.key.proof.io.ProblemLoaderException;
import de.uka.ilkd.key.speclang.Contract;
import edu.kit.iti.formal.psdbg.ChangeBean;
import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.key_project.util.collection.ImmutableList;

import javax.swing.*;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Facade to KeY. Build part of the interpreter
 *
 * @author S. Grebing
 * @author A. Weigl
 */
@Data
@FieldNameConstants
public class KeYProofFacade extends ChangeBean {
    private static final Logger LOGGER = LogManager.getLogger(KeYProofFacade.class);

    /**
     * Current Proof
     */
    private Proof proof;

    /**
     * Current KeYEnvironment
     */
    private KeYEnvironment environment;

    /**
     * Chosen Contract; Null if only key file was loaded
     */
    private Contract contract;
    //Workaround until api is relaxed
    private ProofManagementApi pma;
    private File filepath;

    /**
     * BooleanProperty indicating whether KeY finished loading
     */
    private boolean isReadyToExecute() {
        return proof != null;
    }

    /**
     *
     */
    public ProofState getProofState() {
        Proof p = proof;
        if (p == null)
            return ProofState.EMPTY;

        if (p.root().childrenCount() == 0)
            return ProofState.VIRGIN;

        return ProofState.DIRTY;
    }

    /**
     * reload the current proof. synchronously because only the first load is slow.
     */
    public void reload(File problemFile) throws ProofInputException, ProblemLoaderException {
        if (contract != null) {// reinstante the contract
            pma = KeYApi.loadFromKeyFile(problemFile);
            List<Contract> contracts = pma.getProofContracts();
            contracts.forEach(contract1 -> {
                if (contract.getName().equals(contract1.getName())) {
                    contract = contract1;
                }
            });

            if (contract != null) {
                try {
                    activateContract(contract);
                } catch (ProofInputException pie) {
                    throw new ProofInputException("Contract not reloadable.", pie.getCause());
                }
            }
            /*setProof(getEnvironment().createProof(
                    contract.getProofObl(getEnvironment().getServices())));*/
        } else {
            setProof(KeYApi.loadFromKeyFile(problemFile).getLoadedProof().getProof());
        }
    }

    //region loading
    public SwingWorker<ProofApi, Integer> loadKeyFileTask(File keYFile) {
        return new SwingWorker<>() {
            @Override
            protected ProofApi doInBackground() throws Exception {
                setProgress(0);
                ProofApi pa = loadKeyFile(keYFile);
                setProgress(100);
                return pa;
            }

            @Override
            protected void done() {
                System.out.println("KeYProofFacade.succeeded");
                try {
                    setEnvironment(get().getEnv());
                    //SaG: this needs to be set to filter inapplicable rules
                    getEnvironment().getProofControl().setMinimizeInteraction(true);
                    setProof(get().getProof());
                    setContract(null);
                    setProgress(0);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * This method does not set the environment or proof property, because of threading reason
     *
     * @param keYFile
     * @return
     * @throws ProblemLoaderException
     */
    ProofApi loadKeyFile(File keYFile) throws ProblemLoaderException {
        //setLoading(true);
        ProofManagementApi pma = KeYApi.loadFromKeyFile(keYFile);
        ProofApi pa = pma.getLoadedProof();
        //setLoading(false);
        filepath = keYFile;
        return pa;
    }

    public ProofApi loadKeyFileSync(File keyFile) throws ProblemLoaderException {
        //setLoading(true);
        ProofApi pa = loadKeyFile(keyFile);
        setEnvironment(pa.getEnv());
        setProof(pa.getProof());
        setContract(null);
        //setLoading(false);
        setFilepath(keyFile);
        return pa;
    }

    public SwingWorker<List<Contract>, Integer> getContractsForJavaFileTask(File javaFile) {
        return new SwingWorker<>() {
            @Override
            protected List<Contract> doInBackground() throws Exception {
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
        setEnvironment(pa.getEnv());
        setProof(pa.getProof());
        setContract(null);
    }

    /**
     * Build the KeYInterpreter that handles the execution of the loaded key problem sourceName
     */
    public InterpreterBuilder buildInterpreter() {
        assert isReadyToExecute();
        assert getEnvironment() != null && getProof() != null : "No proof loaded";
        InterpreterBuilder interpreterBuilder = new InterpreterBuilder();
        interpreterBuilder
                .proof(environment, proof)
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

    public void setEnvironment(KeYEnvironment environment) {
        firePropertyChange("environment", this.environment, environment);
        this.environment = environment;
        if (environment != null) {//FIXME weigl not a good place
            getEnvironment().getUi().getProofControl().setMinimizeInteraction(true);
        }
    }

    /**
     * returns filepath of loaded KeY problem
     **/
    public File getFilepath() {
        return filepath;
    }

    public Collection<GoalNode<KeyData>> getPseudoGoals() {
        Proof p = getProof();
        KeYEnvironment env = getEnvironment();
        ImmutableList<Goal> openGoals = p.getSubtreeGoals(p.root());
        List<GoalNode<KeyData>> goals = openGoals.stream().map(g ->
                new GoalNode<>(new KeyData(g, env, p)))
                .collect(Collectors.toList());
        return goals;
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

