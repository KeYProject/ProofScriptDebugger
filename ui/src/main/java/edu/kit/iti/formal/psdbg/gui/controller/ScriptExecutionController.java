package edu.kit.iti.formal.psdbg.gui.controller;

import de.uka.ilkd.key.proof.init.ProofInputException;
import de.uka.ilkd.key.proof.io.ProblemLoaderException;
import edu.kit.iti.formal.psdbg.gui.controls.Utils;
import edu.kit.iti.formal.psdbg.interpreter.InterpreterBuilder;
import edu.kit.iti.formal.psdbg.interpreter.KeYProofFacade;
import edu.kit.iti.formal.psdbg.interpreter.KeyInterpreter;
import edu.kit.iti.formal.psdbg.interpreter.data.SavePoint;
import edu.kit.iti.formal.psdbg.interpreter.dbg.Breakpoint;
import edu.kit.iti.formal.psdbg.parser.ASTDiff;
import edu.kit.iti.formal.psdbg.parser.Facade;
import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;
import edu.kit.iti.formal.psdbg.parser.ast.ProofScript;
import edu.kit.iti.formal.psdbg.parser.ast.Statements;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.antlr.v4.runtime.RecognitionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ScriptExecutionController {
    protected static final Logger LOGGER = LogManager.getLogger(ScriptExecutionController.class);

    DebuggerMain mainCtrl;

    public ScriptExecutionController(DebuggerMain mainController){
        mainCtrl = mainController;
    }


    /**
     * Execute script and indicate whether an initial breakpoint is set
     * @param addInitBreakpoint
     */
    public void executeScript(boolean addInitBreakpoint) {

        if (mainCtrl.getModel().getDebuggerFramework() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Interpreter is already running \nDo you want to abort it?",
                    ButtonType.CANCEL, ButtonType.YES);
            Optional<ButtonType> ans = alert.showAndWait();
            ans.ifPresent(a -> {
                if (a == ButtonType.OK) mainCtrl.abortExecution();
            });

            if (ans.isPresent() && ans.get() == ButtonType.CANCEL) {
                return;
            }
        }

        assert mainCtrl.getModel().getDebuggerFramework() == null : "There should not be any interpreter running.";

        if (mainCtrl.FACADE.getProofState() == KeYProofFacade.ProofState.EMPTY) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No proof loaded is loaded yet. If proof loading was invoked, please wait. Loading may take a while.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if (mainCtrl.FACADE.getProofState() == KeYProofFacade.ProofState.DIRTY) {
            try {
                mainCtrl.FACADE.reload(mainCtrl.getModel().getKeyFile());
            } catch (ProofInputException | ProblemLoaderException e) {
                LOGGER.error(e);
                Utils.showExceptionDialog("Loading Error", "Could not clear Environment", "There was an error when clearing old environment",
                        e
                );
            }
        }

        // else getProofState() == VIRGIN!
        executeScript(mainCtrl.FACADE.buildInterpreter(), addInitBreakpoint);
    }
    /**
     * Execute the script that with using the interpreter that is build using the interpreterbuilder
     *
     * @param ib
     * @param
     */
    private void executeScript(InterpreterBuilder ib, boolean addInitBreakpoint) {
        try {

            Set<Breakpoint> breakpoints = mainCtrl.getScriptController().getBreakpoints();
            // get possible scripts and the main script!
            List<ProofScript> scripts = mainCtrl.getScriptController().getCombinedAST();
            if (mainCtrl.getScriptController().getMainScript() == null) {
                mainCtrl.getScriptController().setMainScript(scripts.get(0));
            }
            Optional<ProofScript> mainScript = mainCtrl.getScriptController().getMainScript().find(scripts);
            ProofScript ms;
            if (!mainScript.isPresent()) {
                mainCtrl.getScriptController().setMainScript(scripts.get(0));
                ms = scripts.get(0);
            } else {
                ms = mainScript.get();
            }

            LOGGER.debug("Parsed Scripts, found {}", scripts.size());
            LOGGER.debug("MainScript: {}", ms.getName());

            ib.setScripts(scripts);
            executeScript0(ib, breakpoints, ms, addInitBreakpoint);
        } catch (RecognitionException e) {
            LOGGER.error(e);
            Utils.showExceptionDialog("Antlr Exception", "", "Could not parse scripts.", e);
        }

    }



    public void executeScriptFromSavePoint(InterpreterBuilder ib, SavePoint point) {
        try {
            Set<Breakpoint> breakpoints = mainCtrl.getScriptController().getBreakpoints();
            // get possible scripts and the main script!
            List<ProofScript> scripts = mainCtrl.getScriptController().getCombinedAST();
            if (mainCtrl.getScriptController().getMainScript() == null) {
                mainCtrl.getScriptController().setMainScript(scripts.get(0));
            }
            Optional<ProofScript> mainScript = mainCtrl.getScriptController().getMainScript().find(scripts);
            ProofScript ms;
            if (!mainScript.isPresent()) {
                mainCtrl.getScriptController().setMainScript(scripts.get(0));
                ms = scripts.get(0);
            } else {
                ms = mainScript.get();
            }

            Statements body = new Statements();
            boolean flag =false;
            for (int i = 0; i < ms.getBody().size(); i++) {
                if(flag) {body.add(ms.getBody().get(i));
                    continue;}
                flag = point.isThisStatement(ms.getBody().get(i));
            }

            ms.setBody(body);

            LOGGER.debug("Parsed Scripts, found {}", scripts.size());
            LOGGER.debug("MainScript: {}", ms.getName());
            //ib.setDirectory(model.getKeyFile() != null ? model.getKeyFile() : model.getJavaFile());
            ib.setScripts(scripts);
            executeScript0(ib, breakpoints, ms, false);
        } catch (RecognitionException e) {
            LOGGER.error(e);
            Utils.showExceptionDialog("Antlr Exception", "", "Could not parse scripts.", e);
        }

    }


    private void executeScript0(InterpreterBuilder ib,
                                Collection<? extends Breakpoint> breakpoints,
                                ProofScript ms, boolean addInitBreakpoint) {
        ib.setProblemPath(mainCtrl.getFacade().getFilepath());
        KeyInterpreter interpreter = ib.build();
        mainCtrl.createDebuggerFramework(breakpoints, ms, addInitBreakpoint, interpreter);
    }


    private ASTNode calculateDiff (ASTDiff dff) {

        return null;
    }


}
