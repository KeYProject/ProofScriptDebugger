package edu.kit.formal.gui.controls;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import edu.kit.formal.gui.model.Breakpoint;
import edu.kit.formal.gui.model.ConditionalBreakpoint;
import edu.kit.formal.gui.model.MainScriptIdentifier;
import edu.kit.formal.proofscriptparser.Facade;
import edu.kit.formal.proofscriptparser.ScriptLanguageLexer;
import edu.kit.formal.proofscriptparser.ast.ProofScript;
import edu.kit.formal.proofscriptparser.lint.LintProblem;
import edu.kit.formal.proofscriptparser.lint.LinterStrategy;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.fxmisc.richtext.CharacterHit;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.MouseOverTextEvent;
import org.fxmisc.richtext.model.NavigationActions;
import org.fxmisc.richtext.model.Paragraph;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyledText;
import org.reactfx.collection.LiveList;
import org.reactfx.value.Val;

import java.io.File;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

/**
 * ScriptArea is the textarea on the left side of the GUI.
 * It displays the script code and allows highlighting of lines and setting of breakpoints
 */
public class ScriptArea extends CodeArea {
    private final ObjectProperty<File> filePath = new SimpleObjectProperty<>();
    /**
     * Lines to highlight?
     */
    private final SetProperty<Integer> markedLines =
            new SimpleSetProperty<>(FXCollections.observableSet());
    /**
     * set by {@link ScriptTabPane}
     */
    private final ObjectProperty<MainScriptIdentifier> mainScript = new SimpleObjectProperty<>();

    private GutterFactory gutter;
    private ANTLR4LexerHighlighter highlighter;
    private ListProperty<LintProblem> problems = new SimpleListProperty<>(FXCollections.observableArrayList());
    private SimpleObjectProperty<CharacterHit> currentMouseOver = new SimpleObjectProperty<>();


    public ScriptArea() {
        init();
    }

    private void init() {
        this.setWrapText(true);
        gutter = new GutterFactory();
        highlighter = new ANTLR4LexerHighlighter(
                (String val) -> new ScriptLanguageLexer(CharStreams.fromString(val)));
        this.setParagraphGraphicFactory(gutter);
        //getStylesheets().add(getClass().getResource("script-keywords.css").toExternalForm());
        getStyleClass().add("script-area");
        textProperty().addListener((prop, oldValue, newValue) -> {
            if (newValue.length() != 0) {
                clearStyle(0, newValue.length());
                StyleSpans<? extends Collection<String>> spans = highlighter.highlight(newValue);
                if (spans != null) setStyleSpans(0, spans);
            }
            highlightProblems();

            updateMainScriptMarker();
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
        getStyleClass().add("script-area");
        installPopup();


        this.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent e) -> {
            CharacterHit hit = this.hit(e.getX(), e.getY());
            currentMouseOver.set(this.hit(e.getX(), e.getY()));
            int characterPosition = hit.getInsertionIndex();
            //System.out.println("characterPosition = " + characterPosition);
            // move the caret to that character's position
            this.moveTo(characterPosition, NavigationActions.SelectionPolicy.CLEAR);
        });

        mainScript.addListener((observable) ->
                updateMainScriptMarker());

        setContextMenu(new ScriptAreaContextMenu());
    }

