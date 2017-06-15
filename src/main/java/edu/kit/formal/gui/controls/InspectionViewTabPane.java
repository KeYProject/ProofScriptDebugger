package edu.kit.formal.gui.controls;

import edu.kit.formal.gui.model.RootModel;
import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.KeyData;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;

import java.io.IOException;

/**
 * TabPane on the right side of the GUI containing the inspection view as tabs
 */
public class InspectionViewTabPane extends TabPane {

    private InspectionViewTab activeInterpreterTab;
    @FXML
    private InspectionViewTab inspectionViewTab;

    public InspectionViewTabPane() {
        super();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InspectionViewTabPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // this.setActiveInterpreterTab(inspectionViewTab);


        // getActiveInspectionViewTab().getGoalView().setCellFactory(GoalNodeListCell::new);


    }

    public InspectionViewTab getInspectionViewTab() {
        return inspectionViewTab;
    }

    public void setActiveInterpreterTab(InspectionViewTab activeInterpreterTab) {
        this.activeInterpreterTab = activeInterpreterTab;
    }

    public InspectionViewTab getActiveInspectionViewTab() {
        return this.activeInterpreterTab;
    }

    public void createNewInspectionViewTab(RootModel model, boolean activeTab) {
        InspectionViewTab tab = new InspectionViewTab();
        if (activeTab) {
            this.setActiveInterpreterTab(tab);
        }
        tab.getGoalView().setCellFactory(GoalNodeListCell::new);
        model.chosenContractProperty().addListener(o -> {
            tab.refresh(model);
        });
        bindGoalNodesWithCurrentTab(model);
        tab.getGoalView().setCellFactory(GoalNodeListCell::new);
        tab.setText("New Tab");
        if (activeTab) {
            this.setActiveInterpreterTab(tab);
        }
        this.getTabs().add(tab);


        // inspectionViewTabPane.getInspectionViewTab().getGoalView().setCellFactory(GoalNodeListCell::new);

    }

    public void refresh(RootModel model) {
        getActiveInspectionViewTab().refresh(model);
    }

    public void bindGoalNodesWithCurrentTab(RootModel model) {
        getActiveInspectionViewTab().getGoalView().itemsProperty().bind(model.currentGoalNodesProperty());
        model.currentSelectedGoalNodeProperty().addListener((p, old, fresh) -> {
            getActiveInspectionViewTab().getGoalView().getSelectionModel().select(fresh);
            /* TODO get lines of active statements marked lines
            javaSourceCode.getMarkedLines().clear();
            javaSourceCode.getMarkedLines().addAll(
            );*/
        });

    }

    /**
     * Cells for GoalView
     */
    private class GoalNodeListCell extends ListCell<GoalNode<KeyData>> {

        public GoalNodeListCell(ListView<GoalNode<KeyData>> goalNodeListView) {
            itemProperty().addListener(this::update);

            // goalOptionsMenu.selectedViewOptionProperty().addListener(this::update);
        }

        private void update(Observable observable) {
            if (getItem() == null) {
                setText("");
                return;
            }
            KeyData item = getItem().getData();
            String text = "n/a";
           /* if (goalOptionsMenu.getSelectedViewOption() != null) {
                text = goalOptionsMenu.getSelectedViewOption().getText(item);
            }*/
            setText(text);
        }
    }


}
