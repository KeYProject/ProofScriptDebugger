package edu.kit.iti.formal.psdbg.gui.controls;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.uka.ilkd.key.pp.LogicPrinter;
import de.uka.ilkd.key.proof.Node;
import edu.kit.iti.formal.psdbg.LabelFactory;
import edu.kit.iti.formal.psdbg.gui.controller.Events;
import edu.kit.iti.formal.psdbg.gui.controls.ScriptTree.AbstractTreeNode;
import javafx.collections.ObservableList;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TreeItem;
import lombok.Setter;

import java.util.function.Consumer;

public class ScriptTreeContextMenu extends javafx.scene.control.ContextMenu {
    MenuItem copyBranchLabel = new MenuItem("Branch Label");
    MenuItem copyProgramLines = new MenuItem("Program Lines");
    MenuItem createCases = new MenuItem("Created Case for Open Goals");

    MenuItem showSequent = new MenuItem("Show Sequent");
    MenuItem showGoal = new MenuItem("Show in Goal List");

    MenuItem expandAllNodes = new MenuItem("Expand Tree from here");
    MenuItem collapseAllNodes = new MenuItem("Collapse Tree from here");


    private ScriptTreeView scriptTreeView;

    public ScriptTreeContextMenu(ScriptTreeView scriptTreeView) {
        this.scriptTreeView = scriptTreeView;

        expandAllNodes.setOnAction((event) -> {
            scriptTreeView.consumeNode(n ->
                    expandNodeToLeaves(n), "");
        });

        collapseAllNodes.setOnAction((event) -> {
            scriptTreeView.consumeNode(n ->
                    collapseNodeToLeaves(n), "");
        });

        showSequent.setOnAction((event -> {
            scriptTreeView.consumeNode(n ->
                    Events.fire(new Events.ShowSequent(((AbstractTreeNode) n.getValue()).getNode())), "");
        }));

        copyBranchLabel.setOnAction(evt -> scriptTreeView.consumeNode(n -> {
            Utils.intoClipboard(
                    LabelFactory.getBranchingLabel(((AbstractTreeNode) n.getValue()).getNode()));
        }, "Copied!"));

        getItems().setAll(expandAllNodes, collapseAllNodes, new SeparatorMenuItem(), showSequent, createCases); //, new SeparatorMenuItem(), createCases, showSequent, showGoal);
        setAutoFix(true);
        setAutoHide(true);
    }


    static void expandNodeToLeaves(TreeItem candidate) {
        if (candidate != null) {
            if (!candidate.isLeaf()) {
                candidate.setExpanded(true);
                ObservableList<TreeItem> children = candidate.getChildren();
                children.forEach(treeItem -> expandNodeToLeaves(treeItem));

            }
        }
    }

    static void collapseNodeToLeaves(TreeItem candidate) {
        if (candidate != null) {
            if (!candidate.isLeaf()) {
                candidate.setExpanded(false);
                ObservableList<TreeItem> children = candidate.getChildren();
                children.forEach(treeItem -> expandNodeToLeaves(treeItem));

            }
        }
    }

}
