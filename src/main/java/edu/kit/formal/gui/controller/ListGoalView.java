package edu.kit.formal.gui.controller;

import edu.kit.formal.gui.model.RootModel;
import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.KeyData;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    protected SimpleListProperty<GoalNode<KeyData>> localGoalListProperty = new SimpleListProperty<>();
    @FXML
    private ListView<GoalNode<KeyData>> listOfGoalsView;
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

        listOfGoalsView.setCellFactory(list -> new GoalNodeCell());

    }


    public void setRootModel(RootModel rootModel) {
        this.rootModel = rootModel;
    }

    /**
     * Set Bindings and listener
     */
    public void init() {
        listOfGoalsView.itemsProperty().bind(this.rootModel.currentGoalNodesProperty());

        listOfGoalsView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<GoalNode<KeyData>>() {
            @Override
            public void changed(ObservableValue<? extends GoalNode<KeyData>> observable, GoalNode<KeyData> oldValue, GoalNode<KeyData> newValue) {
                goalNodeView.setText(newValue.toCellTextForKeYData());
            }
        });

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
