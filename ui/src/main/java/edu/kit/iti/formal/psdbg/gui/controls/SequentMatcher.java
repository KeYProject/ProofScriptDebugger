package edu.kit.iti.formal.psdbg.gui.controls;

import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.logic.PosInOccurrence;
import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.pp.*;
import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.termmatcher.MatcherFacade;
import edu.kit.iti.formal.psdbg.termmatcher.Matchings;
import edu.kit.iti.formal.psdbg.termmatcher.mp.MatchPath;
import javafx.beans.Observable;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.util.HashMap;
import java.util.Map;

public class SequentMatcher extends BorderPane {


    private final Services services;
    //alle aktuellen nicht geschlossene Ziele -> alle leaves später (open+closed)
    private final ListProperty<GoalNode<KeyData>> goals = new SimpleListProperty<>(this, "goals", FXCollections.observableArrayList());
    private final ListProperty<GoalNode<KeyData>> matchingresults = new SimpleListProperty<>(this, "matchingresults", FXCollections.observableArrayList());
    private final ListProperty<Map<String, MatchPath>> results = new SimpleListProperty<>(this, "results", FXCollections.observableArrayList());
    //sicht user selected
    private final ObjectProperty<GoalNode<KeyData>> selectedGoalNodeToShow = new SimpleObjectProperty<>(this, "selectedGoalNodeToShow");
    public GoalOptionsMenu goalOptionsMenu = new GoalOptionsMenu();
    @FXML
    private SequentViewForMatcher sequentView;

    @FXML
    private ListView<GoalNode<KeyData>> goalView;
    @FXML
    private TextArea matchpattern;
    @FXML
    private ListView<Map<String, MatchPath>> matchingsView;
    @FXML
    private Label nomatchings; //only shown when no matchings found, else always hidden
    private Map<PosInOccurrence, Range> cursorPosition = new HashMap<>();

    public SequentMatcher(Services services) {
        this.services = services;

        Utils.createWithFXML(this);

        selectedGoalNodeToShow.addListener((observable, oldValue, newValue) -> {
                    sequentView.setGoal(newValue.getData().getGoal());
                    sequentView.setNode(newValue.getData().getNode());
                    calculateLookupTable();

                }
        );

        goalView.getSelectionModel().selectedItemProperty().addListener((prop, old, nnew) ->
                selectedGoalNodeToShow.setValue(nnew)
        );

        goalView.setCellFactory(GoalNodeListCell::new);


        goals.addListener((observable, oldValue, newValue) -> goalView.setItems(newValue));


        //Highlight Matchings
        matchingsView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                newValue.forEach((name, mp) -> {
                    PosInOccurrence pio = mp.pio();
                    Range r = cursorPosition.get(pio);
                    sequentView.setStyleClass(r.start(), r.end(), "sequent-highlight");

                    System.out.println("Highlight " + r.start() + " " + r.end());
                });

            }
        });
    }

    private void calculateLookupTable() {
        sequentView.update(null);
        cursorPosition.clear();
        LogicPrinter.PosTableStringBackend backend = sequentView.getBackend();
        SequentPrintFilter filter = new IdentitySequentPrintFilter();
        filter.setSequent(selectedGoalNodeToShow.get().getData().getNode().sequent());
        for (int i = 0; i < sequentView.getText().length(); i++) {
            try {
                Range range = backend.getPositionTable().rangeForIndex(i, sequentView.getLength());
                PosInSequent pis = backend.getInitialPositionTable().getPosInSequent(i, filter);
                if (pis != null && pis.getPosInOccurrence() != null) {
                    Term term = pis.getPosInOccurrence().subTerm();
                    cursorPosition.put(pis.getPosInOccurrence(), range);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public void showGoalOptions(MouseEvent actionEvent) {
        Node n = (Node) actionEvent.getTarget();
        goalOptionsMenu.show(n, actionEvent.getScreenX(), actionEvent.getScreenY());
    }

    public void startMatch() {
        sequentView.clearHighlight();

        Matchings matchings = MatcherFacade.matches(matchpattern.getText(), getSelectedGoalNodeToShow().getData().getNode().sequent(), true,
                services);


        ObservableList<Map<String, MatchPath>> resultlist = FXCollections.observableArrayList(matchings);

        //If no matchings found, addCell "No matchings found"
        if (resultlist.isEmpty()) {
            matchingsView.setVisible(false);
            nomatchings.setVisible(true);
        } else {
            nomatchings.setVisible(false);
            matchingsView.setItems(resultlist);
            matchingsView.setVisible(true);
        }


    }

    public ObservableList<GoalNode<KeyData>> getMatchingresults() {
        return matchingresults.get();
    }

    public void setMatchingresults(ObservableList<GoalNode<KeyData>> matchingresults) {
        this.matchingresults.set(matchingresults);
    }

    public ObservableList<Map<String, MatchPath>> getResults() {
        return results.get();
    }

    public void setResults(ObservableList<Map<String, MatchPath>> results) {
        this.results.set(results);
    }

    public ListProperty<Map<String, MatchPath>> resultsProperty() {
        return results;
    }

    public ObservableList<GoalNode<KeyData>> getGoals() {
        return goals.get();
    }

    public void setGoals(ObservableList<GoalNode<KeyData>> goals) {
        this.goals.set(goals);
    }

    public ListProperty<GoalNode<KeyData>> goalsProperty() {
        return goals;
    }

    public GoalNode<KeyData> getSelectedGoalNodeToShow() {
        return selectedGoalNodeToShow.get();
    }

    public void setSelectedGoalNodeToShow(GoalNode<KeyData> selectedGoalNodeToShow) {
        this.selectedGoalNodeToShow.set(selectedGoalNodeToShow);
    }

    public ObjectProperty<GoalNode<KeyData>> selectedGoalNodeToShowProperty() {
        return selectedGoalNodeToShow;
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

    /**
     * Cells for GoalView
     */
    private class GoalNodeListCell extends ListCell<GoalNode<KeyData>> {

        public GoalNodeListCell(ListView<GoalNode<KeyData>> goalNodeListView) {
            itemProperty().addListener(this::update);
            goalOptionsMenu.selectedViewOptionProperty().addListener(this::update);
        }

        private void update(Observable observable) {
            if (getItem() == null) {
                setText("");
                return;
            }
            KeyData item = getItem().getData();
            String text = "n/a";
            if (goalOptionsMenu.getSelectedViewOption() != null) {
                text = goalOptionsMenu.getSelectedViewOption().getText(item);
            }
            //setStyle("-fx-font-size: 12pt;");
            setText(text);
        }
    }
}
