package edu.kit.formal.psdb.gui.controls;

import antlrgrammars.Java8Lexer;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.ObservableSet;
import org.antlr.v4.runtime.CharStreams;
import org.fxmisc.richtext.LineNumberFactory;

/**
 * @author Alexander Weigl
 * @version 1 (03.06.17)
 */
public class JavaArea extends BaseCodeArea {
    private ANTLR4LexerHighlighter highlighter = new ANTLR4LexerHighlighter((s) -> new Java8Lexer(CharStreams.fromString(s)));
    //set with current lines to highlight
    private SimpleSetProperty<Integer> linesToHighlight = new SimpleSetProperty<>(this, "JavaLinesToHighlight");
    public JavaArea() {
        init();
    }

    public JavaArea(String text) {
        super(text);
        init();
    }

    private void init() {
        setEditable(false);
        setParagraphGraphicFactory(LineNumberFactory.get(this));
        setWrapText(true);
        getStyleClass().add("java-area");
        textProperty().addListener(
                (a, b, c) -> updateView());
        linesToHighlightProperty().addListener((observable, oldValue, newValue) -> {
            setEnableLineHighlighting(true);
            unHighlightOldSet(oldValue);
            highlightLineSet();
        });

    }

    /**
     * Remove old highlights
     *
     * @param oldValue
     */
    private void unHighlightOldSet(ObservableSet<Integer> oldValue) {
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
        linesToHighlightProperty().get().forEach(integer -> {
            lineToClassProperty().get().put(integer - 1, "line-highlight");
        });
        highlightLines();
    }

    private void updateView() {
        clearStyle(0, getText().length());
        setStyleSpans(0, highlighter.highlight(textProperty().getValue()));
        highlightLines();
    }


    public ObservableSet<Integer> getLinesToHighlight() {
        return linesToHighlight.get();
    }

    public void setLinesToHighlight(ObservableSet<Integer> linesToHighlight) {
        this.linesToHighlight.set(linesToHighlight);
    }

    public SimpleSetProperty<Integer> linesToHighlightProperty() {
        return linesToHighlight;
    }



}
