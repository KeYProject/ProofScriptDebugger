package edu.kit.iti.formal.psdbg.gui.controls;

import edu.kit.iti.formal.psdbg.gui.model.InspectionModel;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.controlsfx.control.StatusBar;

public class SequentOptionsMenu extends ContextMenu {


    private final InspectionModel model;
    @FXML
    private MenuItem openSequentMatcher;


    public SequentOptionsMenu(InspectionModel model) {
        Utils.createWithFXML(this);
        this.model = model;
        openSequentMatcher.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {


                try {
                    // passt schon!

                    KeyData data = (KeyData) model.getSelectedGoalNodeToShow().getData();
                    SequentMatcher root1 = new SequentMatcher(data.getProof().getServices());
                    root1.setGoals(model.getGoals());
                    root1.setSelectedGoalNodeToShow(model.getSelectedGoalNodeToShow());
                    root1.getStyleClass().add("sequent-view");

                    Stage stage = new Stage();
                    stage.setTitle("Sequent Matcher");
                    Scene scene = new Scene(root1);
                    scene.getStylesheets().addAll(
                            // ProofScriptDebugger.class.getClass().getResource("debugger-ui.css").toExternalForm()
                            getClass().getResource("/edu/kit/iti/formal/psdbg/gui/debugger-ui.css")
                                    .toExternalForm());

                    stage.setScene(scene);

                    stage.show();


                } catch (Exception e) {
                    Utils.showInfoDialog("Please Select a Goal first." ,
                            "Please Select a Goal node from the list first to open the SequentMatcher Window.",
                            "Please Select a Goal node from the list first to open the SequentMatcher Window.", e);
                  //  e.printStackTrace();
                   // System.out.println(e);
                }
            }
        });

    }


}