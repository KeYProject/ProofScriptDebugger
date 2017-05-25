package edu.kit.formal.proofscriptparser.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Alexander Weigl
 * @version 1 (25.05.17)
 */
public class DbgFrame implements Initializable {
    @FXML
    public ScriptArea scriptArea;

    @FXML
    public TextArea sequenceView;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(scriptArea);
    }
}
