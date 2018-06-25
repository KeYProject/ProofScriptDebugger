package edu.kit.iti.formal.psdbg.gui.controls;

import de.uka.ilkd.key.proof.io.ProblemLoaderException;
import edu.kit.iti.formal.psdbg.interpreter.Interpreter;
import edu.kit.iti.formal.psdbg.interpreter.InterpreterBuilder;
import edu.kit.iti.formal.psdbg.interpreter.KeYProofFacade;

import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.dbg.DebuggerFramework;
import edu.kit.iti.formal.psdbg.interpreter.dbg.PTreeNode;
import edu.kit.iti.formal.psdbg.parser.Facade;
import edu.kit.iti.formal.psdbg.parser.ast.ProofScript;
import javafx.scene.control.TreeItem;
import org.antlr.v4.runtime.CharStreams;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.List;

public class ProofTreeTest {
    KeYProofFacade facade;
    List<ProofScript> scripts;
    Interpreter<KeyData> i;



    @Before
    public void setUp() throws Exception {
       facade = new KeYProofFacade();
       scripts = Facade.getAST(CharStreams.fromStream(new FileInputStream("/home/sarah/Documents/KIT_Mitarbeiter/ProofScriptingLanguage/rt-key/src/test/resources/edu/kit/iti/formal/psdbg/interpreter/contraposition/proofTreeScript.kps")));

    }


    @Test
    public void testScriptTree() throws IOException, ProblemLoaderException {
        facade.loadKeyFileSync(new File("/home/sarah/Documents/KIT_Mitarbeiter/ProofScriptingLanguage/rt-key/src/test/resources/edu/kit/iti/formal/psdbg/interpreter/contraposition/contraposition.key"));
        InterpreterBuilder ib = facade.buildInterpreter();
        i = ib.build();
        DebuggerFramework<KeyData> df = new DebuggerFramework<>(i, scripts.get(0), null);
        ScriptTreeTransformation treeScriptCreation = new ScriptTreeTransformation(df.getPtreeManager(), facade.getProof(), facade.getProof().root());
        treeScriptCreation.create(facade.getProof());
        PTreeNode startNode = df.getPtreeManager().getStartNode();
        if (startNode != null) {
            TreeItem<ProofTree.TreeNode> treeItem = treeScriptCreation.buildScriptTree(startNode);
            System.out.println("treeItem = " + treeItem);

        }


    }


}