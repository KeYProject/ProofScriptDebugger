package edu.kit.formal.gui.controls;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import edu.kit.formal.proofscriptparser.Facade;
import edu.kit.formal.proofscriptparser.ScriptLanguageLexer;
import edu.kit.formal.proofscriptparser.lint.LintProblem;
import edu.kit.formal.proofscriptparser.lint.LinterStrategy;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.MouseOverTextEvent;
import org.reactfx.collection.LiveList;
import org.reactfx.value.Val;

import java.time.Duration;
import java.util.Collections;
import java.util.function.IntFunction;

/**
 * Created by sarah on 5/27/17.
 */
public class ScriptArea extends CodeArea {
    private ObservableSet<Integer> markedLines = FXCollections.observableSet();
    private GutterFactory gutter;
    private ANTLR4LexerHighlighter highlighter;
    private ListProperty<LintProblem> problems = new SimpleListProperty<>(FXCollections.observableArrayList());


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

        installPopup();
    }

    private void installPopup() {
        javafx.stage.Popup popup = new javafx.stage.Popup();

        setMouseOverTextDelay(Duration.ofSeconds(1));
        addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_BEGIN, e -> {
            int chIdx = e.getCharacterIndex();
            popup.getContent().setAll(
                    createPopupInformation(chIdx)
            );

            Point2D pos = e.getScreenPosition();
            popup.show(this, pos.getX(), pos.getY() + 10);
        });

        addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_END, e -> {
            popup.hide();
        });
    }

    private Node createPopupInformation(int chIdx) {
        VBox box = new VBox();
        box.getStyleClass().add("problem-popup");
        for (LintProblem p : problems) {
            if (p.includeTextPosition(chIdx)) {
                Label lbl = new Label(p.getMessage());
                lbl.getStyleClass().addAll("problem-popup-label",
                        "problem-popup-label-" + p.getIssue().getRulename(),
                        "problem-popup-label-" + p.getIssue().getSeverity());
                box.getChildren().add(lbl);
            }
        }
        return box;
    }

    public ObservableSet<Integer> getMarkedLines() {
        return markedLines;
    }

    public void setMarkedLines(ObservableSet<Integer> markedLines) {
        this.markedLines = markedLines;
    }

    private void highlightProblems() {
        LinterStrategy ls = LinterStrategy.getDefaultLinter();
        try {
            problems.setAll(ls.check(Facade.getAST(CharStreams.fromString(getText()))));
            for (LintProblem p : problems) {
                for (Token tok : p.getMarkTokens()) {
                    setStyle(tok.getStartIndex(),
                            tok.getStopIndex() + 1, Collections.singleton("problem"));
                }
            }
        } catch (Exception e) {
            //catch parsing exceptions
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