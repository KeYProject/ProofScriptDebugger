package edu.kit.formal.gui.controls;

import edu.kit.formal.gui.model.RootModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TabPane;

import java.io.IOException;

/**
 * TabPane on the right side of the GUI containing the inspection view as tabs
 */
public class InspectionViewTabPane extends TabPane {

    /**
     * active tab in which the interpreter resp. Debugger state is shown.
     * This tab can be changed and later on in this tab it should be possible to select proof commands
     * All other tabs are only post morten tabs which cannot be shown
     */
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
            System.out.println(this.getActiveInspectionViewTab() == null);
            this.setActiveInterpreterTab(tab);
            tab.setText("Active Tab");
            tab.setClosable(false);
            this.setActiveInterpreterTab(tab);
        }

        model.chosenContractProperty().addListener(o -> {
            tab.refresh(model);
        });
        bindGoalNodesWithCurrentTab(model);

        this.getTabs().add(tab);



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



}
