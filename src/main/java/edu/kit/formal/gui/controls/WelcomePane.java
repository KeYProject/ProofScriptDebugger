package edu.kit.formal.gui.controls;

import edu.kit.formal.gui.controller.DebuggerMainWindowController;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;

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

    public void loadContraPosition(ActionEvent event) {
        proofScriptDebugger.getWelcomePaneDock().close();
        proofScriptDebugger.showActiveInspector(null);
        proofScriptDebugger.openScript(
                new File("src/test/resources/edu/kit/formal/interpreter/contraposition/w_branching.kps")
        );

        proofScriptDebugger.openKeyFile(
                new File("src/test/resources/edu/kit/formal/interpreter/contraposition/contraposition.key"));

    }

    public void loadJavaTest(ActionEvent event) {
        proofScriptDebugger.getWelcomePaneDock().close();
        proofScriptDebugger.showActiveInspector(null);
        proofScriptDebugger.openScript(
                new File("src/test/resources/edu/kit/formal/interpreter/dbg.kps")
        );
        proofScriptDebugger.openJavaFile(
                new File("src/test/resources/edu/kit/formal/interpreter/javaExample/TwoWaySwap.java"));
    }

    public void loadHelpPage(ActionEvent event) {
        proofScriptDebugger.getWelcomePaneDock().close();
        proofScriptDebugger.showHelpText();


    }

    public void loadJavaProblem(ActionEvent event) {
        proofScriptDebugger.getWelcomePaneDock().close();
        proofScriptDebugger.showActiveInspector(null);

        proofScriptDebugger.openJavaFile();
        proofScriptDebugger.openScript();

    }
}

