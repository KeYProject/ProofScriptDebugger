package edu.kit.iti.formal.psdbg.gui.controls;

import de.uka.ilkd.key.proof.Node;
import edu.kit.iti.formal.psdbg.gui.model.InspectionModel;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.dockfx.DockNode;
import org.dockfx.DockPane;

/**
 * This controller manages a list of {@link InspectionView} and the associated {@link DockNode}s.
 *
 * Especially, this class holds the active tab, which is connected with the
 * {@link edu.kit.iti.formal.psdbg.interpreter.graphs.ProofTreeController},
 * and shows the current interpreter state.
 *
 * @author weigl
 * @author Changes made by S. Grebing (4.11.17)
 */
public class InspectionViewsController {
    private final DockPane parent;

    /**
     * active tab in which the interpreter resp. Debugger state is shown.
     * This tab can be changed and later on in this tab it should be possible to select proof commands
     * All other tabs are only post morten tabs which cannot be shown
     */
    private final InspectionView activeInterpreterTab = new InspectionView();
    private final DockNode activeInterpreterTabDock = new DockNode(activeInterpreterTab, "Active");
    private final ObservableMap<InspectionView, DockNode> inspectionViews = new SimpleMapProperty<>(FXCollections.observableHashMap());

    public InspectionViewsController(DockPane parent) {
        this.parent = parent;
    }


    public InspectionView getActiveInspectionViewTab() {
        return this.activeInterpreterTab;
    }

    public DockNode getActiveInterpreterTabDock() {
        return activeInterpreterTabDock;
    }


    public DockNode newPostMortemInspector(InspectionModel im) {
        InspectionView iv = new InspectionView();
        iv.getModel().setSelectedGoalNodeToShow(im.getSelectedGoalNodeToShow());
        Node node = ((KeyData) iv.getModel().getSelectedGoalNodeToShow().getData()).getNode();
        String title = "Post-Mortem";
        if (node != null) {
            iv.getSequentView().setNode(node);
            title += " Node Nr. " + node.serialNr();
        }
        DockNode dockNode = new DockNode(iv, title);
        dockNode.closedProperty().addListener(o -> {
            inspectionViews.remove(iv);
        });
        inspectionViews.put(iv, dockNode);
        return dockNode;
    }



    /*public void connectActiveView(DebuggerModel model) {
        getActiveInspectionViewTab().getGoalView().itemsProperty().bind(model.currentGoalNodesProperty());
        model.currentSelectedGoalNodeProperty().addListener((p, old, fresh) -> {
            getActiveInspectionViewTab().getGoalView().getSelectionModel().select(fresh);
        });

    }*/
}
