package edu.kit.formal.gui.controls;

import edu.kit.formal.gui.model.InspectionModel;
import edu.kit.formal.gui.model.RootModel;
import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.KeyData;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
public class InspectionViewTab extends BorderPane {
    public GoalOptionsMenu goalOptionsMenu = new GoalOptionsMenu();

    @FXML
    private SequentView sequentView;

    @FXML
    private ListView<GoalNode<KeyData>> goalView;

    private ObjectProperty<InspectionModel> model = new SimpleObjectProperty<>(
            new InspectionModel()
    );

    public InspectionViewTab() {
        super();
        Utils.createWithFXML(this);

        model.get().selectedGoalNodeToShowProperty().bind(
                goalView.getSelectionModel().selectedItemProperty()
        );

        model.get().selectedGoalNodeToShowProperty().addListener(
                (observable, oldValue, newValue) -> {
                    goalView.getSelectionModel().select(newValue);
                    if (newValue != null && newValue.getData() != null) {
                        getSequentView().setNode(newValue.getData().getNode());
                        // TODO weigl: get marked lines of the program, and set it
                        model.get().highlightedJavaLinesProperty().get()
                                .clear();
                    }
                });

        model.get().goalsProperty().bindBidirectional(goalView.itemsProperty());
        getGoalView().setCellFactory(GoalNodeListCell::new);

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

    public void refresh(RootModel model) {
     /*   IProgramMethod method = (IProgramMethod) model.getChosenContract().getTarget();
        StringWriter writer = new StringWriter();
        ProgramPrinter pp = new ProgramPrinter(writer);
        try {
            pp.printFullMethodSignature(method);
            pp.printStatementBlock(method.getBody());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

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

    public InspectionModel getModel() {
        return model.get();
    }

    public ReadOnlyObjectProperty<InspectionModel> modelProperty() {
        return model;
    }
}
