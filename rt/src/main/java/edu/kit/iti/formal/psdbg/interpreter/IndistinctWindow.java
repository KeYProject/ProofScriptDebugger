package edu.kit.iti.formal.psdbg.interpreter;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;

import java.util.List;

public class IndistinctWindow extends VBox {

    @Getter
    int indexOfSelected = -1;

    public Button accept;

    public IndistinctWindow(ObservableList<String> matchApps) {

        this.setPrefWidth(700);
        //Center

        ListView<String> listView = new ListView<String>(matchApps);
        this.getChildren().add(listView);

        //Bottom
        accept = new Button();
        accept.setText("Accept");
        this.getChildren().add(accept);
        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                indexOfSelected = listView.getSelectionModel().getSelectedIndex();

            }
        });


    }
}
