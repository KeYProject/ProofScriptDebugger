package edu.kit.iti.formal.psdbg.gui.controls;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.uka.ilkd.key.pp.LogicPrinter;
import edu.kit.iti.formal.psdbg.LabelFactory;
import edu.kit.iti.formal.psdbg.gui.controller.Events;
import javafx.collections.ObservableList;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TreeItem;

public class ScriptTreeContextMenu extends javafx.scene.control.ContextMenu {
    MenuItem copyBranchLabel = new MenuItem("Branch Label");
    MenuItem copyProgramLines = new MenuItem("Program Lines");
    MenuItem createCases = new MenuItem("Created Case for Open Goals");
    MenuItem refresh = new MenuItem("Refresh (TOFIX)");
    MenuItem showSequent = new MenuItem("Show Sequent");
    MenuItem showGoal = new MenuItem("Show in Goal List");
    MenuItem expandAllNodes = new MenuItem("Expand Tree");

    private ScriptTreeView scriptTreeView;

    public ScriptTreeContextMenu(ScriptTreeView scriptTreeView) {
        this.scriptTreeView = scriptTreeView;

        refresh.setOnAction(event -> scriptTreeView.setTree(scriptTreeView.toView()));
        refresh.setGraphic(new MaterialDesignIconView(MaterialDesignIcon.REFRESH));

        expandAllNodes.setOnAction((event) -> {
            expandRootToLeaves(scriptTreeView.treeView.getRoot());
        });

        getItems().setAll(refresh, expandAllNodes); //, new SeparatorMenuItem(), createCases, showSequent, showGoal);
        setAutoFix(true);
        setAutoHide(true);
        /*
        copyBranchLabel.setOnAction(evt -> proofTree.consumeNode(n -> Utils.intoClipboard(
                LabelFactory.getBranchingLabel(n)), "Copied!"));

        copyProgramLines.setOnAction(evt -> {
            proofTree.consumeNode(n -> {
                Utils.intoClipboard(
                        LabelFactory.getProgramLines(n));
            }, "Copied!");
        });

        MenuItem copySequent = new MenuItem("Sequent");
        copySequent.setOnAction(evt -> {
            proofTree.consumeNode(n -> {
                assert proofTree.getServices() != null : "set KeY services!";
                String s = LogicPrinter.quickPrintSequent(n.sequent(), proofTree.getServices());
                Utils.intoClipboard(s);
            }, "Copied!");
        });

        MenuItem copyRulesLabel = new MenuItem("Rule labels");
        copyRulesLabel.setOnAction(evt -> {
            proofTree.consumeNode(n -> {
                Utils.intoClipboard(
                        LabelFactory.getRuleLabel(n));
            }, "Copied!");
        });

        MenuItem copyProgramStatements = new MenuItem("Statements");
        copyProgramStatements.setOnAction(event -> {
            proofTree.consumeNode(n -> {
                Utils.intoClipboard(
                        LabelFactory.getProgramStatmentLabel(n));
            }, "Copied!");
        });

        Menu copy = new Menu("Copy", new MaterialDesignIconView(MaterialDesignIcon.CONTENT_COPY),
                copyBranchLabel, copyProgramLines,
                copyProgramStatements, copyRulesLabel,
                copySequent);

        createCases.setOnAction(this::onCreateCases);


        showSequent.setOnAction((evt) ->
                proofTree.consumeNode(n -> Events.fire(new Events.ShowSequent(n)), ""));

        showGoal.setOnAction((evt) -> proofTree.consumeNode(n -> Events.fire(new Events.SelectNodeInGoalList(n)), "Found!"));


        //TODO SCRIPTTREE ACTION
        getItems().setAll(refresh, expandAllNodes, new SeparatorMenuItem(), copy, createCases, showSequent, showGoal);
        setAutoFix(true);
        setAutoHide(true);
*/
    }

    static void expandRootToLeaves(TreeItem candidate) {
        if (candidate != null) {
            if (!candidate.isLeaf()) {
                candidate.setExpanded(true);
                ObservableList<TreeItem> children = candidate.getChildren();
                children.forEach(treeItem -> expandRootToLeaves(treeItem));

            }
        }
    }
}
