package edu.kit.formal.gui.controls;

import edu.kit.formal.gui.model.RootModel;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.dockfx.DockNode;

/**
 * TabPane on the right side of the GUI containing the inspection view as tabs
 */
public class InspectionViewsController {

    /**
     * active tab in which the interpreter resp. Debugger state is shown.
     * This tab can be changed and later on in this tab it should be possible to select proof commands
     * All other tabs are only post morten tabs which cannot be shown
     */
    private final InspectionViewTab activeInterpreterTab = new InspectionViewTab();
    private final DockNode activeInterpreterTabDock = new DockNode(activeInterpreterTab, "Active");

    private final ObservableMap<InspectionViewTab, DockNode> inspectionViews = new SimpleMapProperty<>(FXCollections.observableHashMap());

    public InspectionViewTab getActiveInspectionViewTab() {
        return this.activeInterpreterTab;
    }
    public DockNode getActiveInterpreterTabDock() {
        return activeInterpreterTabDock;
    }

    public void connectActiveView(RootModel model) {
        getActiveInspectionViewTab().getGoalView().itemsProperty().bind(model.currentGoalNodesProperty());
        model.currentSelectedGoalNodeProperty().addListener((p, old, fresh) -> {
            getActiveInspectionViewTab().getGoalView().getSelectionModel().select(fresh);
        });

    }
}
