package edu.kit.iti.formal.psdbg.gui.controls;

import edu.kit.iti.formal.psdbg.gui.controller.DebuggerMain;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class ViewSettings extends BorderPane {

    /*
    TODO: Changeable View
    - ScriptArea
    - Contextmenu
    - Sequentview
    - JavaCode
     */

    @FXML
    private TextField scriptAreaSize;

    @FXML
    private TextField contextAreaSize;

    @FXML
    private TextField sequentSize;

    @FXML
    private TextField javaCodeSize;

    @FXML
    private TextField allSizes;


    @FXML
    private Button apply;

    @FXML
    private Button reset;

    public ViewSettings(DebuggerMain dm) {

    }
}
