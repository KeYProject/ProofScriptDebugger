package edu.kit.formal.gui;

/**
 * Main Entry for Debugger GUI
 *
 * @author S. Grebing
 */

import de.uka.ilkd.key.util.KeYConstants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProofScriptDebugger extends Application {
    public static final String NAME = "Proof Script Debugger";
    public static final String VERSION = "0.1";
    public static final String KEY_VERSION = KeYConstants.VERSION;

    private Logger logger = Logger.getLogger("psdbg");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Locale.setDefault(Locale.ENGLISH);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/DebuggerMain.fxml"));
            Parent root = fxmlLoader.load();
            //DebuggerMainWindowController controller = fxmlLoader.<DebuggerMainWindowController>getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().addAll(
                    getClass().getResource("debugger-ui.css").toExternalForm()
            );
            primaryStage.setTitle(NAME + " (" + VERSION + ") with KeY:" + KEY_VERSION);
            primaryStage.setScene(scene);
            primaryStage.show();


            logger.info("Start: " + NAME);
            logger.info("Version: " + VERSION);
            logger.info("KeY: " + KeYConstants.COPYRIGHT);
            logger.info("KeY Version: " + KeYConstants.VERSION);
            logger.info("KeY Internal: " + KeYConstants.INTERNAL_VERSION);
            logger.log(Level.SEVERE, "sfklsajflksajfsdajfsdalfjsdaf", new IllegalAccessError("dlfsdalfjsadflj"));

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
