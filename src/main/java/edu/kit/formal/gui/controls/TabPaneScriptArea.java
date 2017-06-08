package edu.kit.formal.gui.controls;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Created by sarah on 6/8/17.
 */
public class TabPaneScriptArea extends VBox {
    public TabPaneScriptArea() {
        super();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SectionPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
