package edu.kit.formal.gui.controller;

import de.uka.ilkd.key.logic.op.IProgramMethod;
import de.uka.ilkd.key.pp.ProgramPrinter;
import de.uka.ilkd.key.speclang.Contract;
import edu.kit.formal.gui.controls.JavaArea;
import edu.kit.formal.gui.controls.ScriptArea;
import edu.kit.formal.gui.model.RootModel;
import edu.kit.formal.interpreter.KeYProofFacade;
import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.KeyData;
import edu.kit.formal.interpreter.dbg.Debugger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.controlsfx.dialog.Wizard;

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
 */
public class DebuggerMainWindowController implements Initializable {
    private SimpleBooleanProperty debugMode = new SimpleBooleanProperty(false);


    @FXML
    private Pane rootPane;
    @FXML
    private SplitPane splitPane;

    /***********************************************************************************************************
     *      Code Area
     * **********************************************************************************************************/
    @FXML
    private ScrollPane scrollPaneCode;
    @FXML
    private ScriptArea scriptArea;
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
    private ListView goalView;
    private ExecutorService executorService = null;
    private KeYProofFacade facade;
    private Wizard contractChooserDialog = new Wizard();
    private ContractLoaderService cls;

    /**
     * Model for the DebuggerController containing the neccessary
     * references to objects needed for controlling backend through UI
     */
    private RootModel model;

    @FXML
    private Label lblStatusMessage;
    @FXML
    private Label lblCurrentNodes;
    @FXML
    private Label lblFilename;

    @Setter
    private Debugger debugger;

    private File initialDirectory;

    @FXML
    private JavaArea javaSourceCode;


    /**
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new RootModel();
        setDebugMode(false);
        facade = new KeYProofFacade(this.model);
        cls = new ContractLoaderService();

        model.scriptFileProperty().addListener((observable, oldValue, newValue) -> {
            lblFilename.setText("File: " + (newValue != null ? newValue.getAbsolutePath() : "n/a"));
        });

        model.chosenContractProperty().addListener(o -> {
            IProgramMethod method = (IProgramMethod) model.getChosenContract().getTarget();
            javaSourceCode.clear();
            javaSourceCode.getMarkedLines().clear();
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

            //debug
            javaSourceCode.getMarkedLines().add(2);
            javaSourceCode.getMarkedLines().add(3);


        });
    }

    //region Actions: Execution
    @FXML
    public void executeScript() {
        buttonStartInterpreter.setText("Interpreting...");
        startDebugMode.setDisable(true);
        facade.executeScript(scriptArea.getText());
        List<GoalNode<KeyData>> g = model.getCurrentState().getGoals();
        this.model.getCurrentGoalNodes().addAll(g);
        buttonStartInterpreter.setText("execute Script");
    }

    @FXML
    public void executeInDebugMode() {
        setDebugMode(true);
        executeScript();
    }
    //endregion

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
            FileUtils.write(scriptFile, scriptArea.getText(), Charset.defaultCharset());
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
            openScript(code);
            model.setScriptFile(scriptFile);
        } catch (IOException e) {
            showExceptionDialog("Exception occured", "",
                    "Could not load file " + scriptFile, e);
        }
    }

    private void openScript(String code) {
        model.setScriptFile(null);
        scriptArea.clear();
        scriptArea.setText(code);
    }

    @FXML
    protected void loadKeYFile() {
        File keyFile = openFileChooserOpenDialog("Select KeY File", "KeY Files", "key", "script");
        this.model.setKeYFile(keyFile);
        if (keyFile != null) {
            buildKeYProofFacade();
        }

    }

    public void saveProof(ActionEvent actionEvent) {

    }


    @FXML
    protected void loadJavaFile() {
        File javaFile = openFileChooserOpenDialog("Select Java File", "Java Files", "java");
        if (javaFile != null) {
            model.setJavaFile(javaFile);
            facade = new KeYProofFacade(model);
            cls.start();
            cls.setOnSucceeded(event -> {
                model.getLoadedContracts().addAll(cls.getValue());
                ContractChooser cc = new ContractChooser(facade.getService(),
                        model.loadedContractsProperty());

                cc.showAndWait().ifPresent(result -> {
                    model.setChosenContract(result);
                    if (this.model.getChosenContract() != null) {
                        buildJavaProofFacade();
                        System.out.println("Proof Facade is built");
                    } else {
                        System.out.println("Something went wrong");
                    }
                });
            });
        }
    }

    /**
     * Spawns a thread that builds the proof environment as facade with interpreter
     */
    private void buildKeYProofFacade() {
        executorService = Executors.newFixedThreadPool(2);
        executorService.execute(() -> {
            facade = new KeYProofFacade(model);
            facade.prepareEnvWithKeYFile(model.getKeYFile());

        });
        executorService.shutdown();
    }

    /**
     * Spawns a thread that builds the proof environment as facade with interpreter
     */
    private void buildJavaProofFacade() {
        executorService = Executors.newFixedThreadPool(2);
        executorService.execute(() -> {
            if (facade != null) {
                facade.prepareEnvForContract(model.getChosenContract(), model.getKeYFile());
            }
        });
        executorService.shutdown();
    }
    //endregion

    //region Santa's Little Helper

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
            initialDirectory = new File("src/test/resources/edu/kit/formal/");

        if (!initialDirectory.exists())
            initialDirectory = new File(".");

        fileChooser.setInitialDirectory(initialDirectory);
        return fileChooser;
    }

    public class ContractLoaderService extends Service<List<Contract>> {
        @Override
        protected Task<List<Contract>> createTask() {
            Task<List<Contract>> task1 = new Task<List<Contract>>() {
                @Override
                protected List<Contract> call() throws Exception {
                    List<Contract> contracts = facade.getContractsForJavaFile(model.getJavaFile());
                    System.out.println("Loaded Contracts " + contracts.toString());
                    return contracts;
                }
            };

            return task1;
        }

    }

    public static void showExceptionDialog(String title, String headerText, String contentText, Exception ex) {
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
    //endregion

    //region Property
    public boolean isDebugMode() {
        return debugMode.get();
    }

    public SimpleBooleanProperty debugModeProperty() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode.set(debugMode);
    }
    //endregion
}
