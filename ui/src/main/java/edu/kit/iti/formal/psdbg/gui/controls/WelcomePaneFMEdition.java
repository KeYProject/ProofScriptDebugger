package edu.kit.iti.formal.psdbg.gui.controls;

import edu.kit.iti.formal.psdbg.examples.Examples;
import edu.kit.iti.formal.psdbg.gui.controller.DebuggerMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

/**
 * Welcome pane that allows for a more usable entry point
 * Created by weigl on 7/7/17.
 */
public class WelcomePaneFMEdition extends AnchorPane {
    private final DebuggerMain proofScriptDebugger;

    public WelcomePaneFMEdition(DebuggerMain debugger) {
        this.proofScriptDebugger = debugger;
        Utils.createWithFXML(this);
    }

    /**
     * Load the contraposition example
     *
     * @param event
     */
    public void loadContraPosition(ActionEvent event) {
        proofScriptDebugger.getWelcomePaneDock().close();
        proofScriptDebugger.showActiveInspector(null);

        Examples.loadExamples().forEach(example -> {
            if (example.getName().equals("Contraposition"))
                example.open(proofScriptDebugger);

        });
       /* proofScriptDebugger.openScript(
                new File("")
        );

        proofScriptDebugger.openKeyFile(
                new File("src/test/resources/edu/kit/formal/psdb/interpreter/contraposition/contraposition.key"));
        */
    }

    /**
     * Load a test example
     * @param event
     */
    public void loadJavaTest(ActionEvent event) {
        proofScriptDebugger.getWelcomePaneDock().close();
        proofScriptDebugger.showActiveInspector(null);
    }

    /**
     * Load teh help page documentation
     * @param event
     */
    public void loadHelpPage(ActionEvent event) {
        proofScriptDebugger.getWelcomePaneDock().close();
        proofScriptDebugger.showCommandHelp(event);
        proofScriptDebugger.showHelpText();


    }

    /**
     * Load a Java problem with an existing script
     * @param event
     */
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


    /**
     * Load a Java File and an Empty Script
     *
     * @param event
     */
    public void loadJavaProblemWithNewScript(ActionEvent event) {
        proofScriptDebugger.getWelcomePaneDock().close();
        proofScriptDebugger.showActiveInspector(null);

        proofScriptDebugger.openJavaFile();
        proofScriptDebugger.getScriptController().newScript();

    }

    @FXML
    public void loadKeyProblem(ActionEvent event) {
        proofScriptDebugger.getWelcomePaneDock().close();
        proofScriptDebugger.showActiveInspector(event);
        proofScriptDebugger.loadKeYFile();
        proofScriptDebugger.getScriptController().newScript();
    }

}

