package edu.kit.iti.formal.psdbg.gui.controls;

import com.sun.javafx.css.Style;
import de.uka.ilkd.key.proof.Node;
import edu.kit.iti.formal.psdbg.gui.controller.DebuggerMain;
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
import lombok.Setter;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScriptTreeView extends BorderPane {

    @Setter
    private ScriptTreeGraph stg;

    private ContextMenu contextMenu;

    private ScriptTreeNode rootNode;
    private Map<Node, AbstractTreeNode> mapping;

    @Setter
    private DebuggerMainModel model;
    @Setter
    private KeYProofFacade FACADE;
    /**
     * Contains color of nodes
     */
    private MapProperty<Node, String> colorOfNodes = new SimpleMapProperty<Node, String>(FXCollections.observableHashMap());


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
            if (n != null)
                repaint(tftc);
        });


        //colorOfNodes.addListener((InvalidationListener) o -> repaint(tftc));
        return tftc;
    }


    /**
     * returns treeItem that represents current Script tree
     * @return
     */
    public TreeItem<AbstractTreeNode> toView() {
        TreeItem<AbstractTreeNode> treeItem;
        PTreeNode startnode;
        try {
            startnode = (model.getDebuggerFramework() != null) ?
                    model.getDebuggerFramework().getPtreeManager().getStartNode() :
                    null;
        } catch (NullPointerException e) {
            treeItem = new TreeItem<>(new AbstractTreeNode(null));
            DummyGoalNode dummy = new DummyGoalNode(null, false);
            treeItem.getChildren().add(new TreeItem<>(dummy));

            this.setTree(treeItem);
            return treeItem;
        }

        //No script executed
        if (startnode == null) {
            System.out.println("Entered maybe redundaant toview(inside) method"); //TODO
            treeItem = new TreeItem<>(new AbstractTreeNode(null));
            DummyGoalNode dummy = new DummyGoalNode(null, false);
            treeItem.getChildren().add(new TreeItem<>(dummy));

            this.setTree(treeItem);
            return treeItem;
        }
        stg.createGraph(startnode, FACADE.getProof().root());

        rootNode = stg.getRootNode();
        mapping = stg.getMapping();

        treeItem = new TreeItem<>(new AbstractTreeNode(null));


        List<AbstractTreeNode> children = mapping.get(rootNode.getNode()).getChildren();
        if (children == null) return treeItem;
        treeItem.getChildren().add(new TreeItem<>(mapping.get(rootNode.getNode())));

        while (children.size() == 1) {
            treeItem.getChildren().add(new TreeItem<>(children.get(0)));
            children = children.get(0).getChildren();
            if(children == null) return treeItem;
        }

        if (children.size() != 0) {
            children.forEach(k -> treeItem.getChildren().add(rekursiveToView(k)));
        }

        this.setTree(treeItem);
        return treeItem;
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
}
