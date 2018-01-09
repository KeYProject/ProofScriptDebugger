package edu.kit.iti.formal.psdbg.gui.controls;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import edu.kit.iti.formal.psdbg.gui.ProofScriptDebugger;
import edu.kit.iti.formal.psdbg.gui.model.InspectionModel;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.dockfx.DockNode;

import java.util.function.Function;

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
                    SequentMatcher root1 = new SequentMatcher();
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
                    e.printStackTrace();
                    System.out.println(e);
                }
            }
        });

    }


}