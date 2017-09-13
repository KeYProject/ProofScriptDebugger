package edu.kit.iti.formal.psdbg.interpreter;

import static edu.kit.iti.formal.psdbg.TestHelper.getFile;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.data.State;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Alexander Weigl
 * @version 1 (28.05.17)
 */
public class ExecuteTest {
    private static Execute create(String... cmd) throws IOException, ParseException {
        Execute e = new Execute();
        e.init(cmd);
        return e;
    }

    @Test
    public void testContrapositionAuto() throws IOException, ParseException {
        Execute execute = create(
                getFile(getClass(), "contraposition/contraposition.key"),
                "-s", getFile(getClass(), "contraposition/auto.kps"));
        Interpreter<KeyData> i = execute.run();
        System.out.println(i.getCurrentState());
    }

    @Test
    public void testContrapositionManualWoBranching() throws IOException, ParseException {
        Execute execute = create(
                getFile(getClass(), "contraposition/contraposition.key"),
                "-s", getFile(getClass(), "contraposition/wo_branching.kps"));
        execute.run();
    }

    @Test
    public void testContrapositionManualWBranching() throws IOException, ParseException {
        Execute execute = create(
                getFile(getClass(), "contraposition/contraposition.key"),
                "-s", getFile(getClass(), "contraposition/w_branching.kps"));
        Interpreter<KeyData> i = execute.run();
        System.out.println(i.getCurrentState());

    }

    @Test
    public void testContrapositionCut() throws IOException, ParseException {
        Execute execute = create(
                getFile(getClass(), "contraposition/contraposition.key"),
                "-s", getFile(getClass(), "contraposition/cutTest.kps"));
        Interpreter<KeyData> i = execute.run();
        State<KeyData> currentState = i.getCurrentState();
        System.out.println(currentState);
        //This reveals a bug in the cases merge
        Assert.assertEquals("Number of goals has to be two ", 2, i.getCurrentGoals().size());

    }
}