package edu.kit.iti.formal.psdbg.gui.controller;

import bibliothek.gui.Dockable;
import bibliothek.gui.dock.common.CContentArea;
import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.intern.CDockable;
import com.google.common.eventbus.Subscribe;
import de.uka.ilkd.key.control.KeYEnvironment;
import de.uka.ilkd.key.gui.MainWindow;
import de.uka.ilkd.key.proof.Goal;
import de.uka.ilkd.key.proof.Proof;
import de.uka.ilkd.key.proof.SingleProof;
import de.uka.ilkd.key.proof.init.ProofInputException;
import de.uka.ilkd.key.proof.io.ProblemLoaderException;
import de.uka.ilkd.key.speclang.Contract;
import edu.kit.iti.formal.psdbg.ShortCommandPrinter;
import edu.kit.iti.formal.psdbg.examples.Examples;
import edu.kit.iti.formal.psdbg.gui.ProofScriptDebugger;
import edu.kit.iti.formal.psdbg.gui.model.DebuggerMainModel;
import edu.kit.iti.formal.psdbg.gui.model.InspectionModel;
import edu.kit.iti.formal.psdbg.gui.model.InterpreterThreadState;
import edu.kit.iti.formal.psdbg.interpreter.InterpreterBuilder;
import edu.kit.iti.formal.psdbg.interpreter.KeYProofFacade;
import edu.kit.iti.formal.psdbg.interpreter.KeyInterpreter;
import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.data.SavePoint;
import edu.kit.iti.formal.psdbg.interpreter.data.State;
import edu.kit.iti.formal.psdbg.interpreter.dbg.*;
import edu.kit.iti.formal.psdbg.parser.ast.ProofScript;
import edu.kit.iti.formal.psdbg.util.FindNearestASTNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.key_project.util.collection.ImmutableList;

import javax.annotation.Nullable;
import javax.swing.Timer;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.security.Permission;
import java.util.List;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;


/**
 * Controller for the Debugger MainWindow
 *
 * @author S.Grebing
 * @author Alexander Weigl
 */
public class DebuggerMain extends JFrame {
    public static final KeYProofFacade FACADE = new KeYProofFacade();

    protected static final Logger LOGGER = LogManager.getLogger(DebuggerMain.class);

    public final ContractLoaderService contractLoaderService = new ContractLoaderService();

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Getter
    private final DebuggerMainModel model = new DebuggerMainModel();
    /*TODO LATER:
    private final GraphView graphView = new GraphView();
    private final Graph.PTreeGraph graph = graphView.getGraph();
    private final Dockable graphViewNode = new DockNode(graphView, "Debug graph");
     */

    private JMenuItem menuExecuteFromSavepoint;
    private JMenuItem menuRestartFromSavepoint;
    private JButton buttonStartInterpreter;

//TODO     private ScriptController scriptController;

    private JComboBox<SavePoint> cboSavePoints;

    private JButton btnSavePointRollback;
    //TODO private InspectionViewsController inspectionViewsController;
    private SavePointController savePointController;
    private InterpreterBuilder interpreterBuilder;
    //private DebuggerStatusBar statusBar;
    private JPanel statusBar;
    private JToggleButton togBtnCommandHelp;
    private JToggleButton togBtnProofTree;
    private JToggleButton togBtnActiveInspector;
    private JToggleButton togBtnWelcome;
    private JToggleButton togBtnCodeDock;
    private JCheckBoxMenuItem miCommandHelp;
    private JCheckBoxMenuItem miCodeDock;
    private JCheckBoxMenuItem miWelcomeDock;
    private JCheckBoxMenuItem miActiveInspector;
    private JCheckBoxMenuItem miProofTree;
    private JToggleButton btnInteractiveMode;
    private JButton interactive_undo;

    private JTextArea javaArea = new JTextArea();
    //private JavaArea javaArea = new JavaArea();
    private Dockable javaAreaDock;
    private JPanel proofTree = new JPanel();
    //private ProofTree proofTree = new ProofTree(this);
    private CDockable proofTreeDock = create(proofTree, "Proof Tree");
    private JPanel welcomePane = new JPanel();
    //private WelcomePane welcomePane = new WelcomePane(this);
    private CDockable welcomePaneDock = create(welcomePane, "Welcome");
    private CDockable activeInspectorDock;
    private JPanel commandHelp = new JPanel();
    //private CommandHelp commandHelp = new CommandHelp();
    private CDockable commandHelpDock = create(commandHelp, "DebuggerCommand Help");
    private InteractiveModeController interactiveModeController;
    private ScriptExecutionController scriptExecutionController;
    private JMenu examplesMenu = new JMenu();
    private Timer interpreterThreadTimer;
    private CControl dockControl;
    private CContentArea dockContent;

