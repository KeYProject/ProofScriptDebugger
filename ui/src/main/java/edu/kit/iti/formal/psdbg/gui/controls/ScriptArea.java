package edu.kit.iti.formal.psdbg.gui.controls;

import com.google.common.eventbus.Subscribe;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.uka.ilkd.key.logic.SequentFormula;
import edu.kit.iti.formal.psdbg.gui.controller.Events;
import edu.kit.iti.formal.psdbg.gui.model.Breakpoint;
import edu.kit.iti.formal.psdbg.gui.model.MainScriptIdentifier;
import edu.kit.iti.formal.psdbg.lint.LintProblem;
import edu.kit.iti.formal.psdbg.lint.LinterStrategy;
import edu.kit.iti.formal.psdbg.parser.Facade;
import edu.kit.iti.formal.psdbg.parser.ScriptLanguageLexer;
import edu.kit.iti.formal.psdbg.parser.ast.*;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.PopOver;
import org.fxmisc.richtext.CharacterHit;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.MouseOverTextEvent;
import org.fxmisc.richtext.model.NavigationActions;
import org.fxmisc.richtext.model.StyleSpans;
import org.reactfx.collection.LiveList;
import org.reactfx.value.Val;

import java.io.File;
import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.UnaryOperator;

/**
 * ScriptArea is the {@link CodeArea} for writing Proof Scripts.
 * <p>
 * It displays the script code and allows highlighting of lines and setting of breakpoints
 */
public class ScriptArea extends CodeArea {
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

    private GutterFactory gutter;
    private ANTLR4LexerHighlighter highlighter;
    private ListProperty<LintProblem> problems = new SimpleListProperty<>(FXCollections.observableArrayList());
    private SimpleObjectProperty<CharacterHit> currentMouseOver = new SimpleObjectProperty<>();
    private ScriptAreaContextMenu contextMenu = new ScriptAreaContextMenu();

    private Consumer<Token> onPostMortem = token -> {
    };
    private int getTextWithoutMarker;

    public ScriptArea() {
        init();
    }

    private void init() {
        this.setWrapText(true);
        gutter = new GutterFactory();
        highlighter = new ANTLR4LexerHighlighter(
                (String val) -> new ScriptLanguageLexer(CharStreams.fromString(val)));
        this.setParagraphGraphicFactory(gutter);
        getStyleClass().add("script-area");
        installPopup();

        // setOnMouseClicked(this::showContextMenu);
        setContextMenu(contextMenu);

        textProperty().addListener((prop, oldValue, newValue) -> {
            dirty.set(true);
            if (newValue.isEmpty()) {
                System.err.println("text cleared");
            } else {
                updateMainScriptMarker();
                updateHighlight();
                highlightProblems();
                highlightNonExecutionArea();
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
            CharacterHit hit = this.hit(e.getX(), e.getY());
            currentMouseOver.set(this.hit(e.getX(), e.getY()));
            int characterPosition = hit.getInsertionIndex();
            //System.out.println("characterPosition = " + characterPosition);
            // move the caret to that character's position
            this.moveTo(characterPosition, NavigationActions.SelectionPolicy.CLEAR);
        });

        mainScript.addListener((observable) -> updateMainScriptMarker());
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

    private void updateHighlight() {
        String newValue = getText();
        if (newValue.length() != 0) {
            clearStyle(0, newValue.length());
            StyleSpans<? extends Collection<String>> spans = highlighter.highlight(newValue);
            if (spans != null) setStyleSpans(0, spans);

            markedRegions.forEach(reg -> {
                Collection<String> list = new HashSet<>();
                list.add(reg.clazzName);
                setStyle(reg.start, reg.stop, list);
            });
        }

    }

    private void highlightProblems() {
        LinterStrategy ls = LinterStrategy.getDefaultLinter();
        try {
            problems.setAll(ls.check(Facade.getAST(CharStreams.fromString(getText()))));
            for (LintProblem p : problems) {
                for (Token tok : p.getMarkTokens()) {
                    Set<String> problem = new HashSet<>();
                    problem.add("problem");
                    setStyle(tok.getStartIndex(),
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

            setStyleSpans(0, getStyleSpans(0, getExecutionMarkerPosition()).mapStyles(styleMapper));


            //this results in a NotSupportedOperation Exception because the add to an immutable list is not allowed
           /* getStyleSpans(0, getExecutionMarkerPosition()).forEach(span -> {
                Collection<String> style = span.getStyle();
                if (style.isEmpty()) {
                    setStyle(0, getExecutionMarkerPosition(), Collections.singleton("NON_EXE_AREA"));
                } else {
                    style.add("NON_EXE_AREA");
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
                Breakpoint b = new Breakpoint(filePath.get(), line);
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

    public Consumer<Token> getOnPostMortem() {
        return onPostMortem;
    }

    public void setOnPostMortem(Consumer<Token> onPostMortem) {
        this.onPostMortem = onPostMortem;
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

    public void setText(String text) {
        clear();
        insertText(0, text);
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


    @Subscribe
    public void handle(Events.TacletApplicationEvent tap) {
        LOGGER.debug("ScriptArea.handleTacletApplication");
        String tapName = tap.getApp().taclet().displayName();

        SequentFormula seqForm = tap.getPio().sequentFormula();
        //transform term to parsable string representation
        String term = edu.kit.iti.formal.psdbg.termmatcher.Utils.toPrettyTerm(seqForm.formula());


        String text = getText();

        //set new Command on position of execution marker
        int posExecMarker = this.getExecutionMarkerPosition();
        setText(text.substring(0, posExecMarker) + "\n" + tapName + " formula=`" + term + "`;\n" + text.substring(posExecMarker + 1));

        LineMapping lm = new LineMapping(text);
        int line = lm.getLine(getCaretPosition());
        //System.out.println(line);

        setDirty(true);

        //create Call Statement
        Parameters params = new Parameters();
        params.put(new Variable("formula"), new TermLiteral(term));
        CallStatement call = new CallStatement(tapName, params);
        Events.fire(new Events.ScriptModificationEvent(posExecMarker, call));
        Events.unregister(this);
        //this.getMainScript().getScriptArea().insertText(this.getExecutionMarkerPosition(), tapName+" "+on+ ";");

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

        private void addPlaceholder() {
            Label lbl = new Label();
            lbl.setMinWidth(12);
            lbl.setMinHeight(12);
            getChildren().add(lbl);
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

        public void showPostMortem(ActionEvent event) {
            LOGGER.debug("ScriptAreaContextMenu.showPostMortem " + event);

            CharacterHit pos = currentMouseOver.get();
            Token node = Utils.findToken(getText(), pos.getInsertionIndex());
            onPostMortem.accept(node);

            //TODO forward to ProofTreeController, it jumps to the node and this should be done via the callbacks.

            /*ScriptArea area = ScriptArea.this;
            int chrIdx = currentMouseOver.get().getCharacterIndex().orElse(0);
            if (chrIdx != 0) {
                int lineNumber = area.offsetToPosition(chrIdx, Bias.Forward).getMajor();
                area.highlightStmt(lineNumber, "line-highlight-postmortem");
            }*/
        }

        public void setExecutionMarker(ActionEvent event) {
            LOGGER.debug("ScriptAreaContextMenu.setExecutionMarker");
            int pos = getCaretPosition();
            removeExecutionMarker();
            insertExecutionMarker(pos);
        }
    }
}
