package edu.kit.iti.formal.psdbg.gui.controls;

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

    private Button newScript;

    /**
     * Button to open a script
     */

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
