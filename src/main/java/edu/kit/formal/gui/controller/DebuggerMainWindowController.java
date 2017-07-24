package edu.kit.formal.gui.controller;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.uka.ilkd.key.proof.init.ProofInputException;
import de.uka.ilkd.key.speclang.Contract;
import edu.kit.formal.gui.ProofScriptDebugger;
import edu.kit.formal.gui.controls.*;
import edu.kit.formal.gui.model.InspectionModel;
import edu.kit.formal.interpreter.Interpreter;
import edu.kit.formal.interpreter.InterpreterBuilder;
import edu.kit.formal.interpreter.KeYProofFacade;
import edu.kit.formal.interpreter.data.KeyData;
import edu.kit.formal.interpreter.graphs.PTreeNode;
import edu.kit.formal.interpreter.graphs.ProofTreeController;
import edu.kit.formal.proofscriptparser.ast.ProofScript;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Controller for the Debugger MainWindow
 *
 * @author S.Grebing
 * @author Alexander Weigl
 */
public class DebuggerMainWindowController implements Initializable {
    public static final KeYProofFacade FACADE = new KeYProofFacade();
    protected static final Logger LOGGER = LogManager.getLogger(DebuggerMainWindowController.class);
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
    private WelcomePane welcomePane = new WelcomePane(this);
    private DockNode welcomePaneDock = new DockNode(welcomePane, "Welcome", new MaterialDesignIconView(MaterialDesignIcon.ACCOUNT));
    private DockNode activeInspectorDock = inspectionViewsController.getActiveInterpreterTabDock();
    private BooleanProperty debugMode = new SimpleBooleanProperty(this, "debugMode", false);

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

        //Debugging
        Utils.addDebugListener(javaCode);
        Utils.addDebugListener(executeNotPossible, "executeNotPossible");

