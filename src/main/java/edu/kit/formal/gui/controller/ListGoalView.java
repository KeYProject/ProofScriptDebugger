package edu.kit.formal.gui.controller;

import edu.kit.formal.gui.model.RootModel;
import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.KeyData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Created by sarah on 5/27/17.
 */
public class ListGoalView extends VBox {
    @FXML
    private ListView<GoalNode<KeyData>> listOfGoals;

    @FXML
    private TextArea goalNodeView;

    private RootModel rootModel;

    public ListGoalView() {


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GoalView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        listOfGoals.setCellFactory(list -> new GoalNodeCell());

    }


    public void setRootModel(RootModel rootModel) {
        this.rootModel = rootModel;
    }

    public void init() {
        this.listOfGoals.itemsProperty().bind(this.rootModel.currentGoalNodesProperty());
        // this.listOfGoals.itemsProperty().bind(this.rootModel.currentGoalNodesProperty());
        goalNodeView.textProperty().bind(listOfGoals.getSelectionModel().selectedItemProperty().asString());
    }

    private static class GoalNodeCell extends ListCell<GoalNode<KeyData>> {

        @Override
        protected void updateItem(GoalNode<KeyData> item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setText(item.toListLabelForKeYData());
            }
        }

    }
}
