package edu.kit.iti.formal.psdbg.gui.controls;

import alice.tuprolog.Var;
import edu.kit.iti.formal.psdbg.gui.controls.Utils;
import edu.kit.iti.formal.psdbg.gui.model.InspectionModel;
import edu.kit.iti.formal.psdbg.interpreter.assignhook.DefaultAssignmentHook;
import edu.kit.iti.formal.psdbg.interpreter.data.VariableAssignment;
import edu.kit.iti.formal.psdbg.interpreter.matcher.KeyMatcherFacade;
import edu.kit.iti.formal.psdbg.parser.ast.Variable;
import edu.kit.iti.formal.psdbg.parser.types.Type;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import lombok.Getter;
import lombok.Setter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class VariableAssignmentWindow extends BorderPane {
    @FXML
    TableView declarative_tableView;
    @FXML
    TableView special_tableView;

    @FXML
    TextArea match_variables;

    private VariableAssignment assignment;

    /** Non special Variables that don't start with __ **/
    private ObservableList<VariableModel> declarativeModel;

    /** Variables that start with __ **/
    private ObservableList<VariableModel> specialModel;

    private String numeric_matchexp;
    private String var; //var in numeric_matchexp

    private ScriptEngineManager mgr = new ScriptEngineManager();
    private ScriptEngine engine = mgr.getEngineByName("JavaScript");

    private List<VariableModel> matchlist_declarative = new ArrayList<>();
    private List<VariableModel> matchlist_special = new ArrayList<>();

    public VariableAssignmentWindow(VariableAssignment assignment) {

        //TODO: reduce size of constructor
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

        //Row factory added
        declarative_tableView.setRowFactory(tv -> new TableRow<VariableModel>() {
            @Override
            protected void updateItem(VariableModel vm, boolean empty) {
                if (vm != null && matchlist_declarative != null && matchlist_declarative.contains(vm)) {
                    setStyle("-fx-background-color: lightgreen;");
                } else {
                    setStyle("-fx-background-color: white;");
                }
            }
        });

        special_tableView.setRowFactory(tv -> new TableRow<VariableModel>() {
            @Override
            protected void updateItem(VariableModel vm, boolean empty) {
                if (vm != null && matchlist_special != null && matchlist_special.contains(vm)) {
                    setStyle("-fx-background-color: lightgreen;");
                } else {
                    setStyle("-fx-background-color: white;");
                }
            }
        });
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

    @FXML
    /**
     * Match variables to expression in  TextArea match_variables
     */
    private void startMatch() {
        clearHighlights();
        numeric_matchexp = match_variables.getText();
        if (numeric_matchexp.equals("")) return;

        var = getVariable(numeric_matchexp);
        if (var.equals("")) {
            Utils.showInfoDialog("Variable not found", "Explicit variable not found",
                    "Please declare exactly one variable starting with \"?\". ");
            return;
        }

        matchlist_declarative = getVariableMatches(declarativeModel);
        matchlist_special = getVariableMatches(specialModel);

        highlight(matchlist_declarative);
        highlight(matchlist_special);

        //TODO: remove following after testing
        if (matchlist_declarative != null)
            System.out.println("matchlist_declarative = " + matchlist_declarative.size());
        if (matchlist_special != null) System.out.println("matchlist_special = " + matchlist_special.size());
    }

    /**
     * Highlight all varlist items in the view
     *
     * @param varlist
     */
    private void highlight(List<VariableModel> varlist) {
        if (varlist == null) return;
        declarative_tableView.refresh();
        special_tableView.refresh();
    }

    /**
     * Clear all highlights
     */
    private void clearHighlights() {
        matchlist_declarative = new ArrayList<>();
        matchlist_special = new ArrayList<>();

        declarative_tableView.refresh();
        special_tableView.refresh();
    }

    /**
     * Extracts variable from given string, though only 1 variable is possibla
     *
     * @param expression string to extract variable
     * @return variable starting with ? only if there's only 1 match, else ""
     */
    private String getVariable(String expression) {
        Pattern pattern = Pattern.compile("\\?\\w+");
        Matcher matcher = pattern.matcher(expression);

        List<String> variables_matches = new ArrayList<>();
        while (matcher.find()) {
            if (!variables_matches.contains(matcher.group())) {
                variables_matches.add(matcher.group());
            }
        }

        //if there are multiple variables return null
        if (variables_matches.size() != 1) {
            return "";
        }
        return variables_matches.get(0);
    }

    /**
     * Calculate matching results of matchexpression with given list
     *
     * @param varlist
     * @return list of matching VariableModels
     */
    private List<VariableModel> getVariableMatches(ObservableList<VariableModel> varlist) {
        //Contains numbers
        if (!Pattern.compile("[0-9]+").matcher(numeric_matchexp).find()) return null;

        List<VariableModel> matchlist = new ArrayList<>();

        //append with \\ to escape '?' of variable declaration -> e.g.: \\?X
        String new_var = "\\" + var;

        for (VariableModel vm : varlist) {
            String boolexpression = numeric_matchexp.replaceAll(new_var, vm.getVarval());
            try {
                if (vm.getVartype().equals("int")) {
                    if ((Boolean) engine.eval(boolexpression.toLowerCase())) {
                        matchlist.add(vm);
                    }
                }
            } catch (ScriptException e) {
                continue;
            }
        }

        return matchlist;
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
