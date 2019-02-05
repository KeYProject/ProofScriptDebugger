package edu.kit.iti.formal.psdbg.gui.graph;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import lombok.*;

public class MouseGestures {
    private final DragContext dragContext = new DragContext();
    private final EventHandler<MouseEvent> onMousePressedEventHandler = this::onPress;
    private final EventHandler<MouseEvent> onMouseDraggedEventHandler = this::onDrag;
    private final EventHandler<MouseEvent> onMouseReleasedEventHandler = this::onRelease;

    @Setter
    @Getter
    private double scale = 1;

    public void onRelease(MouseEvent event) {

    }

    private void onPress(MouseEvent event) {
        Node node = (Node) event.getSource();
        dragContext.x = node.getBoundsInParent().getMinX() * scale - event.getScreenX();
        dragContext.y = node.getBoundsInParent().getMinY() * scale - event.getScreenY();
    }
    private void onDrag(MouseEvent event) {
        Node node = (Node) event.getSource();

        double offsetX = event.getScreenX() + dragContext.x;
        double offsetY = event.getScreenY() + dragContext.y;

        offsetX /= scale;
        offsetY /= scale;

        node.relocate(offsetX, offsetY);

    }

    public void makeDraggable(final Node node) {
        node.setOnMousePressed(onMousePressedEventHandler);
        node.setOnMouseDragged(onMouseDraggedEventHandler);
        node.setOnMouseReleased(onMouseReleasedEventHandler);

    }

    static class DragContext {
        private double x;
        private double y;
    }
}