package edu.kit.iti.formal.psdbg.gui.controls;

import com.google.common.base.Strings;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import edu.kit.iti.formal.psdbg.gui.actions.acomplete.AutoCompletionController;
import edu.kit.iti.formal.psdbg.gui.actions.acomplete.CompletionPosition;
import edu.kit.iti.formal.psdbg.gui.actions.acomplete.Suggestion;
import edu.kit.iti.formal.psdbg.gui.actions.inline.InlineActionSupplier;
import edu.kit.iti.formal.psdbg.gui.controller.Events;
import edu.kit.iti.formal.psdbg.gui.model.MainScriptIdentifier;
import edu.kit.iti.formal.psdbg.interpreter.dbg.Breakpoint;
import edu.kit.iti.formal.psdbg.lint.LintProblem;
import edu.kit.iti.formal.psdbg.lint.LinterStrategy;
import edu.kit.iti.formal.psdbg.parser.Facade;
import edu.kit.iti.formal.psdbg.parser.ScriptLanguageLexer;
import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;
import edu.kit.iti.formal.psdbg.parser.ast.ProofScript;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.Token;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.PopOver;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CharacterHit;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.event.MouseOverTextEvent;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.wellbehaved.event.EventPattern;
import org.fxmisc.wellbehaved.event.InputMap;
import org.fxmisc.wellbehaved.event.Nodes;
import org.reactfx.collection.LiveList;
import org.reactfx.value.Val;

import java.io.File;
import java.time.Duration;
import java.util.*;
import java.util.function.IntFunction;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import static javafx.scene.input.KeyCombination.SHORTCUT_DOWN;
import static org.fxmisc.wellbehaved.event.EventPattern.keyPressed;
import static org.fxmisc.wellbehaved.event.InputHandler.Result.PROCEED;
import static org.fxmisc.wellbehaved.event.InputMap.*;

/**
 * ScriptArea is the {@link CodeArea} for writing Proof Scripts.
 * <p>
 * It displays the script code and allows highlighting of lines and setting of breakpoints
 */
public class ScriptArea extends BorderPane {
    public static final Logger LOGGER = LogManager.getLogger(ScriptArea.class);

    public static final String EXECUTION_MARKER = "\u2316";

    /**
     * Underlying filepath, should not be null
     */
    private final ObjectProperty<File> filePath = new SimpleObjectProperty<>(this, "filePath", new File(Utils.getRandomName()));

    /**
     * If true, the content was changed since last save.
     */
    private final BooleanProperty dirty = new SimpleBooleanProperty(this, "dirty", false);

    /**
     * CSS classes for regions, used for "manually" highlightning. e.g. debugging marker
     */
    private final SetProperty<RegionStyle> markedRegions = new SimpleSetProperty<>(FXCollections.observableSet());
    /**
     * set by {@link ScriptController}
     */
    private final ObjectProperty<MainScriptIdentifier> mainScript = new SimpleObjectProperty<>();
    public InlineToolbar inlineToolbar = new InlineToolbar();
    /**
     *
     */
    @Getter
    @Setter
    private AutoCompletionController autoCompletionController;

    private AutoCompletion autoCompletion = new AutoCompletion();

    @Getter
    //@Delegate
    private CodeArea codeArea = new CodeArea();

    @Getter
    private VirtualizedScrollPane<CodeArea> scrollPane = new VirtualizedScrollPane<>(codeArea);
    private GutterFactory gutter;
    private ANTLR4LexerHighlighter highlighter;
    private ListProperty<LintProblem> problems = new SimpleListProperty<>(FXCollections.observableArrayList());
    private SimpleObjectProperty<CharacterHit> currentMouseOver = new SimpleObjectProperty<>();
    private ScriptAreaContextMenu contextMenu = new ScriptAreaContextMenu();
    @Getter
    @Setter
    private List<InlineActionSupplier> inlineActionSuppliers = new ArrayList<>();

    public ScriptArea() {
        init();
    }

