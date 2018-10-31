package edu.kit.iti.formal.psdbg.gui.controls;

import de.uka.ilkd.key.proof.Node;
import edu.kit.iti.formal.psdbg.gui.controller.DebuggerMain;
import edu.kit.iti.formal.psdbg.gui.controls.ScriptTree.AbstractTreeNode;
import edu.kit.iti.formal.psdbg.gui.controls.ScriptTree.BranchLabelNode;
import edu.kit.iti.formal.psdbg.gui.controls.ScriptTree.DummyGoalNode;
import edu.kit.iti.formal.psdbg.gui.controls.ScriptTree.ScriptTreeNode;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

public class ScriptTreeView extends BorderPane {

    @Setter
    private ScriptTreeGraph stg;

    private MapProperty<Node, String> colorOfNodes = new SimpleMapProperty<Node, String>(FXCollections.observableHashMap());
    @FXML
    TreeView<TreeNode> treeView;

    public ScriptTreeView(DebuggerMain main) {
        Utils.createWithFXML(this);
        treeView.setCellFactory(this::cellFactory);

    }

    public void setTree(TreeItem<TreeNode> tree) {
        treeView.setRoot(tree);
    }

    private TreeCell<TreeNode> cellFactory(TreeView<TreeNode> nodeTreeView) {
        TextFieldTreeCell<TreeNode> tftc = new TextFieldTreeCell<>();
        StringConverter<TreeNode> stringConverter = new StringConverter<TreeNode>() {
            @Override
            public String toString(TreeNode object) {
                return object.label;
            }

            @Override
            public TreeNode fromString(String string) {
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

    /*
    public TreeItem<TreeNode> toView () {
        TreeItem<TreeNode> treeItem;
        ScriptTreeNode rootNode = stg.getRootNode();
        Map<Node, AbstractTreeNode> mapping = stg.getMapping();
        if(rootNode == null) {
            treeItem = new TreeItem<>(new TreeNode("Proof", null));
            DummyGoalNode dummy = new DummyGoalNode(null, false);
            treeItem.getChildren().add(new TreeItem<>(dummy.toTreeNode()));
            return treeItem;
        }
        treeItem = new TreeItem<>(new TreeNode("Proof", rootNode.getNode()));


        List<AbstractTreeNode> children = mapping.get(rootNode.getNode()).getChildren();
        if (children == null) return treeItem;
        treeItem.getChildren().add(new TreeItem<>(mapping.get(rootNode.getNode()).toTreeNode()));

        while (children.size() == 1) {
            treeItem.getChildren().add(new TreeItem<>(children.get(0).toTreeNode()));
            children = children.get(0).getChildren();
            if(children == null) return treeItem;
        }

        if (children.size() != 0) {
            children.forEach(k -> treeItem.getChildren().add(rekursiveToView(k)));
        }
        return treeItem;
    }

    private TreeItem<TreeNode> rekursiveToView (AbstractTreeNode current){
        TreeItem<TreeNode> treeItem = new TreeItem<>(current.toTreeNode()); //

        List<AbstractTreeNode> children = current.getChildren();


        while (children != null && children.size() == 1) {
            if(children.get(0) == null) return treeItem;
            treeItem.getChildren().add(new TreeItem<>(children.get(0).toTreeNode()));
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
*/
    private void repaint(TextFieldTreeCell<TreeNode> tftc) {
        TreeNode item = tftc.getItem();
        Node n = item.node;
        tftc.setStyle("");
        if (n != null) {
            if (n.leaf() && !item.label.contains("BRANCH")) {
                if (n.isClosed()) {
                    colorOfNodes.putIfAbsent(n, "lightseagreen");
                    //tftc.setStyle("-fx-background-color: greenyellow");
                } else {
                    colorOfNodes.putIfAbsent(n, "indianred");
                }

                if(stg.getMapping().get(n) instanceof BranchLabelNode){
                    colorOfNodes.putIfAbsent(n, "gray");
                }

                if (colorOfNodes.containsKey(n)) {
                    tftc.setStyle("-fx-background-color: " + colorOfNodes.get(n) + ";");
                }
            }

        }
    }
}
