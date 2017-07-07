package edu.kit.formal.gui.controls;

import edu.kit.formal.gui.model.RootModel;
import org.dockfx.DockNode;

/**
 * TabPane on the right side of the GUI containing the inspection view as tabs
 */
public class InspectionViewTabPane {

    /**
     * active tab in which the interpreter resp. Debugger state is shown.
     * This tab can be changed and later on in this tab it should be possible to select proof commands
     * All other tabs are only post morten tabs which cannot be shown
     */
    private final InspectionViewTab activeInterpreterTab = new InspectionViewTab();
    private final DockNode activeInterpreterTabDock = new DockNode(activeInterpreterTab, "Active");

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


    //TODO schauen wie Goallist ins model kommt
    public void bindGoalNodesWithCurrentTab(RootModel model) {

        getActiveInspectionViewTab().getGoalView().itemsProperty().bind(model.currentGoalNodesProperty());
        model.currentSelectedGoalNodeProperty().addListener((p, old, fresh) -> {
            getActiveInspectionViewTab().getGoalView().getSelectionModel().select(fresh);

            /* TODO get lines of active statements marked lines
            javaSourceCode.getMarkedRegions().clear();
            javaSourceCode.getMarkedRegions().addAll(
            );*/
        });

    }
}
