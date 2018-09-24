package edu.kit.iti.formal.psdbg.gui.controls;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.data.VariableAssignment;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.function.Function;

public class GoalOptionsMenu extends ContextMenu {
    @FXML
    private ToggleGroup toggleProjection;

    @FXML
    private RadioMenuItem rmiShowSequent, rmiCFL, rmiCFS, rmiBranchLabels, rmiNodeNames, rmiRuleNames;

    @FXML
    private MenuItem showVarAssignment;

    private ObjectProperty<ViewOption> selectedViewOption = new SimpleObjectProperty<>();

    private BiMap<Toggle, ViewOption> optionMap = HashBiMap.create(6);

    public GoalOptionsMenu() {
        Utils.createWithFXML(this);

        optionMap.put(rmiShowSequent, ViewOption.SEQUENT);
        optionMap.put(rmiCFS, ViewOption.STATEMENTS);
        optionMap.put(rmiCFL, ViewOption.PROGRAM_LINES);
        optionMap.put(rmiBranchLabels, ViewOption.BRANCHING);
        optionMap.put(rmiNodeNames, ViewOption.NAME);
        optionMap.put(rmiRuleNames, ViewOption.RULES);

        toggleProjection.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            selectedViewOption.setValue(optionMap.get(newValue));
        });

        selectedViewOption.addListener(o -> {
            if (selectedViewOption.get() != null)
                optionMap.inverse().get(selectedViewOption.get()).setSelected(true);
        });

        selectedViewOption.setValue(ViewOption.SEQUENT);

        showVarAssignment.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Show Variable Assignment pressed");

                //TODO: get var assign info
                VariableAssignment varAssign = null;

                Stage stage = new Stage();
                stage.setTitle("Variable Assignment");
                VariableAssignmentWindow vaw = new VariableAssignmentWindow(varAssign);
                Scene scene = new Scene(vaw);
                stage.setScene(scene);

                stage.show();
            }
        });
    }


    public ViewOption getSelectedViewOption() {
        return selectedViewOption.get();
    }

    public ObjectProperty<ViewOption> selectedViewOptionProperty() {
        return selectedViewOption;
    }

    public void setSelectedViewOption(ViewOption selectedViewOption) {
        this.selectedViewOption.set(selectedViewOption);
    }

    public enum ViewOption {
        BRANCHING(KeyData::getBranchingLabel),
        RULES(KeyData::getRuleLabel),
        PROGRAM_LINES(KeyData::getProgramLinesLabel),
        STATEMENTS(KeyData::getProgramStatementsLabel),
        NAME(KeyData::getNameLabel),
        SEQUENT(item -> item.getNode().sequent().toString());


        private final Function<KeyData, String> projection;

        ViewOption(Function<KeyData, String> toString) {
            projection = toString;
        }

        public String getText(KeyData item) {
            return projection.apply(item);
        }
    }


}