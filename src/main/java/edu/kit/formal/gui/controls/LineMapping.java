package edu.kit.formal.gui.controls;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (04.06.17)
 */
class LineMapping {
    private List<Integer> marks = new ArrayList<>();

    public LineMapping(String value) {
        calculate(value);
    }

    private void calculate(String value) {
        marks.add(0);
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '\n') {
                marks.add(i);
            }
        }
        marks.add(value.length());
    }

    public int getLines() {
        return marks.size();
    }

    public int getLineStart(int line) {
        return marks.get(line);
    }

    public int getLineEnd(int line) {
        return getLineStart(line + 1) - 1;
    }

}
