package edu.kit.iti.formal.psdbg.gui.controls;

import com.sun.javafx.css.Style;
import de.uka.ilkd.key.proof.Node;
import edu.kit.iti.formal.psdbg.gui.controller.DebuggerMain;
import edu.kit.iti.formal.psdbg.gui.controller.Events;
import edu.kit.iti.formal.psdbg.gui.controls.ScriptTree.*;
import edu.kit.iti.formal.psdbg.gui.model.DebuggerMainModel;
import edu.kit.iti.formal.psdbg.interpreter.KeYProofFacade;
import edu.kit.iti.formal.psdbg.interpreter.dbg.PTreeNode;
import javafx.beans.binding.Bindings;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Displays a Treeview of the ScriptTree, which represents state of proof with
 *  - statements in the script
 *  - branching labels
 *  - goal nodes
 *
 * @author An.Luong
 */
public class ScriptTreeView extends BorderPane {

    @Setter
    @Getter
    private ScriptTreeGraph stg;

    private ScriptTreeContextMenu contextMenu;

    private AbstractTreeNode rootNode;
    private Map<Node, AbstractTreeNode> mapping;

    @FXML
    TreeView<AbstractTreeNode> treeView;


    public ScriptTreeView(DebuggerMain main) {
        Utils.createWithFXML(this);
        treeView.setCellFactory(this::cellFactory);
        stg = new ScriptTreeGraph();

        setOnContextMenuRequested(evt -> {
            getContextMenu().show(this, evt.getScreenX(), evt.getScreenY());
        });
    }

    public void setTree(TreeItem<AbstractTreeNode> tree) {
        treeView.setRoot(tree);
        treeView.refresh();
    }

    private TreeCell<AbstractTreeNode> cellFactory(TreeView<AbstractTreeNode> nodeTreeView) {


        TextFieldTreeCell<AbstractTreeNode> tftc = new TextFieldTreeCell<>();
        StringConverter<AbstractTreeNode> stringConverter = new StringConverter<AbstractTreeNode>() {
            @Override
            public String toString(AbstractTreeNode object) {
                return object.toTreeNode().label;
            }

            @Override
            public AbstractTreeNode fromString(String string) {
                return null;
            }
        };
        tftc.setConverter(stringConverter);

        tftc.itemProperty().addListener((p, o, n) -> {
            if (n != null) {
                repaint(tftc);
            } else {
                tftc.setStyle("");
            }
        });

        return tftc;
    }


    /**
     * Build ScriptTreeView with ScriptTreeGraph and displays it in the view
     */
    public void toView() {

        TreeItem<AbstractTreeNode> treeItem = new TreeItem<>(new AbstractTreeNode(null));
        rootNode = stg.getRootNode();
        mapping = stg.getMapping();

        if (rootNode instanceof DummyGoalNode) {
            treeItem.getChildren().add(new TreeItem<>(rootNode));
            this.setTree(treeItem);
            return;
        }

        // first scriptcommand not executed yet
        if (mapping.get(rootNode.getNode()) == null) {
            treeItem.getChildren().add(new TreeItem<>(new DummyGoalNode(rootNode.getNode(), rootNode.getNode().isClosed())));
            this.setTree(treeItem);
            return;
        }

        List<AbstractTreeNode> children = mapping.get(rootNode.getNode()).getChildren();

        if (children == null) {
            this.setTree(treeItem);
            return;
        }

        treeItem.getChildren().add(new TreeItem<>(mapping.get(rootNode.getNode())));

        while (children.size() == 1) {
            treeItem.getChildren().add(new TreeItem<>(children.get(0)));
            children = children.get(0).getChildren();
            if (children == null) {
                this.setTree(treeItem);
                return;
            }
        }

        if (children.size() != 0) {
            List<TreeItem> subTreeItems = new ArrayList<>();
            children.forEach(k -> subTreeItems.add(rekursiveToView(k)));
            for (TreeItem item : subTreeItems) {
                treeItem.getChildren().add(item);
            }
        }

        this.setTree(treeItem);
    }

    private TreeItem<AbstractTreeNode> rekursiveToView(AbstractTreeNode current) {
        TreeItem<AbstractTreeNode> treeItem = new TreeItem<>(current); //

        List<AbstractTreeNode> children = current.getChildren();


        while (children != null && children.size() == 1) {
            if(children.get(0) == null) return treeItem;
            treeItem.getChildren().add(new TreeItem<>(children.get(0)));
            children = children.get(0).getChildren();
        }
        if (children == null) {
            return treeItem;
        }

        if (children.size() != 0) {
            children.forEach(k -> treeItem.getChildren().add(rekursiveToView(k)));
        }
        return treeItem;
    }

    private void repaint(TextFieldTreeCell<AbstractTreeNode> tftc) {
        AbstractTreeNode item = tftc.getItem();
        Node n = item.getNode();
        tftc.styleProperty().unbind();
        tftc.setStyle("");
        if (n != null) {
            if(item instanceof ScriptTreeNode) {
                if (!item.isSucc()) {
                    tftc.setStyle("-fx-text-fill: grey");
                }
            } else if (item instanceof BranchLabelNode) {
                tftc.setStyle("-fx-text-fill: blue");
            } else if (item instanceof ForeachTreeNode) {

            } else if (item instanceof DummyGoalNode) {
                if (n.isClosed()) {
                    tftc.styleProperty().setValue("-fx-background-color: lightgreen");
                    //styleProperty().bind(tftc.styleProperty());


                } else {
                    tftc.styleProperty().setValue("-fx-background-color: indianred");
                    //styleProperty().bind(tftc.styleProperty());
                    // tftc.setStyle("-fx-background-color: indianred");
                    //colorOfNodes.putIfAbsent(n, "indianred");
                }
            }

        }
    }

    public ContextMenu getContextMenu() {
        if (contextMenu == null) {
            contextMenu = new ScriptTreeContextMenu(this);
        }
        return contextMenu;
    }

    public void consumeNode(Consumer<TreeItem> consumer, String success) {
        TreeItem<AbstractTreeNode> item = treeView.getSelectionModel().getSelectedItem();
        if (item != null) {
            consumer.accept(item);
        } else {
            Events.fire(new Events.PublishMessage("Current item does not have a node.", 2));
        }
    }
}