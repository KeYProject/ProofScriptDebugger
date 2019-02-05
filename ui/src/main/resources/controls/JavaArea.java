package edu.kit.iti.formal.psdbg.gui.controls;

import antlrgrammars.Java8Lexer;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.CharStreams;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author Alexander Weigl
 * @version 1 (03.06.17)
 */
public class JavaArea extends BaseCodeArea {
    private ANTLR4LexerHighlighter highlighter = new ANTLR4LexerHighlighter((s) -> new Java8Lexer(CharStreams.fromString(s)));
    //set with current lines to highlight
    @Getter
    @Setter
    private Set<Integer> linesToHighlight = new TreeSet<>();

    protected void init() {
        setEditable(false);
        setLineWrap(true);
        setWrapStyleWord(true);
        //TODO swing: setParagraphGraphicFactory(LineNumberFactory.get(this));
        //TODO swing: textProperty().addListener((a, b, c) -> updateView());
        //TODO swing: linesToHighlightProperty().addListener((observable, oldValue, newValue) -> {setEnableLineHighlighting(true);
        // unHighlightOldSet(oldValue);
        //    highlightLineSet();
        //});
    }

    /**
     * Remove old highlights
     *
     * @param oldValue
     */
    private void unHighlightOldSet(Set<Integer> oldValue) {
        if (oldValue != null) {
            oldValue.forEach(integer -> {
                lineToClass.put(integer - 1, "un-highlight-line");
                highlightLines();
            });
        }
    }

    /**
     * highlight new lines
     */
    private void highlightLineSet() {
        if (!linesToHighlight.isEmpty()) {
            //TODO swing: linesToHighlightProperty().forEach(integer -> {
            //    lineToClassProperty().put(integer - 1, "line-highlight");
            //});
            highlightLines();
        }
    }

    private void updateView() {
        //TODO swing: clearStyle(0, getText().length());
        //TODO swing: setStyleSpans(0, highlighter.highlight(textProperty().getValue()));
        highlightLines();
    }
}
