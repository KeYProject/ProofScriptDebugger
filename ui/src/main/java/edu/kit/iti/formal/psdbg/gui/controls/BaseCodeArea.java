package edu.kit.iti.formal.psdbg.gui.controls;

import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.Token;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import java.util.HashMap;
import java.util.Map;

/**
 * A code area with support for line highlightning.
 * Created by weigl on 10.06.2017.
 *
 * @author Alexander Weigl
 */
public class BaseCodeArea extends RSyntaxTextArea {
    @Getter
    @Setter
    protected Map<Integer, String> lineToClass = new HashMap<>();
    @Getter
    @Setter
    protected boolean enableLineHighlighting = false;
    @Getter
    @Setter
    protected boolean enableCurrentLineHighlighting = false;

    public BaseCodeArea() {
        init();
    }

    public BaseCodeArea(String text) {
        super(text);
        init();
    }

    protected void init() {
    }

    protected void highlightLines() {
        if (enableLineHighlighting || enableCurrentLineHighlighting) {
            LineMapping lm = new LineMapping(getText());

            if (enableLineHighlighting) {
                for (Map.Entry<Integer, String> entry : lineToClass.entrySet()) {
                    hightlightLine(lm, entry.getKey(), entry.getValue());
                }
            }

            if (enableCurrentLineHighlighting) {
                int caret = getCaretPosition();
                hightlightLine(lm, lm.getLine(caret), "current-line");
            }
        }
    }

    public void jumpTo(Token token) {
        //token.getStartIndex();
    }

    protected void hightlightLine(LineMapping lm, int line, String clazz) {
        try {
            final int start = lm.getLineStart(line);
            final int end = lm.getLineEnd(line);
            //TODO swing: setStyle(start, end, Collections.singleton(clazz));
        } catch (IndexOutOfBoundsException e) {

        }
    }
}
