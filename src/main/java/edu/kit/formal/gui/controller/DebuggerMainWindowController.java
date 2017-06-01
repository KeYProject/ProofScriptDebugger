package edu.kit.formal.gui.controller;

import de.uka.ilkd.key.speclang.Contract;
import edu.kit.formal.gui.FileUtils;
import edu.kit.formal.gui.model.RootModel;
import edu.kit.formal.interpreter.KeYProofFacade;
import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.KeyData;
import edu.kit.formal.interpreter.dbg.Debugger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Setter;
import org.controlsfx.dialog.Wizard;

import java.io.File;
import java.net.URL;
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
    private static String testFile1 = "/home/sarah/Documents/KIT_Mitarbeiter/ProofScriptingLanguage/src/test/resources/edu/kit/formal/interpreter/";

    private static String testFile = "/home/sarah/Documents/KIT_Mitarbeiter/ProofScriptingLanguage/src/test/resources/edu/kit/formal/interpreter/contraposition/";

    @FXML
    Pane rootPane;
    @FXML
    SplitPane splitPane;

    /***********************************************************************************************************
     *      Code Area
     * **********************************************************************************************************/
    @FXML
    ScrollPane scrollPaneCode;
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
    private ExecutorService executorService = null;


    private KeYProofFacade facade;


    private Wizard contractChooserDialog = new Wizard();

    private ContractLoaderService cls;


    /**
     * Model for the DebuggerController containing the neccessary references to objects needed for controlling backend through UI
     */
    private RootModel model;

    @Setter
    private Debugger debugger;

    private Stage stage;

    @FXML
    public void executeScript() {
        buttonStartInterpreter.setText("Interpreting...");
        startDebugMode.setDisable(true);
        facade.executeScript(model.getCurrentScript());
        List<GoalNode<KeyData>> g = model.getCurrentState().getGoals();
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
        File keyFile = openFileChooserDialog("Select KeY File", "KeY Files", "key", "script");
        this.model.setKeYFile(keyFile);
        if (keyFile != null) {
            buildKeYProofFacade();
        }

    }


    @FXML
    protected void loadJavaFile() {
        File javaFile = openFileChooserDialog("Select Java File", "Java Files", "java", "script");
        this.model.setJavaFile(javaFile);

        if (javaFile != null) {

            facade = new KeYProofFacade(model);

            cls.setRefToRootModel(this.model);
            cls.start();
            cls.setOnSucceeded(event -> {
                model.getLoadedContracts().addAll((List<Contract>) event.getSource().getValue());
                ContractChooser page = new ContractChooser(this.model.loadedContractsProperty(), this.model.getChosenContract());
                // contractChooserDialog.setFlow(new Wizard.LinearFlow(new ContractChooser(this.model.loadedContractsProperty(), this.model.getChosenContract())));

                contractChooserDialog.setFlow(new Wizard.LinearFlow(page));

                contractChooserDialog.showAndWait().ifPresent(result -> {
                    if (result == ButtonType.FINISH) {
                        // System.out.println(contractChooserDialog.getSettings());
                        SimpleObjectProperty<Contract> chosenContractProp = page.getChosen();
                        model.setChosenContract(chosenContractProp.getValue());
                        if (this.model.getChosenContract() != null) {
                            buildJavaProofFacade();
                            System.out.println("Proof Facade is built");
                        } else {
                            System.out.println("Something went wrong");
                        }
                    }

                });
            });
        }


    }



    public void setStage(Stage stage) {
        this.stage = stage;
    }


    /**
     * Needs to be set from calling method
     *
     * @param model
     */
    public void setModel(RootModel model) {
        this.model = model;
    }

    /**
     *
     */
    public void init() {
        facade = new KeYProofFacade(this.model);
        scriptArea.setRootModel(this.model);
        scriptArea.init();
        goalView.setRootModel(this.model);
        goalView.init();
        // create and assign the flow
        cls = new ContractLoaderService();

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

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


    /**
     * Creates a filechooser dialog
     *
     * @param title       of the dialog
     * @param description of the files
     * @param fileEndings file that should be shown
     * @return
     */
    private File openFileChooserDialog(String title, String description, String... fileEndings) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter(description, fileEndings));
        fileChooser.setInitialDirectory(new File(testFile1));
        File file = fileChooser.showOpenDialog(this.stage);
        return file;

    }

    public class ContractLoaderService extends Service<List<Contract>> {
        @Setter
        RootModel refToRootModel;

        @Override
        protected Task<List<Contract>> createTask() {
            Task<List<Contract>> task1 = new Task<List<Contract>>() {
                @Override
                protected List<Contract> call() throws Exception {
                    if (refToRootModel != null) {
                        List<Contract> contracts = facade.getContractsForJavaFile(refToRootModel.getJavaFile());
                        System.out.println("Loaded Contracts " + contracts.toString());
                        return contracts;
                    } else {
                        throw new RuntimeException("No Reference to Rootmodel");
                    }
                }
            };
            System.out.println("Task1 created");
            return task1;
        }
    }



}
