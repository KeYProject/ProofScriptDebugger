package edu.kit.formal.gui.controls;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

/**
 * Replacement for empty Tab with two buttons
 */
public class PlaceHolderTab extends FlowPane {

    /**
     * Button to open a new script
     */
    private final Button newScript = new Button("New Script");

    /**
     * Button to open a script
     */
    private final Button openScript = new Button("Open Script");
    private VBox vbox;

    /**
     * Pane holding the buttons for an empty tab
     */
    public PlaceHolderTab() {
        vbox = new VBox();

        newScript.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        openScript.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        newScript.setAlignment(Pos.CENTER);
        openScript.setAlignment(Pos.CENTER);
        vbox.getChildren().add(newScript);
        vbox.getChildren().add(openScript);
        vbox.setAlignment(Pos.CENTER);
        this.getChildren().add(vbox);
    }

    public VBox getVbox() {
        return vbox;
    }

    public Button getNewScript() {
        return newScript;
    }

    public Button getOpenScript() {
        return openScript;
    }


}
