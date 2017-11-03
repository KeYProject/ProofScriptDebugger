package edu.kit.iti.formal.psdbg.gui;


import de.uka.ilkd.key.util.KeYConstants;
import edu.kit.iti.formal.psdbg.gui.controller.DebuggerMain;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dockfx.DockNode;

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
public class ProofScriptDebugger extends Application {
    public static final String NAME = "Proof Script Debugger";

    public static final String VERSION = "0.1";

    public static final String KEY_VERSION = KeYConstants.VERSION;

    private Logger logger = LogManager.getLogger("psdbg");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Locale.setDefault(Locale.ENGLISH);
        //System.setProperty("log4j.configurationFile", "/tmp/log4j.properties");
        //System.setProperty("log4j2.loggerContextFactory", "org.apache.logging.log4j.core.impl.Log4jContextFactory");

        try {

//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
//                            "controller/DebuggerMain.fxml"));
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/edu/kit/iti/formal/psdbg/gui/controller/DebuggerMain.fxml"));
            //"/edu/kit/formal/psdb/gui/controller/DebuggerMain.fxml"));
            Parent root = fxmlLoader.load();
            DebuggerMain controller = fxmlLoader.getController();
            Scene scene = new Scene(root);


            scene.getAccelerators().put(
                    new KeyCodeCombination(KeyCode.F1),
                    () -> controller.showCommandHelp(null)
            );

            scene.getAccelerators().put(
                    new KeyCodeCombination(KeyCode.F12),
                    () -> controller.showWelcomeDock(null)
            );

            scene.getAccelerators().put(
                    new KeyCodeCombination(KeyCode.F5),
                    () -> controller.stepBack(null)
            );

            scene.getAccelerators().put(
                    new KeyCodeCombination(KeyCode.F6),
                    () -> controller.stepOver(null)
            );


            scene.getAccelerators().put(
                    new KeyCodeCombination(KeyCode.F7),
                    () -> controller.stepInto(null)
            );

            scene.getAccelerators().put(
                    new KeyCodeCombination(KeyCode.F3),
                    controller::executeStepwise
            );


            primaryStage.setOnCloseRequest(event -> Platform.exit());
            scene.getStylesheets().addAll(
                    getClass().getResource("debugger-ui.css").toExternalForm(),
                    DockNode.class.getResource("default.css").toExternalForm()
            );
            primaryStage.setTitle(NAME + " (" + VERSION + ") with KeY:" + KEY_VERSION);
            primaryStage.setScene(scene);
            primaryStage.show();


            logger.info("Start: " + NAME);
            logger.info("Version: " + VERSION);
            logger.info("KeY: " + KeYConstants.COPYRIGHT);
            logger.info("KeY Version: " + KeYConstants.VERSION);
            logger.info("KeY Internal: " + KeYConstants.INTERNAL_VERSION);
            //logger.error("sfklsajflksajfsdajfsdalfjsdaf", new IllegalAccessError("dlfsdalfjsadflj"));


        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void stop() throws Exception {
        System.exit(0);//needed, else non-termination of process
    }
}
