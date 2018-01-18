package edu.kit.iti.formal.psdbg.gui.controller;

import alice.tuprolog.InvalidLibraryException;
import alice.tuprolog.InvalidTheoryException;
import com.google.common.eventbus.Subscribe;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.uka.ilkd.key.api.ProofApi;
import de.uka.ilkd.key.gui.MainWindow;
import de.uka.ilkd.key.proof.Goal;
import de.uka.ilkd.key.proof.Node;
import de.uka.ilkd.key.proof.Proof;
import de.uka.ilkd.key.proof.SingleProof;
import de.uka.ilkd.key.proof.init.ProofInputException;
import de.uka.ilkd.key.proof.io.ProblemLoaderException;
import de.uka.ilkd.key.speclang.Contract;
import edu.kit.iti.formal.psdbg.ShortCommandPrinter;
import edu.kit.iti.formal.psdbg.examples.Examples;
import edu.kit.iti.formal.psdbg.fmt.DefaultFormatter;
import edu.kit.iti.formal.psdbg.gui.ProofScriptDebugger;
import edu.kit.iti.formal.psdbg.gui.controls.*;
import edu.kit.iti.formal.psdbg.gui.graph.Graph;
import edu.kit.iti.formal.psdbg.gui.graph.GraphView;
import edu.kit.iti.formal.psdbg.gui.model.DebuggerMainModel;
import edu.kit.iti.formal.psdbg.gui.model.InspectionModel;
import edu.kit.iti.formal.psdbg.gui.model.InterpreterThreadState;
import edu.kit.iti.formal.psdbg.interpreter.InterpreterBuilder;
import edu.kit.iti.formal.psdbg.interpreter.KeYProofFacade;
import edu.kit.iti.formal.psdbg.interpreter.KeyInterpreter;
import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.data.State;
import edu.kit.iti.formal.psdbg.interpreter.dbg.*;
import edu.kit.iti.formal.psdbg.parser.ast.ProofScript;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.RecognitionException;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dockfx.DockNode;
import org.dockfx.DockPane;
import org.dockfx.DockPos;
import org.reactfx.util.FxTimer;
import org.reactfx.util.Timer;

import javax.annotation.Nullable;
import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import org.reactfx.util.Timer;

/**
 * Controller for the Debugger MainWindow
 *
 * @author S.Grebing
 * @author Alexander Weigl
 */
public class DebuggerMain implements Initializable {
    public static final KeYProofFacade FACADE = new KeYProofFacade();

    protected static final Logger LOGGER = LogManager.getLogger(DebuggerMain.class);

    public final ContractLoaderService contractLoaderService = new ContractLoaderService();

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Getter
    private final DebuggerMainModel model = new DebuggerMainModel();
    private final GraphView graphView = new GraphView();
    private final Graph.PTreeGraph graph = graphView.getGraph();

    private final DockNode graphViewNode = new DockNode(graphView, "Debug graph");
    private InspectionViewsController inspectionViewsController;
    private ScriptController scriptController;
    @FXML
    private DebuggerStatusBar statusBar;
    @FXML
    private DockPane dockStation;
    @FXML
    private ToggleButton togBtnCommandHelp;
    @FXML
    private ToggleButton togBtnProofTree;
    @FXML
    private ToggleButton togBtnActiveInspector;
    @FXML
    private ToggleButton togBtnWelcome;
    @FXML
    private ToggleButton togBtnCodeDock;
    @FXML
    private CheckMenuItem miCommandHelp;
    @FXML
    private CheckMenuItem miCodeDock;
    @FXML
    private CheckMenuItem miWelcomeDock;
    @FXML
    private CheckMenuItem miActiveInspector;
    @FXML
    private CheckMenuItem miProofTree;
    @FXML
    private ToggleButton btnInteractiveMode;
    private JavaArea javaArea = new JavaArea();
    private DockNode javaAreaDock = new DockNode(javaArea, "Java Source",
            new MaterialDesignIconView(MaterialDesignIcon.CODEPEN)
    );
    //-----------------------------------------------------------------------------------------------------------------
    private ProofTree proofTree = new ProofTree();
    private DockNode proofTreeDock = new DockNode(proofTree, "Proof Tree");
    private WelcomePane welcomePane = new WelcomePane(this);
    private DockNode welcomePaneDock = new DockNode(welcomePane, "Welcome", new MaterialDesignIconView(MaterialDesignIcon.ACCOUNT));
    private DockNode activeInspectorDock;
    private CommandHelp commandHelp = new CommandHelp();
    private DockNode commandHelpDock = new DockNode(commandHelp, "DebuggerCommand Help");
    private InteractiveModeController interactiveModeController;
    @FXML
    private Menu examplesMenu;
    private Timer interpreterThreadTimer;

    @Subscribe
    public void handle(Events.ShowSequent ss) {
        SequentView sv = new SequentView();
        sv.setKeYProofFacade(FACADE);
        sv.setNode(ss.getNode());
        DockNode node = new DockNode(sv, "Sequent Viewer " + ss.getNode().serialNr());
        node.dock(dockStation, DockPos.CENTER, getActiveInspectorDock());
    }

    @Subscribe
    public void handle(Events.PublishMessage message) {
        if (message.getFlash() == 0)
            statusBar.publishMessage(message.getMessage());
        else if (message.getFlash() == 1)
            statusBar.publishSuccessMessage(message.getMessage());
        else
            statusBar.publishErrorMessage(message.getMessage());
    }

