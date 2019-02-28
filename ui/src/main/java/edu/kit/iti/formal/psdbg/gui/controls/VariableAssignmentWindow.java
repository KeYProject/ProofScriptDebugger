package edu.kit.iti.formal.psdbg.gui.controls;

import edu.kit.iti.formal.psdbg.gui.model.InspectionModel;
import edu.kit.iti.formal.psdbg.interpreter.Evaluator;
import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.data.VariableAssignment;
import edu.kit.iti.formal.psdbg.parser.Facade;
import edu.kit.iti.formal.psdbg.parser.data.Value;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import lombok.Getter;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class VariableAssignmentWindow extends BorderPane {
    @FXML
    TableView declarative_tableView;
    @FXML
    TableView special_tableView;

    @FXML
    TableView watches_tableView;

    @FXML
    TextArea match_variables;

    @FXML
    TabPane tabPane;

    @FXML
    Tab watchesTab;

    private ContextMenu contextMenu;

    private VariableAssignment assignment;

    /** Non special Variables that don't start with __ **/
    private ObservableList<VariableModel> declarativeModel;

    /** Variables that start with __ **/
    private ObservableList<VariableModel> specialModel;

    private ObservableList<VariableModel> watchesModel = FXCollections.observableArrayList();

    private String matchexp;

    private List<VariableModel> matchlist_declarative = new ArrayList<>();
    private List<VariableModel> matchlist_special = new ArrayList<>();

    private Evaluator evaluator;

    private InspectionModel inspectionModel;

    public VariableAssignmentWindow(InspectionModel inspectionModel) {

        Utils.createWithFXML(this);

        this.inspectionModel = inspectionModel;

        declarative_tableView.setEditable(false);
        special_tableView.setEditable(false);
        watches_tableView.setEditable(false);

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

        special_tableView.getColumns().addAll(spec_varCol, spec_typeCol, spec_valCol);


        //Table Colums for watches_tableView
        TableColumn hist_varCol = new TableColumn("Variable");
        TableColumn hist_typeCol = new TableColumn("Type");
        TableColumn hist_valCol = new TableColumn("Value");

        //Set Colums width proportional to windows with
        hist_varCol.prefWidthProperty().bind(watches_tableView.widthProperty().divide(3));
        hist_typeCol.prefWidthProperty().bind(watches_tableView.widthProperty().divide(3));
        hist_valCol.prefWidthProperty().bind(watches_tableView.widthProperty().divide(3));

        hist_varCol.setCellValueFactory(
                new PropertyValueFactory<VariableModel, String>("varname")
        );
        hist_typeCol.setCellValueFactory(
                new PropertyValueFactory<VariableModel, String>("vartype")
        );
        hist_valCol.setCellValueFactory(
                new PropertyValueFactory<VariableModel, String>("varval")
        );

        watches_tableView.setItems(watchesModel);
        watches_tableView.getColumns().addAll(hist_varCol, hist_typeCol, hist_valCol);

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

        watches_tableView.setRowFactory(tv -> new TableRow<VariableModel>() {
            @Override
            protected void updateItem(VariableModel vm, boolean empty) {
                setStyle("-fx-background-color: white;");
            }
        });

        inspectionModel.currentInterpreterGoalProperty().addListener(new ChangeListener<GoalNode<KeyData>>() {
            @Override
            public void changed(ObservableValue<? extends GoalNode<KeyData>> observable, GoalNode<KeyData> oldValue, GoalNode<KeyData> newValue) {
                refresh();
            }
        });

        watches_tableView.setOnContextMenuRequested(evt -> {
            getContextMenu().show(this, evt.getScreenX(), evt.getScreenY());
        });
        refresh();
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
        matchexp = match_variables.getText();
        if (matchexp.equals("")) return;

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
     * Evaluates String in Textfield matchexp as Expression and puts it into the Tab tabWatches
     */
    @FXML
    private void evaluate() {
        matchexp = match_variables.getText();
        if (matchexp.equals("")) return;
        Value value;
        VariableModel vm;
        try {
            value = evaluator.eval(Facade.parseExpression(matchexp));

            vm = new VariableModel(matchexp,
                    value.getType().toString(),
                    value.getData().toString());


        } catch (Exception e) {

            System.out.println("No evaluable expression");

            vm = new VariableModel(matchexp,
                    "NOT DEFINED",
                    "NOT DEFINED");

        }

        watchesModel.add(vm);
        watches_tableView.refresh();

        //focus on tab
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        selectionModel.select(watchesTab);
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
     * Calculate matching results of matchexpression with given list
     *
     * @param
     * @return list of matching VariableModels
     *
     */

    private List<VariableModel> getVariableMatches(ObservableList<VariableModel> varlist) {

        List<VariableModel> matchlist = new ArrayList<>();

        for (VariableModel vm : varlist) {
            if (vm.getVarname().matches(matchexp)) {
                matchlist.add(vm);
            }
        }

        return matchlist;
    }

    public ContextMenu getContextMenu() {
        if (contextMenu == null) {
            contextMenu = new ContextMenu();
            MenuItem deleteWatch = new MenuItem("Delete Watch");
            deleteWatch.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        int selected = watches_tableView.getSelectionModel().getFocusedIndex();

                        if (0 <= selected && selected < watchesModel.size()) {
                            watchesModel.remove(selected);
                            watches_tableView.refresh();
                        }
                    } catch (Exception e) {

                    }
                }
            });
            contextMenu.getItems().add(deleteWatch);
        }
        return contextMenu;
    }

    private void refresh() {
        assignment = inspectionModel.getCurrentInterpreterGoal().getAssignments();
        evaluator = new Evaluator(assignment, null);
        if (assignment != null) {
            fillInVariableModelsLists(assignment);
        }
        declarative_tableView.setItems(declarativeModel);
        special_tableView.setItems(specialModel);
        declarative_tableView.refresh();
        special_tableView.refresh();
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
