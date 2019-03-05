package edu.kit.iti.formal.psdbg.interpreter;

import de.uka.ilkd.key.api.KeYApi;
import de.uka.ilkd.key.api.ProofApi;
import de.uka.ilkd.key.api.ProofManagementApi;
import de.uka.ilkd.key.control.KeYEnvironment;
import de.uka.ilkd.key.java.abstraction.KeYJavaType;
import de.uka.ilkd.key.logic.op.IObserverFunction;
import de.uka.ilkd.key.parser.ParserException;
import de.uka.ilkd.key.proof.Proof;
import de.uka.ilkd.key.proof.init.ProofInputException;
import de.uka.ilkd.key.proof.io.ProblemLoaderException;
import de.uka.ilkd.key.settings.ChoiceSettings;
import de.uka.ilkd.key.settings.ProofSettings;
import de.uka.ilkd.key.speclang.Contract;
import de.uka.ilkd.key.strategy.StrategyProperties;
import de.uka.ilkd.key.util.KeYTypeUtil;
import de.uka.ilkd.key.util.MiscTools;
import static edu.kit.iti.formal.psdbg.TestHelper.getFile;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.key_project.util.collection.ImmutableSet;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class EnvironmentTest {

        /**
         * The program entry point.
         */
        @Test
        public void bigInt() {
            File location  = new File(getFile(getClass(), "javaTestFiles/BigInteger.java"));

            List<File> classPaths = null; // Optionally: Additional specifications for API classes
            File bootClassPath = null; // Optionally: Different default specifications for Java API
            List<File> includes = null; // Optionally: Additional includes to consider
            try {
                // Ensure that Taclets are parsed
                if (!ProofSettings.isChoiceSettingInitialised()) {
                    KeYEnvironment<?> env = KeYEnvironment.load(location, classPaths, bootClassPath, includes);
                    env.dispose();
                }
                // Set Taclet options
                ChoiceSettings choiceSettings = ProofSettings.DEFAULT_SETTINGS.getChoiceSettings();
                HashMap<String, String> oldSettings = choiceSettings.getDefaultChoices();
                HashMap<String, String> newSettings = new HashMap<String, String>(oldSettings);
                newSettings.putAll(MiscTools.getDefaultTacletOptions());
                choiceSettings.setDefaultChoices(newSettings);
                // Load source code
                KeYEnvironment<?> env = KeYEnvironment.load(location, classPaths, bootClassPath, includes); // env.getLoadedProof() returns performed proof if a *.proof file is loaded
                try {
                    // List all specifications of all types in the source location (not classPaths and bootClassPath)
                    final List<Contract> proofContracts = new LinkedList<Contract>();
                    Set<KeYJavaType> kjts = env.getJavaInfo().getAllKeYJavaTypes();
                    for (KeYJavaType type : kjts) {
                        if (!KeYTypeUtil.isLibraryClass(type)) {
                            ImmutableSet<IObserverFunction> targets = env.getSpecificationRepository().getContractTargets(type);
                            for (IObserverFunction target : targets) {
                                ImmutableSet<Contract> contracts = env.getSpecificationRepository().getContracts(type, target);
                                for (Contract contract : contracts) {
                                    proofContracts.add(contract);

                                    System.out.println("contract.getPlainText(env.getServices()) = " + contract.getPlainText(env.getServices()));
                                }
                            }
                        }
                    }

                }
                finally {
                    env.dispose(); // Ensure always that all instances of KeYEnvironment are disposed
                }
            }
            catch (ProblemLoaderException e) {
                System.out.println("Exception at '" + location + "':");
                e.printStackTrace();
            }
        }


    /**
     * API test
     */
    @Test
    public void bigInt2() {
        File location  = new File(getFile(getClass(), "javaTestFiles/BigInteger.java"));
        try {
            KeYProofFacade facade = new KeYProofFacade();
           // ProofApi proofApi = facade.loadKeyFile(location);
            //List<Contract> contractsForJavaFile = facade.getContractsForJavaFile(location);
            //contractsForJavaFile.forEach(contract -> System.out.println("contract.getPlainText(facade.getService()) = " + contract.getPlainText(facade.getService())));



            ProofManagementApi pma = KeYApi.loadFromKeyFile(location);
            facade.setEnvironment(pma.getCurrentEnv());
            List<Contract> proofContracts = pma.getProofContracts();
            proofContracts.forEach(contract -> System.out.println(contract.getPlainText(facade.getService())));


        } catch (ProblemLoaderException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadQuickSortSplit() throws IOException, ParseException, ParserException {
        File javaFile = new File(getFile(getClass(), "javaTestFiles/quicksort/split/Quicksort.java"));
        File scriptFile = new File(getFile(getClass(), "javaTestFiles/quicksort/split/script.kps"));
        String contractName= "Quicksort[Quicksort::split([I,int,int)].JML normal_behavior operation contract";

        try {
            KeYProofFacade facade = new KeYProofFacade();
            ProofManagementApi pma = KeYApi.loadFromKeyFile(javaFile);
            facade.setEnvironment(pma.getCurrentEnv());
            List<Contract> proofContracts = pma.getProofContracts();
            Contract chosen = null;
            for (Contract c : proofContracts) {
                if(c.getTypeName().equals(contractName)){
                    chosen = c;
                }
            }
            Assert.assertNotNull("We were able to find the contract", chosen);


        } catch (ProblemLoaderException e) {
            e.printStackTrace();
        }
    }

}