    private void init() {
        codeArea.setAutoScrollOnDragDesired(false);
        InputMap<KeyEvent> inputMap = sequence(
                process(EventPattern.keyPressed(),
                        (e) -> {
                            if (autoCompletion.isVisible()) autoCompletion.update();
                            inlineToolbar.hide();
                            return PROCEED;
                        }),
                consumeWhen(keyPressed(KeyCode.ENTER), autoCompletion::isVisible,
                        e -> autoCompletion.complete()),
                consume(keyPressed(KeyCode.ENTER, SHORTCUT_DOWN),
                        (e) -> simpleReformat()),
                consume(keyPressed(KeyCode.H, SHORTCUT_DOWN),
                        (e) -> inlineToolbar.show()),
                consume(keyPressed(KeyCode.SPACE, SHORTCUT_DOWN), e -> {
                    if (autoCompletion.isVisible()) {
                        autoCompletion.hide();
                    } else {
                        autoCompletion.update();
                        autoCompletion.show();
                    }
                }),
                consumeWhen(keyPressed(KeyCode.DOWN), autoCompletion::isVisible, e -> autoCompletion.selection(+1)),
                consumeWhen(keyPressed(KeyCode.UP), autoCompletion::isVisible, e -> autoCompletion.selection(-1)),
                consumeWhen(keyPressed(KeyCode.PAGE_DOWN), autoCompletion::isVisible, e -> autoCompletion.selection(+3)),
                consumeWhen(keyPressed(KeyCode.PAGE_UP), autoCompletion::isVisible, e -> autoCompletion.selection(-3)),
                consume(keyPressed(KeyCode.ESCAPE), e -> {
                    autoCompletion.hide();
                    inlineToolbar.hide();
                }));

        Nodes.addInputMap(codeArea, inputMap);

        setCenter(scrollPane);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        scrollPane.widthProperty().addListener((a, b, c) ->
                codeArea.setMinSize(scrollPane.getWidth(), scrollPane.getHeight()));

        /*
        scrollPane.estimatedScrollYProperty().addListener(new ChangeListener<Double>() {
            @Override
            public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
                System.out.println("SCROLL:" + newValue);
            }
        });*/

        codeArea.setWrapText(true);
        gutter = new GutterFactory();
        highlighter = new ANTLR4LexerHighlighter(
                (String val) -> new ScriptLanguageLexer(CharStreams.fromString(val)));
        codeArea.setParagraphGraphicFactory(gutter);
        getStyleClass().add("script-area");
        installPopup();

        setOnMouseClicked(evt -> {
            System.out.println("ScriptArea.init" + evt.isControlDown());
            inlineToolbar.hide();
            if (evt.isControlDown() && evt.getButton() == MouseButton.PRIMARY) {
                inlineToolbar.show();
                evt.consume();
            }
        });
        codeArea.setContextMenu(contextMenu);

        codeArea.textProperty().addListener((prop, oldValue, newValue) -> {
            dirty.set(true);
            if (newValue.isEmpty()) {
                LOGGER.debug("text cleared");
            } else {
                updateMainScriptMarker();
                updateHighlight();
                //highlightProblems();
                //highlightNonExecutionArea();
            }
        });


        markedRegions.addListener((InvalidationListener) o -> updateHighlight());

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

        this.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent e) -> {
            CharacterHit hit = codeArea.hit(e.getX(), e.getY());
            currentMouseOver.set(codeArea.hit(e.getX(), e.getY()));
            int characterPosition = hit.getInsertionIndex();
            //System.out.println("characterPosition = " + characterPosition);
            // move the caret to that character's position
            //this.moveTo(characterPosition, NavigationActions.SelectionPolicy.CLEAR);
        });

        mainScript.addListener((observable) -> updateMainScriptMarker());
    }


    private void installPopup() {
        javafx.stage.Popup popup = new javafx.stage.Popup();
        codeArea.setMouseOverTextDelay(Duration.ofSeconds(1));
        addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_BEGIN, e -> {
            int chIdx = e.getCharacterIndex();
            popup.getContent().setAll(
                    createPopupInformation(chIdx)
            );

            Point2D pos = e.getScreenPosition();
            popup.show(this, pos.getX(), pos.getY() + 10);
        });