        scriptController.mainScriptProperty().bindBidirectional(statusBar.mainScriptIdentifierProperty());

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
            scriptController.getDebugPositionHighlighter().highlight(newValue);
        });

        Utils.addDebugListener(proofTreeController.currentGoalsProperty(), Utils::reprKeyDataList);
        Utils.addDebugListener(proofTreeController.currentSelectedGoalProperty(), Utils::reprKeyData);
        Utils.addDebugListener(proofTreeController.currentHighlightNodeProperty());
    }

    public void marriageJavaCode() {
        /*javaFileProperty().addListener((observable, oldValue, newValue) -> {
            showCodeDock(null);
            try {
                String code = FileUtils.readFileToString(newValue, Charset.defaultCharset());
                javaCode.set(code);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });*/

        chosenContract.addListener(o -> {
            javaCode.set(Utils.getJavaCode(chosenContract.get()));

            showCodeDock(null);
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


    /**
     * If the mouse moves other toolbar button, the help text should display in the status bar
     */
    private void registerToolbarToStatusBar() {
        /*toolbar.getChildrenUnmodifiable().forEach(
                n -> n.setOnMouseEntered(statusBar.getTooltipHandler()));

        buttonStartInterpreter.setOnMouseEntered(statusBar.getTooltipHandler());
        */
    }

    //region Actions: Execution
    @FXML
    public void executeScript() {
        executeScript(FACADE.buildInterpreter(), false);
    }

    @FXML
    public void executeScriptFromCursor() {
        InterpreterBuilder ib = FACADE.buildInterpreter();
//        ib.inheritState(interpreterService.interpreterProperty().get());
///*
//        LineMapping lm = new LineMapping(scriptArea.getText());
//        int line = lm.getLine(scriptArea.getCaretPosition());
//        int inLine = lm.getCharInLine(scriptArea.getCaretPosition());
//*/
//        ib.ignoreLinesUntil(scriptController.getSelectedScriptArea().getCaretPosition());
//        executeScript(ib, true);
    }

    @FXML
    public void executeInDebugMode() {
        executeScript(FACADE.buildInterpreter(), true);
    }
    //endregion

    /**
     * Execute the script that with using the interpreter that is build using teh interpreterbuilder
     *
     * @param ib
     * @param debugMode
     */
    private void executeScript(InterpreterBuilder ib, boolean debugMode) {
        this.debugMode.set(debugMode);
        statusBar.publishMessage("Parse ...");
        try {
            List<ProofScript> scripts = scriptController.getCombinedAST();
            statusBar.publishMessage("Creating new Interpreter instance ...");
            ib.setScripts(scripts);
            Interpreter<KeyData> currentInterpreter = ib.build();

            proofTreeController.setCurrentInterpreter(currentInterpreter);
            proofTreeController.setMainScript(scripts.get(0));
            proofTreeController.executeScript(this.debugMode.get());
            //highlight signature of main script
            //scriptController.setDebugMark(scripts.get(0).getStartPosition().getLineNumber());
        } catch (RecognitionException e) {
            Utils.showExceptionDialog("Antlr Exception", "", "Could not parse scripts.", e);
        }
    }

    //region Actions: Menu
    @FXML
    public void closeProgram() {
        System.exit(0);
    }

    @FXML
    public void openScript() {
        File scriptFile = openFileChooserOpenDialog("Select Script File",
                "Proof Script File", "kps");
        if (scriptFile != null) {
            openScript(scriptFile);
        }
    }

    @FXML
    public void saveScript() {
        scriptController.saveCurrentScript();
    }

    private void saveScript(File scriptFile) {
        try {
            scriptController.saveCurrentScriptAs(scriptFile);
        } catch (IOException e) {
            Utils.showExceptionDialog("Could not save sourceName", "blubb", "...fsfsfsf fsa", e);
        }
    }

    @FXML
    public void saveAsScript() {
        File f = openFileChooserSaveDialog("Save script", "", "kps");
        if (f != null) {
            saveScript(f);
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

    @FXML
    protected void loadKeYFile() {
        File keyFile = openFileChooserOpenDialog("Select KeY File", "KeY Files", "key", "script");
        openKeyFile(keyFile);
    }

    public void openKeyFile(File keyFile) {
        if (keyFile != null) {
            setKeyFile(keyFile);
            setInitialDirectory(keyFile.getParentFile());
            Task<Void> task = FACADE.loadKeyFileTask(keyFile);
            task.setOnSucceeded(event -> {
                statusBar.publishMessage("Loaded key sourceName: %s", keyFile);
                getInspectionViewsController().getActiveInspectionViewTab().getModel().getGoals().setAll(FACADE.getPseudoGoals());
            });

            task.setOnFailed(event -> {
                event.getSource().exceptionProperty().get();
                Utils.showExceptionDialog("Could not load sourceName", "Key sourceName loading error", "",
                        (Throwable) event.getSource().exceptionProperty().get()
                );
            });

            ProgressBar bar = new ProgressBar();
            bar.progressProperty().bind(task.progressProperty());
            executorService.execute(task);
        }
    }

    public void saveProof(ActionEvent actionEvent) {
        LOGGER.error("saveProof not implemented!!!");
    }
    //endregion

    //region Santa's Little Helper

    @FXML
    protected void loadJavaFile() {
        File javaFile = openFileChooserOpenDialog("Select Java File", "Java Files", "java");
        openJavaFile(javaFile);
    }

    public void openJavaFile(File javaFile) {
        if (javaFile != null) {
            setJavaFile(javaFile);
            initialDirectory.set(javaFile.getParentFile());
            contractLoaderService.start();
        }
    }

    public void openJavaFile() {
        loadJavaFile();
        showCodeDock(null);
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

    public void stepOver(ActionEvent actionEvent) {
        LOGGER.debug("DebuggerMainWindowController.stepOver");
        PTreeNode newState = proofTreeController.stepOver();
    }

    public void stepBack(ActionEvent actionEvent) {
        LOGGER.debug("DebuggerMainWindowController.stepBack");
        PTreeNode newState = proofTreeController.stepBack();
    }


    public KeYProofFacade getFacade() {
        return FACADE;
    }

    //region Property
    public boolean isDebugMode() {
        return debugMode.get();
    }
    //endregion

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
        //linenumberMainscript from model?
        //scriptController.getActiveScriptAreaTab().getScriptArea().removeHighlightStmt(lineNumberMainScript);
        //inspectionViewsController.getInspectionViewTab.clear();
    }

    public void newScript(ActionEvent actionEvent) {
        scriptController.newScript();
    }

    public void showCodeDock(ActionEvent actionEvent) {
        if (!javaAreaDock.isDocked()) {
            javaAreaDock.dock(dockStation, DockPos.RIGHT);
        }
    }

    public void showWelcomeDock(ActionEvent actionEvent) {
        if (!welcomePaneDock.isDocked()) {
            welcomePaneDock.dock(dockStation, DockPos.CENTER);
        }
    }

    public void showActiveInspector(ActionEvent actionEvent) {
        if (!activeInspectorDock.isDocked() &&
                !activeInspectorDock.isFloating()) {
            activeInspectorDock.dock(dockStation, DockPos.CENTER);
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

        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        String url = ProofScriptDebugger.class.getResource("intro.html").toExternalForm();
        webEngine.load(url);
        DockNode dn = new DockNode(browser);

        this.dockStation.getChildren().add(dn);

    }

    public ScriptController getScriptController() {
        return scriptController;
    }

    public ProofTreeController getProofTreeController() {
        return proofTreeController;
    }

    public InspectionViewsController getInspectionViewsController() {
        return inspectionViewsController;
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

    public File getJavaFile() {
        return javaFile.get();
    }

    public void setJavaFile(File javaFile) {
        this.javaFile.set(javaFile);
    }

    public ObjectProperty<File> javaFileProperty() {
        return javaFile;
    }

    public File getKeyFile() {
        return keyFile.get();
    }

    public void setKeyFile(File keyFile) {
        this.keyFile.set(keyFile);
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
        protected Task<List<Contract>> createTask() {
            return FACADE.getContractsForJavaFileTask(getJavaFile());
        }

        @Override
        protected void failed() {
            Utils.showExceptionDialog("", "", "", exceptionProperty().get());
        }

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
    }

    //endregion
}