    private void updateMainScriptMarker() {
        try {
            MainScriptIdentifier ms = mainScript.get();
            System.out.println("ScriptArea.updateMainScriptMarker");
            if (ms != null && filePath.get().getAbsolutePath().equals(ms.getSourceName())) {
                System.out.println(ms);
                CharStream stream = CharStreams.fromString(getText(), filePath.get().getAbsolutePath());
                Optional<ProofScript> ps = ms.find(Facade.getAST(stream));

                if (ps.isPresent()) {
                    setMainMarker(ps.get().getStartPosition().getLineNumber());
                    return;
                }
            }
            setMainMarker(-1);
        } catch (NullPointerException e) {
        }
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

    public MainScriptIdentifier getMainScript() {
        return mainScript.get();
    }

    public void setMainScript(MainScriptIdentifier mainScript) {
        this.mainScript.set(mainScript);
    }

    public ObjectProperty<MainScriptIdentifier> mainScriptProperty() {
        return mainScript;
    }

    public void setText(String text) {
        insertText(0, text);
    }

    private void toggleBreakpoint(int idx) {
        GutterAnnotation a = gutter.getLineAnnotation(idx);
        a.setBreakpoint(!a.isBreakpoint());
    }

    /**
     * Highlight line given by the characterindex
     *
     * @param lineNumber
     */
    public void highlightStmt(int lineNumber, String cssStyleTag) {
        //calculate line number from characterindex
        //int lineNumber = this.offsetToPosition(chrIdx, Bias.Forward).getMajor();
        Paragraph<Collection<String>, StyledText<Collection<String>>, Collection<String>> paragraph = this.getParagraph(lineNumber);
        //calculate start and endposition
        //int startPos = getAbsolutePosition(this.offsetToPosition(chrIdx, Bias.Forward).getMajor(), 0);
        int startPos = getAbsolutePosition(lineNumber, 0);

        int length = paragraph.length();
        //highlight line

        this.setStyle(startPos, startPos + length, Collections.singleton(cssStyleTag));

    }

    /**
     * Remove the highlighting of a statement
     *
     * @param lineNumber
     */
    public void removeHighlightStmt(int lineNumber) {
        highlightStmt(lineNumber, "line-unhighlight");
    }

    /**
     * Set a mark in the gutter next to the definition of the main script
     *
     * @param lineNumberOfSigMainScript
     */
    public void setDebugMark(int lineNumberOfSigMainScript) {
        highlightStmt(lineNumberOfSigMainScript - 1, "line-highlight-mainScript");

    }

    public void unsetDebugMark(int lineNumberOfSigMainScript) {
        removeHighlightStmt(lineNumberOfSigMainScript - 1);
    }

    public File getFilePath() {
        return filePath.get();
    }

    public void setFilePath(File filePath) {
        this.filePath.set(filePath);
    }

    public ObjectProperty<File> filePathProperty() {
        return filePath;
    }

    public void setMainMarker(int lineNumber) {
        gutter.lineAnnotations.forEach(a -> a.setMainScript(false));
        if (lineNumber > 0)
            gutter.getLineAnnotation(lineNumber - 1).setMainScript(true);
    }

    public Collection<? extends Breakpoint> getBreakpoints() {
        return gutter.lineAnnotations.stream()
                .filter(GutterAnnotation::isBreakpoint)
                .map(ga -> new Breakpoint(filePath.get(),
                        Integer.parseInt(ga.getText())))
                .collect(Collectors.toList());
    }

    private static class GutterView extends HBox {
        private final SimpleObjectProperty<GutterAnnotation> annotation = new SimpleObjectProperty<>();

        private MaterialDesignIconView iconMainScript = new MaterialDesignIconView(
                MaterialDesignIcon.SQUARE_INC, "12"
        );
        private MaterialDesignIconView iconBreakPoint = new MaterialDesignIconView(
                MaterialDesignIcon.STOP_CIRCLE_OUTLINE, "12"
        );

        private MaterialDesignIconView iconConditionalBreakPoint = new MaterialDesignIconView(
                MaterialDesignIcon.ACCOUNT_CIRCLE, "12"
        );

        private Label lineNumber = new Label("not set");

        public GutterView(GutterAnnotation ga) {
            annotation.addListener((o, old, nv) -> {
                if (old != null) {
                    old.breakpoint.removeListener(this::update);
                    old.conditionalBreakpoint.removeListener(this::update);
                    old.mainScript.removeListener(this::update);
                    lineNumber.textProperty().unbind();
                }

                nv.breakpoint.addListener(this::update);
                nv.mainScript.addListener(this::update);
                nv.conditionalBreakpoint.addListener(this::update);


                lineNumber.textProperty().bind(nv.textProperty());

                update(null);
            });
            setAnnotation(ga);
        }

        public void update(Observable o) {
            getChildren().setAll(lineNumber);
            if (getAnnotation().isMainScript()) getChildren().add(iconMainScript);
            else addPlaceholder();

            if (getAnnotation().isBreakpoint() && getAnnotation().conditionalBreakpoint.isNull().get())
                getChildren().add(iconBreakPoint);
            else if (getAnnotation().isBreakpoint() && getAnnotation().conditionalBreakpoint.isNotNull().get())
                getChildren().add(iconConditionalBreakPoint);
            else
                addPlaceholder();
        }

        private void addPlaceholder() {
            Label lbl = new Label();
            lbl.setMinWidth(12);
            lbl.setMinHeight(12);
            getChildren().add(lbl);
        }


        public GutterAnnotation getAnnotation() {
            return annotation.get();
        }

        public void setAnnotation(GutterAnnotation annotation) {
            this.annotation.set(annotation);
        }

        public SimpleObjectProperty<GutterAnnotation> annotationProperty() {
            return annotation;
        }
    }

    private static class GutterAnnotation {
        private StringProperty text = new SimpleStringProperty();
        private BooleanProperty breakpoint = new SimpleBooleanProperty();
        private BooleanProperty mainScript = new SimpleBooleanProperty();
        private SimpleObjectProperty<ConditionalBreakpoint> conditionalBreakpoint = new SimpleObjectProperty<>();

        public GutterAnnotation() {

        }

        public String getText() {
            return text.get();
        }

        public void setText(String text) {
            this.text.set(text);
        }

        public StringProperty textProperty() {
            return text;
        }

        public boolean isBreakpoint() {
            return breakpoint.get();
        }

        public void setBreakpoint(boolean breakpoint) {
            this.breakpoint.set(breakpoint);
        }

        public BooleanProperty breakpointProperty() {
            return breakpoint;
        }

        public boolean isMainScript() {
            return mainScript.get();
        }

        public void setMainScript(boolean mainScript) {
            this.mainScript.set(mainScript);
        }

        public BooleanProperty mainScriptProperty() {
            return mainScript;
        }

        public ConditionalBreakpoint getConditionalBreakpoint() {
            return conditionalBreakpoint.get();
        }

        public void setConditionalBreakpoint(ConditionalBreakpoint conditionalBreakpoint) {
            this.conditionalBreakpoint.set(conditionalBreakpoint);
        }

        public SimpleObjectProperty<ConditionalBreakpoint> conditionalBreakpointProperty() {
            return conditionalBreakpoint;
        }
    }

    public class GutterFactory implements IntFunction<Node> {
        private final Background defaultBackground =
                new Background(new BackgroundFill(Color.web("#ddd"), null, null));
        private final Val<Integer> nParagraphs;
        private Insets defaultInsets = new Insets(0.0, 5.0, 0.0, 5.0);
        private Paint defaultTextFill = Color.web("#666");
        private Font defaultFont = Font.font("monospace", FontPosture.ITALIC, 13);

        private ObservableList<GutterAnnotation> lineAnnotations =
                new SimpleListProperty<>(FXCollections.observableArrayList());


        public GutterFactory() {
            nParagraphs = LiveList.sizeOf(getParagraphs());
            for (int i = 0; i < 100; i++) {
                lineAnnotations.add(new GutterAnnotation());
            }

            // Update the gutter.
            // If a line is deleted, delete also the image entry for this line
            nParagraphs.addInvalidationObserver((n) -> {
                int diff = lineAnnotations.size() - n;
                if (diff > 0) {
                    lineAnnotations.remove(n, lineAnnotations.size());
                }
            });

        }


        @Override
        public Node apply(int idx) {
            Val<String> formatted = nParagraphs.map(n -> format(idx + 1, n));
            GutterAnnotation model = getLineAnnotation(idx);
            GutterView hbox = new GutterView(model);
            model.textProperty().bind(formatted);

            hbox.setOnMouseClicked((mevent) -> {
                if (mevent.getButton() == MouseButton.SECONDARY)
                    updateMainScriptMarker();
                else
                    toggleBreakpoint(idx);
            });

            hbox.lineNumber.setFont(defaultFont);
            hbox.setBackground(defaultBackground);
            hbox.lineNumber.setTextFill(defaultTextFill);
            hbox.setPadding(defaultInsets);
            hbox.getStyleClass().add("lineno");
            hbox.setStyle("-fx-cursor: hand");

            return hbox;
        }

        private String format(int x, int max) {
            int digits = (int) Math.floor(Math.log10(max)) + 1;
            return String.format("%" + digits + "d", x);
        }

        public GutterAnnotation getLineAnnotation(int idx) {
            if (lineAnnotations.size() <= idx) {
                for (int i = lineAnnotations.size(); i < idx + 1; i++) {
                    lineAnnotations.add(new GutterAnnotation());
                }
            }
            return lineAnnotations.get(idx);
        }
    }

    public class ScriptAreaContextMenu extends ContextMenu {
        public ScriptAreaContextMenu() {
            Utils.createWithFXML(this);
        }

        public void setMainScript(ActionEvent event) {
            System.out.println("ScriptAreaContextMenu.setMainScript");
            List<ProofScript> ast = Facade.getAST(getText());
            int pos = currentMouseOver.get().getInsertionIndex();
            ast.stream().filter(ps ->
                    ps.getRuleContext().getStart().getStartIndex() <= pos
                            && pos <= ps.getRuleContext().getStop().getStopIndex())
                    .findFirst()
                    .ifPresent(proofScript -> mainScript.set(
                            new MainScriptIdentifier(filePath.get().getAbsolutePath(),
                                    proofScript.getStartPosition().getLineNumber(),
                                    proofScript.getName(), ScriptArea.this)));
        }

        public void showPostMortem(ActionEvent event) {
            ScriptArea area = ScriptArea.this;
            int chrIdx = currentMouseOver.get().getCharacterIndex().orElse(0);
            if (chrIdx != 0) {
                int lineNumber = area.offsetToPosition(chrIdx, Bias.Forward).getMajor();
                area.highlightStmt(lineNumber, "line-highlight-postmortem");
            }
        }
    }
}