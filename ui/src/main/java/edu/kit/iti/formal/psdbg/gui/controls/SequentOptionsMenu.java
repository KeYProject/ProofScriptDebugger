package edu.kit.iti.formal.psdbg.gui.controls;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
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

                //TODO: Abchecken ob überhaupt eine Sequenz vorhanden ist

                try {
                    SequentMatcher root1 = new SequentMatcher();
                    root1.setGoals(model.getGoals());
                    root1.setSelectedGoalNodeToShow(model.getSelectedGoalNodeToShow());
                    Stage stage = new Stage();
                    stage.setTitle("Sequent Matcher");
                    stage.setScene(new Scene(root1));
                    stage.show();

                    //TODO: probably have to add a few things here (Lulu)

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e);
                }
            }
        });

    }


}