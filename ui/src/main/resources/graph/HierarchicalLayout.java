package edu.kit.iti.formal.psdbg.gui.graph;

import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.dbg.PTreeNode;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Alexander Weigl
 * @version 1 (18.01.18)
 */
@RequiredArgsConstructor
public class HierarchicalLayout extends Layout {
    public static final double MARGIN_TOP = 50.0;
    public static final double MARGIN_LEFT = 50.0;

    private final Graph<PTreeNode<KeyData>> graph;
    double y = MARGIN_TOP;
    double py = MARGIN_TOP;

    @Override
    public void execute() {
        Optional<Cell<PTreeNode<KeyData>>> root = findRoot();
        if (!root.isPresent()) {
            return;
        }
        int level = 0;
        explore(root.get(), MARGIN_LEFT);
    }

    private void explore(Cell<PTreeNode<KeyData>> cell, double px) {
        if (cell == null)
            return;
        double x = px;
        double y = py + MARGIN_TOP;
        py += MARGIN_TOP;
        if (cell.isInvalid()) {
            cell.relocate(x, y);
        } else {
            x = cell.getLayoutX();
            py = cell.getLayoutY();
        }
        cell.setInvalid(false);
        py += MARGIN_TOP;

        if (cell.getCellId().getStepInto() != null) {
            explore(graph.getCell(cell.getCellId().getStepInto()), x + MARGIN_LEFT);
        }

        if (cell.getCellId().getStepOver() != null) {
            explore(graph.getCell(cell.getCellId().getStepOver()), x);
        }
    }

    private Optional<Cell<PTreeNode<KeyData>>> findRoot() {
        Set<Cell<PTreeNode<KeyData>>> cells = new HashSet<>(graph.getNodes());
        graph.getEdges().forEach(
                edge -> cells.remove(edge.getTarget())
        );
        if (cells.size() != 1) {
            return Optional.empty();
        }
        return Optional.of(cells.iterator().next());
    }
}
