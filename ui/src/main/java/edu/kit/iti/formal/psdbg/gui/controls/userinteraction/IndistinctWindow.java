package edu.kit.iti.formal.psdbg.gui.controls.userinteraction;

import de.uka.ilkd.key.rule.TacletApp;
import edu.kit.iti.formal.psdbg.gui.controls.Utils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;

public class IndistinctWindow extends BorderPane {

    @Getter
    private TacletApp chosen;
    private List<TacletApp> matchingapps;
    @FXML
    ListView listView;

    @FXML
    TextField input;

    public IndistinctWindow(List<TacletApp> matchingapps) {
        Utils.createWithFXML(this);
        this.matchingapps = matchingapps;
        listView = new ListView(FXCollections.observableArrayList(matchingapps));
    }

    @FXML
    private void accept() {
        String userinput = input.getText();

        if (userinput.equals("")) {
            System.out.println("Invalid input!");
            return;
        }

        int index;
        try {
            index = Integer.parseInt(userinput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
            return;
        }

        if (0 < index && index < matchingapps.size()) {
            chosen = matchingapps.get(index);
        } else {
            System.out.println("Invalid input!");
            return;
        }

    }

}
