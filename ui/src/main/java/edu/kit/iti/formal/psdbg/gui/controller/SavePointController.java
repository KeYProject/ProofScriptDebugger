package edu.kit.iti.formal.psdbg.gui.controller;

import edu.kit.iti.formal.psdbg.interpreter.data.SavePoint;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ListProperty;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.util.StringConverter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (17.05.18)
 */
@RequiredArgsConstructor
public class SavePointController {
    private final DebuggerMain debuggerMain;
    private final ListProperty<SavePoint> savePoints;

    private final List<MenuItem> menuItemsExecuteFrom = new ArrayList<>();
    private final List<MenuItem> menuItemsRollbackTo = new ArrayList<>();

    private final BooleanBinding rollbackEnabled;

    public SavePointController(DebuggerMain main) {
        debuggerMain = main;
        savePoints = main.scriptController.mainScriptSavePointsProperty();

        rollbackEnabled = savePoints.emptyProperty().not()
/*                .and(main.getModel().debuggerFrameworkProperty().isNotNull())
                .and(main.getModel().interpreterStateProperty().isNotEqualTo(
                        InterpreterThreadState.RUNNING
                ))*/;

        main.btnSavePointRollback.disableProperty().bind(rollbackEnabled.not());
        main.cboSavePoints.setDisable(false);

        main.cboSavePoints.setConverter(new StringConverter<SavePoint>() {
            @Override
            public String toString(SavePoint object) {
                return String.format("%s (in line %d)",
                        object.getName(), object.getLineNumer());
            }

            @Override
            public SavePoint fromString(String string) {
                //not supported
                return null;
            }
        });

        /*savePoints.emptyProperty().addListener((prop, old, empty) -> {
            debuggerMain.btnSavePointRollback.setDisable(empty);
            debuggerMain.cboSavePoints.setDisable(empty);
        });*/

        main.cboSavePoints.setPlaceholder(new Label("No savepoint inside main script, or no main script selected."));

        main.cboSavePoints.itemsProperty().bind(savePoints);

        savePoints.addListener((a, b, c) -> updateStartInterpreter());

        debuggerMain.buttonStartInterpreter.getItems().add(new SeparatorMenuItem());
    }

    private void updateStartInterpreter() {
        debuggerMain.buttonStartInterpreter.getItems().removeAll(menuItemsExecuteFrom);
        menuItemsRollbackTo.clear();
        menuItemsExecuteFrom.clear();

        int i = 0;
        KeyCode[] quickStart = new KeyCode[]{
                KeyCode.DIGIT1,
                KeyCode.DIGIT2,
                KeyCode.DIGIT3,
                KeyCode.DIGIT4,
                KeyCode.DIGIT5,
                KeyCode.DIGIT6,
                KeyCode.DIGIT7,
                KeyCode.DIGIT8,
                KeyCode.DIGIT9,
                KeyCode.DIGIT0,
        };

        for (SavePoint sp : savePoints) {
            MenuItem mi = new MenuItem(String.format("%s (from line %d)", sp.getName(), sp.getLineNumer()));
            menuItemsExecuteFrom.add(mi);

            MenuItem mirollback = new MenuItem(String.format("%s (from line %d)", sp.getName(), sp.getLineNumer()));
            menuItemsRollbackTo.add(mirollback);

            if (i < 10) {
                mi.setAccelerator(new KeyCodeCombination(quickStart[i],
                        KeyCodeCombination.CONTROL_DOWN,
                        KeyCodeCombination.SHIFT_DOWN
                        ));

                mirollback.setAccelerator(new KeyCodeCombination(quickStart[i],
                        KeyCodeCombination.ALT_DOWN));
            }

            i++;
        }
        debuggerMain.buttonStartInterpreter.getItems().addAll(menuItemsExecuteFrom);
        debuggerMain.menuExecuteFromSavepoint.getItems().setAll(menuItemsExecuteFrom);
        debuggerMain.menuRestartFromSavepoint.getItems().setAll(menuItemsRollbackTo);
    }
}
