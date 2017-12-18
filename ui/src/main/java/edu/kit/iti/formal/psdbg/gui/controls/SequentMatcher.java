package edu.kit.iti.formal.psdbg.gui.controls;

import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.termmatcher.MatcherFacade;
import edu.kit.iti.formal.psdbg.termmatcher.Matchings;
import edu.kit.iti.formal.psdbg.termmatcher.mp.MatchPath;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.util.Map;

public class SequentMatcher extends BorderPane {


    @FXML
    private SequentView sequentView;

    @FXML
    private ListView<GoalNode<KeyData>> goalView;

    @FXML
    private TextArea matchpattern;

    @FXML
    private ListView<Map<String, MatchPath>> matchingsView;

    public SequentMatcher() {
        Utils.createWithFXML(this);

        selectedGoalNodeToShow.addListener((observable, oldValue, newValue) -> {
                    sequentView.setGoal(newValue.getData().getGoal());
                    sequentView.setNode(newValue.getData().getNode());
                }
        );

        goalView.getSelectionModel().selectedItemProperty().addListener((prop, old, nnew) ->
                selectedGoalNodeToShow.setValue(nnew)
        );

        goals.addListener((observable, oldValue, newValue) -> goalView.setItems(newValue));

    }

    public void startMatch() {
        Matchings matchings = MatcherFacade.matches(matchpattern.getText(), getSelectedGoalNodeToShow().getData().getNode().sequent(), true);
        ObservableList<Map<String, MatchPath>> resultlist = FXCollections.observableArrayList(matchings);

        if (resultlist.isEmpty()) {
            System.out.println("No matchings found for this sequent");
        }

        matchingsView.setItems(resultlist);


    }

    //alle aktuellen nicht geschlossene Ziele -> alle leaves später (open+closed)
    private final ListProperty<GoalNode<KeyData>> goals = new SimpleListProperty<>(this, "goals", FXCollections.observableArrayList());

    private final ListProperty<GoalNode<KeyData>> matchingresults = new SimpleListProperty<>(this, "matchingresults", FXCollections.observableArrayList());

    public ObservableList<GoalNode<KeyData>> getMatchingresults() {
        return matchingresults.get();
    }


    public ObservableList<Map<String, MatchPath>> getResults() {
        return results.get();
    }

    public ListProperty<Map<String, MatchPath>> resultsProperty() {
        return results;
    }

    public void setResults(ObservableList<Map<String, MatchPath>> results) {
        this.results.set(results);
    }

    private final ListProperty<Map<String, MatchPath>> results = new SimpleListProperty<>(this, "results", FXCollections.observableArrayList());


    //sicht user selected
    private final ObjectProperty<GoalNode<KeyData>> selectedGoalNodeToShow = new SimpleObjectProperty<>(this, "selectedGoalNodeToShow");

    public ObservableList<GoalNode<KeyData>> getGoals() {
        return goals.get();
    }

    public ListProperty<GoalNode<KeyData>> goalsProperty() {
        return goals;
    }

    public void setGoals(ObservableList<GoalNode<KeyData>> goals) {
        this.goals.set(goals);
    }

    public GoalNode<KeyData> getSelectedGoalNodeToShow() {
        return selectedGoalNodeToShow.get();
    }

    public ObjectProperty<GoalNode<KeyData>> selectedGoalNodeToShowProperty() {
        return selectedGoalNodeToShow;
    }


    public void setSelectedGoalNodeToShow(GoalNode<KeyData> selectedGoalNodeToShow) {
        this.selectedGoalNodeToShow.set(selectedGoalNodeToShow);
    }


    public ListView<Map<String, MatchPath>> getMatchingsView() {
        return matchingsView;
    }

    public void setMatchingsView(ListView<Map<String, MatchPath>> matchingsView) {
        this.matchingsView = matchingsView;
    }

    public ListProperty<GoalNode<KeyData>> matchingresultsProperty() {
        return matchingresults;
    }

    public void setMatchingresults(ObservableList<GoalNode<KeyData>> matchingresults) {
        this.matchingresults.set(matchingresults);
    }
}