    public static DefaultSingleCDockable create(JPanel content, String title) {
        return new DefaultSingleCDockable(title, content);
    }

    @Subscribe
    public void handle(Events.ShowPostMortem spm) {
        FindNearestASTNode fna = new FindNearestASTNode(spm.getPosition());
        List<PTreeNode<KeyData>> result =
                model.getDebuggerFramework().getPtreeManager().getNodes()
                        .stream()
                        .filter(it -> Objects.equals(fna.childOrMe(it.getStatement()), it.getStatement()))
                        .collect(Collectors.toList());

        LOGGER.info(result);


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
                List<GoalNode<KeyData>> goals = new ArrayList<>(stateAfterStmt.getGoals());

                im.setGoals(goals);
                if (stateAfterStmt.getSelectedGoalNode() != null) {
                    im.setSelectedGoalNodeToShow(stateAfterStmt.getSelectedGoalNode());
                } else {
                    if (goals.size() > 0) {
                        im.setSelectedGoalNodeToShow(goals.get(0));
                    } else {
                        im.setSelectedGoalNodeToShow(stateBeforeStmt.getSelectedGoalNode());
                        //TODO statusBar.publishMessage("This goal node was closed by the selected mutator.");
                    }
                }

                //TODO CDockable ivc = inspectionViewsController.newPostMortemInspector(im);
            } else {
                //TODO statusBar.publishErrorMessage("There is no post mortem state to show to this node, because this node was not executed or is a selector statement.");
            }
        }

    }

    @Subscribe
    public void handle(Events.ShowSequent ss) {
        //TODO SequentView sv = new SequentView();
        //sv.setKeYProofFacade(FACADE);
        //sv.setNode(ss.getNode());
        JPanel sv = new JPanel();
        var node = create(sv, "Sequent Viewer " + ss.getNode().serialNr());
        dockControl.addDockable(node);
    }

    @Subscribe
    public void handle(Events.PublishMessage message) {
        /*TODO if (message.getFlash() == 0)
            statusBar.publishMessage(message.getMessage());
        else if (message.getFlash() == 1)
            statusBar.publishSuccessMessage(message.getMessage());
        else
            statusBar.publishErrorMessage(message.getMessage());*/
    }

    @Subscribe
    public void handle(Events.SelectNodeInGoalList evt) {
        /*TODO
        InspectionModel im = getInspectionViewsController().getActiveInspectionViewTab().getModel();
        var dockNode = getInspectionViewsController().newPostMortemInspector(im);
        dockControl.addDockable(dockNode);
        dockNode.setVisible(true);
        for (GoalNode<KeyData> gn : im.getGoals()) {
            if (gn.getData().getNode().equals(evt.getNode())) {
                im.setSelectedGoalNodeToShow(gn);
                //dockNode.requestFocus();
                return;
            }
        }
        statusBar.publishErrorMessage("Node not present in Goal List");
        */
    }

    private void setupUI() {
        setupDocking();
        //TODO scriptController = new ScriptController(dockControl);
        //TODO interactiveModeController = new InteractiveModeController(scriptController);
        btnInteractiveMode.setSelected(false);
        //TODO inspectionViewsController = new InspectionViewsController(dockControl);
        //TODO activeInspectorDock = inspectionViewsController.getActiveInterpreterTabDock();
        //register the welcome dock in the center

        marriageJavaCode();

        //TODO getFacade().addPropertyChangeListener("environment", evt ->
        //TODO         scriptController.getAutoCompleter().getRuleCompleter().setEnvironment((KeYEnvironment) evt.getNewValue()));

        PropertyChangeListener proofReload = evt -> {
            Proof p = (Proof) evt.getNewValue();
            KeYEnvironment env = getFacade().getEnvironment();
            if (p == null || env == null) return;
            ImmutableList<Goal> openGoals = p.getSubtreeGoals(p.root());
            KeyData kd = new KeyData(openGoals.get(0), env, p);
            //TODO scriptController.getAutoCompleter().getArgumentCompleter().setDefaultKeyData(kd);
        };
        getFacade().addPropertyChangeListener("proof", proofReload);

        //marriage key proof facade to proof tree
        getFacade().addPropertyChangeListener("proof", evt -> {
                    Proof n = (Proof) evt.getNewValue();
                    if (n == null) {
                        //TODO proofTree.setRoot(null);
                    } else {
                        /*TODO proofTree.setRoot(n.root());
                        InspectionViewsController inspectionViewsController = getInspectionViewsController();
                        InspectionView activeInspectionViewTab = inspectionViewsController.getActiveInspectionViewTab();
                        InspectionModel model = activeInspectionViewTab.getModel();
                        List<GoalNode<KeyData>> goals = model.getGoals();
                        goals.clear();
                        goals.addAll(FACADE.getPseudoGoals());
                        model.setSelectedGoalNodeToShow(null);

                        // frames / contextes zurück setzen
                        activeInspectionViewTab.getModel();
                        scriptController.getOpenScripts()
                                .keys()
                                .forEach(ScriptArea::removeExecutionMarker);
                    */
                    }
                    //TODO proofTree.setProof(n);
                }
        );

        //
        /*TODO model.statePointerProperty().addListener((prop, o, n) -> {
            this.handleStatePointerUI(n);
        });
        */
        //Debugging
        /*TODO scriptController.mainScriptProperty().bindBidirectional(statusBar.mainScriptIdentifierProperty());*/
        initializeExamples();

        //if threadstate finished, stepping should still be possible
        /*TODO BooleanBinding disableStepping = FACADE.loadingProperty().
                or(FACADE.proofProperty().isNull()).
                or(model.interpreterStateProperty().isNotEqualTo(InterpreterThreadState.WAIT));
*/
        //set scriptareas to disable if loading is in process, as otherwise the scriptarea jumps
  /*TODO       FACADE.readyToExecuteProperty().addListener((observable, oldValue, newValue) -> {
            scriptController.disablePropertyForAreasProperty().set(!newValue);
        });
*/
    /*TODO
        model.stepBackPossibleProperty().bind(disableStepping);
        model.stepIntoPossibleProperty().bind(disableStepping);
        model.stepOverPossibleProperty().bind(disableStepping);
        model.stepReturnPossibleProperty().bind(disableStepping);

        model.executeNotPossibleProperty().bind(FACADE.loadingProperty().or(FACADE.proofProperty().isNull()));

        statusBar.interpreterStatusModelProperty().bind(model.interpreterStateProperty());

        scriptExecutionController = new ScriptExecutionController(this);
        renewThreadStateTimer();
                */


        savePointController = new SavePointController(this);
        Events.register(this);
    }

    private void setupDocking() {
        dockControl = new CControl(this);
        dockContent = dockControl.createContentArea("main");
        //TODO dockControl.addDockable(javaAreaDock);
        //TODO welcomePaneDock.dock(dockStation, DockPos.LEFT);
        /*//TODO
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
*/
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
     * You should listen to the {@link DebuggerMainModel#}
     *
     * @param node
     */
    private void handleStatePointer(PTreeNode<KeyData> node) {
        SwingUtilities.invokeLater(() -> model.setStatePointer(node));
    }

    /**
     * Handling of a new state in the {@link DebuggerFramework}, now in the JavaFX Thread
     *
     * @see {@link #handleStatePointer(PTreeNode)}
     */
    private void handleStatePointerUI(PTreeNode<KeyData> node) {
        if (node != null) {
            //TODO getInspectionViewsController().getActiveInspectionViewTab().activate(node, node.getStateBeforeStmt());
            //TODO scriptController.getDebugPositionHighlighter().highlight(node.getStatement());
        } else {
            //TODO getInspectionViewsController().getActiveInspectionViewTab().getFrames().getItems().clear();
            //TODO scriptController.getDebugPositionHighlighter().remove();
        }
        //TODO graph.addPartiallyAndMark(node);
    }

    /**
     * Connect the Javacode area with the model and the rest of the GUI
     */
    private void marriageJavaCode() {
        /*TODO
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
        */
    }


    private void undo(ActionEvent e) {
        interactiveModeController.undo(e);
    }

    public KeYProofFacade getFacade() {
        return FACADE;
    }

    private void initializeExamples() {
        examplesMenu.removeAll();
        Examples.loadExamples().forEach(example -> {
            JMenuItem item = new JMenuItem(example.getName());
            item.addActionListener(event -> example.open(this));
            examplesMenu.add(item);
        });
    }

    public void showCodeDock(ActionEvent actionEvent) {
        /*TODOif (!javaAreaDock.isDocked()) {
            javaAreaDock.dock(dockStation, DockPos.RIGHT);
        }*/
    }

    public void dockingNodeHandling(JToggleButton btn, JCheckBoxMenuItem cmi, CDockable dn,
                                    int defaultPosition) {
        /*BooleanBinding prop = dn.dockedProperty().or(dn.floatingProperty());
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
        cmi.setOnAction(handler);*/
    }


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

    public void reloadProblem(ActionEvent event) {
        //abort current execution();
        //save old information and refresh models
        //TODO statusBar.publishMessage("Reloading...");
        File lastLoaded;
        Contract chosen = null;
        if (model.getKeyFile() != null) {
            lastLoaded = model.getKeyFile();
        } else {
            chosen = model.getChosenContract();
            lastLoaded = model.getJavaFile();
        }
        //model.reload();
        abortExecution();
        SwingUtilities.invokeLater(() -> model.setStatePointer(null));
        handleStatePointerUI(null);

        //reload getInspectionViewsController().getActiveInspectionViewTab().getModel()
        //TODO InspectionModel iModel = getInspectionViewsController().getActiveInspectionViewTab().getModel();
        //iModel.setHighlightedJavaLines(FXCollections.emptyObservableSet());
        //TODO iModel.clearHighlightLines();
        //TODO iModel.getGoals().clear();
        //TODO iModel.setSelectedGoalNodeToShow(null);
        if (chosen != null) {
            FACADE.setContract(chosen);
        }
        try {

            FACADE.reload(lastLoaded);
            /*//TODO if (iModel.getGoals().size() > 0) {
                iModel.setSelectedGoalNodeToShow(iModel.getGoals().get(0));
            }
            if (FACADE.getReadyToExecute()) {
                LOGGER.info("Reloaded Successfully");
                statusBar.publishMessage("Reloaded Sucessfully");
            }*/
        } catch (ProofInputException e) {
            e.printStackTrace();
        } catch (ProblemLoaderException e) {
            e.printStackTrace();
        }


    }

    public void showAbout(ActionEvent event) {
        try {
            JPanel content = new JPanel();
            Box root = new Box(BoxLayout.Y_AXIS);
            JLabel lblProgram, lblSubTitle;
            root.add(lblProgram = new JLabel("${controller.programName}"));
            lblProgram.setFont(new Font("System", Font.BOLD, 24));
            root.add(lblSubTitle = new JLabel("${controller.subtitle}"));
            lblSubTitle.setFont(new Font("System", Font.ITALIC, 18));
            content.add(root, BorderLayout.NORTH);
            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.addTab("About PSDBG", new JEditorPane("${controller.aboutText}"));
            tabbedPane.addTab("License", new JEditorPane("${controller.license}"));
            tabbedPane.addTab("KeY License", new JEditorPane("${controller.keyLicense}"));
            tabbedPane.addTab("Third Party License", new JEditorPane("${controller.thirdPartyLicense}"));
            content.add(tabbedPane, BorderLayout.CENTER);

            JDialog dialog = new JDialog();
            dialog.setTitle("About PSDBG");
            dialog.setContentPane(root);
            dialog.setSize(800, 600);
            dialog.setResizable(true);
            dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void onInterpreterSucceed(DebuggerFramework<KeyData> keyDataDebuggerFramework) {
        SwingUtilities.invokeLater(() -> {
            //TODO scriptController.getDebugPositionHighlighter().remove();
            //TODO statusBar.publishSuccessMessage("Interpreter finished.");
            //TODO btnInteractiveMode.setDisable(false);
            assert model.getDebuggerFramework() != null;
            btnInteractiveMode.setSelected(false);
            PTreeNode<KeyData> statePointer = model.getDebuggerFramework().getStatePointer();
            assert statePointer != null;
            State<KeyData> lastState = statePointer.getStateAfterStmt();
            //TODO getInspectionViewsController().getActiveInspectionViewTab().activate(statePointer, lastState);
            if (lastState.getGoals().isEmpty()) {
                //TODO statusBar.setNumberOfGoals(0);
                //TODO Utils.showClosedProofDialog("the script " + scriptController.getMainScript().getScriptName());
            } else {
                //TODO Utils.showOpenProofNotificationDialog(lastState.getGoals().size());
                //(lastState.getGoals().size());
            }

        });
    }


    public void debugGraph(ActionEvent event) {
        /*//TODO
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
    */
    }


    public void debugPrintDot(@Nullable ActionEvent ae) {
        if (model.getDebuggerFramework() == null) {
            //TODO statusBar.publishErrorMessage("can print debug info, no debugger started!");
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
            //TODO statusBar.publishErrorMessage(e.getMessage());
        }
    }

    private void onInterpreterError(DebuggerFramework<KeyData> keyDataDebuggerFramework, Throwable throwable) {
        SwingUtilities.invokeLater(() -> {
            /*TODO Utils.showExceptionDialog("An error has occurred during execution.", "Error during Script Execution",
                    "Please reload the problem to get a consistent proof state.",
                    throwable
            );*/
        });
    }

    private void renewThreadStateTimer() {
        if (interpreterThreadTimer != null) {
            interpreterThreadTimer.stop();
        }
        interpreterThreadTimer = new Timer(500, (evt) -> {
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
        interpreterThreadTimer.setRepeats(true);
        interpreterThreadTimer.start();
    }


    public void executeStepwise() {
        //execute stepwise from start
        scriptExecutionController.executeScript(true);
        //executeScript(true);
    }


    public void createDebuggerFramework(Collection<? extends Breakpoint> breakpoints, ProofScript ms, boolean addInitBreakpoint, KeyInterpreter interpreter) {
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
    }


    public void executeToBreakpoint() {
        /*TODO  Set<Breakpoint> breakpoints = scriptController.getBreakpoints();
        if (breakpoints.size() == 0) {
            statusBar.publishMessage("There was is no breakpoint set");
        }

        scriptExecutionController.executeScript(false);
        */
        //executeScript(false);
    }

    public void openKeyFile(File keyFile) {
        if (keyFile != null) {
            model.setKeyFile(keyFile);
            model.setInitialDirectory(keyFile.getParentFile());

            /*//TODO Task<ProofApi> task = FACADE.loadKeyFileTask(keyFile);
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
            */
        }
    }

    public void openJavaFile(File javaFile) {
        if (javaFile != null) {
            model.setJavaFile(javaFile);
            model.setInitialDirectory(javaFile.getParentFile());
            //TODO contractLoaderService.reset();
            //TODO contractLoaderService.start();
        }
    }


    public void loadKeYFile() {
        File keyFile = openFileChooserOpenDialog("Select KeY File", "KeY Files", "key", "kps");
        openKeyFile(keyFile);
    }

    public void openJavaFile() {
        loadJavaFile();
        showCodeDock(null);
    }


    protected void loadJavaFile() {
        File javaFile = openFileChooserOpenDialog("Select Java File", "Java Files", "java");
        openJavaFile(javaFile);
    }

    private File openFileChooserOpenDialog(String title, String description, String... fileEndings) {
        JFileChooser fileChooser = getFileChooser(title, description, fileEndings);
        //File sourceName = fileChooser.showOpenDialog(inspectionViewsController.getInspectionViewTab().getGoalView().getScene().getWindow());
        var c = fileChooser.showOpenDialog(this);
        if (c == JFileChooser.APPROVE_OPTION) {
            var file = fileChooser.getSelectedFile();
            if (file != null) model.setInitialDirectory(file.getParentFile());
            return file;
        }
        return null;
    }

    private JFileChooser getFileChooser(String title, String description, String[] fileEndings) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(title);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return Arrays.stream(fileEndings).anyMatch(s -> f.getName().endsWith(s));
            }

            @Override
            public String getDescription() {
                return description;
            }
        });
        if (model.getInitialDirectory() == null)
            model.setInitialDirectory(new File("src/test/resources/edu/kit/formal/interpreter/contraposition/"));

        if (!model.getInitialDirectory().exists())
            model.setInitialDirectory(new File("."));

        fileChooser.setCurrentDirectory(model.getInitialDirectory());
        return fileChooser;
    }


    public void executeDiff() {
        //save old PT


        // Calculate top difference between current and last executed script
        // Find last state of common ASTNode
        // Use this state to build new interpreter in proof tree controller.
        // execute residual script


    }


    @Deprecated
    public void incremental() {
        ProofScript oldScript = null; //used to be global var, that was set in selectSavepoint()

        if (oldScript == null) {
            return;
        }
        /*TODO ProofScript newScript = scriptController.getMainScript().byName(scriptController.getCombinedAST()).get();

        ASTDiff astDiff = new ASTDiff();
        ProofScript scriptdiff = astDiff.diff(oldScript, newScript);
        ASTNode pruneAstNode = scriptdiff.getBody().get(0);

        Set<PTreeNode<KeyData>> ptnodes = model.getDebuggerFramework().getPtreeManager().getNodes();
        Iterator iterator = ptnodes.iterator();
        while (iterator.hasNext()) {
            PTreeNode<KeyData> ptn = (PTreeNode<KeyData>) iterator.next();
            if (ptn.getStatement().eq(pruneAstNode)) {
                getFacade().getProof().pruneProof(ptn.getStateBeforeStmt().getSelectedGoalNode().getData().getNode());
                break;
            }
        }


        scriptController.setMainScript(oldScript);
        */
    }

    /**
     * Continue after the interpreter has reached a breakpoint
     *
     * @param event
     */

    public void continueAfterRun(ActionEvent event) {
        LOGGER.debug("DebuggerMain.continueAfterBreakpoint");
        try {
            assert model.getDebuggerFramework() != null : "You should have started the prove";
            model.getDebuggerFramework().execute(new ContinueCommand<>());
        } catch (DebuggerException e) {
            //TODO Utils.showWarningDialog("", "", "", e);
            LOGGER.error(e);
        }
    }


    public void abortExecution() {
        //TODO statusBar.publishMessage("Aborting Execution...");
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
                //TODO statusBar.publishMessage("Execution aborted.");
            }
        } else {
            LOGGER.info("no interpreter running");
        }
        assert model.getDebuggerFramework() == null;
    }


    public void closeProgram() {
        System.exit(0);
    }