    @Subscribe
    public void handle(Events.SelectNodeInGoalList evt) {
        InspectionModel im = getInspectionViewsController().getActiveInspectionViewTab().getModel();
        DockNode dockNode = getInspectionViewsController().newPostMortemInspector(im);
        dockNode.dock(dockStation, DockPos.CENTER, getActiveInspectorDock());
        for (GoalNode<KeyData> gn : im.getGoals()) {
            if (gn.getData().getNode().equals(evt.getNode())) {
                im.setSelectedGoalNodeToShow(gn);
                dockNode.focus();
                return;
            }
        }

        statusBar.publishErrorMessage("Node not present in Goal List");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
    }

    private void init() {
        Events.register(this);
        model.setDebugMode(false);
        scriptController = new ScriptController(dockStation);
        interactiveModeController = new InteractiveModeController(scriptController);
        btnInteractiveMode.setSelected(false);
        inspectionViewsController = new InspectionViewsController(dockStation);
        activeInspectorDock = inspectionViewsController.getActiveInterpreterTabDock();
        //register the welcome dock in the center
        welcomePaneDock.dock(dockStation, DockPos.LEFT);

        marriageJavaCode();

        //marriage key proof facade to proof tree
        getFacade().proofProperty().addListener(
                (prop, o, n) -> {
                    if (n == null) {
                        proofTree.setRoot(null);
                    } else {
                        proofTree.setRoot(n.root());
                        InspectionViewsController inspectionViewsController = getInspectionViewsController();
                        InspectionView activeInspectionViewTab = inspectionViewsController.getActiveInspectionViewTab();
                        InspectionModel model = activeInspectionViewTab.getModel();
                        ObservableList<GoalNode<KeyData>> goals = model.getGoals();
                        goals.setAll(FACADE.getPseudoGoals());
                        model.setSelectedGoalNodeToShow(null);

                        // frames / contextes zurück setzen
                        activeInspectionViewTab.getModel();
                        scriptController.getOpenScripts().keySet().forEach(ScriptArea::removeExecutionMarker);
                    }
                    proofTree.setProof(n);
                }
        );

        //
        model.statePointerProperty().addListener((prop, o, n) -> {
            this.handleStatePointerUI(n);
        });

        //Debugging
        Utils.addDebugListener(model.javaCodeProperty());
        Utils.addDebugListener(model.executeNotPossibleProperty(), "executeNotPossible");
        scriptController.mainScriptProperty().bindBidirectional(statusBar.mainScriptIdentifierProperty());
        initializeExamples();


        dockingNodeHandling(togBtnActiveInspector,
                miActiveInspector,
                activeInspectorDock,
                DockPos.CENTER);

        dockingNodeHandling(togBtnCodeDock,
                miCodeDock,
                javaAreaDock,
                DockPos.RIGHT);

        dockingNodeHandling(togBtnCommandHelp,
                miCommandHelp,
                commandHelpDock,
                DockPos.RIGHT);

        dockingNodeHandling(togBtnWelcome,
                miWelcomeDock,
                welcomePaneDock,
                DockPos.CENTER);

        dockingNodeHandling(togBtnProofTree,
                miProofTree,
                proofTreeDock,
                DockPos.LEFT);

        statusBar.interpreterStatusModelProperty().bind(model.interpreterStateProperty());
        renewThreadStateTimer();
    }

    /**
     * If the mouse moves other toolbar button, the help text should display in the status bar
     */
    private void registerToolbarToStatusBar() {
        /*toolbar.getChildrenUnmodifiable().forEach(
                n -> n.setOnMouseEntered(statusBar.getTooltipHandler()));

        buttonStartInterpreter.setOnMouseEntered(statusBar.getTooltipHandler());
        */
    }

    /**
     * <b>Note:</b> This is executed in the Interpreter Thread!
     * You should listen to the {@link DebuggerMainModel#statePointerProperty}
     *
     * @param node
     */
    private void handleStatePointer(PTreeNode<KeyData> node) {
        Platform.runLater(() -> model.setStatePointer(node));
    }

    /**
     * Handling of a new state in the {@link DebuggerFramework}, now in the JavaFX Thread
     *
     * @see {@link #handleStatePointer(PTreeNode)}
     */
    private void handleStatePointerUI(PTreeNode<KeyData> node) {
        graph.addPartiallyAndMark(node);

        if (node != null) {
            getInspectionViewsController().getActiveInspectionViewTab().activate(node, node.getStateBeforeStmt());
            scriptController.getDebugPositionHighlighter().highlight(node.getStatement());
        } else {
            getInspectionViewsController().getActiveInspectionViewTab().getFrames().getItems().clear();
            scriptController.getDebugPositionHighlighter().remove();
        }
    }

    private void marriageJavaCode() {
        //Listener on chosenContract from
        model.chosenContractProperty().addListener(o -> {
            //javaCode.set(Utils.getJavaCode(chosenContract.get()));
            try {
                LOGGER.debug("Selected contract: {}", model.getChosenContract().getHTMLText(getFacade().getService()));
                String encoding = null; //encoding Plattform default
                model.setJavaCode(FileUtils.readFileToString(model.javaFileProperty().get(), Charset.defaultCharset()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            showCodeDock(null);
        });

        //addCell listener for linehighlighting when changing selection in inspectionview
        getInspectionViewsController().getActiveInspectionViewTab().getModel().highlightedJavaLinesProperty().
                addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        javaArea.linesToHighlightProperty().set(newValue);
                    } else {
                        javaArea.linesToHighlightProperty().set(FXCollections.emptyObservableSet());
                    }
                });

        model.javaCodeProperty().addListener((observable, oldValue, newValue) -> {
            try {
                javaArea.setText(newValue);
            } catch (Exception e) {
                LOGGER.catching(e);
            }
        });

    }

