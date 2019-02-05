package edu.kit.iti.formal.psdbg.gui.graph;

import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;

import java.util.Random;

public class Edge<T> extends Group {
    private static Random r = new Random();
    protected SimpleObjectProperty<Cell<T>> source = new SimpleObjectProperty<>(), target = new SimpleObjectProperty<>();
    private Line line = new Line();
    private Label label = new Label();
    private Circle circle = new Circle();
    private double shiftX, shiftY;

    public Edge(Cell<T> source, Cell<T> target, String label) {
        setSource(source);
        setTarget(target);

        shiftX = r.nextInt(50) - 25;
        shiftY = r.nextInt(50) - 25;

        line = new Line();
        line.getStyleClass().addAll("edge", label);
        circle.getStyleClass().addAll("edge", label);
        this.label.getStyleClass().addAll("edge-label", label);
        this.label.setText(label);
        this.source.addListener(this::renewNode);
        this.target.addListener(this::renewNode);


        renewNode(this.source, null, source);
        renewNode(this.target, null, target);

        getChildren().addAll(line, this.label, circle);
        redraw();
    }

    private void renewNode(ObservableValue<? extends Cell> observableValue, Cell<T> old, Cell<T> nValue) {
        if (old != null) {
            old.layoutBoundsProperty().removeListener(this::redraw);
            old.layoutXProperty().removeListener(this::redraw);
            old.layoutYProperty().removeListener(this::redraw);
        }
        if (nValue != null) {
            nValue.layoutBoundsProperty().addListener(this::redraw);
            nValue.layoutXProperty().addListener(this::redraw);
            nValue.layoutYProperty().addListener(this::redraw);
        }
        redraw();
    }

    void redraw() {
        redraw(null);
    }

    void redraw(Observable observable) {
        double x1, y1, x2, y2;
        if (getSource() == null || getTarget() == null) {
            x1 = y1 = x2 = y2 = 0;
        } else {

            x1 = getSource().getLayoutX() + getSource().getBoundsInParent().getWidth() / 2.0;
            x2 = getTarget().getLayoutX() + getTarget().getBoundsInParent().getWidth() / 2.0;

            y1 = getSource().getLayoutY() + getSource().getBoundsInParent().getHeight() / 2.0;
            y2 = getTarget().getLayoutY() + getTarget().getBoundsInParent().getHeight() / 2.0;

        }

        Point2D start = new Point2D(x1, y1);
        Point2D end = new Point2D(x2, y2);

        Point2D midpoint = start.midpoint(end);

        label.relocate(midpoint.getX() + shiftX, shiftY + midpoint.getY());

        line.setStartX(x1);
        line.setStartY(y1);
        line.setEndX(x2);
        line.setEndY(y2);

        circle.setCenterX(x2);
        circle.setCenterY(y2);
        circle.setRadius(15);

        if (getTarget() != null) {
            end = correctToRectangle(getTarget().getLayoutX(),
                    getTarget().getLayoutY(), getTarget().getWidth(), getTarget().getHeight());
            if (end != null) {
                circle.setCenterX(end.getX());
                circle.setCenterY(end.getY());

                line.setEndX(end.getX());
                line.setEndY(end.getY());
            }
        }
    }

    private Point2D correctToRectangle(double x, double y, double width, double height) {
        Rectangle r = new Rectangle(x, y, width, height);
        r.setStroke(Paint.valueOf("black"));
        r.setStrokeWidth(1);
        r.setFill(null);
        try {
            Path inter = (Path) Shape.intersect(line, r);
            MoveTo mt = (MoveTo) inter.getElements().get(0);
            return new Point2D(mt.getX(), mt.getY());
        } catch (ClassCastException | IndexOutOfBoundsException cce) {
            //cce.printStackTrace();
        }
        return null;
    }

    public Cell getSource() {
        return source.get();
    }

    public void setSource(Cell source) {
        this.source.set(source);
    }

    public SimpleObjectProperty<Cell<T>> sourceProperty() {
        return source;
    }

    public Cell getTarget() {
        return target.get();
    }

    public void setTarget(Cell target) {
        this.target.set(target);
    }

    public SimpleObjectProperty<Cell<T>> targetProperty() {
        return target;
    }
}