package edu.kit.iti.formal.psdbg.gui.graph;

import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.interpreter.dbg.PTreeNode;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomLayout extends Layout {
    private final List<Cell> cells = new ArrayList<>();
    private Random rnd = new Random();

    public <T> RandomLayout(List<Cell<T>> cells) {
        this.cells.addAll(cells);
    }

    public void execute() {
        for (Cell cell : cells) {
            if (cell.isInvalid()) {
                double x = rnd.nextDouble() * 1000;
                double y = rnd.nextDouble() * 1000;
                cell.relocate(x, y);
            }
            cell.setInvalid(false);
        }
    }
}