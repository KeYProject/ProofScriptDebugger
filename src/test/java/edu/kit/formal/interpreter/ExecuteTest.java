package edu.kit.formal.interpreter;

import edu.kit.formal.interpreter.data.KeyData;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import java.io.IOException;

import static edu.kit.formal.proofscriptparser.TestHelper.getFile;

/**
 * @author Alexander Weigl
 * @version 1 (28.05.17)
 */
public class ExecuteTest {
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
        /*TODO
        Execute execute = create(
                getFile(getClass(), "contraposition/contraposition.key"),
                "-s", getFile(getClass(), "contraposition/w_branching.kps"));
        execute.run();*/
    }

    private static Execute create(String... cmd) throws IOException, ParseException {
        Execute e = new Execute();
        e.init(cmd);
        return e;
    }


}