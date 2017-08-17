package edu.kit.formal.psdb.gui.controls;

import edu.kit.formal.psdb.gui.controller.DebuggerMain;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;

import java.io.File;

/**
 * Welcome pane that allows for a more usable entry point
 * Created by weigl on 7/7/17.
 */
public class WelcomePane extends AnchorPane {
    private final DebuggerMain proofScriptDebugger;

    public WelcomePane(DebuggerMain debugger) {
        this.proofScriptDebugger = debugger;
        Utils.createWithFXML(this);
    }

    public void loadContraPosition(ActionEvent event) {
        proofScriptDebugger.getWelcomePaneDock().close();
        proofScriptDebugger.showActiveInspector(null);
        proofScriptDebugger.openScript(
                new File("src/test/resources/edu/kit/formal/psdb/interpreter/contraposition/w_branching.kps")
        );

        proofScriptDebugger.openKeyFile(
                new File("src/test/resources/edu/kit/formal/psdb/interpreter/contraposition/contraposition.key"));

    }

    public void loadJavaTest(ActionEvent event) {
        proofScriptDebugger.getWelcomePaneDock().close();
        proofScriptDebugger.showActiveInspector(null);
        proofScriptDebugger.openScript(
                new File("src/test/resources/edu/kit/formal/psdb/interpreter/javaExample/test.kps")
        );
        proofScriptDebugger.openJavaFile(
                new File("src/test/resources/edu/kit/formal/psdb/interpreter/javaExample/Test.java"));
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

    public void loadNewScript(ActionEvent event) {
        proofScriptDebugger.getWelcomePaneDock().close();
        proofScriptDebugger.showActiveInspector(null);

        proofScriptDebugger.getScriptController().newScript();
    }


    public void openScript(ActionEvent event) {
        proofScriptDebugger.getWelcomePaneDock().close();
        proofScriptDebugger.showActiveInspector(null);

        proofScriptDebugger.openScript();
    }
}

