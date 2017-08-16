package edu.kit.formal.psdb.gui.controls;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

/**
 * Replacement for empty Tab with two buttons
 */
public class PlaceHolderTab extends BorderPane {
    /**
     * Button to open a new script
     */
    @FXML
    private Button newScript;

    /**
     * Button to open a script
     */
    @FXML
    private Button openScript;


    /**
     * Pane holding the buttons for an empty tab
     */
    public PlaceHolderTab() {
        Utils.createWithFXML(this);

    }

    public Button getNewScript() {
        return newScript;
    }

    public Button getOpenScript() {
        return openScript;
    }
}
