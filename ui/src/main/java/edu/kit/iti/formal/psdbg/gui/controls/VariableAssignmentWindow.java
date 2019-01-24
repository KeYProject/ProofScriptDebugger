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
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;


public class VariableAssignmentWindow extends TabPane {
    @FXML
    TableView declarative_tableView;

    @FXML
    TableView special_tableView;


    /** Non special Variables that don't start with __ **/
    private ObservableList<VariableModel> declarativeModel;

    /** Variables that start with __ **/
    private ObservableList<VariableModel> specialModel;

    public VariableAssignmentWindow(VariableAssignment assignment) {

        Utils.createWithFXML(this);

        if (assignment != null) {
            fillInVariableModelsLists(assignment);
        }

        declarative_tableView.setEditable(false);
        special_tableView.setEditable(false);

        //Table Colums for declarative_tableView
        TableColumn decl_varCol = new TableColumn("Variable");
        TableColumn decl_typeCol = new TableColumn("Type");
        TableColumn decl_valCol = new TableColumn("Value");

        //Set Colums width proportional to windows with
        decl_varCol.prefWidthProperty().bind(declarative_tableView.widthProperty().divide(3));
        decl_typeCol.prefWidthProperty().bind(declarative_tableView.widthProperty().divide(3));
        decl_valCol.prefWidthProperty().bind(declarative_tableView.widthProperty().divide(3));

        decl_varCol.setCellValueFactory(
                new PropertyValueFactory<VariableModel,String>("varname")
        );
        decl_typeCol.setCellValueFactory(
                new PropertyValueFactory<VariableModel,String>("vartype")
        );
        decl_valCol.setCellValueFactory(
                new PropertyValueFactory<VariableModel,String>("varval")
        );

        declarative_tableView.setItems(declarativeModel);
        declarative_tableView.getColumns().addAll(decl_varCol, decl_typeCol, decl_valCol);

        //Table Colums for special_tableView
        TableColumn spec_varCol = new TableColumn("Variable");
        TableColumn spec_typeCol = new TableColumn("Type");
        TableColumn spec_valCol = new TableColumn("Value");

        //Set Colums width proportional to windows with
        spec_varCol.prefWidthProperty().bind(special_tableView.widthProperty().divide(3));
        spec_typeCol.prefWidthProperty().bind(special_tableView.widthProperty().divide(3));
        spec_valCol.prefWidthProperty().bind(special_tableView.widthProperty().divide(3));

        spec_varCol.setCellValueFactory(
                new PropertyValueFactory<VariableModel,String>("varname")
        );
        spec_typeCol.setCellValueFactory(
                new PropertyValueFactory<VariableModel,String>("vartype")
        );
        spec_valCol.setCellValueFactory(
                new PropertyValueFactory<VariableModel,String>("varval")
        );

        special_tableView.setItems(specialModel);
        special_tableView.getColumns().addAll(spec_varCol, spec_typeCol, spec_valCol);

        // TODO: set css for TableView

        //declarative_tableView.getStyleClass().add("table_view");


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

                if (variableModel.getVarname().startsWith("__")) {
                    if (!special_varmodel.contains(variableModel)) {
                        special_varmodel.add(variableModel);
                    }
                } else if (!varmodel.contains(variableModel)) {
                    varmodel.add(variableModel);
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
