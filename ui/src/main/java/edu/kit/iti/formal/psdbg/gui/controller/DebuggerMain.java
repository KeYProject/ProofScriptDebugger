package edu.kit.iti.formal.psdbg.gui.controller;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.uka.ilkd.key.api.ProofApi;
import de.uka.ilkd.key.proof.init.ProofInputException;
import de.uka.ilkd.key.speclang.Contract;
import edu.kit.iti.formal.psdbg.examples.Examples;
import edu.kit.iti.formal.psdbg.gui.ProofScriptDebugger;
import edu.kit.iti.formal.psdbg.gui.controls.*;
import edu.kit.iti.formal.psdbg.gui.model.Breakpoint;
import edu.kit.iti.formal.psdbg.gui.model.InspectionModel;
import edu.kit.iti.formal.psdbg.gui.model.MainScriptIdentifier;
import edu.kit.iti.formal.psdbg.interpreter.InterpreterBuilder;
import edu.kit.iti.formal.psdbg.interpreter.KeYProofFacade;
import edu.kit.iti.formal.psdbg.interpreter.KeyInterpreter;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.graphs.PTreeNode;
import edu.kit.iti.formal.psdbg.parser.ast.ProofScript;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import org.antlr.v4.runtime.RecognitionException;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dockfx.DockNode;
import org.dockfx.DockPane;
import org.dockfx.DockPos;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private final ProofTreeController proofTreeController = new ProofTreeController();
    private final InspectionViewsController inspectionViewsController = new InspectionViewsController();
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    /**
     * Property: current loaded javaFile
     */
    private final ObjectProperty<File> javaFile = new SimpleObjectProperty<>(this, "javaFile");
    /**
     * Property: current loaded KeY File
     */
    private final ObjectProperty<File> keyFile = new SimpleObjectProperty<>(this, "keyFile");
    /**
     * Chosen contract for problem
     */
    private final ObjectProperty<Contract> chosenContract = new SimpleObjectProperty<>(this, "chosenContract");
    private ScriptController scriptController;

    @FXML
    private DebuggerStatusBar statusBar;

    @FXML
    private DockPane dockStation;
    private JavaArea javaArea = new JavaArea();
    private DockNode javaAreaDock = new DockNode(javaArea, "Java Source",
            new MaterialDesignIconView(MaterialDesignIcon.CODEPEN)
    );


    //-----------------------------------------------------------------------------------------------------------------
    private ProofTree proofTree = new ProofTree();
    private DockNode proofTreeDock = new DockNode(proofTree, "Proof Tree");
    private WelcomePane welcomePane = new WelcomePane(this);
    private DockNode welcomePaneDock = new DockNode(welcomePane, "Welcome", new MaterialDesignIconView(MaterialDesignIcon.ACCOUNT));
    private DockNode activeInspectorDock = inspectionViewsController.getActiveInterpreterTabDock();
    private BooleanProperty debugMode = new SimpleBooleanProperty(this, "debugMode", false);

    private CommandHelp commandHelp = new CommandHelp();
    private DockNode commandHelpDock = new DockNode(commandHelp, "Command Help");


    /**
     * True, iff the execution is not possible
     */
    private ObservableBooleanValue executeNotPossible = proofTreeController.executeNotPossibleProperty().or(FACADE.readyToExecuteProperty().not());

    /**
     *
     */
    private ObjectProperty<File> initialDirectory = new SimpleObjectProperty<>(this, "initialDirectory");

    /**
     *
     */
    private StringProperty javaCode = new SimpleStringProperty(this, "javaCode");


    //-----------------------------------------------------------------------------------------------------------------
    @FXML
    private Menu examplesMenu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Events.register(this);

        setDebugMode(false);
        scriptController = new ScriptController(dockStation);

        //register the welcome dock in the center
        welcomePaneDock.dock(dockStation, DockPos.LEFT);
        registerToolbarToStatusBar();
        marriageProofTreeControllerWithActiveInspectionView();
        //statusBar.publishMessage("File: " + (newValue != null ? newValue.getAbsolutePath() : "n/a"));
        marriageJavaCode();

        //marriage key proof facade to proof tree
        getFacade().proofProperty().addListener(
                (prop, o, n) -> {
                    proofTree.setRoot(n.root());
                    proofTree.setProof(n);
                    getInspectionViewsController().getActiveInspectionViewTab().getModel().getGoals().setAll(FACADE.getPseudoGoals());
                }
        );


        //Debugging
        Utils.addDebugListener(javaCode);
        Utils.addDebugListener(executeNotPossible, "executeNotPossible");

        scriptController.mainScriptProperty().bindBidirectional(statusBar.mainScriptIdentifierProperty());

        initializeExamples();

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
     * Connects the proof tree controller with the model of the active inspection view model.
     */
    private void marriageProofTreeControllerWithActiveInspectionView() {
        InspectionModel imodel = getInspectionViewsController().getActiveInspectionViewTab().getModel();

        //set all listeners
        proofTreeController.currentGoalsProperty().addListener((o, old, fresh) -> {
            if (fresh != null) {
                imodel.setGoals(fresh);

            } else {
                // no goals, set an empty list
                imodel.setGoals(FXCollections.observableArrayList());
            }
        });


        proofTreeController.currentSelectedGoalProperty().addListener((observable, oldValue, newValue) -> {
            imodel.setCurrentInterpreterGoal(newValue);
            //also update the selected to be shown
            imodel.setSelectedGoalNodeToShow(newValue);
            //System.out.println("Pos: "+newValue.getData().getNode().getNodeInfo().getActiveStatement().getPositionInfo());
        });


        proofTreeController.currentHighlightNodeProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                scriptController.getDebugPositionHighlighter().highlight(newValue);
            }

        });

        imodel.goalsProperty().addListener((observable, oldValue, newValue) -> statusBar.setNumberOfGoals(newValue.size()));




      /*proofTreeController.currentExecutionEndProperty().addListener((observable, oldValue, newValue) -> {
                    scriptController.getMainScript().getScriptArea().removeExecutionMarker();
                    LineMapping lm = new LineMapping(scriptController.getMainScript().getScriptArea().getText());
                    int i = lm.getLineEnd(newValue.getEndPosition().getLineNumber() - 1);
                    scriptController.getMainScript().getScriptArea().insertExecutionMarker(i);

                });*/
        Utils.addDebugListener(proofTreeController.currentGoalsProperty(), Utils::reprKeyDataList);
        Utils.addDebugListener(proofTreeController.currentSelectedGoalProperty(), Utils::reprKeyData);
        Utils.addDebugListener(proofTreeController.currentHighlightNodeProperty());
    }

    public void marriageJavaCode() {
        //Listener on chosenContract from
        chosenContract.addListener(o -> {
            //javaCode.set(Utils.getJavaCode(chosenContract.get()));
            try {
                System.out.println(chosenContract.get().getHTMLText(getFacade().getService()));
                String encoding = null; //encoding Plattform default
                javaCode.set(FileUtils.readFileToString(javaFile.get(), encoding));
            } catch (IOException e) {
                e.printStackTrace();
            }
            showCodeDock(null);
        });

        //add listener for linehighlighting when changing selection in inspectionview
        getInspectionViewsController().getActiveInspectionViewTab().getModel().highlightedJavaLinesProperty().
                addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        javaArea.linesToHighlightProperty().set(newValue);
                    } else {
                        javaArea.linesToHighlightProperty().set(FXCollections.emptyObservableSet());
                    }
                });

        javaCode.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    javaArea.setText(newValue);
                } catch (Exception e) {
                    LOGGER.catching(e);
                }
            }
        });

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


    /* public InspectionViewsController getInspectionViewsController() {
         return inspectionViewsController;
     }

     public KeYProofFacade getFacade() {
         return FACADE;
     }


 */
    //region Actions: Execution

    /**
     * When play button is used
     */
    @FXML
    public void executeScript() {
        executorHelper();
    }

    private void executorHelper() {
        if (proofTreeController.isAlreadyExecuted()) {
            File file;
            boolean isKeyfile = false;
            if (getJavaFile() != null) {
                file = getJavaFile();
            } else {
                isKeyfile = true;
                file = getKeyFile();
            }


            Task<Void> reloading = reloadEnvironment(file, isKeyfile);
            reloading.setOnSucceeded(event -> {
                statusBar.publishMessage("Cleared and Reloaded Environment");
                executeScript(FACADE.buildInterpreter(), false);
            });

            reloading.setOnFailed(event -> {
                event.getSource().exceptionProperty().get();
                Utils.showExceptionDialog("Loading Error", "Could not clear Environment", "There was an error when clearing old environment",
                        (Throwable) event.getSource().exceptionProperty().get()
                );
            });

            ProgressBar bar = new ProgressBar();
            bar.progressProperty().bind(reloading.progressProperty());
            executorService.execute(reloading);
        } else {

            executeScript(FACADE.buildInterpreter(), false);
        }

    }

    public File getJavaFile() {
        return javaFile.get();
    }

    //region Actions: Menu
   /* @FXML
    public void closeProgram() {
        System.exit(0);
    }*/

 /*   @FXML
    public void openScript() {
        File scriptFile = openFileChooserOpenDialog("Select Script File",
                "Proof Script File", "kps");
        if (scriptFile != null) {
            openScript(scriptFile);
        }
    }*/


   /* private void saveScript(File scriptFile) {
        try {
            scriptController.saveCurrentScriptAs(scriptFile);
        } catch (IOException e) {
            Utils.showExceptionDialog("Could not save file", "Saving File Error", "Could not save to file " + scriptFile.getName(), e);
        }
    }

    @FXML
    public void saveAsScript() throws IOException {
        File f = openFileChooserSaveDialog("Save script", "Save Script files", "kps");
        if (f != null) {
           /* if(!f.exists()){
                f.createNewFile();
            }
            saveScript(f);
        }
    }*/

  /*  public void openScript(File scriptFile) {
        assert scriptFile != null;
        setInitialDirectory(scriptFile.getParentFile());
        try {
            String code = FileUtils.readFileToString(scriptFile, Charset.defaultCharset());
            ScriptArea area = scriptController.createNewTab(scriptFile);
        } catch (IOException e) {
            Utils.showExceptionDialog("Exception occured", "",
                    "Could not load sourceName " + scriptFile, e);
        }
    }*/

    public void setJavaFile(File javaFile) {
        this.javaFile.set(javaFile);
    }

    public File getKeyFile() {
        return keyFile.get();
    }

    public void setKeyFile(File keyFile) {
        this.keyFile.set(keyFile);
    }

    /**
     * Reload the KeY environment, to execute the script again
     * TODO: reload views
     *
     * @param file
     * @param keyfile
     * @return
     */
    public Task<Void> reloadEnvironment(File file, boolean keyfile) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                FACADE.reloadEnvironment();
                if (keyfile) {
                    openKeyFile(file);
                } else {
                    openJavaFile(file);
                }
                return null;
            }
        };
        return task;
    }

    /**
     * Execute the script that with using the interpreter that is build using the interpreterbuilder
     *
     * @param ib
     * @param debugMode
     */
    private void executeScript(InterpreterBuilder ib, boolean debugMode) {

        Set<Breakpoint> breakpoints = scriptController.getBreakpoints();

        if (proofTreeController.isAlreadyExecuted()) {
            proofTreeController.saveGraphs();
        }
        this.debugMode.set(debugMode);
        statusBar.publishMessage("Parse ...");
        try {

            List<ProofScript> scripts = scriptController.getCombinedAST();
            int n = 0;
            if (scriptController.getMainScript() == null) {
                MainScriptIdentifier msi = new MainScriptIdentifier();
                msi.setLineNumber(scripts.get(0).getStartPosition().getLineNumber());
                msi.setScriptName(scripts.get(0).getName());
                msi.setSourceName(scripts.get(0).getRuleContext().getStart().getInputStream().getSourceName());
                msi.setScriptArea(scriptController.findEditor(new File(scripts.get(0).getRuleContext().getStart().getInputStream().getSourceName())));
                scriptController.setMainScript(msi);
                n = 0;
            } else {
                for (int i = 0; i < scripts.size(); i++) {
                    ProofScript proofScript = scripts.get(i);
                    if (proofScript.getName().equals(scriptController.getMainScript().getScriptName())) {
                        n = i;
                        break;
                    }
                }
            }

            statusBar.publishMessage("Creating new Interpreter instance ...");
            ib.setScripts(scripts);
            KeyInterpreter currentInterpreter = ib.build();

            proofTreeController.setCurrentInterpreter(currentInterpreter);
            proofTreeController.setMainScript(scripts.get(n));

            statusBar.publishMessage("Executing script " + scripts.get(n).getName());

            proofTreeController.executeScript(this.debugMode.get(), statusBar, breakpoints);
            //highlight signature of main script
            //scriptController.setDebugMark(scripts.get(0).getStartPosition().getLineNumber());
        } catch (RecognitionException e) {
            Utils.showExceptionDialog("Antlr Exception", "", "Could not parse scripts.", e);
        }
    }

    public void openKeyFile(File keyFile) {
        if (keyFile != null) {
            setKeyFile(keyFile);
            setInitialDirectory(keyFile.getParentFile());
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
            setJavaFile(javaFile);
            initialDirectory.set(javaFile.getParentFile());
            contractLoaderService.start();
        }
    }

    /*    @FXML
        protected void loadKeYFile() {
            File keyFile = openFileChooserOpenDialog("Select KeY File", "KeY Files", "key", "kps");
            openKeyFile(keyFile);
        }
    */

    @FXML
    public void executeToBreakpoint() {
        Set<Breakpoint> breakpoints = scriptController.getBreakpoints();
        if (breakpoints.size() == 0) {
            //we need to add breakpoint at end if no breakpoint exists
        }
        executorHelper();
    }


    //endregion

    //region Santa's Little Helper

    @FXML
    public void executeInDebugMode() {
        executeScript(FACADE.buildInterpreter(), true);
    }

    @FXML
    public void saveScript() {
        try {
            scriptController.saveCurrentScript();
        } catch (IOException e) {
            Utils.showExceptionDialog("Could not save file", "Saving File Error", "Could not save current script", e);

        }
    }

    public void openJavaFile() {
        loadJavaFile();
        showCodeDock(null);
    }

    //endregion

    //region Santa's Little Helper
    @FXML
    protected void loadJavaFile() {
        File javaFile = openFileChooserOpenDialog("Select Java File", "Java Files", "java");
        openJavaFile(javaFile);
    }

    private File openFileChooserOpenDialog(String title, String description, String... fileEndings) {
        FileChooser fileChooser = getFileChooser(title, description, fileEndings);
        //File sourceName = fileChooser.showOpenDialog(inspectionViewsController.getInspectionViewTab().getGoalView().getScene().getWindow());
        File file = fileChooser.showOpenDialog(statusBar.getScene().getWindow());
        if (file != null) setInitialDirectory(file.getParentFile());
        return file;
    }

    private FileChooser getFileChooser(String title, String description, String[] fileEndings) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter(description, fileEndings));
        if (initialDirectory.get() == null)
            setInitialDirectory(new File("src/test/resources/edu/kit/formal/interpreter/contraposition/"));

        if (!initialDirectory.get().exists())
            setInitialDirectory(new File("."));

        fileChooser.setInitialDirectory(initialDirectory.get());
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


    //endregion

    //region Actions: Menu
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
        setInitialDirectory(scriptFile.getParentFile());
        try {
            String code = FileUtils.readFileToString(scriptFile, Charset.defaultCharset());
            ScriptArea area = scriptController.createNewTab(scriptFile);
        } catch (IOException e) {
            Utils.showExceptionDialog("Exception occured", "",
                    "Could not load sourceName " + scriptFile, e);
        }
    }

    /*   @FXML
       public void saveScript() {
           try {
               scriptController.saveCurrentScript();
           } catch (IOException e) {
               Utils.showExceptionDialog("Could not save file", "Saving File Error", "Could not save current script", e);

           }
       }
   */
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
        if (file != null) setInitialDirectory(file.getParentFile());
        return file;
    }
    //endregion

    private void saveScript(File scriptFile) {
        try {
            scriptController.saveCurrentScriptAs(scriptFile);
        } catch (IOException e) {
            Utils.showExceptionDialog("Could not save file", "Saving File Error", "Could not save to file " + scriptFile.getName(), e);
        }
    }

    @FXML
    protected void loadKeYFile() {
        File keyFile = openFileChooserOpenDialog("Select KeY File", "KeY Files", "key", "kps");
        openKeyFile(keyFile);
    }

    /**
     * Save KeY proof as proof file
     *
     * @param actionEvent
     */
    public void saveProof(ActionEvent actionEvent) {

        LOGGER.error("saveProof not implemented!!!");
    }

    /**
     * Perform a step over
     *
     * @param actionEvent
     */
    public void stepOver(ActionEvent actionEvent) {
        LOGGER.debug("DebuggerMain.stepOver");
        PTreeNode newState = proofTreeController.stepOver();
    }

    /**
     * Perform a step back
     *
     * @param actionEvent
     */
    public void stepBack(ActionEvent actionEvent) {
        LOGGER.debug("DebuggerMain.stepBack");
        PTreeNode<KeyData> newState = proofTreeController.stepBack();
    }

    //region Property
    public boolean isDebugMode() {
        return debugMode.get();
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode.set(debugMode);
    }

    public BooleanProperty debugModeProperty() {
        return debugMode;
    }

    public Boolean getExecuteNotPossible() {
        return executeNotPossible.get();

    }

    public ObservableBooleanValue executeNotPossibleProperty() {
        return executeNotPossible;
    }

    public void stopDebugMode(ActionEvent actionEvent) {
        scriptController.getDebugPositionHighlighter().remove();
        Button stop = (Button) actionEvent.getSource();
        stop.setText("Reload");
        //linenumberMainscript from model?
        //scriptController.getActiveScriptAreaTab().getScriptArea().removeHighlightStmt(lineNumberMainScript);
        //inspectionViewsController.getInspectionViewTab.clear();
    }

    public void newScript(ActionEvent actionEvent) {
        scriptController.newScript();
    }

    public void showWelcomeDock(ActionEvent actionEvent) {
        if (!welcomePaneDock.isDocked()) {
            welcomePaneDock.dock(dockStation, DockPos.CENTER);
        }
    }

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
        //this.dockStation.getChildren().add(dn);
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

    public ProofTreeController getProofTreeController() {
        return proofTreeController;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public ContractLoaderService getContractLoaderService() {
        return contractLoaderService;
    }

    public DebuggerStatusBar getStatusBar() {
        return statusBar;
    }

    public DockPane getDockStation() {
        return dockStation;
    }

    public JavaArea getJavaArea() {
        return javaArea;
    }

    public WelcomePane getWelcomePane() {
        return welcomePane;
    }

    public ObjectProperty<File> javaFileProperty() {
        return javaFile;
    }

    public ObjectProperty<File> keyFileProperty() {
        return keyFile;
    }

    public Contract getChosenContract() {
        return chosenContract.get();
    }

    public void setChosenContract(Contract chosenContract) {
        this.chosenContract.set(chosenContract);
    }

    public ObjectProperty<Contract> chosenContractProperty() {
        return chosenContract;
    }

    public File getInitialDirectory() {
        return initialDirectory.get();
    }

    public void setInitialDirectory(File initialDirectory) {
        this.initialDirectory.set(initialDirectory);
    }

    public ObjectProperty<File> initialDirectoryProperty() {
        return initialDirectory;
    }

    public class ContractLoaderService extends Service<List<Contract>> {
        @Override
        protected void succeeded() {
            statusBar.publishMessage("Contract loaded");
            List<Contract> contracts = getValue();
            ContractChooser cc = new ContractChooser(FACADE.getService(), contracts);

            cc.showAndWait().ifPresent(result -> {
                setChosenContract(result);
                try {
                    FACADE.activateContract(result);
                    getInspectionViewsController().getActiveInspectionViewTab().getModel().getGoals().setAll(FACADE.getPseudoGoals());
                } catch (ProofInputException e) {
                    Utils.showExceptionDialog("", "", "", e);
                }
            });
        }

        @Override
        protected void failed() {
            Utils.showExceptionDialog("", "", "", exceptionProperty().get());
        }

        @Override
        protected Task<List<Contract>> createTask() {
            return FACADE.getContractsForJavaFileTask(getJavaFile());
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
