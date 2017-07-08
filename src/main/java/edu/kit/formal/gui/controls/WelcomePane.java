package edu.kit.formal.gui.controls;

import edu.kit.formal.gui.controller.DebuggerMainWindowController;
import javafx.scene.layout.AnchorPane;

import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Created by weigl on 7/7/17.
 */
public class WelcomePane extends AnchorPane {
    private final DebuggerMainWindowController proofScriptDebugger;

    public WelcomePane(DebuggerMainWindowController debugger) {
        this.proofScriptDebugger = debugger;
        Utils.createWithFXML(this);
    }

    public void loadContraPosition(javafx.event.ActionEvent event) {
        proofScriptDebugger.getWelcomePaneDock().close();
        proofScriptDebugger.showActiveInspector(null);
        proofScriptDebugger.openScript(
                new File("src/test/resources/edu/kit/formal/interpreter/contraposition/test.kps")
        );

        proofScriptDebugger.openKeyFile(
                new File("src/test/resources/edu/kit/formal/interpreter/contraposition/contraposition.key"));

    }
}
