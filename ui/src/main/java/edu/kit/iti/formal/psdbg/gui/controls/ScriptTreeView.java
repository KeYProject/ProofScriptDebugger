package edu.kit.iti.formal.psdbg.gui.controls;

import javafx.fxml.FXML;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;

public class ScriptTreeView extends BorderPane {
    @FXML
    TreeView<TreeNode> treeView;

    public ScriptTreeView() {
        Utils.createWithFXML(this);

        treeView = new TreeView<>();
        treeView.setCellFactory(this::cellFactory);

    }

    public void setTree(TreeItem<TreeNode> tree) {
        treeView.setRoot(tree);
        treeView.refresh();
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
        /*
        tftc.itemProperty().addListener((p, o, n) -> {
            if (n != null)
                repaint(tftc);
        }); */

        //colorOfNodes.addListener((InvalidationListener) o -> repaint(tftc));
        return tftc;
    }
}
