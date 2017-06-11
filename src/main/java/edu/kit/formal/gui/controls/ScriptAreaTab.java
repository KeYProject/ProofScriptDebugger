package edu.kit.formal.gui.controls;

import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;

import java.io.IOException;

/**
 * Tab with a ScriptArea
 */
public class ScriptAreaTab extends Tab {


    @FXML
    private ScriptArea scriptArea;

    public ScriptAreaTab(@NamedArg("text") String title) {
        super(title);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TabScriptArea.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ScriptArea getScriptArea() {
        return scriptArea;
    }
}
