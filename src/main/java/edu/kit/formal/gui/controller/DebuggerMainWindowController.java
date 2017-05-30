package edu.kit.formal.gui.controller;

import edu.kit.formal.gui.FileUtils;
import edu.kit.formal.gui.model.RootModel;
import edu.kit.formal.interpreter.Interpreter;
import edu.kit.formal.interpreter.KeYInterpreterFacade;
import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.KeyData;
import edu.kit.formal.interpreter.data.State;
import edu.kit.formal.interpreter.dbg.Debugger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the Debugger MainWindow
 * @author S.Grebing
 */
public class DebuggerMainWindowController implements Initializable {

    @FXML
    Pane rootPane;
    @FXML
    SplitPane splitPane;
    @FXML
    ScrollPane scrollPaneCode;
    /***********************************************************************************************************
     *      Code Area
     * **********************************************************************************************************/

    @FXML
    ScriptArea scriptArea;
    /***********************************************************************************************************
     *      MenuBar
     * **********************************************************************************************************/
    @FXML
    MenuBar menubar;
    @FXML
    Menu fileMenu;
    @FXML
    MenuItem closeMenuItem;
    /***********************************************************************************************************
     *      ToolBar
     * **********************************************************************************************************/
    @FXML
    ToolBar toolbar;
    @FXML
    Button buttonStartInterpreter;
    @FXML
    Button startDebugMode;
    /***********************************************************************************************************
     *      GoalView
     * **********************************************************************************************************/
    @FXML
    ListGoalView goalView;
    private RootModel model;
    @Setter
    private Interpreter<KeyData> interpreter;
    @Setter
    private Debugger debugger;
    private Stage stage;

    @FXML
    public void executeScript() {
        buttonStartInterpreter.setText("Interpreting...");

        startDebugMode.setDisable(true);

        String currentScript = this.model.getCurrentScript();
        KeYInterpreterFacade inter = new KeYInterpreterFacade();

        State<KeyData> s = inter.executeScriptWithKeYProblemFile(this.model.getCurrentScript(), this.model.getKeYFile());
        List<GoalNode<KeyData>> g = s.getGoals();


        this.model.getCurrentGoalNodes().addAll(g);
        buttonStartInterpreter.setText("execute Script");
    }

    @FXML
    public void changeToDebugMode() {
    }

    @FXML
    protected void closeProgram() {
        System.exit(0);
    }

    @FXML
    protected void openScript() {
        File scriptFile = openFileChooserDialog("Select Script File", "Proof Script File", "kps");
        if (scriptFile != null) {
            model.setScriptFile(scriptFile);
            this.model.currentScriptProperty().set(FileUtils.readFile(scriptFile).toString());
        }
    }

    @FXML
    protected void loadKeYFile() {
        File keyFile = openFileChooserDialog("Select KeY File", "KeY Files", "key", "java", "script");
        this.model.setKeYFile(keyFile);

    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * @param title
     * @param description
     * @param fileEndings
     * @return
     */
    private File openFileChooserDialog(String title, String description, String... fileEndings) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter(description, fileEndings));
        fileChooser.setInitialDirectory(new File("/home/sarah/Documents/KIT_Mitarbeiter/ProofScriptingLanguage/src/test/resources/edu/kit/formal/interpreter/contraposition/"));
        File file = fileChooser.showOpenDialog(this.stage);
        return file;

    }

    public void setModel(RootModel model) {
        this.model = model;
    }

    public void init() {
        scriptArea.setRootModel(this.model);
        scriptArea.init();
        goalView.setRootModel(this.model);
        goalView.init();

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
