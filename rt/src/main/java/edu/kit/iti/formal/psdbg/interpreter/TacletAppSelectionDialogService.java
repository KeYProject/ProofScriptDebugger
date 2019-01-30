package edu.kit.iti.formal.psdbg.interpreter;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;

public abstract class TacletAppSelectionDialogService {

    @Setter
    Pane pane;

    @Getter
    int userIndexInput;

    private CountDownLatch latch = new CountDownLatch(1);

    public void showDialog() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage stage = new Stage();
                stage.setTitle("TacletAppSelectionDialog");
                if (pane != null) {
                    Scene scene = new Scene(pane);
                    stage.setScene(scene);
                }
                ((IndistinctWindow) pane).accept.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        getIndex();
                        if (userIndexInput != -1) {
                            stage.close();
                        }
                    }
                });
                stage.showAndWait();
                latch.countDown();
            }
        });

    }

    private void getIndex() {
        userIndexInput = ((IndistinctWindow) pane).getIndexOfSelected();
    }


}
