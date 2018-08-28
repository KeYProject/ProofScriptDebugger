package edu.kit.iti.formal.psdbg.gui.controller;

import alice.tuprolog.InvalidLibraryException;
import alice.tuprolog.InvalidTheoryException;
import com.google.common.eventbus.Subscribe;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.uka.ilkd.key.api.ProofApi;
import de.uka.ilkd.key.control.KeYEnvironment;
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
import edu.kit.iti.formal.psdbg.gui.model.MainScriptIdentifier;
import edu.kit.iti.formal.psdbg.interpreter.InterpreterBuilder;
import edu.kit.iti.formal.psdbg.interpreter.KeYProofFacade;
import edu.kit.iti.formal.psdbg.interpreter.KeyInterpreter;
import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.data.SavePoint;
import edu.kit.iti.formal.psdbg.interpreter.data.State;
import edu.kit.iti.formal.psdbg.interpreter.dbg.*;
import edu.kit.iti.formal.psdbg.parser.ASTDiff;
import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;
import edu.kit.iti.formal.psdbg.parser.ast.ProofScript;
import edu.kit.iti.formal.psdbg.storage.KeyPersistentFacade;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dockfx.DockNode;
import org.dockfx.DockPane;
import org.dockfx.DockPos;
import org.key_project.util.collection.ImmutableList;
import org.reactfx.util.FxTimer;
import org.reactfx.util.Timer;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.Permission;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;


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

    @FXML
    public Menu menuExecuteFromSavepoint;

    @FXML
    public Menu menuRestartFromSavepoint;

    @FXML
    SplitMenuButton buttonStartInterpreter;

    ScriptController scriptController;
    @FXML
    ComboBox<SavePoint> cboSavePoints;
    @FXML
    Button btnSavePointRollback;
    private InspectionViewsController inspectionViewsController;
    private SavePointController savePointController;
    private InterpreterBuilder interpreterBuilder;
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
    private CheckMenuItem miScriptTree;
    @FXML
    private ToggleButton btnInteractiveMode;

    @FXML
    private Button interactive_undo;


    private JavaArea javaArea = new JavaArea();
    private DockNode javaAreaDock = new DockNode(javaArea, "Java Source",
            new MaterialDesignIconView(MaterialDesignIcon.CODEPEN)
    );
    //-----------------------------------------------------------------------------------------------------------------
    private ProofTree proofTree = new ProofTree(this);
    private DockNode proofTreeDock = new DockNode(proofTree, "Proof Tree");

    //TODO: anpassen
    private ScriptTreeGraph scriptTreeGraph = new ScriptTreeGraph();
    private ScriptTreeView scriptTreeView = new ScriptTreeView(this);
    private DockNode scriptTreeDock = new DockNode(scriptTreeView,"Script Tree");

    private WelcomePane welcomePane = new WelcomePane(this);
    private DockNode welcomePaneDock = new DockNode(welcomePane, "Welcome", new MaterialDesignIconView(MaterialDesignIcon.ACCOUNT));
    private DockNode activeInspectorDock;
    private CommandHelp commandHelp = new CommandHelp();
    private DockNode commandHelpDock = new DockNode(commandHelp, "DebuggerCommand Help");
    private InteractiveModeController interactiveModeController;
    private ScriptExecutionController scriptExecutionController;

    @FXML
    private Menu examplesMenu;
    private Timer interpreterThreadTimer;

    @Subscribe
    public void handle(Events.ShowPostMortem spm) {
        FindNearestASTNode fna = new FindNearestASTNode(spm.getPosition());
        List<PTreeNode<KeyData>> result =
                model.getDebuggerFramework().getPtreeManager().getNodes()
                        .stream()
                        .filter(it -> Objects.equals(fna.childOrMe(it.getStatement()), it.getStatement()))
                        .collect(Collectors.toList());

        System.out.println(result);


        for (PTreeNode<KeyData> statePointerToPostMortem : result) {
            if (statePointerToPostMortem != null && statePointerToPostMortem.getStateAfterStmt() != null) {

                State<KeyData> stateBeforeStmt = statePointerToPostMortem.getStateBeforeStmt();
                // stateBeforeStmt.getGoals().forEach(keyDataGoalNode -> System.out.println("BeforeSeq = " + keyDataGoalNode.getData().getNode().sequent()));
                State<KeyData> stateAfterStmt = statePointerToPostMortem.getStateAfterStmt();
                // stateAfterStmt.getGoals().forEach(keyDataGoalNode -> System.out.println("AfterSeq = " + keyDataGoalNode.getData().getNode().sequent()));

                /*List<GoalNode<KeyData>> list = stateAfterStmt.getGoals().stream().filter(keyDataGoalNode ->
                    keyDataGoalNode.getData().getNode().parent().equals(stateBeforeStmt.getSelectedGoalNode().getData().getNode())
                ).collect(Collectors.toList());

                list.forEach(keyDataGoalNode -> System.out.println("list = " + keyDataGoalNode.getData().getNode().sequent()));*/

                InspectionModel im = new InspectionModel();
                ObservableList<GoalNode<KeyData>> goals = FXCollections.observableArrayList(stateAfterStmt.getGoals());

                im.setGoals(goals);
                if (stateAfterStmt.getSelectedGoalNode() != null) {
                    im.setSelectedGoalNodeToShow(stateAfterStmt.getSelectedGoalNode());
                } else {
                    im.setSelectedGoalNodeToShow(goals.get(0));
                }
                inspectionViewsController.newPostMortemInspector(im)
                        .dock(dockStation, DockPos.CENTER, getActiveInspectorDock());

            } else {
                statusBar.publishErrorMessage("There is no post mortem state to show to this node, because this node was not executed.");
            }
        }
    }

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
                dockNode.requestFocus();
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
        // model.setDebugMode(false);
        scriptController = new ScriptController(dockStation);

        interactiveModeController = new InteractiveModeController(scriptController);
        btnInteractiveMode.setSelected(false);
        inspectionViewsController = new InspectionViewsController(dockStation);
        activeInspectorDock = inspectionViewsController.getActiveInterpreterTabDock();
        //register the welcome dock in the center
        welcomePaneDock.dock(dockStation, DockPos.LEFT);

        marriageJavaCode();

        getFacade().environmentProperty().addListener(
                (prop, o, n) -> {
                    scriptController.getAutoCompleter().getRuleCompleter().setEnvironment(n);
                });

        InvalidationListener invalidationListener = observable -> {
            Proof p = getFacade().getProof();
            KeYEnvironment env = getFacade().getEnvironment();
            if (p == null || env == null) return;
            ImmutableList<Goal> openGoals = p.getSubtreeGoals(p.root());
            KeyData kd = new KeyData(openGoals.get(0), env, p);
            scriptController.getAutoCompleter().getArgumentCompleter().setDefaultKeyData(kd);

        };
        getFacade().environmentProperty().addListener(invalidationListener);
        getFacade().proofProperty().addListener(invalidationListener);


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


        //if threadstate finished, stepping should still be possible
        BooleanBinding disableStepping = FACADE.loadingProperty().
                or(FACADE.proofProperty().isNull()).
                or(model.interpreterStateProperty().isNotEqualTo(InterpreterThreadState.WAIT));

        //set scriptareas to disable if loading is in process, as otherwise the scriptarea jumps
        FACADE.readyToExecuteProperty().addListener((observable, oldValue, newValue) -> {
            scriptController.disablePropertyForAreasProperty().set(!newValue);
        });


      /*  model.statePointerProperty().addListener((observable, oldValue, newValue) -> {

            //set all steppings -> remove binding
            if(newValue.getStepInvOver() != null)
                model.setStepReturnPossible(true);
            if(newValue.getStepOver() != null)
                model.setStepOverPossible(true);

            if(newValue.getStepInvInto() != null)
                model.setStepBackPossible(true);

            if(newValue.getStepInto() != null)
                model.setStepIntoPossible(true);

        });*/

        model.stepBackPossibleProperty().bind(disableStepping);
        model.stepIntoPossibleProperty().bind(disableStepping);
        model.stepOverPossibleProperty().bind(disableStepping);
        model.stepReturnPossibleProperty().bind(disableStepping);


        model.executeNotPossibleProperty().bind(FACADE.loadingProperty().or(FACADE.proofProperty().isNull()));

        statusBar.interpreterStatusModelProperty().bind(model.interpreterStateProperty());

        scriptExecutionController = new ScriptExecutionController(this);
        renewThreadStateTimer();

        savePointController = new SavePointController(this);
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
        if (node != null) {
            getInspectionViewsController().getActiveInspectionViewTab().activate(node, node.getStateBeforeStmt());
            scriptController.getDebugPositionHighlighter().highlight(node.getStatement());
        } else {
            getInspectionViewsController().getActiveInspectionViewTab().getFrames().getItems().clear();
            scriptController.getDebugPositionHighlighter().remove();
        }
        graph.addPartiallyAndMark(node);

    }

    /**
     * Connect the Javacode area with the model and the rest of the GUI
     */
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

    @FXML
    private void undo(ActionEvent e) {
        interactiveModeController.undo(e);
    }


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
                //if (dn.getLastDockPos() != null)
                //    dn.dock(dockStation, dn.getLastDockPos());
                //else
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
        //execute script without stepwise
        scriptExecutionController.executeScript(false);
        //executeScript(false);
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
        statusBar.publishMessage("Reloading...");
        File lastLoaded;
        if (model.getKeyFile() != null) {
            lastLoaded = model.getKeyFile();
        } else {
            Contract chosen = model.getChosenContract();
            lastLoaded = model.getJavaFile();
        }
        //model.reload();
        abortExecution();
        Platform.runLater(() -> model.setStatePointer(null));
        handleStatePointerUI(null);

        //reload getInspectionViewsController().getActiveInspectionViewTab().getModel()
        InspectionModel iModel = getInspectionViewsController().getActiveInspectionViewTab().getModel();
        //iModel.setHighlightedJavaLines(FXCollections.emptyObservableSet());
        iModel.clearHighlightLines();
        iModel.getGoals().clear();
        iModel.setSelectedGoalNodeToShow(null);

        try {
            FACADE.reload(lastLoaded);
            if (iModel.getGoals().size() > 0) {
                iModel.setSelectedGoalNodeToShow(iModel.getGoals().get(0));
            }
            if (FACADE.getReadyToExecute()) {
                LOGGER.info("Reloaded Successfully");
                statusBar.publishMessage("Reloaded Sucessfully");
            }
        } catch (ProofInputException e) {
            e.printStackTrace();
        } catch (ProblemLoaderException e) {
            e.printStackTrace();
        }


    }

    @FXML
    public void showAbout(ActionEvent event) {
        try {
            BorderPane content = FXMLLoader.load(AboutDialog.class.getResource("AboutDialog.fxml"));
            Dialog dialog = new Dialog();
            dialog.setTitle("About PSDBG");
            DialogPane pane = new DialogPane();
            pane.setContent(content);
            pane.getButtonTypes().add(ButtonType.OK);
            dialog.setDialogPane(pane);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setWidth(800);
            dialog.setHeight(600);
            dialog.setResizable(true);
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void onInterpreterSucceed(DebuggerFramework<KeyData> keyDataDebuggerFramework) {
        Platform.runLater(() -> {
            scriptController.getDebugPositionHighlighter().remove();
            statusBar.publishSuccessMessage("Interpreter finished.");
            btnInteractiveMode.setDisable(false);
            assert model.getDebuggerFramework() != null;
            btnInteractiveMode.setSelected(false);
            PTreeNode<KeyData> statePointer = model.getDebuggerFramework().getStatePointer();
            assert statePointer != null;
            State<KeyData> lastState = statePointer.getStateAfterStmt();
            getInspectionViewsController().getActiveInspectionViewTab().activate(statePointer, lastState);
            if (lastState.getGoals().isEmpty()) {
                statusBar.setNumberOfGoals(0);
                Utils.showClosedProofDialog("the script " + scriptController.getMainScript().getScriptName());
            } else {
                Utils.showOpenProofNotificationDialog(lastState.getGoals().size());
                //(lastState.getGoals().size());
            }

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
            Utils.showExceptionDialog("An error has occurred during execution.", "Error during Script Execution",
                    "Please reload the problem to get a consistent proof state.",
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
        //execute stepwise from start
        scriptExecutionController.executeScript(true);
        //executeScript(true);
    }


    public void createDebuggerFramework(Collection<? extends Breakpoint> breakpoints, ProofScript ms, boolean addInitBreakpoint, KeyInterpreter interpreter) {
        DebuggerFramework<KeyData> df = new DebuggerFramework<>(interpreter, ms);
        df.setSucceedListener(this::onInterpreterSucceed);
        df.setErrorListener(this::onInterpreterError);
        if (addInitBreakpoint) {
            df.releaseUntil(new Blocker.CounterBlocker(1)); // just execute
        }
        df.getBreakpoints().addAll(breakpoints);
        df.getStatePointerListener().add(this::handleStatePointer);
        df.start();
        model.setDebuggerFramework(df);
    }

    @FXML
    public void executeToBreakpoint() {
        Set<Breakpoint> breakpoints = scriptController.getBreakpoints();
        if (breakpoints.size() == 0) {
            statusBar.publishMessage("There was is no breakpoint set");
        }

        scriptExecutionController.executeScript(false);
        //executeScript(false);
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
                System.out.println("event.getSource().getMessage() = " + event.getSource().getMessage());
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




    @Deprecated
    public  void incremental() {
        ProofScript oldScript = null; //used to be global var, that was set in selectSavepoint()

        if (oldScript == null) {
            return;
        }
        ProofScript newScript = scriptController.getMainScript().byName(scriptController.getCombinedAST()).get();

        ASTDiff astDiff = new ASTDiff();
        ProofScript scriptdiff = astDiff.diff(oldScript, newScript);
        ASTNode pruneAstNode = scriptdiff.getBody().get(0);

        Set<PTreeNode<KeyData>> ptnodes = model.getDebuggerFramework().getPtreeManager().getNodes();
        Iterator iterator = ptnodes.iterator();
        while (iterator.hasNext()) {
            PTreeNode<KeyData> ptn = (PTreeNode<KeyData>) iterator.next();
            if(ptn.getStatement().eq(pruneAstNode)) {
                getFacade().getProof().pruneProof(ptn.getStateBeforeStmt().getSelectedGoalNode().getData().getNode());
                break;
            }
        }


        scriptController.setMainScript(oldScript);
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
            Utils.showWarningDialog("", "", "", e);
            LOGGER.error(e);
        }
    }

    @FXML
    public void abortExecution() {
        statusBar.publishMessage("Aborting Execution...");
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
                // urgently stop
                model.getDebuggerFramework().hardStop();
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            } finally {
                model.setDebuggerFramework(null);
                statusBar.publishMessage("Execution aborted.");
            }
        } else {
            LOGGER.info("no interpreter running");
        }
        assert model.getDebuggerFramework() == null;
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
        FileChooser fc = new FileChooser();
        File file = fc.showSaveDialog(btnInteractiveMode.getScene().getWindow());
        if (file != null) {
                saveScript(file);
        } else {
            Utils.showInfoDialog("","Select a destination", "No valid path has been selected.");
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
        File file = fc.showSaveDialog(btnInteractiveMode.getScene().getWindow());
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
            throw new RuntimeException("Something went wrong when reloading");
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
        if (btnInteractiveMode.isSelected()) {
            assert model.getDebuggerFramework() != null;
            interactiveModeController.setDebuggerFramework(model.getDebuggerFramework());
            interactiveModeController.setKeYServices(this.getFacade().getService());
            interactiveModeController.setActivated(true);

            interactiveModeController.start(getFacade().getProof(), getInspectionViewsController().getActiveInspectionViewTab().getModel());

            interactive_undo.setDisable(false);
        } else {
            interactiveModeController.stop();
            interactive_undo.setDisable(true);
        }
    }

    @FXML
    public void selectSavepoint(ActionEvent actionEvent) {
        //TODO remove highlight of SPs

        SavePoint selected = cboSavePoints.getValue();

        if (selected == null) {
            Utils.showInfoDialog("Select Savepoint", "Select Savepoint", "There is no selected Savepoint. Select a Savepoint first.");
            return;
        }

        //prune to savepoint if possible
        boolean pruneable = false;
        try{
            Iterator<PTreeNode<KeyData>> iterator = model.getDebuggerFramework().getPtreeManager().getNodes().iterator();
            PTreeNode<KeyData> pTreeNodeOfSave;
            while (iterator.hasNext()) {
                pTreeNodeOfSave = iterator.next();

                if (pTreeNodeOfSave.getStatement().getStartPosition().getLineNumber() == selected.getLineNumber()) {
                    FACADE.getProof().pruneProof(pTreeNodeOfSave.getStateBeforeStmt().getSelectedGoalNode().getData().getNode());
                    inspectionViewsController.getActiveInspectionViewTab().activate(pTreeNodeOfSave, pTreeNodeOfSave.getStateAfterStmt());
                    break;
                }
            }

            //TODO: refresh GUI (Context + Script)
            //State<KeyData> proofstate = KeyPersistentFacade.read(FACADE.getEnvironment(), FACADE.getProof(), new StringReader());

        } catch (Exception ex){

            //Hard Loading of Savepoint
            stopDebugMode(actionEvent);

            try {
                abortExecution();
            } catch (Exception e) {
                e.printStackTrace();
            }
            /**
             * reload with selected savepoint
             */
            Platform.runLater(() -> model.setStatePointer(null));
            handleStatePointerUI(null);
            //reload getInspectionViewsController().getActiveInspectionViewTab().getModel()
            InspectionModel iModel = getInspectionViewsController().getActiveInspectionViewTab().getModel();

            //iModel.setHighlightedJavaLines(FXCollections.emptyObservableSet());
            iModel.clearHighlightLines();
            iModel.getGoals().clear();
            iModel.setSelectedGoalNodeToShow(null);
            try {
                File dir = FACADE.getFilepath().getAbsoluteFile().getParentFile();
                File proofFile = selected.getProofFile(dir);
                FACADE.reload(proofFile);
                if (iModel.getGoals().size() > 0) {
                    iModel.setSelectedGoalNodeToShow(iModel.getGoals().get(0));
                }
                if (FACADE.getReadyToExecute()) {
                    LOGGER.info("Reloaded Successfully");
                    statusBar.publishMessage("Reloaded Sucessfully");
                }


            } catch (ProofInputException | ProblemLoaderException e) {
                LOGGER.error(e);
                Utils.showExceptionDialog("Loading Error", "Could not clear Environment",
                        "There was an error when clearing old environment",
                        e
                );
            }
        }

        //Update Gui
        MainScriptIdentifier msi = scriptController.getMainScript();
        msi.getScriptArea().setSavepointMarker(selected.getLineNumber());
        scriptController.getMainScript().getScriptArea().underlineSavepoint(selected);


        try {
            State state = KeyPersistentFacade.read(FACADE.getEnvironment(), FACADE.getProof(), new StringReader(selected.getPersistedStateFile(FACADE.getFilepath()).toString()));
            //TODO setstate
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        scriptExecutionController.executeScriptFromSavePoint(interpreterBuilder, selected);



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

    @FXML
    public void showScriptTree(ActionEvent actionEvent) {
        //TODO: anpassen
        if (!scriptTreeDock.isDocked() && !scriptTreeDock.isFloating()) {
            scriptTreeDock.dock(dockStation, DockPos.LEFT);
        }

        // old version of scripttree

        ScriptTreeGraph stg = new ScriptTreeGraph();
        PTreeNode startnode = (model.getDebuggerFramework() != null)?model.getDebuggerFramework().getPtreeManager().getStartNode():null;
        if(startnode == null) return;
        stg.createGraph(startnode, FACADE.getProof().root());

        TreeItem<TreeNode> item = (stg.toView());

        scriptTreeView.setTree(item);



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
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No proof is loaded yet. If loading a proof was invoked, proof state loading may take a while.", ButtonType.OK);
            alert.show();
            return;
        }

        SwingUtilities.invokeLater(() -> {
            SecurityManager secManager = new KeYSecurityManager();
            System.setSecurityManager(secManager);

            // Swing Thread!
            try {
                MainWindow keyWindow = MainWindow.getInstance();
                keyWindow.addProblem(new SingleProof(FACADE.getProof(), "script"));
                //keyWindow.getProofList().getModel().addProof();
                keyWindow.makePrettyView();
                keyWindow.setVisible(true);
            } catch (SecurityException e) {
                e.printStackTrace();
            }



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

            ProofTree ptree = new ProofTree(DebuggerMain.this);
            Proof proof = beforeNode.getData().getProof();

            Node pnode = beforeNode.getData().getNode();
            //      stateAfterStmt.forEach(keyDataGoalNode -> System.out.println("keyDataGoalNode.getData().getNode().serialNr() = " + keyDataGoalNode.getData().getNode().serialNr()));

            ptree.setProof(proof);
            ptree.setRoot(pnode);
            ptree.setNodeColor(pnode, "blueviolet");
            ptree.setDeactivateRefresh(true);

            if (stateAfterStmt.size() > 0) {
                Set<Node> sentinels = proof.getSubtreeGoals(pnode)
                        .stream()
                        .map(Goal::node)
                        .collect(Collectors.toSet());
                ptree.getSentinels().addAll(sentinels);
                sentinels.forEach(node -> ptree.setNodeColor(node, "blueviolet"));
            } else {
                Set<Node> sentinels = new HashSet<>();
                Iterator<Node> nodeIterator = beforeNode.getData().getNode().leavesIterator();
                while (nodeIterator.hasNext()) {
                    Node next = nodeIterator.next();

                    sentinels.add(next);
                }
                ptree.getSentinels().addAll(sentinels);
                sentinels.forEach(node -> ptree.setNodeColor(node, "blueviolet"));
                //traverseProofTreeAndAddSentinelsToLeaves();
            }


            ptree.expandRootToSentinels();
            DockNode node = new DockNode(ptree, "Proof Tree for Step Into: " +
                    original.getStatement().accept(new ShortCommandPrinter())
            );

            node.dock(dockStation, DockPos.CENTER, scriptController.getOpenScripts().get(getScriptController().getMainScript().getScriptArea()));
            node.requestFocus();
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

            ProofTree ptree = new ProofTree(DebuggerMain.this);
            Proof proof = beforeNode.getData().getProof();

            Node pnode = beforeNode.getData().getNode();
            //      stateAfterStmt.forEach(keyDataGoalNode -> System.out.println("keyDataGoalNode.getData().getNode().serialNr() = " + keyDataGoalNode.getData().getNode().serialNr()));

            ptree.setProof(proof);
            ptree.setRoot(pnode);
            ptree.setNodeColor(pnode, "blueviolet");
            ptree.setDeactivateRefresh(true);

            if (stateAfterStmt.size() > 0) {
                Set<Node> sentinels = proof.getSubtreeGoals(pnode)
                        .stream()
                        .map(Goal::node)
                        .collect(Collectors.toSet());
                ptree.getSentinels().addAll(sentinels);
                sentinels.forEach(node -> ptree.setNodeColor(node, "blueviolet"));
            } else {
                Set<Node> sentinels = new HashSet<>();
                Iterator<Node> nodeIterator = beforeNode.getData().getNode().leavesIterator();
                while (nodeIterator.hasNext()) {
                    Node next = nodeIterator.next();

                    sentinels.add(next);
                }
                ptree.getSentinels().addAll(sentinels);
                sentinels.forEach(node -> ptree.setNodeColor(node, "blueviolet"));
                //traverseProofTreeAndAddSentinelsToLeaves();
            }


            ptree.expandRootToSentinels();
            DockNode node = new DockNode(ptree, "Proof Tree for Step Inverse Into: " +
                    original.getStatement().accept(new ShortCommandPrinter())
            );

            node.dock(dockStation, DockPos.CENTER, scriptController.getOpenScripts().get(getScriptController().getMainScript().getScriptArea()));

        }
    }


    private class KeYSecurityManager extends SecurityManager {
        @Override public void checkExit(int status) {
            throw new SecurityException();
        }

        @Override public void checkPermission(Permission perm) {
            // Allow other activities by default
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

   /*  private void executeScript(boolean addInitBreakpoint) {
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No proof loaded is loaded yet. If proof loading was onvoked, please wait. Loading may take a while.", ButtonType.OK);
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
  /**
     * Execute the script that with using the interpreter that is build using the interpreterbuilder
     *
     * @param ib
     * @param
     */
/*   private void executeScript(InterpreterBuilder ib, boolean addInitBreakpoint) {
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
           executeScript0(ib, breakpoints, ms, addInitBreakpoint);
       } catch (RecognitionException e) {
           LOGGER.error(e);
           Utils.showExceptionDialog("Antlr Exception", "", "Could not parse scripts.", e);
       }

   }



    private void executeScriptFromSavePoint(InterpreterBuilder ib, SavePoint point) {
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
    } */