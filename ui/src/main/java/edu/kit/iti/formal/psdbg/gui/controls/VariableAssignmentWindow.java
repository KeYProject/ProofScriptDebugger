package edu.kit.iti.formal.psdbg.gui.controls;

import alice.tuprolog.Var;
import edu.kit.iti.formal.psdbg.gui.controls.Utils;
import edu.kit.iti.formal.psdbg.gui.model.InspectionModel;
import edu.kit.iti.formal.psdbg.interpreter.data.VariableAssignment;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import lombok.Getter;
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


        ObservableList<VariableModel> varmodell = FXCollections.observableArrayList();

            if (assignment != null) {
                //iterate over types map
                assignment.getTypes().forEach((k, v) -> {
                    varmodell.add(new VariableModel(k.getIdentifier(), v.symbol(), assignment.getValue(k).getData().toString()));
             /*       varCol.getColumns().add(k.toString());
                    typeCol.getColumns().add(v.toString());
                    valCol.getColumns().add(assignment.getValue(k).toString());*/
                });
            }

        varCol.setCellValueFactory(
                new PropertyValueFactory<VariableModel,String>("varname")
        );
        typeCol.setCellValueFactory(
                new PropertyValueFactory<VariableModel,String>("vartype")
        );
        valCol.setCellValueFactory(
                new PropertyValueFactory<VariableModel,String>("varval")
        );
        tableView.setItems(varmodell);
        tableView.getColumns().addAll(varCol, typeCol, valCol);



    }

    public static class VariableModel {
        @Getter
        private final String varname;
        @Getter
        private final String vartype;
        @Getter
        private final String varval;

        private VariableModel(String varname, String vartype, String varval) {
            this.varname = varname;
            this.vartype = vartype;
            this.varval = varval;
        }
    }
}
