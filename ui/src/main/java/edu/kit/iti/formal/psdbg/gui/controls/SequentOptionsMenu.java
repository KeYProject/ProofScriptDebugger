package edu.kit.iti.formal.psdbg.gui.controls;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
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


    @FXML
    private MenuItem openSequentMatcher;


    public SequentOptionsMenu() {
        Utils.createWithFXML(this);

        openSequentMatcher.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                //TODO: Abchecken ob überhaupt eine Sequenz vorhanden ist

                try{
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SequentMatcher.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Sequent Matcher");
                    stage.setScene(new Scene(root1));
                    stage.show();

                    //TODO: probably have to add a few things here (Lulu)

                } catch (Exception e){
                    System.out.println(e);
                }
            }
        });

    }






}