package edu.kit.iti.formal.psdbg.gui.controls;

import alice.tuprolog.Var;
import edu.kit.iti.formal.psdbg.gui.controls.Utils;
import edu.kit.iti.formal.psdbg.gui.model.InspectionModel;
import edu.kit.iti.formal.psdbg.interpreter.assignhook.DefaultAssignmentHook;
import edu.kit.iti.formal.psdbg.interpreter.data.VariableAssignment;
import edu.kit.iti.formal.psdbg.parser.ast.Variable;
import edu.kit.iti.formal.psdbg.parser.types.Type;
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

import java.util.Map;


public class VariableAssignmentWindow extends BorderPane {
    @FXML
    TableView tableView;

    @Setter
    private InspectionModel model;

    /** Non special Variables that don't start with __ **/
    private ObservableList<VariableModel> declarativeModel;

    /** Variables that start with __ **/
    private ObservableList<VariableModel> specialModel;

    public VariableAssignmentWindow(VariableAssignment assignment) {

        Utils.createWithFXML(this);

        tableView.setEditable(false);
        TableColumn varCol = new TableColumn("Variable");
        TableColumn typeCol = new TableColumn("Type");
        TableColumn valCol = new TableColumn("Value");


            if (assignment != null) {
                fillInVariableModelsLists(assignment);
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
        tableView.setItems(declarativeModel);
        tableView.getColumns().addAll(varCol, typeCol, valCol);



    }

    /**
     * Combine all previous Variableassignments and return it
     * @param assignment
     */
    private void fillInVariableModelsLists(VariableAssignment assignment) {
        ObservableList<VariableModel> varmodel = FXCollections.observableArrayList();
        ObservableList<VariableModel> special_varmodel = FXCollections.observableArrayList();
        VariableAssignment current = assignment;


        while (current != null) {

            final VariableAssignment currentcopy = current;
            //iterate over types map
            currentcopy.getTypes().forEach((k, v) -> {
                VariableModel variableModel = new VariableModel(k.getIdentifier(), v.symbol(), currentcopy.getValue(k).getData().toString());
                if(!varmodel.contains(variableModel)) {
                    if(variableModel.getVarname().toString().startsWith("__")) {
                        special_varmodel.add(variableModel);
                    } else {
                        varmodel.add(variableModel);
                    }
                }
            });
            current = current.getParent();
        }
        declarativeModel = varmodel;
        specialModel = special_varmodel;
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
