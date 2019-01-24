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

    private DebuggerMain dm;

    final private float OLD_SCRIPTSIZE = 16; // -fx-font-size: 16pt;
    final private float OLD_CONTEXTMENUSIZE = 12;
    final private float OLD_SEQUENTSIZE = 21;
    final private float OLD_JAVACODESIZE = 16;

    final private float MINIMUM_SIZE = 5;
    final private float MAXIMUM_SIZE = 30;
    public ViewSettings(DebuggerMain dm) {
        Utils.createWithFXML(this);
        this.dm = dm;

        scriptAreaSize.setText(Float.toString(OLD_SCRIPTSIZE));
        contextAreaSize.setText(Float.toString(OLD_CONTEXTMENUSIZE));
        sequentSize.setText(Float.toString(OLD_SEQUENTSIZE));
        javaCodeSize.setText(Float.toString(OLD_JAVACODESIZE));
    }

    @FXML
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

    }


    private void setFontSizes(float scriptarea_f, float contextmenu_f, float sequent_f, float javacode_f) {
        final String SETFONT_START = "-fx-font-size: ";
        final String SETFONT_END = " pt;";

        contextmenu_f = getInInterval(contextmenu_f);
        sequent_f = getInInterval(sequent_f);
        javacode_f = getInInterval(javacode_f);

        // Set font size for open script areas
        Set<ScriptArea> scriptareas = dm.getScriptController().getOpenScripts().keySet();
        if (scriptareas.size() != 0) {
            scriptareas.forEach(k -> k.setStyle(SETFONT_START + getInInterval(scriptarea_f) + SETFONT_END));
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

    private float getInInterval(float input) {
        if (input < MINIMUM_SIZE) return MINIMUM_SIZE;
        if (input > MAXIMUM_SIZE) return MAXIMUM_SIZE;
        return input;
    }

    @FXML
    private void resetChanges() {
        allSizes.setText("");

        scriptAreaSize.setText(Float.toString(OLD_SCRIPTSIZE));
        contextAreaSize.setText(Float.toString(OLD_CONTEXTMENUSIZE));
        sequentSize.setText(Float.toString(OLD_SEQUENTSIZE));
        javaCodeSize.setText(Float.toString(OLD_JAVACODESIZE));
    }

    private void increaseByOne(TextField textField) {
        float currentvalue;
        try {
            currentvalue = Float.parseFloat(textField.getText());
            if (currentvalue + 1 > 30) {
                textField.setText(Float.toString(30));
            } else {
                textField.setText(Float.toString(currentvalue + 1));
            }
        } catch (NumberFormatException e) {
            Utils.showInfoDialog("Not a float", "Not a float", "At least one entry is not a float. \n " +
                    "Hint: Decimal points are declared with '.' .");
        }
    }


    private void decreaseByOne(TextField textField) {
        float currentvalue;
        try {
            currentvalue = Float.parseFloat(textField.getText());
            if (currentvalue - 1 < 5) {
                textField.setText(Float.toString(5));
            } else {
                textField.setText(Float.toString(currentvalue - 1));
            }
        } catch (NumberFormatException e) {
            Utils.showInfoDialog("Not a float", "Not a float", "At least one entry is not a float. \n " +
                    "Hint: Decimal points are declared with '.' .");
        }
    }

    @FXML
    private void increaseAll() {
        increaseByOne(scriptAreaSize);
        increaseByOne(contextAreaSize);
        increaseByOne(sequentSize);
        increaseByOne(javaCodeSize);
    }

    @FXML
    private void decreaseAll() {
        decreaseByOne(scriptAreaSize);
        decreaseByOne(contextAreaSize);
        decreaseByOne(sequentSize);
        decreaseByOne(javaCodeSize);
    }

    @FXML
    private void increaseScriptArea() {
        increaseByOne(scriptAreaSize);
    }

    @FXML
    private void decreaseScriptArea() {
        decreaseByOne(scriptAreaSize);
    }

    @FXML
    private void increaseContextArea() {
        increaseByOne(contextAreaSize);
    }

    @FXML
    private void decreaseContextArea() {
        decreaseByOne(contextAreaSize);
    }

    @FXML
    private void increaseSequent() {
        increaseByOne(sequentSize);
    }

    @FXML
    private void decreaseSequent() {
        decreaseByOne(sequentSize);
    }

    @FXML
    private void increaseJavaCode() {
        increaseByOne(javaCodeSize);
    }

    @FXML
    private void decreaseJavaCode() {
        decreaseByOne(javaCodeSize);
    }
}
