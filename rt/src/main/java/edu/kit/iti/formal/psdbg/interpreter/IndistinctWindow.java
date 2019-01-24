package edu.kit.iti.formal.psdbg.interpreter;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import lombok.Getter;

import java.util.List;

public class IndistinctWindow extends BorderPane {

    @Getter
    int indexOfSelected = -1;

    public Button accept;

    public IndistinctWindow(ObservableList<String> matchApps) {

        //Top
        ScrollPane scrollPane = new ScrollPane();
        ListView<String> listView = new ListView<String>(matchApps);
        scrollPane.setContent(listView);
        this.setTop(scrollPane);
        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                indexOfSelected = listView.getSelectionModel().getSelectedIndex();

            }
        });

        //Center
        /*
        HBox hbox = new HBox();
        Text inputlabel = new Text("Enter a number:");
        TextField textField = new TextField();
        hbox.getChildren().addAll(inputlabel, textField);
        this.setCenter(hbox);
*/

        //Bottom
        accept = new Button();
        accept.setText("Accept");
        this.setBottom(accept);

    }
}
