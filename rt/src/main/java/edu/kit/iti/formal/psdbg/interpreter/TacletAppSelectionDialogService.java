package edu.kit.iti.formal.psdbg.interpreter;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

public abstract class TacletAppSelectionDialogService {

    @Setter
    IndistinctWindow pane;

    @Getter
    int userIndexInput;

    private Runnable runnable;

    @Setter
    private CyclicBarrier cyclicBarrier;

    public void showDialog() {
        runnable = getRunnable();
        Platform.runLater(runnable);
    }

    public Runnable getRunnable() {
        return () -> {
            if (pane != null) {
                Dialog<Object> stage = new Dialog<>();
                stage.setTitle("TacletAppSelectionDialog");
                //Scene scene = new Scene(pane);
                stage.getDialogPane().setContent(pane);
                Optional<Object> app = stage.showAndWait();
                try {
                    stage.close();
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException ignored) {
                }
            }
        };

    }

    private void getIndex() {
        userIndexInput = ((IndistinctWindow) pane).getIndexOfSelected();
    }


}
