package edu.kit.iti.formal.psdbg.gui.controller;

import edu.kit.iti.formal.psdbg.interpreter.data.SavePoint;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (17.05.18)
 */
public class SavePointController {
    private final DebuggerMain debuggerMain;
    private List<SavePoint> savePoints;

    private final List<JMenuItem> menuItemsExecuteFrom = new ArrayList<>();
    private final List<JMenuItem> menuItemsRollbackTo = new ArrayList<>();

    private boolean rollbackEnabled;

    public SavePointController(DebuggerMain main) {
        debuggerMain = main;
        //TODO savePoints = main.scriptController.mainScriptSavePointsProperty();

        //TODO rollbackEnabled = savePoints.emptyProperty().not()
/*                .and(main.getModel().debuggerFrameworkProperty().isNotNull())
                .and(main.getModel().interpreterStateProperty().isNotEqualTo(
                        InterpreterThreadState.RUNNING
                ))*/
        ;

        //TODO main.btnSavePointRollback.disableProperty().bind(rollbackEnabled.not());
        //TODO main.cboSavePoints.setDisable(false);

        /*TODO main.cboSavePoints.setConverter(new StringConverter<SavePoint>() {
            @Override
            public String toString(SavePoint object) {
                if(object != null) {
                    return String.format("%s (in line %d)",
                            object.getName(), object.getLineNumber());
                } else {
                    return "";
                }
            }

            @Override
            public SavePoint fromString(String string) {
                //not supported
                return null;
            }
        });
        */
        //TODO         main.cboSavePoints.setPlaceholder(new Label("No savepoint inside main script, or no main script selected."));

        //TODO main.cboSavePoints.itemsProperty().bind(savePoints);

        //TODO savePoints.addListener((a, b, c) -> updateStartInterpreter());

        //TODO debuggerMain.buttonStartInterpreter.getItems().add(new SeparatorMenuItem());
    }

    private void updateStartInterpreter() {
        //TODO debuggerMain.buttonStartInterpreter.getItems().removeAll(menuItemsExecuteFrom);
        menuItemsRollbackTo.clear();
        menuItemsExecuteFrom.clear();

        int i = 0;
        int[] quickStart = new int[]{
                KeyEvent.VK_1,
                KeyEvent.VK_2,
                KeyEvent.VK_3,
                KeyEvent.VK_4,
                KeyEvent.VK_5,
                KeyEvent.VK_6,
        };

        for (SavePoint sp : savePoints) {
            JMenuItem mi = new JMenuItem(String.format("%s (from line %d)", sp.getName(), sp.getLineNumber()));
            menuItemsExecuteFrom.add(mi);

            JMenuItem mirollback = new JMenuItem(String.format("%s (from line %d)", sp.getName(), sp.getLineNumber()));
            menuItemsRollbackTo.add(mirollback);

            if (i < 10) {
                mi.setAccelerator(KeyStroke.getKeyStroke(quickStart[i],
                        KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
                mirollback.setAccelerator(KeyStroke.getKeyStroke(quickStart[i],
                        KeyEvent.ALT_DOWN_MASK));
            }
            i++;
        }
        //TODO debuggerMain.buttonStartInterpreter.getItems().addAll(menuItemsExecuteFrom);
        //TODO debuggerMain.menuExecuteFromSavepoint.getItems().setAll(menuItemsExecuteFrom);
        //TODO debuggerMain.menuRestartFromSavepoint.getItems().setAll(menuItemsRollbackTo);
    }
}