    //region Actions: Menu

    /*@FXML
    public void saveAsScript() throws IOException {
        File f = openFileChooserSaveDialog("Save script", "Save Script files", "kps");
        if (f != null) {
           /* if(!f.exists()){
                f.createNewFile();
            }
            saveScript(f);
        }
    }*/

    public KeYProofFacade getFacade() {
        return FACADE;
    }

    public InspectionViewsController getInspectionViewsController() {
        return inspectionViewsController;
    }

    private void initializeExamples() {
        examplesMenu.getItems().clear();
        Examples.loadExamples().forEach(example -> {
            MenuItem item = new MenuItem(example.getName());
            item.setOnAction(event -> {
                example.open(this);
            });
            examplesMenu.getItems().add(item);
        });
    }

    public void showCodeDock(ActionEvent actionEvent) {
        if (!javaAreaDock.isDocked()) {
            javaAreaDock.dock(dockStation, DockPos.RIGHT);
        }
    }

    public void dockingNodeHandling(ToggleButton btn, CheckMenuItem cmi, DockNode dn, DockPos defaultPosition) {
        BooleanBinding prop = dn.dockedProperty().or(dn.floatingProperty());
        prop.addListener((p, o, n) -> {
            btn.setSelected(n);
            cmi.setSelected(n);
        });

        EventHandler<ActionEvent> handler = event -> {
            if (!prop.get()) {
                if (dn.getLastDockPos() != null)
                    dn.dock(dockStation, dn.getLastDockPos());
                else
                    dn.dock(dockStation, defaultPosition);
            } else {
                dn.undock();
            }
        };
        btn.setOnAction(handler);
        cmi.setOnAction(handler);
    }

    @FXML
    public void executeScript() {
        executeScript(false);
    }

    private void executeScript(boolean addInitBreakpoint) {
        if (model.getDebuggerFramework() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Interpreter is already running \nDo you want to abort it?",
                    ButtonType.CANCEL, ButtonType.YES);
            Optional<ButtonType> ans = alert.showAndWait();
            ans.ifPresent(a -> {
                if (a == ButtonType.OK) abortExecution();
            });

            if (ans.isPresent() && ans.get() == ButtonType.CANCEL) {
                return;
            }
        }

        assert model.getDebuggerFramework() == null : "There should not be any interpreter running.";

        if (FACADE.getProofState() == KeYProofFacade.ProofState.EMPTY) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No proof loaded!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if (FACADE.getProofState() == KeYProofFacade.ProofState.DIRTY) {
            try {
                FACADE.reload(model.getKeyFile());
            } catch (ProofInputException | ProblemLoaderException e) {
                LOGGER.error(e);
                Utils.showExceptionDialog("Loading Error", "Could not clear Environment", "There was an error when clearing old environment",
                        e
                );
            }
        }

