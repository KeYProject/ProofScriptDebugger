package edu.kit.iti.formal.psdbg.gui.controls;

import com.google.common.base.Strings;
import de.uka.ilkd.key.proof.init.ProofInputException;
import de.uka.ilkd.key.proof.io.ProblemLoaderException;
import de.uka.ilkd.key.speclang.Contract;
import edu.kit.iti.formal.psdbg.examples.java.quicksort.QuickSort;
import edu.kit.iti.formal.psdbg.interpreter.Interpreter;
import edu.kit.iti.formal.psdbg.interpreter.InterpreterBuilder;
import edu.kit.iti.formal.psdbg.interpreter.KeYProofFacade;
import edu.kit.iti.formal.psdbg.interpreter.KeyInterpreter;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.dbg.DebuggerFramework;
import edu.kit.iti.formal.psdbg.parser.Facade;
import edu.kit.iti.formal.psdbg.parser.ast.ProofScript;
import javafx.scene.control.TreeItem;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ProofTreeTest {
    private KeYProofFacade facade;

    private static void printTree(TreeItem<TreeNode> rootItem, int level) {
        System.out.format("%s* %s%n", Strings.repeat(" ", level * 4), rootItem.getValue().label);
        rootItem.getChildren().forEach(item -> {
            printTree(item, level + 1);
        });
    }

    @Before
    public void setUp() throws Exception {
        facade = new KeYProofFacade();
    }

    @Test
    public void testScriptTreeContraPosition() throws IOException, ProblemLoaderException {
        List<ProofScript> scripts;
        Interpreter<KeyData> interpreter;
        File keyProblem = new File(getClass().getResource("contraposition.key").getFile());
        facade.loadKeyFileSync(keyProblem);
        scripts = Facade.getAST(CharStreams.fromStream(getClass().getResourceAsStream("proofTreeScript.kps")));
        InterpreterBuilder ib = facade.buildInterpreter();
        interpreter = ib.build();
        DebuggerFramework<KeyData> df = new DebuggerFramework<>(interpreter, scripts.get(0));
        interpreter.interpret(scripts.get(0));

        ScriptTreeGraph stg = new ScriptTreeGraph();
        stg.createGraph(df.getPtreeManager().getStartNode(), facade.getProof().root());
        assert stg.getClosedGoals() == 2;
        assert stg.getOpenGoals() == 0;
        System.out.println("stg = " + stg);


/*        ScriptTreeTransformation treeScriptCreation = new ScriptTreeTransformation(df.getPtreeManager());
        treeScriptCreation.create(facade.getProof());
        PTreeNode startNode = df.getPtreeManager().getStartNode();
        if (startNode != null) {
            TreeItem<TreeNode> treeItem = treeScriptCreation.buildScriptTree(startNode, facade.getProof());

        }

        TreeItem<TreeNode> rootItem = treeScriptCreation.create(ib.getProof());
        printTree(rootItem, 0);*/

    }


    @Test
    public void testScriptTreeQuicksort() throws IOException, ProblemLoaderException, ProofInputException {
        QuickSort qs = new QuickSort();
        File javaFile = new File(qs.getJavaFile().getFile());
        List<Contract> contracts = facade.getContractsForJavaFile(javaFile);
        Contract c = contracts.get(0);
        facade.activateContract(c);
        System.out.println("Contract: '" + c.getName() + "' activated!");

        List<ProofScript> scripts = Facade.getAST(
                CharStreams.fromStream(qs.getScriptFile().openStream()));
        System.out.println("Script loaded.");

        InterpreterBuilder ib = facade.buildInterpreter();
        KeyInterpreter interpreter = ib.build();
        DebuggerFramework<KeyData> df = new DebuggerFramework<>(interpreter, scripts.get(0));
        System.out.println("Interpreter ready! Start proofing...");

        interpreter.interpret(scripts.get(0));
        System.out.println("Script successfully finished!");


        System.out.println("Begin construction of the tree...");
        ScriptTreeGraph stg = new ScriptTreeGraph();
        stg.createGraph(df.getPtreeManager().getStartNode(), facade.getProof().root());
        System.out.println("stg = " + stg);
    }
}