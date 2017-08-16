package edu.kit.formal.psdb.gui.controls;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (04.06.17)
 */
public class LineMapping {
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

    public int getCharInLine(int caretPosition) {
        return caretPosition - getLineStart(getLine(caretPosition));
    }

    public int getLine(int caretPosition) {
        for (int i = 0; i < marks.size(); i++) {
            if (caretPosition >= marks.get(i)) {
                return i;
            }
        }
        return -1;
    }
}
