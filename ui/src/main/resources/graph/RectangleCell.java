package edu.kit.iti.formal.psdbg.gui.graph;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author Alexander Weigl
 * @version 1 (17.01.18)
 */
public class RectangleCell extends Cell {

    public RectangleCell(String id) {
        super(id);
        Rectangle view = new Rectangle(50, 50);
        view.setStroke(Color.DODGERBLUE);
        view.setFill(Color.DODGERBLUE);
        getChildren().add(view);
    }
}
