package edu.kit.iti.formal.psdbg.gui.graph;

import edu.kit.iti.formal.psdbg.ShortCommandPrinter;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.dbg.PTreeNode;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class Graph<T> {
    final private SimpleListProperty<Cell<T>> nodes = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final SimpleListProperty<Edge<T>> edges = new SimpleListProperty<>(FXCollections.observableArrayList());
    protected HashMap<T, Cell<T>> map = new HashMap<>();

    public ObservableList<Cell<T>> getNodes() {
        return nodes.get();
    }

    public void setNodes(ObservableList<Cell<T>> nodes) {
        this.nodes.set(nodes);
    }

    public SimpleListProperty<Cell<T>> nodesProperty() {
        return nodes;
    }

    public ObservableList<Edge<T>> getEdges() {
        return edges.get();
    }

    public void setEdges(ObservableList<Edge<T>> edges) {
        this.edges.set(edges);
    }

    public SimpleListProperty<Edge<T>> edgesProperty() {
        return edges;
    }

    public void clear(boolean clearMap) {
        getEdges().clear();
        getNodes().clear();
        if (clearMap) map.clear();
    }

    public Cell<T> getCell(T cellId) {
        return map.get(cellId);
    }


    public static class PTreeGraph extends Graph<PTreeNode<KeyData>> {
        private Cell lastHighlightedCell;

        public Cell<PTreeNode<KeyData>> addCell(PTreeNode<KeyData> n) {
            if (map.containsKey(n)) {
                return map.get(n);
            }
            Cell<PTreeNode<KeyData>> cell = new Cell<>(n,
                    (String) n.getStatement().accept(new ShortCommandPrinter()),
                    "Line #" + n.getStatement().getStartPosition().getLineNumber(),
                    n.getStateBeforeStmt().getGoals().size() + "# goals"
            );
            map.put(n, cell);
            getNodes().add(cell);
            return cell;
        }

        public void addEdge(PTreeNode<KeyData> from, PTreeNode<KeyData> to, String label) {
            Edge edge = new Edge(map.get(from), map.get(to), label);
            getEdges().add(edge);
        }

        public void addPartiallyAndMark(PTreeNode<KeyData> node) {
            if (lastHighlightedCell != null) {
                lastHighlightedCell.getStyleClass().remove("current-node");
            }

            if (node != null) {
                Cell cell = addCell(node);

                if (node.getStepOver() != null) {
                    addEdge(node, node.getStepOver(), "over");
                }
                if (node.getStepInto() != null) {
                    addEdge(node, node.getStepInto(), "into");
                }
                if (node.getStepInvOver() != null) {
                    addEdge(node, node.getStepInvOver(), "revo");
                }
                if (node.getStepInvInto() != null) {
                    addEdge(node, node.getStepInvInto(), "otni");
                }
                if (node.getStepReturn() != null) {
                    addEdge(node, node.getStepReturn(), "rtrn");
                }

                cell.getStyleClass().add("current-node");
                lastHighlightedCell = cell;

            }
        }
    }
}
