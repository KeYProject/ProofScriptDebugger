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
import java.util.concurrent.*;

public abstract class TacletAppSelectionDialogService {

    @Setter
    Pane pane;

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
        return new Runnable() {
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
                            try {
                                stage.close();
                                cyclicBarrier.await();
                            } catch (InterruptedException ex) {

                            } catch (BrokenBarrierException ex) {

                            }

                        }
                    }
                });
                stage.showAndWait();
            }
        };

    }

    private void getIndex() {
        userIndexInput = ((IndistinctWindow) pane).getIndexOfSelected();
    }


}