        // else getProofState() == VIRGIN!
        executeScript(FACADE.buildInterpreter(), addInitBreakpoint);
    }

    @FXML
    public void abortExecution() {

        if (model.getDebuggerFramework() != null) {
            try {
                // try to friendly
                Future future = executorService.submit(() -> {
                    model.getDebuggerFramework().stop();
                    model.getDebuggerFramework().unregister();
                    model.getDebuggerFramework().release();
                });

                // wait a second!
                future.get(1, TimeUnit.SECONDS);
                // ungently stop
                model.getDebuggerFramework().hardStop();

            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            } finally {
                model.setDebuggerFramework(null);
            }
        } else {
            LOGGER.info("no interpreter running");
        }
    }

    /**
     * Execute the script that with using the interpreter that is build using the interpreterbuilder
     *
     * @param ib
     * @param
     */
    private void executeScript(InterpreterBuilder ib, boolean addInitBreakpoint) {
        try {
            Set<Breakpoint> breakpoints = scriptController.getBreakpoints();
            // get possible scripts and the main script!
            List<ProofScript> scripts = scriptController.getCombinedAST();
            if (scriptController.getMainScript() == null) {
                scriptController.setMainScript(scripts.get(0));
            }
            Optional<ProofScript> mainScript = scriptController.getMainScript().find(scripts);
            ProofScript ms;
            if (!mainScript.isPresent()) {
                scriptController.setMainScript(scripts.get(0));
                ms = scripts.get(0);
            } else {
                ms = mainScript.get();
            }

            LOGGER.debug("Parsed Scripts, found {}", scripts.size());
            LOGGER.debug("MainScript: {}", ms.getName());

            ib.setScripts(scripts);
            KeyInterpreter interpreter = ib.build();
            DebuggerFramework<KeyData> df = new DebuggerFramework<>(interpreter, ms, null);
            df.setSucceedListener(this::onInterpreterSucceed);
            df.setErrorListener(this::onInterpreterError);
            if (addInitBreakpoint) {
                df.releaseUntil(new Blocker.CounterBlocker(1)); // just execute
            }
            df.getBreakpoints().addAll(breakpoints);
            df.getStatePointerListener().add(this::handleStatePointer);
            df.start();

            model.setDebuggerFramework(df);
        } catch (RecognitionException e) {
            LOGGER.error(e);
            Utils.showExceptionDialog("Antlr Exception", "", "Could not parse scripts.", e);
        }
    }

    private void onInterpreterSucceed(DebuggerFramework<KeyData> keyDataDebuggerFramework) {
        Platform.runLater(() -> {
            scriptController.getDebugPositionHighlighter().remove();
            statusBar.publishSuccessMessage("Interpreter finished.");
            btnInteractiveMode.setDisable(false);
            assert model.getDebuggerFramework() != null;
            btnInteractiveMode.setSelected(true);
            PTreeNode<KeyData> statePointer = model.getDebuggerFramework().getStatePointer();
            assert statePointer!=null;
            State<KeyData> lastState = statePointer.getStateAfterStmt();
            getInspectionViewsController().getActiveInspectionViewTab().activate(statePointer, lastState);

        });
    }

    @FXML
    public void debugGraph(ActionEvent event) {
        if (!graphViewNode.isDocked() && !graphViewNode.isFloating()) {
            graphViewNode.dock(dockStation, DockPos.CENTER);
        }

        graph.clear(true);

        for (PTreeNode<KeyData> n : model.getDebuggerFramework().getStates()) {
            graph.addCell(n);
        }

        for (PTreeNode<KeyData> n : model.getDebuggerFramework().getStates()) {
            if (n.getStepOver() != null) {
                graph.addEdge(n, n.getStepOver(), "over");
            }
            if (n.getStepInto() != null) {
                graph.addEdge(n, n.getStepInto(), "into");
            }
            if (n.getStepInvOver() != null) {
                graph.addEdge(n, n.getStepInvOver(), "revo");
            }
            if (n.getStepInvInto() != null) {
                graph.addEdge(n, n.getStepInvInto(), "otni");
            }
            if (n.getStepReturn() != null) {
                graph.addEdge(n, n.getStepReturn(), "rtrn");
            }
        }
    }

    @FXML
    public void debugPrintDot(@Nullable ActionEvent ae) {
        if (model.getDebuggerFramework() == null) {
            statusBar.publishErrorMessage("can print debug info, no debugger started!");
            return;
        }

        debugGraph(ae);

        try (PrintWriter out = new PrintWriter(new FileWriter("debug.dot"))) {
            out.println("digraph G {");
            for (PTreeNode<KeyData> n : model.getDebuggerFramework().getStates()) {
                out.format("%d [label=\"%s@%s (G: %d)\"]%n", n.hashCode(),
                        n.getStatement().accept(new ShortCommandPrinter()),
                        n.getStatement().getStartPosition().getLineNumber(),
                        n.getStateBeforeStmt().getGoals().size()
                );

                if (n.getStepOver() != null)
                    out.format("%d -> %d [label=\"SO\"]%n", n.hashCode(), n.getStepOver().hashCode());
                if (n.getStepInto() != null)
                    out.format("%d -> %d [label=\"SI\"]%n", n.hashCode(), n.getStepInto().hashCode());

                if (n.getStepInvOver() != null)
                    out.format("%d -> %d [label=\"<SO\"]%n", n.hashCode(), n.getStepInvOver().hashCode());
                if (n.getStepInvInto() != null)
                    out.format("%d -> %d [label=\"<SI\"]%n", n.hashCode(), n.getStepInvInto().hashCode());

                if (n.getStepReturn() != null)
                    out.format("%d -> %d [label=\"R\"]%n", n.hashCode(), n.getStepReturn().hashCode());

            }
            out.println("}");

        } catch (IOException e) {
            statusBar.publishErrorMessage(e.getMessage());
        }
    }

    private void onInterpreterError(DebuggerFramework<KeyData> keyDataDebuggerFramework, Throwable throwable) {
        Platform.runLater(() -> {
            Utils.showExceptionDialog("Error during Execution", "Error during Script Execution",
                    "Here should be some really good text...\nNothing will be the same. Everything broken.",
                    throwable
            );
        });
    }

    private void renewThreadStateTimer() {
        if (interpreterThreadTimer != null) {
            interpreterThreadTimer.stop();
        }
        interpreterThreadTimer = FxTimer.runPeriodically(Duration.ofMillis(500),
                () -> {
                    if (model.getDebuggerFramework() == null) {
                        model.setInterpreterState(InterpreterThreadState.NO_THREAD);
                    } else {
                        Thread t = model.getDebuggerFramework().getInterpreterThread();
                        switch (t.getState()) {
                            case NEW:
                            case BLOCKED:
                            case WAITING:
                            case TIMED_WAITING:
                                model.setInterpreterState(InterpreterThreadState.WAIT);
                                break;
                            case TERMINATED:
                                if (model.getDebuggerFramework().hasError())
                                    model.setInterpreterState(InterpreterThreadState.ERROR);
                                else
                                    model.setInterpreterState(InterpreterThreadState.FINISHED);
                                break;
                            default:
                                model.setInterpreterState(InterpreterThreadState.RUNNING);
                        }
                    }
                });
    }

    @FXML
    public void executeStepwise() {
        executeScript(true);
        //executeScript(FACADE.buildInterpreter(), true);
    }

    @FXML
    public void executeToBreakpoint() {
        Set<Breakpoint> breakpoints = scriptController.getBreakpoints();
        if (breakpoints.size() == 0) {
            statusBar.publishMessage("There was is no breakpoint set");
        }

        executeScript(false);
    }

    public void openKeyFile(File keyFile) {
        if (keyFile != null) {
            model.setKeyFile(keyFile);
            model.setInitialDirectory(keyFile.getParentFile());
            Task<ProofApi> task = FACADE.loadKeyFileTask(keyFile);
            task.setOnSucceeded(event -> {
                statusBar.publishMessage("Loaded key sourceName: %s", keyFile);
                statusBar.stopProgress();
            });

            task.setOnFailed(event -> {
                statusBar.stopProgress();
                event.getSource().exceptionProperty().get();

                Utils.showExceptionDialog("Could not load sourceName", "Key sourceName loading error", "",
                        (Throwable) event.getSource().exceptionProperty().get()
                );
            });

            ProgressBar bar = new ProgressBar();
            bar.progressProperty().bind(task.progressProperty());
            executorService.execute(task);
            this.showActiveInspector(null);
        }
    }

    public void openJavaFile(File javaFile) {
        if (javaFile != null) {
            model.setJavaFile(javaFile);
            model.setInitialDirectory(javaFile.getParentFile());
            contractLoaderService.start();
        }
    }

    @FXML
    public void loadKeYFile() {
        File keyFile = openFileChooserOpenDialog("Select KeY File", "KeY Files", "key", "kps");
        openKeyFile(keyFile);
    }

    public void openJavaFile() {
        loadJavaFile();
        showCodeDock(null);
    }

    @FXML
    protected void loadJavaFile() {
        File javaFile = openFileChooserOpenDialog("Select Java File", "Java Files", "java");
        openJavaFile(javaFile);
    }

    private File openFileChooserOpenDialog(String title, String description, String... fileEndings) {
        FileChooser fileChooser = getFileChooser(title, description, fileEndings);
        //File sourceName = fileChooser.showOpenDialog(inspectionViewsController.getInspectionViewTab().getGoalView().getScene().getWindow());
        File file = fileChooser.showOpenDialog(statusBar.getScene().getWindow());
        if (file != null) model.setInitialDirectory(file.getParentFile());
        return file;
    }

    private FileChooser getFileChooser(String title, String description, String[] fileEndings) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter(description, fileEndings));
        if (model.getInitialDirectory() == null)
            model.setInitialDirectory(new File("src/test/resources/edu/kit/formal/interpreter/contraposition/"));

        if (!model.getInitialDirectory().exists())
            model.setInitialDirectory(new File("."));

        fileChooser.setInitialDirectory(model.getInitialDirectory());
        return fileChooser;
    }

    @FXML
    public void executeDiff() {
        //save old PT
        // Calculate top difference between current and last executed script
        // Find last state of common ASTNode
        // Use this state to build new interpreter in proof tree controller.
        // execute residual script


    }

    /**
     * Continue after the interpreter has reached a breakpoint
     *
     * @param event
     */
    @FXML
    public void continueAfterRun(ActionEvent event) {
        LOGGER.debug("DebuggerMain.continueAfterBreakpoint");
        try {
            assert model.getDebuggerFramework() != null : "You should have started the prove";
            model.getDebuggerFramework().execute(new ContinueCommand<>());
        } catch (DebuggerException e) {
            Utils.showExceptionDialog("", "", "", e);
            LOGGER.error(e);
        }
    }

    /**
     * Reload a problem from the beginning
     *
     * @param event
     */
    @FXML
    public void reloadProblem(ActionEvent event) {
        //abort current execution();
        //save old information and refresh models
        File lastLoaded;
        if (model.getKeyFile() != null) {
            lastLoaded = model.getKeyFile();
        } else {
            Contract chosen = model.getChosenContract();
            lastLoaded = model.getJavaFile();
        }
        //model.reload();
        abortExecution();
        handleStatePointerUI(null);
        model.setStatePointer(null);
        //reload getInspectionViewsController().getActiveInspectionViewTab().getModel()
        InspectionModel iModel = getInspectionViewsController().getActiveInspectionViewTab().getModel();
        iModel.setHighlightedJavaLines(null);
        iModel.getGoals().clear();
        iModel.setSelectedGoalNodeToShow(null);

        try {
            FACADE.reload(lastLoaded);
            if (iModel.getGoals().size() > 0) {
                iModel.setSelectedGoalNodeToShow(iModel.getGoals().get(0));
            }
        } catch (ProofInputException e) {
            e.printStackTrace();
        } catch (ProblemLoaderException e) {
            e.printStackTrace();
        }


    }

    @FXML
    public void closeProgram() {
        System.exit(0);
    }

