package edu.kit.formal.gui.controls;

import antlrgrammars.Java8Lexer;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import org.antlr.v4.runtime.CharStreams;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.util.Collections;
import java.util.Set;

/**
 * @author Alexander Weigl
 * @version 1 (03.06.17)
 */
public class JavaArea extends CodeArea {
    private static final Set<String> HIGHLIGHT_LINE_CLAZZ = Collections.singleton("hl-line");
    private ANTLR4LexerHighlighter highlighter = new ANTLR4LexerHighlighter((s) -> new Java8Lexer(CharStreams.fromString(s)));
    private SimpleSetProperty<Integer> markedLines = new SimpleSetProperty<>(this, "markedLines",
            FXCollections.observableSet());

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

        getStylesheets().add(getClass().getResource("java-keywords.css").toExternalForm());
        getStyleClass().add("java-area");
        textProperty().addListener(
                (a, b, c) -> updateView());
        markedLines.addListener((SetChangeListener<Integer>) change -> updateView());
    }

    private void updateView() {
        clearStyle(0, getText().length());
        setStyleSpans(0, highlighter.highlight(textProperty().getValue()));
        LineMapping lmap = new LineMapping(textProperty().getValue());
        for (Integer line : markedLines) {
            int start = lmap.getLineStart(line);
            int end = lmap.getLineEnd(line);
            setStyle(start, end, HIGHLIGHT_LINE_CLAZZ);
        }


    }

    public ObservableSet<Integer> getMarkedLines() {
        return markedLines.get();
    }

    public SimpleSetProperty<Integer> markedLinesProperty() {
        return markedLines;
    }

    public void setMarkedLines(ObservableSet<Integer> markedLines) {
        this.markedLines.set(markedLines);
    }
}
