package edu.kit.iti.formal.psdbg.gui.controls;

import edu.kit.iti.formal.psdbg.gui.model.InspectionModel;
import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

public class SequentMatcher extends BorderPane {


    @FXML
    private SequentView sequentView;

    @FXML
    private ListView<GoalNode<KeyData>> goalView;


    public SequentMatcher() {
        sequentView = new SequentView();

    }

    public void startMatch() {
        System.out.println("Start Match got clicked");
    }
}