/*    public void openJavaFile() {
        loadJavaFile();
        showCodeDock(null);
    }
*/


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
            //TODO ScriptArea area = scriptController.createNewTab(scriptFile);
        } catch (IOException e) {
            LOGGER.error(e);
            /*TODO Utils.showExceptionDialog("Exception occured", "",
                    "Could not load sourceName " + scriptFile, e);
                    */
        }
    }


    public void saveScript() {
        //TODO scriptController.saveCurrentScript();
        //TODO LOGGER.error(e);
        //TODO Utils.showExceptionDialog("Could not save file", "Saving File Error", "Could not save current script", e);
    }


    public void saveAsScript() throws IOException {
        JFileChooser fc = getFileChooser("Save as...", "Proof Script", new String[]{"kps"});
        int c = fc.showSaveDialog(this);
        if (c == JFileChooser.APPROVE_OPTION) {
            var file = fc.getSelectedFile();
            if (file != null) {
                saveScript(file);
            } else {
                //TODO Utils.showInfoDialog("", "Select a destination", "No valid path has been selected.");
            }
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
        JFileChooser fileChooser = getFileChooser(title, description, fileEndings);
        // File sourceName = fileChooser.showSaveDialog(inspectionViewsController.getInspectionViewTab().getGoalView().getScene().getWindow());
        int c = fileChooser.showOpenDialog(this);
        if (c == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (file != null) model.setInitialDirectory(file.getParentFile());
            return file;
        }
        return null;
    }
    //endregion

    private void saveScript(File scriptFile) {
        //TODO scriptController.saveCurrentScriptAs(scriptFile);
        //TODO LOGGER.error(e);
        //TODO Utils.showExceptionDialog("Could not save file", "Saving File Error", "Could not save to file " + scriptFile.getName(), e);
    }


    /**
     * Save KeY proof as proof file
     *
     * @param actionEvent
     */
    public void saveProof(ActionEvent actionEvent) {
        var fc = getFileChooser("Save Proof", "Proof files", new String[]{"kps"});
        var c = fc.showSaveDialog(this);
        if (c == JFileChooser.APPROVE_OPTION) {
            var file = fc.getSelectedFile();
            if (file != null) {
                try {
                    saveProof(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
            //TODO Utils.showExceptionDialog("", "", "", e);
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
            //TODO Utils.showExceptionDialog("", "", "", e);
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
            //TODO Utils.showExceptionDialog("", "", "", e);
            LOGGER.error(e);
        }
    }

    public void stepOverReverse(ActionEvent actionEvent) {
        LOGGER.debug("DebuggerMain.stepBack");
        try {
            model.getDebuggerFramework().execute(new StepOverReverseCommand<>());
        } catch (DebuggerException e) {
            //TODO Utils.showExceptionDialog("", "", "", e);
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
            //TODO Utils.showExceptionDialog("", "", "", e);
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
        /*TODO scriptController.getDebugPositionHighlighter().remove();
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
        */
    }

    public void newScript(ActionEvent actionEvent) {
        //TODO scriptController.newScript();
    }

    public void interactiveMode(ActionEvent actionEvent) {
        if (btnInteractiveMode.isSelected()) {
            assert model.getDebuggerFramework() != null;
            interactiveModeController.setDebuggerFramework(model.getDebuggerFramework());
            interactiveModeController.setKeYServices(this.getFacade().getService());
            interactiveModeController.setActivated(true);

            //TODO interactiveModeController.start(getFacade().getProof(),
            //        getInspectionViewsController().getActiveInspectionViewTab().getModel());

            //TODO interactive_undo.setDisable(false);
        } else {
            interactiveModeController.stop();
            //TODO interactive_undo.setDisable(true);
        }
    }


    public void selectSavepoint(ActionEvent actionEvent) {
        //TODO remove highlight of SPs

        /*TODO SavePoint selected = cboSavePoints.getValue();

        if (selected == null) {
            //TODO Utils.showInfoDialog("Select Savepoint", "Select Savepoint", "There is no selected Savepoint. Select a Savepoint first.");
            return;
        }
        */

        //prune to savepoint if possible
        boolean pruneable = false;
        try {
            Iterator<PTreeNode<KeyData>> iterator = model.getDebuggerFramework().getPtreeManager().getNodes().iterator();
            PTreeNode<KeyData> pTreeNodeOfSave;
            while (iterator.hasNext()) {
                pTreeNodeOfSave = iterator.next();

                /*TODO  if (pTreeNodeOfSave.getStatement().getStartPosition().getLineNumber() == selected.getLineNumber()) {
                    FACADE.getProof().pruneProof(pTreeNodeOfSave.getStateBeforeStmt().getSelectedGoalNode().getData().getNode());
                    inspectionViewsController.getActiveInspectionViewTab().activate(pTreeNodeOfSave, pTreeNodeOfSave.getStateAfterStmt());
                    break;
                }
                */
            }

            //TODO: refresh GUI (Context + Script)
            //State<KeyData> proofstate = KeyPersistentFacade.read(FACADE.getEnvironment(), FACADE.getProof(), new StringReader());

        } catch (Exception ex) {

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
            SwingUtilities.invokeLater(() -> model.setStatePointer(null));
            handleStatePointerUI(null);
            //reload getInspectionViewsController().getActiveInspectionViewTab().getModel()
            //TODO InspectionModel iModel = getInspectionViewsController().getActiveInspectionViewTab().getModel();
            //TODO iModel.clearHighlightLines();
            //TODO iModel.getGoals().clear();
            //TODO iModel.setSelectedGoalNodeToShow(null);
            File dir = FACADE.getFilepath().getAbsoluteFile().getParentFile();
                /*TODO File proofFile = selected.getProofFile(dir);
                FACADE.reload(proofFile);
                if (iModel.getGoals().size() > 0) {
                    iModel.setSelectedGoalNodeToShow(iModel.getGoals().get(0));
                }
                if (FACADE.getReadyToExecute()) {
                    LOGGER.info("Reloaded Successfully");
                    statusBar.publishMessage("Reloaded Sucessfully");
                }
                */

        }

        //Update Gui
        //TODO MainScriptIdentifier msi = scriptController.getMainScript();
        //TODO msi.getScriptArea().setSavepointMarker(selected.getLineNumber());
        //TODO scriptController.getMainScript().getScriptArea().underlineSavepoint(selected);

        /*TODO
        try {
            KeyPersistentFacade.read(FACADE.getEnvironment(), FACADE.getProof(),
                    new StringReader(selected.getPersistedStateFile(FACADE.getFilepath()).toString()));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        scriptExecutionController.executeScriptFromSavePoint(interpreterBuilder, selected);
        */
    }


    public void showWelcomeDock(ActionEvent actionEvent) {
        //TODO if (!welcomePaneDock.isDocked() && !welcomePaneDock.isFloating()) {
        // TODO     welcomePaneDock.dock(dockStation, DockPos.CENTER);
        //TODO }
    }


    public void showActiveInspector(ActionEvent actionEvent) {
        /*TODO if (!activeInspectorDock.isDocked() &&
            !activeInspectorDock.isFloating()) {
            activeInspectorDock.dock(dockStation, DockPos.RIGHT);
        }*/
    }


    public void showCommandHelp(ActionEvent event) {
        /*TODO if (!commandHelpDock.isDocked() && !commandHelpDock.isFloating()) {
            commandHelpDock.dock(dockStation, DockPos.LEFT);
        }*/
    }


    public void showProofTree(ActionEvent actionEvent) {
       /*TODO  if (!proofTreeDock.isDocked() && !proofTreeDock.isFloating()) {
            proofTreeDock.dock(dockStation, DockPos.CENTER);
        }
        */
    }

    public void showHelpText() {
        String url = ProofScriptDebugger.class.getResource("intro.html").toExternalForm();
        showHelpText(url);
    }

    public void showHelpText(String url) {
        /*WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.load(url);
        DockNode dn = new DockNode(browser);
        dn.setTitle("ScriptLanguage Description");
        //this.dockStation.getChildren().addCell(dn);
        dn.dock(dockStation, DockPos.LEFT);*/
    }

    public void openNewHelpDock(String title, String content) {
        /*TODO WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.loadContent(content);
        DockNode dn = new DockNode(browser, title);
        dn.dock(dockStation, DockPos.LEFT);
        */
    }

    public void reformatCurrentEditor(ActionEvent event) {
        /*TODO scriptController.getOpenScripts().values().forEach(ed -> {
            try {
                DefaultFormatter df = new DefaultFormatter();
                ((ScriptArea) ed.getContents()).setText(
                        df.format(((ScriptArea) ed.getContents()).getText()));
            } catch (InvalidLibraryException | IOException | InvalidTheoryException e) {
                LOGGER.debug(e);
            }
        });
   */
    }

    public void openInKey(@Nullable ActionEvent event) {
        if (FACADE.getProofState() == KeYProofFacade.ProofState.EMPTY) {
            /*TODO Alert alert = new Alert(Alert.AlertType.INFORMATION, "No proof is loaded yet. If loading a proof was invoked, proof state loading may take a while.", ButtonType.OK);
            alert.show();
            */
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

    public class ContractLoaderService extends SwingWorker<List<Contract>, Integer> {
        @Override
        protected void done() {
            //TODO statusBar.publishMessage("Contract loaded");
            try {
                List<Contract> contracts = get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            /*
            String javaFile = model.getJavaFile().getName();

            List<Contract> filteredContracts = new ArrayList<>();
            for (Contract contract : contracts) {
                String contractFile = contract.getKJT().getFullName();
                if (javaFile == contractFile) {
                    filteredContracts.add(contract);
                }
            }

            if (filteredContracts.size() == 0) {
               Utils.showInfoDialog("No loadable contract", "No loadable contract",
                       "There's no loadable contract for the chosen java file.");
                return;
            }
            */

            ContractChooser cc = null;
            try {
                cc = new ContractChooser(FACADE.getService(), FACADE.getContractsForJavaFile(model.getJavaFile()));
            } catch (ProblemLoaderException e) {
                e.printStackTrace();
            }

            /*TODO cc.showAndWait().ifPresent(result -> {
                model.setChosenContract(result);
                try {
                    FACADE.activateContract(result);
                    //TODO getInspectionViewsController().getActiveInspectionViewTab().getModel().getGoals().setAll(FACADE.getPseudoGoals());
                } catch (ProofInputException e) {
                    LOGGER.error(e);
                    //TODO Utils.showExceptionDialog("", "", "", e);
                }
            });*/
        }

        /*TODO @Override
        protected void failed() {
            LOGGER.error(exceptionProperty().get());
            Utils.showExceptionDialog("", "", "", exceptionProperty().get());
        }*/

        @Override
        protected List<Contract> doInBackground() {
            try {
                return FACADE.getContractsForJavaFile(model.getJavaFile());
            } catch (ProblemLoaderException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    //endregion

    //region: StepIntoHandlers
    @RequiredArgsConstructor
    private class StepIntoHandler implements java.util.function.Consumer<PTreeNode<KeyData>> {
        private final PTreeNode<KeyData> original;

        @Override
        public void accept(PTreeNode<KeyData> keyDataPTreeNode) {
            SwingUtilities.invokeLater(this::acceptUI);
        }

        public void acceptUI() {
            model.getDebuggerFramework().getStatePointerListener().remove(this);
            GoalNode<KeyData> beforeNode = original.getStateBeforeStmt().getSelectedGoalNode();
            List<GoalNode<KeyData>> stateAfterStmt = original.getStateAfterStmt().getGoals();

            /*TODO ProofTree ptree = new ProofTree(DebuggerMain.this);
            Proof proof = beforeNode.getData().getProof();

            Node pnode = beforeNode.getData().getNode();
            //      stateAfterStmt.forEach(keyDataGoalNode -> System.out.println("keyDataGoalNode.getData().getNode().serialNr() = " + keyDataGoalNode.getData().getNode().serialNr()));

            ptree.setProof(proof);
            ptree.setRoot(pnode);
            ptree.setNodeColor(pnode, "blueviolet");
            ptree.setDeactivateRefresh(false);

            if (stateAfterStmt.size() > 0) {
                proof.getSubtreeGoals(pnode).forEach(goal -> System.out.println("goal.node().serialNr() = " + goal.node().serialNr()));
                Set<Node> sentinels;
                sentinels = proof.getSubtreeGoals(pnode)
                        .stream()
                        .map(Goal::node)
                        .collect(Collectors.toSet());
                if (sentinels.size() == 0) {
                    sentinels = new LinkedHashSet();
                    sentinels.add(pnode);
                    //sentinels.add(stateAfterStmt.get(0).getData().getNode());
                }
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
            System.out.println("ptree = " + ptree.getRoot());
            DockNode node = new DockNode(ptree, "Proof Tree for Step Into: " +
                    original.getStatement().accept(new ShortCommandPrinter())
            );

            node.dock(dockStation, DockPos.CENTER, scriptController.getOpenScripts().get(getScriptController().getMainScript().getScriptArea()));
            node.requestFocus();
            */
        }
    }

    @RequiredArgsConstructor
    private class StepIntoReverseHandler implements java.util.function.Consumer<PTreeNode<KeyData>> {
        private final PTreeNode<KeyData> original;

        @Override
        public void accept(PTreeNode<KeyData> keyDataPTreeNode) {
            SwingUtilities.invokeLater(this::acceptUI);
        }

        public void acceptUI() {
            model.getDebuggerFramework().getStatePointerListener().remove(this);
            PTreeNode<KeyData> stepInvOver = model.getDebuggerFramework().getStatePointer();
            GoalNode<KeyData> beforeNode = stepInvOver.getStateBeforeStmt().getSelectedGoalNode();
            List<GoalNode<KeyData>> stateAfterStmt = stepInvOver.getStateAfterStmt().getGoals();

            /*TODO
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
            */
        }
    }

    private class KeYSecurityManager extends SecurityManager {
        @Override
        public void checkExit(int status) {
            throw new SecurityException();
        }

        @Override
        public void checkPermission(Permission perm) {
            // Allow other activities by default
        }
    }
    //endregion
}
