package edu.kit.iti.formal.psdbg.gui.controls;

import alice.tuprolog.Var;
import edu.kit.iti.formal.psdbg.gui.controls.Utils;
import edu.kit.iti.formal.psdbg.gui.model.InspectionModel;
import edu.kit.iti.formal.psdbg.interpreter.data.VariableAssignment;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import lombok.Setter;


public class VariableAssignmentWindow extends BorderPane {
    @FXML
    TableView tableView;

    @Setter
    private InspectionModel model;

    public VariableAssignmentWindow(VariableAssignment assignment) {

        Utils.createWithFXML(this);

        tableView.setEditable(false);
        TableColumn varCol = new TableColumn("Variable");
        TableColumn typeCol = new TableColumn("Type");
        TableColumn valCol = new TableColumn("Value");
        tableView.getColumns().addAll(varCol, typeCol, valCol);


            if (assignment != null) {
                //iterate over types map
                assignment.getTypes().forEach((k, v) -> {
                    varCol.getColumns().add(k);
                    typeCol.getColumns().add(v);
                    valCol.getColumns().add(assignment.getValue(k));
                });
            }


    }
}
