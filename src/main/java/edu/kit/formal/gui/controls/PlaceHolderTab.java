package edu.kit.formal.gui.controls;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * Replacement for empty Tab with two buttons
 */
public class PlaceHolderTab extends VBox {


    private final Button newScript = new Button("New Script");

    private final Button openScript = new Button("Open Script");

    public PlaceHolderTab() {
        this.setPadding(new Insets(10, 50, 50, 50));
        this.setSpacing(10);

        this.getChildren().add(newScript);
        this.getChildren().add(openScript);
    }

    public Button getNewScript() {
        return newScript;
    }

    public Button getOpenScript() {
        return openScript;
    }


}
