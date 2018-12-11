package edu.kit.iti.formal.psdbg.gui.controls;

import edu.kit.iti.formal.psdbg.gui.controller.DebuggerMain;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.util.Set;

/**
 * View to change font sizes
 *
 * @author An.Luong
 */
public class ViewSettings extends BorderPane {


    @FXML
    private TextField scriptAreaSize;

    @FXML
    private TextField contextAreaSize;

    @FXML
    private TextField sequentSize;

    @FXML
    private TextField javaCodeSize;

    @FXML
    private TextField allSizes;


    @FXML
    private Button apply;

    @FXML
    private Button reset;

    private DebuggerMain dm;

    final private float OLD_SCRIPTSIZE = 16; // -fx-font-size: 16pt;
    final private float OLD_CONTEXTMENUSIZE = 12;
    final private float OLD_SEQUENTSIZE = 21;
    final private float OLD_JAVACODESIZE = 16;

    public ViewSettings(DebuggerMain dm) {
        Utils.createWithFXML(this);
        this.dm = dm;

        scriptAreaSize.setText(Float.toString(OLD_SCRIPTSIZE));
        contextAreaSize.setText(Float.toString(OLD_CONTEXTMENUSIZE));
        sequentSize.setText(Float.toString(OLD_SEQUENTSIZE));
        javaCodeSize.setText(Float.toString(OLD_JAVACODESIZE));

        // add action listeners
        apply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                applyChanges();
            }
        });

        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                resetChanges();
            }
        });

    }

    private void applyChanges() {
        float allSizes_f;
        float scriptAreaSize_f;
        float contextAreaSize_f;
        float sequentSize_f;
        float javaCodeSize_f;
        if (allSizes.getText().equals("")) {
            try {
                scriptAreaSize_f = Float.parseFloat(scriptAreaSize.getText());
                contextAreaSize_f = Float.parseFloat(contextAreaSize.getText());
                sequentSize_f = Float.parseFloat(sequentSize.getText());
                javaCodeSize_f = Float.parseFloat(javaCodeSize.getText());
            } catch (NumberFormatException e) {
                Utils.showInfoDialog("Not a float", "Not a float", "At least one entry is not a float. \n " +
                        "Hint: Decimal points are declared with '.' .");
                return;
            }
            setFontSizes(scriptAreaSize_f, contextAreaSize_f, sequentSize_f, javaCodeSize_f);

        } else {
            try {
                allSizes_f = Float.parseFloat(allSizes.getText());
            } catch (NumberFormatException e) {
                Utils.showInfoDialog("Not a float", "Not a float", "Entry in 'All font sizes' is not a float. \n " +
                        "Hint: Decimal points are declared with '.' .");
                return;
            }

            scriptAreaSize.setText(allSizes_f + "");
            contextAreaSize.setText(allSizes_f + "");
            sequentSize.setText(allSizes_f + "");
            javaCodeSize.setText(allSizes_f + "");

            setFontSizes(allSizes_f, allSizes_f, allSizes_f, allSizes_f);
        }

        /*TODO:
        what if flaot = 4.5654234 or 1230
         */
    }


    private void setFontSizes(float scriptarea_f, float contextmenu_f, float sequent_f, float javacode_f) {
        final String SETFONT_START = "-fx-font-size: ";
        final String SETFONT_END = " pt;";

        // Set font size for open script areas
        Set<ScriptArea> scriptareas = dm.getScriptController().getOpenScripts().keySet();
        if (scriptareas.size() != 0) {
            scriptareas.forEach(k -> k.setStyle(SETFONT_START + scriptarea_f + SETFONT_END));
        }

        // Set font size for the context menu
        if (dm.getInspectionViewsController().getActiveInspectionViewTab() != null) {
            dm.getInspectionViewsController().getActiveInspectionViewTab().setStyle(SETFONT_START + contextmenu_f + SETFONT_END);
        }

        // Set font size for the sequent
        if (dm.getInspectionViewsController().getActiveInspectionViewTab().getSequentView() != null) {
            dm.getInspectionViewsController().getActiveInspectionViewTab().getSequentView().setStyle(SETFONT_START + sequent_f + SETFONT_END);
        }

        // Set font size for the java code
        if (dm.getJavaAreaDock().getContents() != null) {
            dm.getJavaAreaDock().getContents().setStyle(SETFONT_START + javacode_f + SETFONT_END);
        }

    }

    private void resetChanges() {
        allSizes.setText("");

        scriptAreaSize.setText(Float.toString(OLD_SCRIPTSIZE));
        contextAreaSize.setText(Float.toString(OLD_CONTEXTMENUSIZE));
        sequentSize.setText(Float.toString(OLD_SEQUENTSIZE));
        javaCodeSize.setText(Float.toString(OLD_JAVACODESIZE));
    }
}
