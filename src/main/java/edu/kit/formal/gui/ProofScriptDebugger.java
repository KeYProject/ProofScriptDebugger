package edu.kit.formal.gui;

/**
 * Main Entry for Debugger GUI
 * @author S. Grebing
 */

import edu.kit.formal.gui.controller.DebuggerMainWindowController;
import edu.kit.formal.gui.model.RootModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ProofScriptDebugger extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        RootModel rm = new RootModel();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/DebuggerMain.fxml"));

        Parent root = null;
        try {
            root = (Parent) fxmlLoader.load();
            DebuggerMainWindowController controller = fxmlLoader.<DebuggerMainWindowController>getController();
            controller.setStage(primaryStage);
            controller.setModel(rm);
            controller.init();
            Scene scene = new Scene(root);
            primaryStage.setTitle("Proof Script Debugger");

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
