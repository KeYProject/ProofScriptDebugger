package edu.kit.iti.formal.psdbg.gui.controls.userinteraction;

import de.uka.ilkd.key.macros.scripts.meta.ProofScriptArgument;
import de.uka.ilkd.key.parser.DefaultTermParser;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


public class MissingInstWindow {
    @FXML
    Text commandname;

    @FXML
    Text requiredInput;

    @FXML
    TextField input;

    private ProofScriptArgument meta;

    public MissingInstWindow(ProofScriptArgument meta) {
        this.meta = meta;
        commandname.setText(meta.getCommand().getClass().toString());
        requiredInput.setText(meta.getField().getType().toString());
    }

    public void accept() {
        DefaultTermParser parser = new DefaultTermParser();


    }
}
