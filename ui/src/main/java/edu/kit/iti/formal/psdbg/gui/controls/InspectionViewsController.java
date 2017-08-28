package edu.kit.iti.formal.psdbg.gui.controls;

import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.dockfx.DockNode;

/**
 * This controller manages a list of {@link InspectionView} and the associated {@link DockNode}s.
 *
 * Espeically, this class holds the active tab, which is connected with the {@link edu.kit.iti.formal.psdbg.interpreter.graphs.ProofTreeController},
 * and shows the current interpreter state.
 *
 * @author weigl
 */
public class InspectionViewsController {

    /**
     * active tab in which the interpreter resp. Debugger state is shown.
     * This tab can be changed and later on in this tab it should be possible to select proof commands
     * All other tabs are only post morten tabs which cannot be shown
     */
    private final InspectionView activeInterpreterTab = new InspectionView();
    private final DockNode activeInterpreterTabDock = new DockNode(activeInterpreterTab, "Active");
    private final ObservableMap<InspectionView, DockNode> inspectionViews = new SimpleMapProperty<>(FXCollections.observableHashMap());

    public InspectionView getActiveInspectionViewTab() {
        return this.activeInterpreterTab;
    }

    public DockNode getActiveInterpreterTabDock() {
        return activeInterpreterTabDock;
    }


    public DockNode newPostMortemInspector() {
        InspectionView iv = new InspectionView();
        DockNode dn = new DockNode(iv, "post mortem: ");
        inspectionViews.put(iv, dn);
        return dn;
    }

    /*public void connectActiveView(DebuggerModel model) {
        getActiveInspectionViewTab().getGoalView().itemsProperty().bind(model.currentGoalNodesProperty());
        model.currentSelectedGoalNodeProperty().addListener((p, old, fresh) -> {
            getActiveInspectionViewTab().getGoalView().getSelectionModel().select(fresh);
        });

    }*/
}
