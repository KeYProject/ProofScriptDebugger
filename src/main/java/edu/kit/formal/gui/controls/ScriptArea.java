package edu.kit.formal.gui.controls;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import edu.kit.formal.proofscriptparser.Facade;
import edu.kit.formal.proofscriptparser.ScriptLanguageLexer;
import edu.kit.formal.proofscriptparser.lint.LintProblem;
import edu.kit.formal.proofscriptparser.lint.LinterStrategy;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.fxmisc.richtext.CodeArea;
import org.reactfx.collection.LiveList;
import org.reactfx.value.Val;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.IntFunction;

/**
 * Created by sarah on 5/27/17.
 */
public class ScriptArea extends CodeArea {
    private ObservableSet<Integer> markedLines = FXCollections.observableSet();
    private GutterFactory gutter;
    private ANTLR4LexerHighlighter highlighter;

    public ScriptArea() {
        init();
    }

    private void init() {
        this.setWrapText(true);
        gutter = new GutterFactory();
        highlighter = new ANTLR4LexerHighlighter(
                (String val) -> new ScriptLanguageLexer(CharStreams.fromString(val)));
        this.setParagraphGraphicFactory(gutter);
        getStylesheets().add(getClass().getResource("script-keywords.css").toExternalForm());
        getStyleClass().add("script-area");
        textProperty().addListener((prop, oldValue, newValue) -> {
            clearStyle(0, newValue.length());
            setStyleSpans(0, highlighter.highlight(newValue));
            highlightProblems();
        });
               /* .successionEnds(Duration.ofMillis(100))
                .hook(collectionRichTextChange -> this.getUndoManager().mark())
                .supplyTask(this::computeHighlightingAsync).awaitLatest(richChanges())
                .filterMap(t -> {
                    if (t.isSuccess()) {
                        return Optional.of(t.get());
                    } else {
                        t.getFailure().printStackTrace();
                        return Optional.empty();
                    }
                }).subscribe(s -> setStyleSpans(0, s));*/
    }

    private void highlightProblems() {
        LinterStrategy ls = LinterStrategy.getDefaultLinter();
        List<LintProblem> pl = ls.check(Facade.getAST(CharStreams.fromString(getText())));

        for (LintProblem p : pl) {
            for (Token tok : p.getMarkTokens()) {
                setStyle(tok.getStartIndex(),
                        tok.getStopIndex()+1, Collections.singleton("problem"));
            }
        }
    }

    public void setText(String text) {
        insertText(0, text);
    }

    private void onLineClicked(int idx) {
        if (markedLines.contains(idx)) {
            markedLines.remove(idx);
            gutter.lineAnnotations.get(idx).set(null);
        } else {
            markedLines.add(idx);
            gutter.lineAnnotations.get(idx).set(
                    new MaterialDesignIconView(MaterialDesignIcon.STOP_CIRCLE_OUTLINE)
            );
        }
    }


    public class GutterFactory implements IntFunction<Node> {
        private Insets defaultInsets = new Insets(0.0, 5.0, 0.0, 5.0);
        private Paint defaultTextFill = Color.web("#666");
        private Font defaultFont = Font.font("monospace", FontPosture.ITALIC, 13);
        private final Background defaultBackground =
                new Background(new BackgroundFill(Color.web("#ddd"), null, null));

        private final Val<Integer> nParagraphs;

        private ObservableList<SimpleObjectProperty<Node>> lineAnnotations = new SimpleListProperty<>(FXCollections.observableArrayList());


        public GutterFactory() {
            nParagraphs = LiveList.sizeOf(getParagraphs());

            for (int i = 0; i < 100; i++) {
                lineAnnotations.add(new SimpleObjectProperty<>());
            }

            // Update the gutter.
            // If a line is deleted, delete also the image entry for this line
            nParagraphs.addInvalidationObserver((n) -> {
                int diff = lineAnnotations.size() - n;
            /*if (diff < 0) {
                for (int i = 0; i < diff; i++) {
                    lineAnnotations.add(new SimpleObjectProperty<>());
                }
            }*/
                if (diff > 0) {
                    lineAnnotations.remove(n, lineAnnotations.size());
                }
            });

        }


        @Override
        public Node apply(int idx) {
            Val<String> formatted = nParagraphs.map(n -> format(idx + 1, n));

            Label lineNo = new Label();
            // bind label's text to a Val that stops observing area's paragraphs
            // when lineNo is removed from scene
            lineNo.textProperty().bind(formatted.conditionOnShowing(lineNo));


            if (lineAnnotations.size() <= idx) {
                for (int i = lineAnnotations.size(); i < idx + 1; i++) {
                    lineAnnotations.add(new SimpleObjectProperty<>());
                }
            }

            Label labelInfo = new Label();
            labelInfo.setMinHeight(12);
            labelInfo.setMinWidth(12);
            SimpleObjectProperty<Node> annotation = lineAnnotations.get(idx);
            annotation.addListener((a, b, c) -> {
                labelInfo.setGraphic(c);
            });
            labelInfo.setGraphic(annotation.get());

            HBox hbox = new HBox(lineNo, labelInfo);
            hbox.setOnMouseClicked((mevent) -> onLineClicked(idx));

            lineNo.setFont(defaultFont);
            labelInfo.setFont(defaultFont);
            hbox.setBackground(defaultBackground);
            lineNo.setTextFill(defaultTextFill);
            hbox.setPadding(defaultInsets);
            hbox.getStyleClass().add("lineno");
            return hbox;
        }

        private String format(int x, int max) {
            int digits = (int) Math.floor(Math.log10(max)) + 1;
            return String.format("%" + digits + "d", x);
        }

    }
}