/*
        addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_END, e -> {
            popup.hide();
        });*/

        popup.setAutoHide(true);
    }

    private void updateMainScriptMarker() {
        try {
            MainScriptIdentifier ms = mainScript.get();
            LOGGER.debug("ScriptArea.updateMainScriptMarker");

            if (ms != null && filePath.get().getAbsolutePath().equals(ms.getSourceName())) {
                LOGGER.debug("ScriptArea.updateIdentifier" + ms);
                CharStream stream = CharStreams.fromString(codeArea.getText(), filePath.get().getAbsolutePath());
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

    private void updateHighlight() {
        String newValue = codeArea.getText();
        if (newValue.length() != 0) {
            //weigl: resets the text position

            double x = scrollPane.getEstimatedScrollX();
            double y = scrollPane.getEstimatedScrollY();
            //codeArea.clearStyle(0, newValue.length());

            StyleSpans<? extends Collection<String>> spans = highlighter.highlight(newValue);
            if (spans != null) codeArea.setStyleSpans(0, spans);

            markedRegions.forEach(reg -> {
                Collection<String> list = new HashSet<>();
                list.add(reg.clazzName);
                try {
                    codeArea.setStyle(reg.start, reg.stop, list);
                } catch (IndexOutOfBoundsException e) {
                    //weigl silently ignore
                }
            });
            scrollPane.estimatedScrollXProperty().setValue(x);
            scrollPane.estimatedScrollYProperty().setValue(y);
            codeArea.estimatedScrollYProperty().setValue(y);
            //System.out.println(y + ":" + scrollPane.estimatedScrollYProperty().getValue());
        }

    }

    private void simpleReformat() {
        Pattern spacesAtLineEnd = Pattern.compile("[\t ]+\n", Pattern.MULTILINE);
        String text = getText();
        text = spacesAtLineEnd.matcher(text).replaceAll("\n");

        ScriptLanguageLexer lexer = new ScriptLanguageLexer(CharStreams.fromString(text));

        int nested = 0;
        StringBuilder builder = new StringBuilder();
        List<? extends Token> tokens = lexer.getAllTokens();
        for (int i = 0; i < tokens.size(); i++) {
            Token tok = tokens.get(i);
            if (tok.getType() == ScriptLanguageLexer.INDENT)
                nested++;

            if (i + 1 < tokens.size() &&
                    tokens.get(i + 1).getType() == ScriptLanguageLexer.DEDENT)
                nested--;

            if (tok.getType() == ScriptLanguageLexer.WS && tok.getText().startsWith("\n")) {
                builder.append(
                        tok.getText().replaceAll("\n[ \t]*",
                                "\n" + Strings.repeat(" ", nested * 4)));
            } else {
                builder.append(tok.getText());
            }
        }

        int pos = codeArea.getCaretPosition();
        setText(builder.toString());
        codeArea.moveTo(pos);
    }


    private void highlightProblems() {
        LinterStrategy ls = LinterStrategy.getDefaultLinter();
        try {
            problems.setAll(ls.check(Facade.getAST(CharStreams.fromString(getText()))));
            for (LintProblem p : problems) {
                for (Token tok : p.getMarkTokens()) {
                    Set<String> problem = new HashSet<>();
                    problem.add("problem");
                    codeArea.setStyle(tok.getStartIndex(),
                            tok.getStopIndex() + 1, problem);
                }
            }
        } catch (Exception e) {
            //catch parsing exceptions
        }
    }

    private void highlightNonExecutionArea() {
        if (hasExecutionMarker()) {
            LOGGER.debug("getExecutionMarkerPosition() = " + getExecutionMarkerPosition());


            UnaryOperator<Collection<String>> styleMapper = strings -> {
                if (strings.isEmpty()) {
                    return Collections.singleton("NON_EXE_AREA");
                } else {
                    Collection res = strings;
                    res.add("NON_EXE_AREA");
                    return res;
                }
            };

            codeArea.setStyleSpans(0, codeArea.getStyleSpans(0, getExecutionMarkerPosition()).mapStyles(styleMapper));


            //this results in a NotSupportedOperation Exception because the addCell to an immutable list is not allowed
           /* getStyleSpans(0, getExecutionMarkerPosition()).forEach(span -> {
                Collection<String> style = span.getStyle();
                if (style.isEmpty()) {
                    setStyle(0, getExecutionMarkerPosition(), Collections.singleton("NON_EXE_AREA"));
                } else {
                    style.addCell("NON_EXE_AREA");
                }
            });
*/
        }
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

    public void setMainMarker(int lineNumber) {
        gutter.lineAnnotations.forEach(a -> a.setMainScript(false));
        if (lineNumber > 0)
            gutter.getLineAnnotation(lineNumber - 1).setMainScript(true);
    }

    private boolean hasExecutionMarker() {
        return getText().contains(EXECUTION_MARKER);
    }

    public int getExecutionMarkerPosition() {
        return getText().lastIndexOf(EXECUTION_MARKER);
    }

    public void showContextMenu(MouseEvent mouseEvent) {
        if (contextMenu.isShowing())
            contextMenu.hide();

        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            contextMenu.setAutoHide(true);
            contextMenu.show(this, mouseEvent.getScreenX(), mouseEvent.getScreenY());
            mouseEvent.consume();
        } else {

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

    private void toggleBreakpoint(int idx) {
        GutterAnnotation a = gutter.getLineAnnotation(idx);
        a.setBreakpoint(!a.isBreakpoint());
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

    public Collection<? extends Breakpoint> getBreakpoints() {
        List<Breakpoint> list = new ArrayList<>();
        int line = 1;
        for (GutterAnnotation s : gutter.lineAnnotations) {
            if (s.isBreakpoint()) {
                Breakpoint b = new Breakpoint(filePath.get().toString(), line);
                b.setCondition(s.getBreakpointCondition());
                list.add(b);
            }
            line++;
        }
        return list;
    }

    private void showBreakPointMenu(ContextMenuEvent event, int line) {
        event.consume();

        PopOver d = new PopOver();
        d.setStyle("-fx-font-family:sans-serif; -fx-font-size:9pt");
        //d.initStyle(StageStyle.UNDECORATED);
        // d.setX(event.getScreenX());
        //d.setY(event.getScreenY());
        BreakpointDialog bd = new BreakpointDialog();
        GutterAnnotation anno = gutter.getLineAnnotation(line);
        String oldcondition = anno.getBreakpointCondition();
        boolean oldEnabled = anno.isBreakpoint();

        bd.condition.textProperty().bindBidirectional(anno.breakpointCondition);
        bd.enabled.selectedProperty().bindBidirectional(anno.breakpoint);

        bd.title.setText(filePath.get().getName() + ":" + line);
        d.setContentNode(bd);
        d.setFadeInDuration(javafx.util.Duration.millis(500));
        d.setAutoFix(true);
        d.setAutoHide(true);
        d.setOnHiding(windowEvent -> {
            if (!bd.accepted) {
                anno.setBreakpointCondition(oldcondition);
                anno.setBreakpoint(oldEnabled);
            }
        });
        d.show((Node) event.getTarget(), event.getScreenX(), event.getScreenY());
    }


    public ObservableSet<RegionStyle> getMarkedRegions() {
        return markedRegions.get();
    }

    public void setMarkedRegions(ObservableSet<RegionStyle> markedRegions) {
        this.markedRegions.set(markedRegions);
    }

    public SetProperty<RegionStyle> markedRegionsProperty() {
        return markedRegions;
    }

    public boolean isDirty() {
        return dirty.get();
    }

    public void setDirty(boolean dirty) {
        this.dirty.set(dirty);
    }

    public BooleanProperty dirtyProperty() {
        return dirty;
    }

    public void removeExecutionMarker() {
        setText(getTextWithoutMarker());
        //Events.unregister(this);
    }

    private String getTextWithoutMarker() {
        return getText().replace("" + EXECUTION_MARKER, "");
    }

    /**
     * Insert Execution marker at absolute character position
     *
     * @param pos
     */
    public void insertExecutionMarker(int pos) {
        LOGGER.debug("ScriptArea.insertExecutionMarker");
        Events.register(this);
        String text = getText();
        setText(text.substring(0, pos) + EXECUTION_MARKER + text.substring(pos));
    }

    public CodePointCharStream getStream() {
        return CharStreams.fromString(getText(), getFilePath().getAbsolutePath());
    }

    public ObservableValue<String> textProperty() {
        return codeArea.textProperty();
    }

    public void insertText(int index, String s) {
        codeArea.insertText(index, s);
    }

    //region delegates
    public String getText() {
        return getCodeArea().getText();
    }

    public void setText(String text) {
        codeArea.clear();
        codeArea.insertText(0, text);
    }

    public void deleteText(int i, int length) {
            codeArea.deleteText(i,length);
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
                MaterialDesignIcon.CHECK, "12"
        );

        private Label lineNumber = new Label("not set");

        public GutterView(GutterAnnotation ga) {
            annotation.addListener((o, old, nv) -> {
                if (old != null) {
                    old.breakpoint.removeListener(this::update);
                    old.breakpoint.removeListener(this::update);
                    old.mainScript.removeListener(this::update);
                    lineNumber.textProperty().unbind();
                }

                nv.breakpoint.addListener(this::update);
                nv.mainScript.addListener(this::update);
                nv.breakpoint.addListener(this::update);
                nv.conditional.addListener(this::update);

                lineNumber.textProperty().bind(nv.textProperty());

                update(null);
            });
            setAnnotation(ga);
        }

        public void update(Observable o) {
            getChildren().setAll(lineNumber);
            if (getAnnotation().isMainScript()) getChildren().add(iconMainScript);
            else addPlaceholder();
            if (getAnnotation().isBreakpoint())
                getChildren().add(getAnnotation().getConditional()
                        ? iconConditionalBreakPoint
                        : iconBreakPoint);
            else
                addPlaceholder();
        }

        public GutterAnnotation getAnnotation() {
            return annotation.get();
        }

        public void setAnnotation(GutterAnnotation annotation) {
            this.annotation.set(annotation);
        }

        private void addPlaceholder() {
            Label lbl = new Label();
            lbl.setMinWidth(12);
            lbl.setMinHeight(12);
            getChildren().add(lbl);
        }

        public SimpleObjectProperty<GutterAnnotation> annotationProperty() {
            return annotation;
        }
    }

    private static class GutterAnnotation {
        private StringProperty text = new SimpleStringProperty();

        private SimpleBooleanProperty breakpoint = new SimpleBooleanProperty();

        private StringProperty breakpointCondition = new SimpleStringProperty();

        private BooleanBinding conditional = breakpointCondition.isNotNull().and(breakpointCondition.isNotEmpty());

        private BooleanProperty mainScript = new SimpleBooleanProperty();

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

        public SimpleBooleanProperty breakpointProperty() {
            return breakpoint;
        }

        public String getBreakpointCondition() {
            return breakpointCondition.get();
        }

        public void setBreakpointCondition(String breakpointCondition) {
            this.breakpointCondition.set(breakpointCondition);
        }

        public StringProperty breakpointConditionProperty() {
            return breakpointCondition;
        }

        public Boolean getConditional() {
            return conditional.get();
        }

        public BooleanBinding conditionalProperty() {
            return conditional;
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
    }

    public static class BreakpointDialog extends BorderPane {
        @FXML
        private CheckBox enabled;

        @FXML
        private TextField condition;

        @FXML
        private Label title;

        @FXML
        private boolean accepted = false;

        public BreakpointDialog() {
            Utils.createWithFXML(this);
            getStyleClass().add("breakpoint-menu");
        }

        public void save(ActionEvent e) {
            accepted = true;
            ((Button) e.getTarget()).getScene().getWindow().hide();
        }
    }

    @RequiredArgsConstructor
    @Data
    public static class RegionStyle {
        public final int start, stop;

        public final String clazzName;
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
            nParagraphs = LiveList.sizeOf(codeArea.getParagraphs());
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

            hbox.setOnContextMenuRequested(mevent ->
                    showBreakPointMenu(mevent, idx)
            );

            hbox.setOnMouseClicked((mevent) -> {
                mevent.consume();
                if (mevent.getButton() == MouseButton.PRIMARY)
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

        @FXML
        public void setMainScript(ActionEvent event) {
            LOGGER.debug("ScriptAreaContextMenu.setMainScript");
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

        @FXML
        public void showPostMortem(ActionEvent event) {
            LOGGER.debug("ScriptAreaContextMenu.showPostMortem " + event);

            CharacterHit pos = currentMouseOver.get();
            Token node = Utils.findToken(getText(), pos.getInsertionIndex());

            Events.fire(new Events.ShowPostMortem(node.toString(), node.getStopIndex()));
            //TODO forward to ProofTreeManager, it jumps to the node and this should be done via the callbacks.

            /*ScriptArea area = ScriptArea.this;
            int chrIdx = currentMouseOver.get().getCharacterIndex().orElse(0);
            if (chrIdx != 0) {
                int lineNumber = area.offsetToPosition(chrIdx, Bias.Forward).getMajor();
                area.highlightStmt(lineNumber, "line-highlight-postmortem");
            }*/
        }

        public void setExecutionMarker(ActionEvent event) {
            LOGGER.debug("ScriptAreaContextMenu.setExecutionMarker");
            int pos = codeArea.getCaretPosition();
            removeExecutionMarker();
            insertExecutionMarker(pos);
        }
    }

    public class InlineToolbar {
        private Stage inlineToolbar;
        private Scene scene;

        public void hide() {
            getInlineToolbar().hide();
        }

        public void show() {
            inlineToolbar = getInlineToolbar();
            VBox vbox = new VBox();
            //ToolBar tb = new ToolBar();
            HBox tb = new HBox();

            List<ProofScript> ast = Facade.getAST(getText());
            int pos = codeArea.getCaretPosition();
            FindNearestASTNode findNearestASTNode = new FindNearestASTNode(pos);
            Optional<ASTNode> node = findNearestASTNode.find(ast);

            if (node.isPresent()) {
                inlineActionSuppliers.stream()
                        .flatMap(i -> i.get(node.get()).stream())
                        .sorted()
                        .forEach(ia -> {
                            Button btn = new Button("", ia.getGraphics());
                            btn.setOnAction(ia.getEventHandler());
                            tb.getChildren().add(btn);
                        });

                vbox.getChildren().addAll(tb, new Label(node.toString()));
                inlineToolbar.getScene().setRoot(vbox);
                Bounds b = codeArea.getCaretBounds().get();
                inlineToolbar.setX(b.getMaxX());
                inlineToolbar.setY(b.getMaxY());
                inlineToolbar.show();
            }
        }

        public Scene getScene() {
            if (scene == null) {
                scene = new Scene(new VBox(new Label("dummy")));
                //scene.getStylesheets().addAll(ScriptArea.this.getScene().getStylesheets());
            }
            return scene;
        }

        public Stage getInlineToolbar() {
            if (inlineToolbar == null) {
                inlineToolbar = new Stage();
                inlineToolbar.setScene(getScene());
                inlineToolbar.initModality(Modality.NONE);
                inlineToolbar.initStyle(StageStyle.TRANSPARENT);
                inlineToolbar.initOwner(ScriptArea.this.getScene().getWindow());
                inlineToolbar.setAlwaysOnTop(true);
                inlineToolbar.setResizable(false);
                inlineToolbar.setIconified(false);
                inlineToolbar.focusedProperty().addListener((p, o, n) -> {
                    if (o && !n)
                        inlineToolbar.hide();
                });
            }
            return inlineToolbar;
        }
    }

    public class AutoCompletion {
        /*private AutoCompletePopup<Suggestion> popup = new AutoCompletePopup<>();
        private ListView<Suggestion> suggestionView;
        private ObservableList<Suggestion> suggestions;*/
        private int lastSelected = -1;
        private Stage popup;
        private ListView<Suggestion> suggestionView = new ListView<>();
        private ObservableList<Suggestion> suggestions;

        public AutoCompletion() {
            /*popup.setAutoFix(true);
            popup.setAutoHide(true);
            popup.setSkin(new AutoCompletePopupSkin<>(popup));
            suggestionView = (ListView<Suggestion>) popup.getSkin().getNode();*/
            suggestions = suggestionView.getItems();
            suggestionView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            suggestionView.getSelectionModel().getSelectedIndices().addListener((InvalidationListener) observable -> {
                System.out.println(" = " + suggestionView.getSelectionModel().getSelectedIndex());
                //lastSelected = suggestionView.getSelectionModel().getSelectedIndex();
            });
            //popup.setVisibleRowCount(5);

            suggestionView.setEditable(false);
            suggestionView.setCellFactory(param -> new SuggestionCell());
        }

        public Stage getPopup() {
            if (popup == null) {
                popup = new Stage();
                popup.initOwner(ScriptArea.this.getScene().getWindow());
                popup.initStyle(StageStyle.TRANSPARENT);
                popup.initModality(Modality.NONE);
                Scene scene = new Scene(suggestionView);
                scene.getStylesheets().setAll(getScene().getStylesheets());
                popup.setScene(scene);
            }
            return popup;
        }

        private void handle(Event event) {
            System.out.println("event = " + event);
            event.consume();
        }

        public void update() {
            popup = getPopup();
            int end = codeArea.getCaretPosition() - 1;
            //int start = text.lastIndexOf(' ');
            //final String searchPrefix = text.substring(start).trim();
            //System.out.println("searchPrefix = " + searchPrefix);

            CompletionPosition cp = new CompletionPosition(getText(), end);
            System.out.println("cp.getPrefix() = " + cp.getPrefix());
            List<Suggestion> newS = autoCompletionController.getSuggestions(cp);
            suggestions.setAll(newS);

            Bounds b = codeArea.getCaretBounds().get();
            popup.setX(b.getMaxX());
            popup.setY(b.getMaxY());
            popup.setHeight(25 * Math.min(Math.max(newS.size(), 3), 10));

        }

        public void show() {
            //popup.show(ScriptArea.this.getScene().getWindow());
            popup.show();
            codeArea.requestFocus();
        }

        public void hide() {
            popup.hide();
        }

        public boolean isVisible() {
            return popup != null && popup.isShowing();
        }

        public void complete() {
            String entry = suggestions.get(lastSelected).getText();
            codeArea.selectWord();
            codeArea.replaceSelection(entry);
            hide();
            codeArea.requestFocus();
        }

        public void selection(int relline) {
            if (lastSelected < 0) {
                if (relline < 0) {
                    lastSelected = suggestionView.getItems().size() - 1;
                } else {
                    lastSelected = 0;
                }
            } else {
                lastSelected += relline;
            }
            suggestionView.getSelectionModel().select(lastSelected);
            suggestionView.scrollTo(lastSelected);
        }
    }

    //endregion
}

class SuggestionCell extends ListCell<Suggestion> {
    @Override
    protected void updateItem(Suggestion item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
            setText("");
        } else {
            setText(item.getText());
            setGraphic(new MaterialDesignIconView(item.getCategory().getIcon()));
        }
    }
}
