package edu.kit.iti.formal.psdbg.gui.graph;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

@RequiredArgsConstructor
public class Cell<T> extends Pane {
    @Getter
    private final T cellId;

    /**
     * if true, this cell is handled by the layouter.
     */
    @Getter
    @Setter
    private boolean invalid = true;

    private Dragger dragger = new Dragger(this);

    public Cell(T cellId, Node... children) {
        super(children);
        getStyleClass().add("graph-cell");
        this.cellId = cellId;
    }

    public Cell(T cellId, String... children) {
        getStyleClass().add("graph-cell");
        this.cellId = cellId;
        VBox hbox = new VBox();
        hbox.getStyleClass().add("vbox");
        getChildren().addAll(hbox);
        Arrays.stream(children).forEachOrdered(s -> hbox.getChildren().add(new Label(s)));
    }
}

class Dragger {
    private double mouseX;
    private double mouseY;

    public Dragger(Node node) {
        assert node != null;
        node.setOnMousePressed(event -> {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        });

        node.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - mouseX;
            double deltaY = event.getSceneY() - mouseY;
            node.relocate(node.getLayoutX() + deltaX, node.getLayoutY() + deltaY);
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        });
    }
}


