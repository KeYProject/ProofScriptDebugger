package edu.kit.iti.formal.psdbg.gui.controls;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.antlr.v4.runtime.Token;
import org.fxmisc.richtext.CodeArea;

import java.util.Collections;
import java.util.Map;

/**
 * A code area with support for line highlightning.
 * Created by weigl on 10.06.2017.
 *
 * @author Alexander Weigl
 */
public class BaseCodeArea extends CodeArea {
    protected MapProperty<Integer, String> lineToClass = new SimpleMapProperty<>(FXCollections.observableHashMap());
    protected BooleanProperty enableLineHighlighting = new SimpleBooleanProperty();
    protected BooleanProperty enableCurrentLineHighlighting = new SimpleBooleanProperty();

    public BaseCodeArea() {
        init();
    }

    public BaseCodeArea(String text) {
        super(text);
        init();
    }

    private void init() {

    }

    public void setText(String text) {
        replaceText(text);
    }

    protected void highlightLines() {
        if (enableLineHighlighting.get() || enableCurrentLineHighlighting.get()) {
            LineMapping lm = new LineMapping(getText());

            if (enableLineHighlighting.get()) {
                for (Map.Entry<Integer, String> entry : lineToClass.entrySet()) {
                    hightlightLine(lm, entry.getKey(), entry.getValue());
                }
            }

            if (enableCurrentLineHighlighting.get()) {
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
            setStyle(start, end, Collections.singleton(clazz));
        } catch (IndexOutOfBoundsException e) {

        }

    }

    public ObservableMap<Integer, String> getLineToClass() {
        return lineToClass.get();
    }

    public MapProperty<Integer, String> lineToClassProperty() {
        return lineToClass;
    }

    public void setLineToClass(ObservableMap<Integer, String> lineToClass) {
        this.lineToClass.set(lineToClass);
    }

    public boolean isEnableLineHighlighting() {
        return enableLineHighlighting.get();
    }

    public BooleanProperty enableLineHighlightingProperty() {
        return enableLineHighlighting;
    }

    public void setEnableLineHighlighting(boolean enableLineHighlighting) {
        this.enableLineHighlighting.set(enableLineHighlighting);
    }

    public boolean isEnableCurrentLineHighlighting() {
        return enableCurrentLineHighlighting.get();
    }

    public BooleanProperty enableCurrentLineHighlightingProperty() {
        return enableCurrentLineHighlighting;
    }

    public void setEnableCurrentLineHighlighting(boolean enableCurrentLineHighlighting) {
        this.enableCurrentLineHighlighting.set(enableCurrentLineHighlighting);
    }
}
