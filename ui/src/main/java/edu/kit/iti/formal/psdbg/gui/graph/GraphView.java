package edu.kit.iti.formal.psdbg.gui.graph;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * @author Alexander Weigl
 * @version 1 (17.01.18)
 */
public class GraphView extends BorderPane {
    private Graph.PTreeGraph graph = new Graph.PTreeGraph();
    private Group canvas = new Group();
    //private ZoomableScrollPane scrollPane;
    private MouseGestures mouseGestures;

    private Pane cellLayer = new Pane();
    private Pane edgeLayer = new Pane();
    private ScrollPane scrollPane = new ScrollPane();


    public GraphView() {
        setCenter(scrollPane);
        StackPane root = new StackPane();
        root.getStyleClass().add("graph");
        scrollPane.setContent(root);
        root.getChildren().addAll(edgeLayer, cellLayer);
        //canvas.getChildren().addCell(cellLayer);
        mouseGestures = new MouseGestures();
        /*scrollPane = new ZoomableScrollPane(this);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);*/

        edgeLayer.minWidthProperty().bind(scrollPane.widthProperty());
        edgeLayer.minHeightProperty().bind(scrollPane.heightProperty());
        cellLayer.minWidthProperty().bind(scrollPane.widthProperty());
        cellLayer.minHeightProperty().bind(scrollPane.heightProperty());

        graph.getEdges().addListener((ListChangeListener<Edge>) c -> {
            while (c.next()) {
                if (c.wasRemoved())
                    edgeLayer.getChildren().removeAll(c.getRemoved());
                if (c.wasAdded())
                    edgeLayer.getChildren().addAll(c.getAddedSubList());
            }
            redraw();
        });

        graph.getNodes().addListener((ListChangeListener<Cell>) c -> {
            while (c.next()) {
                if (c.wasRemoved())
                    cellLayer.getChildren().removeAll(c.getRemoved());
                if (c.wasAdded())
                    cellLayer.getChildren().addAll(c.getAddedSubList());
            }
            redraw();
        });

        redraw();
    }

    public void redraw() {
        //Layout layout = new RandomLayout(graph.getNodes());
        Layout layout = new HierarchicalLayout(graph);
        layout.execute();
        graph.getEdges().forEach(Edge::redraw);
    }

    public Graph.PTreeGraph getGraph() {
        return graph;
    }
}
