package edu.kit.formal.proofscriptparser.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;

/**
 * @author Alexander Weigl
 * @version 1 (25.05.17)
 */
public class App extends Application {
    public static void main(String[] args) {
        Application.launch(App.class, args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL url = getClass().getResource("mainframe.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        BorderPane bp = loader.load();
        Scene scene = new Scene(bp);
        DbgFrame mf = loader.getController();
        mf.setCode("script Simple1(i:int, j:int) {\n" +
                "    i := 1;\n" +
                "    j := 2;\n" +
                "\n" +
                "    theonly {\n" +
                "        i := 4;\n" +
                "        j := i+i;\n" +
                "    }\n" +
                "\n" +
                "    split 5;\n" +
                "\n" +
                "    foreach {\n" +
                "        i := 4;\n" +
                "        j := i+i;\n" +
                "    }\n" +
                "\n" +
                "    cases {\n" +
                "        case match '.*a' {\n" +
                "              i:=100;\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    repeat {\n" +
                "        foreach { i := i + 1; split;}\n" +
                "    }\n" +
                "}");

        primaryStage.setScene(scene);
        primaryStage.setTitle("DbgKps");
        primaryStage.show();
    }
}
