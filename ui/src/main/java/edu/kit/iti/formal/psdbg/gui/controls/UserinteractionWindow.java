package edu.kit.iti.formal.psdbg.gui.controls;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

public class UserinteractionWindow extends BorderPane {

    @FXML
    Button cancelButton;

    @FXML
    Button applyButton;

    @FXML
    TextArea tacletDescription;

    public UserinteractionWindow() {
        Utils.createWithFXML(this);

    }
}
