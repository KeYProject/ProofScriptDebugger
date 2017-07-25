package edu.kit.formal.gui.controls;

import edu.kit.formal.gui.model.InspectionModel;
import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.KeyData;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

/**
 * Right part of the splitpane that displays the different parts of a state
 *
 * @author S. Grebing
 */
public class InspectionView extends BorderPane {
    private final ReadOnlyObjectProperty<InspectionModel> model = new SimpleObjectProperty<>(
            new InspectionModel()
    );
    public GoalOptionsMenu goalOptionsMenu = new GoalOptionsMenu();
    @FXML
    private SequentView sequentView;
    @FXML
    private ListView<GoalNode<KeyData>> goalView;

    public InspectionView() {
        Utils.createWithFXML(this);

        model.get().selectedGoalNodeToShowProperty().addListener((o, a, b) -> {
            goalView.getSelectionModel().select(b);
        });
        goalView.getSelectionModel().selectedItemProperty().addListener((o, a, b) -> {
            model.get().setSelectedGoalNodeToShow(b);
            model.get().setCurrentInterpreterGoal(b);
        });

        model.get().currentInterpreterGoalProperty().addListener(
                (observable, oldValue, newValue) -> {
                    goalView.getSelectionModel().select(newValue);
                    if (newValue != null && newValue.getData() != null) {
                        getSequentView().setNode(newValue.getData().getNode());
                        getSequentView().setGoal(newValue.getData().getGoal());
                        //set Java lines to highlight in model
                        model.get().setHighlightedJavaLines(FXCollections.observableSet(newValue.getData().constructLinesSet()));
                    }
                });

        model.get().goalsProperty().bindBidirectional(goalView.itemsProperty());


        getGoalView().setCellFactory(GoalNodeListCell::new);

        Utils.addDebugListener(model.get().goalsProperty());
        Utils.addDebugListener(model.get().selectedGoalNodeToShowProperty());
        Utils.addDebugListener(model.get().currentInterpreterGoalProperty());
        Utils.addDebugListener(model.get().highlightedJavaLinesProperty());

        /*TODO redefine CSS bases on selected mode
        mode.addListener(o -> {
            getStyleClass().removeAll(
                    Mode.DEAD.name(),
                    Mode.LIVING.name(),
                    Mode.POSTMORTEM.name()
            );
            getStyleClass().add(mode.get().name());
        });
         */
    }


    public SequentView getSequentView() {
        return sequentView;
    }


    public ListView<GoalNode<KeyData>> getGoalView() {
        return goalView;
    }

    public GoalOptionsMenu getGoalOptionsMenu() {
        return goalOptionsMenu;
    }

    public void showGoalOptions(MouseEvent actionEvent) {
        Node n = (Node) actionEvent.getTarget();
        goalOptionsMenu.show(n, actionEvent.getScreenX(), actionEvent.getScreenY());
    }

    public InspectionModel getModel() {
        return model.get();
    }

    public ReadOnlyObjectProperty<InspectionModel> modelProperty() {
        return model;
    }

    /**
     * Cells for GoalView
     */
    private class GoalNodeListCell extends ListCell<GoalNode<KeyData>> {

        public GoalNodeListCell(ListView<GoalNode<KeyData>> goalNodeListView) {
            itemProperty().addListener(this::update);
            goalOptionsMenu.selectedViewOptionProperty().addListener(this::update);
        }

        private void update(Observable observable) {
            if (getItem() == null) {
                setText("");
                return;
            }
            KeyData item = getItem().getData();
            String text = "n/a";
            if (goalOptionsMenu.getSelectedViewOption() != null) {
                text = goalOptionsMenu.getSelectedViewOption().getText(item);
            }
            setText(text);
        }
    }
}