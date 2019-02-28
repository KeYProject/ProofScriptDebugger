package edu.kit.iti.formal.psdbg.gui.controls;

import de.uka.ilkd.key.proof.io.ProblemLoaderException;
import edu.kit.iti.formal.psdbg.gui.model.InspectionModel;
import edu.kit.iti.formal.psdbg.interpreter.Interpreter;
import edu.kit.iti.formal.psdbg.interpreter.InterpreterBuilder;
import edu.kit.iti.formal.psdbg.interpreter.KeYProofFacade;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.dbg.DebuggerFramework;
import edu.kit.iti.formal.psdbg.parser.Facade;
import edu.kit.iti.formal.psdbg.parser.ast.ProofScript;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IndistinctWindowTest {
    private KeYProofFacade facade;

    @Before
    public void setUp() throws Exception {
        facade = new KeYProofFacade();
    }

    @Test
    public void testApplyEqIndistinct() throws IOException, ProblemLoaderException {
        List<ProofScript> scripts;
        Interpreter<KeyData> interpreter;
        File keyProblem = new File(getClass().getResource("indistinctWindow/applyEq.key").getFile());
        facade.loadKeyFileSync(keyProblem);
        scripts = Facade.getAST(CharStreams.fromStream(getClass().getResourceAsStream("indistinctWindow/applyEq.kps")));
        InterpreterBuilder ib = facade.buildInterpreter();
        interpreter = ib.build();
        DebuggerFramework<KeyData> df = new DebuggerFramework<>(interpreter, scripts.get(0));

        boolean exceptionThrown = false;
        try {
            interpreter.interpret(scripts.get(0));
            //should throw an exception, because last command should be indistinct therefore not applicable
        } catch (Exception e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testApplyEqDistinct() throws IOException, ProblemLoaderException {
        List<ProofScript> scripts;
        Interpreter<KeyData> interpreter;
        File keyProblem = new File(getClass().getResource("indistinctWindow/applyEq.key").getFile());
        facade.loadKeyFileSync(keyProblem);
        scripts = Facade.getAST(CharStreams.fromStream(getClass().getResourceAsStream("indistinctWindow/applyEq.kps")));
        InterpreterBuilder ib = facade.buildInterpreter();
        interpreter = ib.build();
        DebuggerFramework<KeyData> df = new DebuggerFramework<>(interpreter, scripts.get(1));

        boolean exceptionThrown = false;
        try {
            interpreter.interpret(scripts.get(1));
            //should not throw an exception, because last command should be distinct
        } catch (Exception e) {
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }


    @Test
    public void testImpLeftIndistinct() throws IOException, ProblemLoaderException {
        List<ProofScript> scripts;
        Interpreter<KeyData> interpreter;
        File keyProblem = new File(getClass().getResource("indistinctWindow/impLeft.key").getFile());
        facade.loadKeyFileSync(keyProblem);
        scripts = Facade.getAST(CharStreams.fromStream(getClass().getResourceAsStream("indistinctWindow/impLeft.kps")));
        InterpreterBuilder ib = facade.buildInterpreter();
        interpreter = ib.build();
        DebuggerFramework<KeyData> df = new DebuggerFramework<>(interpreter, scripts.get(0));

        boolean exceptionThrown = false;
        try {
            interpreter.interpret(scripts.get(0));
            //should throw an exception, because last command should be indistinct therefore not applicable
        } catch (Exception e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testImpLeftDistinct() throws IOException, ProblemLoaderException {
        List<ProofScript> scripts;
        Interpreter<KeyData> interpreter;
        File keyProblem = new File(getClass().getResource("indistinctWindow/impLeft.key").getFile());
        facade.loadKeyFileSync(keyProblem);
        scripts = Facade.getAST(CharStreams.fromStream(getClass().getResourceAsStream("indistinctWindow/impLeft.kps")));
        InterpreterBuilder ib = facade.buildInterpreter();
        interpreter = ib.build();
        DebuggerFramework<KeyData> df = new DebuggerFramework<>(interpreter, scripts.get(1));

        boolean exceptionThrown = false;
        try {
            interpreter.interpret(scripts.get(1));
            //should not throw an exception, because last command should be distinct
        } catch (Exception e) {
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

}