/*    public void openJavaFile() {
        loadJavaFile();
        showCodeDock(null);
    }
*/

    @FXML
    public void openScript() {
        File scriptFile = openFileChooserOpenDialog("Select Script File",
                "Proof Script File", "kps");
        if (scriptFile != null) {
            openScript(scriptFile);
        }
    }

    public void openScript(File scriptFile) {
        assert scriptFile != null;
        model.setInitialDirectory(scriptFile.getParentFile());
        try {
            String code = FileUtils.readFileToString(scriptFile, Charset.defaultCharset());
            ScriptArea area = scriptController.createNewTab(scriptFile);
        } catch (IOException e) {
            LOGGER.error(e);
            Utils.showExceptionDialog("Exception occured", "",
                    "Could not load sourceName " + scriptFile, e);
        }
    }

    @FXML
    public void saveScript() {
        try {
            scriptController.saveCurrentScript();
        } catch (IOException e) {
            LOGGER.error(e);
            Utils.showExceptionDialog("Could not save file", "Saving File Error", "Could not save current script", e);

        }
    }

    @FXML
    public void saveAsScript() throws IOException {
        File f = openFileChooserSaveDialog("Save script", "Save Script files", "kps");
        if (f != null) {
           /* if(!f.exists()){
                f.createNewFile();
            }*/
            saveScript(f);
        }
    }

    /**
     * Creates a filechooser dialog
     *
     * @param title       of the dialog
     * @param description of the files
     * @param fileEndings sourceName that should be shown
     * @return
     */
    private File openFileChooserSaveDialog(String title, String description, String... fileEndings) {
        FileChooser fileChooser = getFileChooser(title, description, fileEndings);
        // File sourceName = fileChooser.showSaveDialog(inspectionViewsController.getInspectionViewTab().getGoalView().getScene().getWindow());
        File file = fileChooser.showOpenDialog(statusBar.getScene().getWindow());
        if (file != null) model.setInitialDirectory(file.getParentFile());
        return file;
    }
    //endregion

    private void saveScript(File scriptFile) {
        try {
            scriptController.saveCurrentScriptAs(scriptFile);
        } catch (IOException e) {
            LOGGER.error(e);
            Utils.showExceptionDialog("Could not save file", "Saving File Error", "Could not save to file " + scriptFile.getName(), e);
        }
    }


    /**
     * Save KeY proof as proof file
     *
     * @param actionEvent
     */
    public void saveProof(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(btnInteractiveMode.getScene().getWindow());
        if (file != null) {
            try {
                saveProof(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveProof(File file) throws IOException {
        if (FACADE.getProof() != null)
            FACADE.getProof().saveToFile(file);
    }

    /**
     * Perform a step over
     * TODO Uebergabe des selektierten Knotens damit richtiges ausgewählt
     *
     * @param actionEvent
     */
    public void stepOver(ActionEvent actionEvent) {
        LOGGER.debug("DebuggerMain.stepOver");
        try {
            assert model.getDebuggerFramework() != null : "You should have started the prove";
            model.getDebuggerFramework().execute(new StepOverCommand<>());
        } catch (DebuggerException e) {
            Utils.showExceptionDialog("", "", "", e);
            LOGGER.error(e);
        }
    }


    /**
     * Perform a step into
     *
     * @param actionEvent
     */
    public void stepInto(ActionEvent actionEvent) {
        LOGGER.debug("DebuggerMain.stepInto");
        try {
            if (model.getDebuggerFramework().getStatePointer().isAtomic()) {
                model.getDebuggerFramework().getStatePointerListener()
                        .add(new StepIntoHandler(model.getStatePointer()));
            }
            model.getDebuggerFramework().execute(new StepIntoCommand<>());
        } catch (DebuggerException e) {
            Utils.showExceptionDialog("", "", "", e);
            LOGGER.error(e);
        }
    }

    private void handleStepInto(PTreeNode<KeyData> newState) {


    }

    /**
     * Perform a step back
     * Does stepinto back
     *
     * @param actionEvent
     */
    @Deprecated
    public void stepBack(ActionEvent actionEvent) {
        LOGGER.debug("DebuggerMain.stepBack");
        try {
            model.getDebuggerFramework().execute(new StepBackCommand<>());
        } catch (DebuggerException e) {
            Utils.showExceptionDialog("", "", "", e);
            LOGGER.error(e);
        }
    }

    public void stepOverReverse(ActionEvent actionEvent) {
        LOGGER.debug("DebuggerMain.stepBack");
        try {
            model.getDebuggerFramework().execute(new StepOverReverseCommand<>());
        } catch (DebuggerException e) {
            Utils.showExceptionDialog("", "", "", e);
            LOGGER.error(e);
        }
    }


    public void stepIntoReverse(ActionEvent actionEvent) {
        LOGGER.debug("DebuggerMain.stepIntoReverser");
        try {
            PTreeNode<KeyData> statePointer = model.getDebuggerFramework().getStatePointer();
            if (statePointer.getStepInvInto() == null) {
                if (statePointer.getStepInvOver() != null) {
                    if (statePointer.getStepInvOver().isAtomic()) {
                        model.getDebuggerFramework().getStatePointerListener()
                                .add(new StepIntoReverseHandler(model.getStatePointer()));
                    }
                    model.getDebuggerFramework().execute(new StepIntoReverseCommand<>());
                } else {
                    if (statePointer.isLastNode() || statePointer.isFirstNode()) {
                        LOGGER.error("We need a special treatment");
                    } else {
                        LOGGER.error("There is no state to step into reverse");
                    }
                }
            }
        } catch (DebuggerException e) {
            Utils.showExceptionDialog("", "", "", e);
            LOGGER.error(e);
        }


/*        LOGGER.debug("DebuggerMain.stepOverReverse");
        try {
            model.getDebuggerFramework().execute(new StepIntoReverseCommand<>());
        } catch (DebuggerException e) {
            Utils.showExceptionDialog("", "", "", e);
            LOGGER.error(e);
        }*/
    }

    public void stopDebugMode(ActionEvent actionEvent) {
        scriptController.getDebugPositionHighlighter().remove();
        Button stop = (Button) actionEvent.getSource();
        javafx.scene.Node graphic = stop.getGraphic();
        if (graphic instanceof MaterialDesignIconView) {
            MaterialDesignIconView buttonLabel = (MaterialDesignIconView) graphic;
            if (buttonLabel.getGlyphName().equals("STOP")) {
                stop.setGraphic(new MaterialDesignIconView(MaterialDesignIcon.RELOAD, "24.0"));
                stop.setTooltip(new Tooltip("Reload Problem"));
            } else {
                if (buttonLabel.getGlyphName().equals("RELOAD")) {
                    stop.setGraphic(new MaterialDesignIconView(MaterialDesignIcon.STOP, "24.0"));
                    stop.setTooltip(new Tooltip("Stop Debugging"));
                    reloadProblem(actionEvent);
                }
            }
        } else {
            throw new RuntimeException("Something went wrong when reading stop button");
        }

    }

  /*  public void handle(Events.TacletApplicationEvent tap){
        TacletApp app = tap.getApp();
        Goal currentGoal = tap.getCurrentGoal();
        ImmutableList<Goal> apply = currentGoal.apply(app);



    }*/

    public void newScript(ActionEvent actionEvent) {
        scriptController.newScript();
    }


    @FXML
    public void interactiveMode(ActionEvent actionEvent) {
        if (!btnInteractiveMode.isSelected()) {
            interactiveModeController.setActivated(true);
            //SaG: this needs to be set to filter inapplicable rules
            this.getFacade().getEnvironment().getProofControl().setMinimizeInteraction(true);
            interactiveModeController.start(getFacade().getProof(), getInspectionViewsController().getActiveInspectionViewTab().getModel());
        } else {
            interactiveModeController.stop();
        }
    }

    @FXML
    public void showWelcomeDock(ActionEvent actionEvent) {
        if (!welcomePaneDock.isDocked() && !welcomePaneDock.isFloating()) {
            welcomePaneDock.dock(dockStation, DockPos.CENTER);
        }
    }

    @FXML
    public void showActiveInspector(ActionEvent actionEvent) {
        if (!activeInspectorDock.isDocked() &&
                !activeInspectorDock.isFloating()) {

            activeInspectorDock.dock(dockStation, DockPos.RIGHT);
        }
    }

    @FXML
    public void showCommandHelp(ActionEvent event) {
        if (!commandHelpDock.isDocked() && !commandHelpDock.isFloating()) {
            commandHelpDock.dock(dockStation, DockPos.LEFT);
        }
    }

    @FXML
    public void showProofTree(ActionEvent actionEvent) {
        if (!proofTreeDock.isDocked() && !proofTreeDock.isFloating()) {
            proofTreeDock.dock(dockStation, DockPos.CENTER);
        }
    }

    public DockNode getJavaAreaDock() {
        return javaAreaDock;
    }

    public DockNode getWelcomePaneDock() {
        return welcomePaneDock;
    }

    public DockNode getActiveInspectorDock() {
        return activeInspectorDock;
    }

    public void showHelpText() {
        String url = ProofScriptDebugger.class.getResource("intro.html").toExternalForm();
        showHelpText(url);
    }

    public void showHelpText(String url) {
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.load(url);
        DockNode dn = new DockNode(browser);
        dn.setTitle("ScriptLanguage Description");
        //this.dockStation.getChildren().addCell(dn);
        dn.dock(dockStation, DockPos.LEFT);
    }

    public void openNewHelpDock(String title, String content) {
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.loadContent(content);
        DockNode dn = new DockNode(browser, title);
        dn.dock(dockStation, DockPos.LEFT);
    }

    public ScriptController getScriptController() {
        return scriptController;
    }

    @FXML
    public void reformatCurrentEditor(ActionEvent event) {
        scriptController.getOpenScripts().values().forEach(ed -> {
            try {
                DefaultFormatter df = new DefaultFormatter();
                ((ScriptArea) ed.getContents()).setText(
                        df.format(((ScriptArea) ed.getContents()).getText()));
            } catch (InvalidLibraryException | IOException | InvalidTheoryException e) {
                LOGGER.debug(e);
            }
        });
    }

    public void openInKey(@Nullable ActionEvent event) {
        if (FACADE.getProofState() == KeYProofFacade.ProofState.EMPTY) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No proof is loaded", ButtonType.OK);
            alert.show();
            return;
        }

        SwingUtilities.invokeLater(() -> {
            // Swing Thread!
            MainWindow keyWindow = MainWindow.getInstance();
            keyWindow.addProblem(new SingleProof(FACADE.getProof(), "script"));
            //keyWindow.getProofList().getModel().addProof();
            keyWindow.makePrettyView();
            keyWindow.setVisible(true);
        });
    }


    public class ContractLoaderService extends Service<List<Contract>> {
        @Override
        protected void succeeded() {
            statusBar.publishMessage("Contract loaded");
            List<Contract> contracts = getValue();
            ContractChooser cc = new ContractChooser(FACADE.getService(), contracts);

            cc.showAndWait().ifPresent(result -> {
                model.setChosenContract(result);
                try {
                    FACADE.activateContract(result);
                    getInspectionViewsController().getActiveInspectionViewTab().getModel().getGoals().setAll(FACADE.getPseudoGoals());
                } catch (ProofInputException e) {
                    LOGGER.error(e);
                    Utils.showExceptionDialog("", "", "", e);
                }
            });
        }

        @Override
        protected void failed() {
            LOGGER.error(exceptionProperty().get());
            Utils.showExceptionDialog("", "", "", exceptionProperty().get());
        }

        @Override
        protected Task<List<Contract>> createTask() {
            return FACADE.getContractsForJavaFileTask(model.getJavaFile());
        }
    }
    //endregion

    //region: StepIntoHandlers
    @RequiredArgsConstructor
    private class StepIntoHandler implements java.util.function.Consumer<PTreeNode<KeyData>> {
        private final PTreeNode<KeyData> original;

        @Override
        public void accept(PTreeNode<KeyData> keyDataPTreeNode) {
            Platform.runLater(this::acceptUI);
        }

        public void acceptUI() {
            model.getDebuggerFramework().getStatePointerListener().remove(this);
            GoalNode<KeyData> beforeNode = original.getStateBeforeStmt().getSelectedGoalNode();
            List<GoalNode<KeyData>> stateAfterStmt = original.getStateAfterStmt().getGoals();

            ProofTree ptree = new ProofTree();
            Proof proof = beforeNode.getData().getProof();

            Node pnode = beforeNode.getData().getNode();
            //      stateAfterStmt.forEach(keyDataGoalNode -> System.out.println("keyDataGoalNode.getData().getNode().serialNr() = " + keyDataGoalNode.getData().getNode().serialNr()));

            ptree.setProof(proof);
            ptree.setRoot(pnode);
            ptree.addNodeColor(pnode, "blueviolet");
            ptree.setDeactivateRefresh(true);

            if (stateAfterStmt.size() > 0) {
                Set<Node> sentinels = proof.getSubtreeGoals(pnode)
                        .stream()
                        .map(Goal::node)
                        .collect(Collectors.toSet());
                ptree.getSentinels().addAll(sentinels);
                sentinels.forEach(node -> ptree.addNodeColor(node, "blueviolet"));
            } else {
                Set<Node> sentinels = new HashSet<>();
                Iterator<Node> nodeIterator = beforeNode.getData().getNode().leavesIterator();
                while (nodeIterator.hasNext()) {
                    Node next = nodeIterator.next();

                    sentinels.add(next);
                }
                ptree.getSentinels().addAll(sentinels);
                sentinels.forEach(node -> ptree.addNodeColor(node, "blueviolet"));
                //traverseProofTreeAndAddSentinelsToLeaves();
            }


            ptree.expandRootToSentinels();
            DockNode node = new DockNode(ptree, "Proof Tree for Step Into: " +
                    original.getStatement().accept(new ShortCommandPrinter())
            );

            node.dock(dockStation, DockPos.CENTER, scriptController.getOpenScripts().get(getScriptController().getMainScript().getScriptArea()));
        }
    }

    @RequiredArgsConstructor
    private class StepIntoReverseHandler implements java.util.function.Consumer<PTreeNode<KeyData>> {
        private final PTreeNode<KeyData> original;

        @Override
        public void accept(PTreeNode<KeyData> keyDataPTreeNode) {
            Platform.runLater(this::acceptUI);
        }

        public void acceptUI() {
            model.getDebuggerFramework().getStatePointerListener().remove(this);
            PTreeNode<KeyData> stepInvOver = model.getDebuggerFramework().getStatePointer();
            GoalNode<KeyData> beforeNode = stepInvOver.getStateBeforeStmt().getSelectedGoalNode();
            List<GoalNode<KeyData>> stateAfterStmt = stepInvOver.getStateAfterStmt().getGoals();

            ProofTree ptree = new ProofTree();
            Proof proof = beforeNode.getData().getProof();

            Node pnode = beforeNode.getData().getNode();

            ptree.setProof(proof);
            ptree.setRoot(pnode);
            ptree.addNodeColor(pnode, "blueviolet");
            ptree.setDeactivateRefresh(true);

            if (stateAfterStmt.size() > 0) {
                Set<Node> sentinels = proof.getSubtreeGoals(pnode)
                        .stream()
                        .map(Goal::node)
                        .collect(Collectors.toSet());
                ptree.getSentinels().addAll(sentinels);
                sentinels.forEach(node -> ptree.addNodeColor(node, "blueviolet"));
            } else {
                Set<Node> sentinels = new HashSet<>();
                Iterator<Node> nodeIterator = beforeNode.getData().getNode().leavesIterator();
                while (nodeIterator.hasNext()) {
                    Node next = nodeIterator.next();

                    sentinels.add(next);
                }
                ptree.getSentinels().addAll(sentinels);
                sentinels.forEach(node -> ptree.addNodeColor(node, "blueviolet"));
                //traverseProofTreeAndAddSentinelsToLeaves();
            }


            ptree.expandRootToSentinels();
            DockNode node = new DockNode(ptree, "Proof Tree for Step Inverse Into: " +
                    original.getStatement().accept(new ShortCommandPrinter())
            );

            node.dock(dockStation, DockPos.CENTER, scriptController.getOpenScripts().get(getScriptController().getMainScript().getScriptArea()));

        }
    }

    //endregion
}
//deprecated
   /* @FXML
    public void executeScriptFromCursor() {
        InterpreterBuilder ib = FACADE.buildInterpreter();
        //b.inheritState(interpreterService.interpreterProperty().get());

        // Get State before cursor
        // use goalnode to build new interpreter in proof tree controller.
        //
        //LineMapping lm = new LineMapping(scriptArea.getText());
        //int line = lm.getLine(scriptArea.getCaretPosition());
        //int inLine = lm.getCharInLine(scriptArea.getCaretPosition());

        //ib.ignoreLinesUntil(scriptController.getSelectedScriptArea().getCaretPosition());
        //executeScript(ib, true);
    }*/
