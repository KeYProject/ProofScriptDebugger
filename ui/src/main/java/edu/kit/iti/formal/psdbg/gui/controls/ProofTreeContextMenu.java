package edu.kit.iti.formal.psdbg.gui.controls;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.uka.ilkd.key.pp.LogicPrinter;
import edu.kit.iti.formal.psdbg.LabelFactory;
import edu.kit.iti.formal.psdbg.gui.controller.Events;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

import java.util.Arrays;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (24.05.18)
 */
public class ProofTreeContextMenu extends javafx.scene.control.ContextMenu {
    MenuItem copyBranchLabel = new MenuItem("Branch Label");
    MenuItem copyProgramLines = new MenuItem("Program Lines");
    MenuItem createCases = new MenuItem("Created Case for Open Goals");
    MenuItem refresh = new MenuItem("Refresh");
    MenuItem showSequent = new MenuItem("Show Sequent");
    MenuItem showGoal = new MenuItem("Show in Goal List");
    MenuItem expandAllNodes = new MenuItem("Expand Tree");



    private ProofTree proofTree;

    public ProofTreeContextMenu(ProofTree proofTree) {
        this.proofTree = proofTree;
        refresh.setOnAction(event -> proofTree.repopulate());
        refresh.setGraphic(new MaterialDesignIconView(MaterialDesignIcon.REFRESH));

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

        expandAllNodes.setOnAction((event) -> {
            proofTree.expandRootToLeaves(proofTree.getTreeProof().getRoot());
        });
        getItems().setAll(refresh, expandAllNodes, new SeparatorMenuItem(), copy, createCases, showSequent, showGoal);
        setAutoFix(true);
        setAutoHide(true);
    }

    public void onCreateCases(javafx.event.ActionEvent evt) {
        if (proofTree.getProof() == null) {
            return;
        }

        List<String[]> labels = LabelFactory.getLabelOfOpenGoals(proofTree.getProof(),
                LabelFactory::getBranchingLabel);
        String text;
        if (labels.isEmpty()) {
            text = "// no open goals";
        } else if (labels.size() == 1) {
            text = "// only one goal";
        } else {
            int upperLimit = 0;
            /* trying to find the common suffix*/
            try {
                String[] ref = labels.get(0);
                for (; true; upperLimit++) {
                    for (String[] lbl : labels) {
                        if (!lbl[upperLimit].equals(ref[upperLimit])) {
                            break;
                        }
                    }
                    upperLimit++;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }

            int finalUpperLimit = upperLimit;
            text = labels.stream()
                    .map(a -> Arrays.stream(a, finalUpperLimit, a.length))
                    .map(s -> s.reduce((a, b) -> b + LabelFactory.SEPARATOR + a).orElse("error"))
                    .map(s -> String.format("\tcase match \"%s\" :\n\t\t//commands", s))
                    .reduce((a, b) -> a + "\n" + b)
                    .orElse("ERROR");
        }

        String s = "cases {\n" + text + "\n}";
        Events.fire(new Events.InsertAtTheEndOfMainScript(s));
        Events.fire(new Events.PublishMessage("Copied to Clipboard"));
    }
}
