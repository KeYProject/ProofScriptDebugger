package edu.kit.iti.formal.psdbg.interpreter.funchdl;

import edu.kit.iti.formal.psdbg.interpreter.ErrorInformation;
import edu.kit.iti.formal.psdbg.interpreter.IndistinctInformation;
import javafx.application.Platform;

import javafx.scene.control.ChoiceDialog;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

public abstract class TacletAppSelectionDialogService {

    @Setter
    ErrorInformation errorInformation;

    @Setter
    CountDownLatch countDownLatch;

    @Getter
    int userIndexInput;

    @Getter
    private Runnable runnable;

    public void showDialog() {
        Platform.runLater(runnable);
    }


    public void makeRunnable() {
        runnable = new Runnable() {
            @Override
            public void run() {

                if (errorInformation instanceof IndistinctInformation) {
                    List tacletApps = ((IndistinctInformation) errorInformation).getMatchApps();
                    ChoiceDialog<String> dialog = new ChoiceDialog<>(tacletApps.get(0), tacletApps);
                    dialog.setTitle("Indistinct taclet application:");
                    dialog.setHeaderText("Choose a taclet application:");
                    dialog.setContentText("Possible taclet applications:");

                    // Traditional way to get the response value.
                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(tacletApp -> {
                        userIndexInput = tacletApps.indexOf(tacletApp);
                        countDownLatch.countDown();

                    });

                }
            }
        };
    }


}
