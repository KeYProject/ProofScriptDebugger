package edu.kit.iti.formal.psdbg.interpreter;

import javafx.collections.ObservableList;
import lombok.Getter;

/**
 * Contains information
 */
public class IndistinctInformation implements ErrorInformation {

    @Getter
    ObservableList<String> matchApps;

    public IndistinctInformation(ObservableList<String> matchApps) {
        this.matchApps = matchApps;
    }
}
