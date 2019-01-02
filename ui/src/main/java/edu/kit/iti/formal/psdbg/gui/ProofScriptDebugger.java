package edu.kit.iti.formal.psdbg.gui;


import de.uka.ilkd.key.util.KeYConstants;
import edu.kit.iti.formal.psdbg.gui.controller.DebuggerMain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.util.Locale;

/**
 * Entry class for the {@link ProofScriptDebugger} (JavaFX Application).
 * <p>
 * For a command line interface see {@link edu.kit.iti.formal.psdbg.interpreter.Execute}
 * <p>
 *
 * @author S. Grebing
 * @author Alexander Weigl
 */
public class ProofScriptDebugger {
    public static final String NAME = "Proof Script Debugger";
    public static final String VERSION = "1.2-experimental";
    public static final String KEY_VERSION = KeYConstants.VERSION;
    private static Logger consoleLogger = LogManager.getLogger("console");
    private static Logger logger = LogManager.getLogger("psdbg");

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        try {
            DebuggerMain frame = new DebuggerMain();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            frame.setTitle(NAME + " (" + VERSION + ") with KeY:" + KEY_VERSION);
            logger.info("Start: " + NAME);
            logger.info("Version: " + VERSION);
            logger.info("KeY: " + KeYConstants.COPYRIGHT);
            logger.info("KeY Version: " + KeYConstants.VERSION);
            logger.info("KeY Internal: " + KeYConstants.INTERNAL_VERSION);
            consoleLogger.info("Welcome!");
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
