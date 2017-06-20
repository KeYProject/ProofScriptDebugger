package edu.kit.formal.gui.controls;

import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;

import java.io.IOException;
import java.io.UTFDataFormatException;

/**
 * Tab with a ScriptArea
 */
public class ScriptAreaTab extends Tab {
    @FXML
    private ScriptArea scriptArea;

    public ScriptAreaTab(@NamedArg("text") String title) {
        super(title);
        Utils.createWithFXML(this);
    }

    public ScriptArea getScriptArea() {
        return scriptArea;
    }
}
