package edu.kit.formal.gui.controls;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

/**
 * @author Alexander Weigl
 * @version 1 (05.06.17)
 */
public class Utils {
    private Utils() {
    }

    public static void createWithFXML(Object node) {
        FXMLLoader loader = new FXMLLoader(
                node.getClass().getResource(node.getClass().getSimpleName() + ".fxml")
        );
        loader.setController(node);
        loader.setRoot(node);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Could not load fxml", e);
        }
    }
}
