package edu.kit.formal.gui.controller;

import de.uka.ilkd.key.logic.op.IProgramMethod;
import de.uka.ilkd.key.pp.ProgramPrinter;
import de.uka.ilkd.key.proof.init.ProofInputException;
import de.uka.ilkd.key.speclang.Contract;
import edu.kit.formal.gui.controls.*;
import edu.kit.formal.gui.model.RootModel;
import edu.kit.formal.interpreter.Interpreter;
import edu.kit.formal.interpreter.InterpreterBuilder;
import edu.kit.formal.interpreter.KeYProofFacade;
import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.KeyData;
import edu.kit.formal.interpreter.data.State;
import edu.kit.formal.proofscriptparser.Facade;
import edu.kit.formal.proofscriptparser.ast.ProofScript;
import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.SetChangeListener;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import org.antlr.v4.runtime.RecognitionException;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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
    private static final Logger LOGGER = LogManager.getLogger(DebuggerMainWindowController.class);

    private final PuppetMaster blocker = new PuppetMaster();
    private SimpleBooleanProperty debugMode = new SimpleBooleanProperty(false);
    private GoalOptionsMenu goalOptionsMenu = new GoalOptionsMenu();

    @FXML
    private Pane rootPane;
    @FXML
    private SplitPane splitPane;
    /***********************************************************************************************************
     *      Code Area
     * **********************************************************************************************************/

    //@FXML
    //private ScriptArea scriptArea;
    @FXML
    private ScriptTabsController tabPane;
    //@FXML
    //private Tab startTab;
    /***********************************************************************************************************
     *      MenuBar
     * **********************************************************************************************************/
    @FXML
    private MenuBar menubar;
    @FXML
    private Menu fileMenu;
    @FXML
    private MenuItem closeMenuItem;
    /***********************************************************************************************************
     *      ToolBar
     * **********************************************************************************************************/
    @FXML
    private ToolBar toolbar;
    @FXML
    private Button buttonStartInterpreter;
    @FXML
    private Button startDebugMode;
    /***********************************************************************************************************
     *      GoalView
     * **********************************************************************************************************/
    @FXML
    private ListView<GoalNode<KeyData>> goalView;
    private ExecutorService executorService = Executors.newFixedThreadPool(2);
    private KeYProofFacade facade = new KeYProofFacade();
    private ContractLoaderService contractLoaderService = new ContractLoaderService();
    /**
     * Model for the DebuggerController containing the neccessary
     * references to objects needed for controlling backend through UI
     */
    private RootModel model = new RootModel();


    @FXML
    private DebuggerStatusBar statusBar;

    private File initialDirectory;

    @FXML
    private JavaArea javaSourceCode;

    @FXML
    private SequentView sequentView;


    private InterpretingService interpreterService = new InterpretingService();

    private ObservableBooleanValue executeNotPossible = interpreterService.runningProperty().or(facade.readyToExecuteProperty().not());

    public static void showExceptionDialog(String title, String headerText, String contentText, Throwable ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");
        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }

    /**
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDebugMode(false);

        toolbar.getChildrenUnmodifiable().forEach(
                n -> n.setOnMouseEntered(statusBar.getTooltipHandler()));

        buttonStartInterpreter.setOnMouseEntered(statusBar.getTooltipHandler());

        model.scriptFileProperty().addListener((observable, oldValue, newValue) -> {
            statusBar.publishMessage("File: " + (newValue != null ? newValue.getAbsolutePath() : "n/a"));
        });


        model.chosenContractProperty().addListener(o -> {
            IProgramMethod method = (IProgramMethod) model.getChosenContract().getTarget();
            javaSourceCode.clear();
            javaSourceCode.getLineToClass().clear();
            StringWriter writer = new StringWriter();
            ProgramPrinter pp = new ProgramPrinter(writer);
            try {
                pp.printFullMethodSignature(method);
                pp.printStatementBlock(method.getBody());
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            javaSourceCode.insertText(0, writer.toString());
        });

//        scriptArea.getMarkedLines().addListener(new SetChangeListener<Integer>() {
        tabPane.getActiveScriptAreaTab().getScriptArea().getMarkedLines().addListener(new SetChangeListener<Integer>() {

            @Override
            public void onChanged(Change<? extends Integer> change) {
                blocker.getBreakpoints().clear();
                blocker.getBreakpoints().addAll(change.getSet());
            }
        });

        blocker.currentGoalsProperty().addListener((o, old, fresh) -> {
            model.currentGoalNodesProperty().setAll(fresh);
        });
        model.currentSelectedGoalNodeProperty().bind(blocker.currentSelectedGoalProperty());
        goalView.itemsProperty().bind(model.currentGoalNodesProperty());

        model.currentSelectedGoalNodeProperty().addListener((p, old, fresh) -> {
            goalView.getSelectionModel().select(fresh);

            /* TODO get lines of active statements marked lines
            javaSourceCode.getMarkedLines().clear();
            javaSourceCode.getMarkedLines().addAll(
            );*/
        });

        goalView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            sequentView.setNode(newValue.getData().getNode());
        });

        goalView.setCellFactory(GoalNodeListCell::new);
        CustomTabPaneSkin skin = new CustomTabPaneSkin(tabPane);

      /*  startTab.textProperty().bind(new StringBinding() {
            {
                super.bind(model.scriptFileProperty());
            }

            @Override
            protected String computeValue() {
                if (model.scriptFileProperty().getValue() != null) {
                    System.out.println(model.scriptFileProperty());
                    return model.scriptFileProperty().get().getName();
                } else {
                    return "Untitled";
                }
            }

        });*/


    }

    //region Actions: Execution
    @FXML
    public void executeScript() {
        executeScript(facade.buildInterpreter(), false);
    }

    @FXML
    public void executeScriptFromCursor() {
        InterpreterBuilder ib = facade.buildInterpreter();
        ib.inheritState(interpreterService.interpreter.get());
/*
        LineMapping lm = new LineMapping(scriptArea.getText());
        int line = lm.getLine(scriptArea.getCaretPosition());
        int inLine = lm.getCharInLine(scriptArea.getCaretPosition());
*/
        ib.ignoreLinesUntil(tabPane.getActiveScriptAreaTab().getScriptArea().getCaretPosition());


        executeScript(ib, true);
    }

    @FXML
    public void executeInDebugMode() {
        executeScript(facade.buildInterpreter(), true);
    }
    //endregion

    private void executeScript(InterpreterBuilder ib, boolean debugMode) {
        this.debugMode.set(debugMode);
        blocker.deinstall();
        statusBar.publishMessage("Parse ...");
        try {
            List<ProofScript> scripts = Facade.getAST(scriptArea.getText());
            statusBar.publishMessage("Creating new Interpreter instance ...");
            ib.setScripts(scripts);
            ib.captureHistory();

            Interpreter<KeyData> currentInterpreter = ib.build();

            if (debugMode) {
                blocker.getStepUntilBlock().set(1);
                blocker.install(currentInterpreter);
            }
            interpreterService.interpreter.set(currentInterpreter);
            interpreterService.mainScript.set(scripts.get(0));
            interpreterService.start();
        } catch (RecognitionException e) {
            showExceptionDialog("Antlr Exception", "", "Could not parse scripts.", e);
        }
    }

    //region Actions: Menu
    @FXML
    public void closeProgram() {
        System.exit(0);
    }

    @FXML
    protected void openScript() {
        File scriptFile = openFileChooserOpenDialog("Select Script File",
                "Proof Script File", "kps");
        if (scriptFile != null) {
            openScript(scriptFile);
        }
    }

    @FXML
    public void saveScript() {
        if (model.getScriptFile() != null) {
            saveScript(model.getScriptFile());
        } else {
            saveAsScript();
        }
    }

    private void saveScript(File scriptFile) {
        try {
            FileUtils.write(scriptFile, tabPane.getActiveScriptAreaTab().getScriptArea().getText(), Charset.defaultCharset());
        } catch (IOException e) {
            showExceptionDialog("Could not save file", "blubb", "...fsfsfsf fsa", e);
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
        try {
            String code = FileUtils.readFileToString(scriptFile, Charset.defaultCharset());
            tabPane.createNewTab(scriptFile.getAbsolutePath());
            openScript(code, tabPane.getActiveScriptAreaTab().getScriptArea());
            model.setScriptFile(scriptFile);
        } catch (IOException e) {
            showExceptionDialog("Exception occured", "",
                    "Could not load file " + scriptFile, e);
        }
    }

    private void openScript(String code, ScriptArea area) {
        model.setScriptFile(null);
        if (area.textProperty().getValue() != "") {
            System.out.println(area.textProperty().getValue());
            area.deleteText(0, area.textProperty().getValue().length());
        }
        area.setText(code);

    }

    @FXML
    protected void loadKeYFile() {
        File keyFile = openFileChooserOpenDialog("Select KeY File", "KeY Files", "key", "script");
        this.model.setKeYFile(keyFile);
        if (keyFile != null) {
            Task<Void> task = facade.loadKeyFileTask(keyFile);
            task.setOnSucceeded(event -> {
                statusBar.publishMessage("Loaded key file: %s", keyFile);
                model.getCurrentGoalNodes().setAll(facade.getPseudoGoals());
            });

            task.setOnFailed(event -> {
                event.getSource().exceptionProperty().get();
                showExceptionDialog("Could not load file", "Key file loading error", "",
                        (Throwable) event.getSource().exceptionProperty().get()
                );
            });

            ProgressBar bar = new ProgressBar();
            bar.progressProperty().bind(task.progressProperty());
            executorService.execute(task);
        }
    }

    public void saveProof(ActionEvent actionEvent) {

    }
    //endregion

    //region Santa's Little Helper

    @FXML
    protected void loadJavaFile() {
        File javaFile = openFileChooserOpenDialog("Select Java File", "Java Files", "java");
        if (javaFile != null) {
            model.setJavaFile(javaFile);
            contractLoaderService.start();
        }
    }

    /**
     * Creates a filechooser dialog
     *
     * @param title       of the dialog
     * @param description of the files
     * @param fileEndings file that should be shown
     * @return
     */
    private File openFileChooserSaveDialog(String title, String description, String... fileEndings) {
        FileChooser fileChooser = getFileChooser(title, description, fileEndings);
        File file = fileChooser.showSaveDialog(goalView.getScene().getWindow());
        if (file != null) initialDirectory = file.getParentFile();
        return file;
    }

    private File openFileChooserOpenDialog(String title, String description, String... fileEndings) {
        FileChooser fileChooser = getFileChooser(title, description, fileEndings);
        File file = fileChooser.showOpenDialog(goalView.getScene().getWindow());
        if (file != null) initialDirectory = file.getParentFile();
        return file;
    }

    private FileChooser getFileChooser(String title, String description, String[] fileEndings) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter(description, fileEndings));
        if (initialDirectory == null)
            initialDirectory = new File("src/test/resources/edu/kit/formal/interpreter/contraposition/");

        if (!initialDirectory.exists())
            initialDirectory = new File(".");

        fileChooser.setInitialDirectory(initialDirectory);
        return fileChooser;
    }

    public void stepOver(ActionEvent actionEvent) {
        blocker.getStepUntilBlock().addAndGet(1);
        blocker.unlock();
    }

    public void showGoalOptions(MouseEvent actionEvent) {
        Node n = (Node) actionEvent.getTarget();
        goalOptionsMenu.show(n, actionEvent.getScreenX(), actionEvent.getScreenY());
    }

    public KeYProofFacade getFacade() {
        return facade;
    }

    //region Property
    public boolean isDebugMode() {
        return debugMode.get();
    }
    //endregion

    public void setDebugMode(boolean debugMode) {
        this.debugMode.set(debugMode);
    }

    public SimpleBooleanProperty debugModeProperty() {
        return debugMode;
    }

    public Boolean getExecuteNotPossible() {
        return executeNotPossible.get();
    }

    public ObservableBooleanValue executeNotPossibleProperty() {
        return executeNotPossible;
    }

    public class ContractLoaderService extends Service<List<Contract>> {
        @Override
        protected Task<List<Contract>> createTask() {
            return facade.getContractsForJavaFileTask(model.getJavaFile());
        }

        @Override
        protected void failed() {
            showExceptionDialog("", "", "", exceptionProperty().get());
        }

        @Override
        protected void succeeded() {
            statusBar.publishMessage("Contract loaded");
            model.getLoadedContracts().setAll(getValue());
            //FIXME model braucht contracts nicht
            ContractChooser cc = new ContractChooser(facade.getService(), model.loadedContractsProperty());

            cc.showAndWait().ifPresent(result -> {
                model.setChosenContract(result);
                try {
                    facade.activateContract(result);
                    model.getCurrentGoalNodes().setAll(facade.getPseudoGoals());
                } catch (ProofInputException e) {
                    showExceptionDialog("", "", "", e);
                }
            });
        }
    }

    private class GoalNodeListCell extends ListCell<GoalNode<KeyData>> {

        public GoalNodeListCell(ListView<GoalNode<KeyData>> goalNodeListView) {
            itemProperty().addListener(this::update);
            goalOptionsMenu.selectedViewOptionProperty().addListener(this::update);
        }

        private void update(Observable observable) {
            if (getItem() == null) {
                setText("");
                return;
            }
            KeyData item = getItem().getData();
            String text = "n/a";
            if (goalOptionsMenu.getSelectedViewOption() != null) {
                text = goalOptionsMenu.getSelectedViewOption().getText(item);
            }
            setText(text);
        }
    }

    private class InterpretingService extends Service<State<KeyData>> {
        private final SimpleObjectProperty<Interpreter<KeyData>> interpreter = new SimpleObjectProperty<>();
        private final SimpleObjectProperty<ProofScript> mainScript = new SimpleObjectProperty<>();

        @Override
        protected void succeeded() {
            updateView();
        }

        @Override
        protected void cancelled() {
            updateView();
        }

        @Override
        protected void failed() {
            getException().printStackTrace();
            showExceptionDialog("Execution failed", "", "", getException());
            updateView();
        }

        private void updateView() {
            //check proof
            // check state for empty/error goal nodes leer
            //currentGoals.set(state.getGoals());
            //currentSelectedGoal.set(state.getSelectedGoalNode());
            blocker.publishState();
        }

        @Override
        protected Task<edu.kit.formal.interpreter.data.State<KeyData>> createTask() {
            return new Task<edu.kit.formal.interpreter.data.State<KeyData>>() {
                final Interpreter<KeyData> i = interpreter.get();
                final ProofScript ast = mainScript.get();

                @Override
                protected edu.kit.formal.interpreter.data.State<KeyData> call() throws Exception {
                    i.interpret(ast);
                    return i.peekState();
                }
            };
        }
    }
//endregion
}
