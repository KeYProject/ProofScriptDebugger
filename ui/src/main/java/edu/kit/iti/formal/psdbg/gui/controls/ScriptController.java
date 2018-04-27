package edu.kit.iti.formal.psdbg.gui.controls;

import com.google.common.eventbus.Subscribe;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.uka.ilkd.key.control.KeYEnvironment;
import edu.kit.iti.formal.psdbg.gui.actions.acomplete.DefaultAutoCompletionController;
import edu.kit.iti.formal.psdbg.gui.actions.inline.FindLabelInGoalList;
import edu.kit.iti.formal.psdbg.gui.actions.inline.FindTermLiteralInSequence;
import edu.kit.iti.formal.psdbg.gui.actions.inline.InlineActionSupplier;
import edu.kit.iti.formal.psdbg.gui.controller.Events;
import edu.kit.iti.formal.psdbg.gui.model.MainScriptIdentifier;
import edu.kit.iti.formal.psdbg.gui.actions.acomplete.Suggestion;
import edu.kit.iti.formal.psdbg.interpreter.data.SavePoint;
import edu.kit.iti.formal.psdbg.interpreter.dbg.Breakpoint;
import edu.kit.iti.formal.psdbg.parser.Facade;
import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;
import edu.kit.iti.formal.psdbg.parser.ast.CallStatement;
import edu.kit.iti.formal.psdbg.parser.ast.ProofScript;
import edu.kit.iti.formal.psdbg.parser.ast.Statement;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dockfx.DockNode;
import org.dockfx.DockPane;
import org.dockfx.DockPos;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A controller for managing the open script files in the dock nodes.
 *
 * @author Sarah Grebing
 */
public class ScriptController {
    public static final String LINE_HIGHLIGHT_POSTMORTEM = "line-highlight-postmortem";

    private static Logger logger = LogManager.getLogger(ScriptController.class);

    private final DockPane parent;

    private final ObservableMap<ScriptArea, DockNode> openScripts = FXCollections.observableMap(new HashMap<>());

    private ObjectProperty<MainScriptIdentifier> mainScript = new SimpleObjectProperty<>();

    private ScriptArea lastScriptArea;

    private ASTNodeHighlighter postMortemHighlighter = new ASTNodeHighlighter(LINE_HIGHLIGHT_POSTMORTEM);

    @Getter
    @Setter
    private List<InlineActionSupplier> actionSuppliers = new ArrayList<>();

    @Getter
    @Setter
    private DefaultAutoCompletionController autoCompleter = new DefaultAutoCompletionController();



    public ScriptController(DockPane parent) {
        this.parent = parent;
        Events.register(this);
        addDefaultInlineActions();

        mainScript.addListener((p,o,n)-> {
            if(o!=null)
                o.getScriptArea().textProperty().removeListener( a-> updateSavePoints());
            n.getScriptArea().textProperty().addListener(a->updateSavePoints());
            updateSavePoints();
        });


    }

    private ObservableList<SavePoint> mainScriptSavePoints
            = new SimpleListProperty<>(FXCollections.observableArrayList());

    private void updateSavePoints() {
        Optional<ProofScript> ms = getMainScript().find(getCombinedAST());
        if(ms.isPresent()) {
            List<SavePoint> list = ms.get().getBody().stream()
                    .filter(SavePoint::isSaveCommand)
                    .map(a -> (CallStatement) a)
                    .map(SavePoint::new)
                    .collect(Collectors.toList());

            mainScriptSavePoints.setAll(list);
        }
    }

    private void addDefaultInlineActions() {
        actionSuppliers.add(new FindLabelInGoalList());
        actionSuppliers.add(new FindTermLiteralInSequence());
    }

    @Subscribe
    public void handle(Events.InsertAtTheEndOfMainScript text) {
        String t = text.getText();
        logger.debug("Try to insert text: {}", text);
        List<ProofScript> ast = Facade.getAST(getMainScript().getScriptArea().getStream());
        Optional<ProofScript> ps = getMainScript().find(ast);
        if (ps.isPresent()) {
            int index = ps.get().getRuleContext().getStop().getStartIndex();
            logger.debug("Found main script! Insert at {}", index);
            getMainScript().getScriptArea().insertText(index, t + "\n");
        }
    }

    @Subscribe
    public void handle(Events.FocusScriptArea fsa) {
        logger.debug("FocusScriptArea handled!");
        openScripts.get(fsa.getScriptArea()).focus();
        fsa.getScriptArea().requestFocus();
    }

    @Subscribe
    public void handle(Events.NewNodeExecuted newNode) {
        logger.debug("Handling new node added event!");
        ASTNode scriptNode = newNode.getCorrespondingASTNode();
        highlightASTNode(newNode.getCorrespondingASTNode());
    }

    public void highlightASTNode(ASTNode node) {
        ScriptArea editor = findEditor(node);
        editor.removeExecutionMarker();
        LineMapping lm = new LineMapping(editor.getText());
        int pos = lm.getLineEnd(node.getStartPosition().getLineNumber() - 1);
        logger.debug("Highlight position: {}", pos);
        // editor.insertExecutionMarker(pos);
    }

    private ScriptArea findEditor(ASTNode node) {
        if (node.getOrigin() != null) {
            File f = new File(node.getOrigin());
            return findEditor(f);
        }
        return null;
    }

    /**
     * Find the scriptarea for the requested file
     *
     * @param filePath
     * @return
     */
    public ScriptArea findEditor(File filePath) {
        return openScripts.keySet().stream()
                .filter(scriptArea ->
                        scriptArea.getFilePath().equals(filePath)
                )
                .findAny()
                .orElse(null);
    }

    public ObservableMap<ScriptArea, DockNode> getOpenScripts() {
        return openScripts;
    }

    public DockNode getDockNode(File filepath) {
        return getDockNode(findEditor(filepath));
    }

    /**
     * Create a new Tab in the ScriptTabPane containing the contents of the file given as argument
     *
     * @param filePath to file that should be loaded to new tab
     * @return refernce to new scriptArea in new tab
     * @throws IOException if an Exception occurs while loading file
     */
    public ScriptArea createNewTab(File filePath) throws IOException {
        filePath = filePath.getAbsoluteFile();
        if (findEditor(filePath) == null) {
            ScriptArea area = new ScriptArea();
            area.setInlineActionSuppliers(getActionSuppliers());
            area.setAutoCompletionController(getAutoCompleter());
            area.mainScriptProperty().bindBidirectional(mainScript);
            area.setFilePath(filePath);
            DockNode dockNode = createDockNode(area);
            openScripts.put(area, dockNode);

            if (filePath.exists()) {
                String code = FileUtils.readFileToString(filePath, "utf-8");
                if (!area.textProperty().getValue().isEmpty()) {
                    area.deleteText(0, area.textProperty().getValue().length());
                }
                area.setText(code);
            }
            return area;
        } else {
            logger.info("File already exists. Will not load it again");
            ScriptArea area = findEditor(filePath);
            return area;
        }
    }

    /* Create new DockNode for ScriptArea Tab
    *
    * @param area ScriptAreaTab
    * @return
    */
    private DockNode createDockNode(ScriptArea area) {
        DockNode dockNode = new DockNode(area, area.getFilePath().getName(), new MaterialDesignIconView(MaterialDesignIcon.FILE_DOCUMENT));
        dockNode.closedProperty().addListener(o -> {
            openScripts.remove(area);
        });
        area.filePathProperty().addListener((observable, oldValue, newValue) -> dockNode.setTitle(newValue.getName()));

        if (lastScriptArea == null)
            dockNode.dock(parent, DockPos.LEFT);
        else
            dockNode.dock(parent, DockPos.CENTER, getDockNode(lastScriptArea));

        area.dirtyProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue)
                    dockNode.setGraphic(new MaterialDesignIconView(MaterialDesignIcon.FILE_DOCUMENT));
                else
                    dockNode.setGraphic(new MaterialDesignIconView(MaterialDesignIcon.FILE_DOCUMENT_BOX));
            }
        });

        this.lastScriptArea = area;
        area.focusedProperty().addListener((observable, oldValue, newValue) -> {
            logger.debug("area = [" + area + "]");
            if (newValue)
                lastScriptArea = area;
        });

        return dockNode;
    }


    public DockNode getDockNode(ScriptArea editor) {
        if (editor == null) {
            return null;
        }
        return openScripts.get(editor);
    }

    /**
     * Get all breakpoints in the current area
     *
     * @return set of all breakpoints in tab
     */
    public Set<Breakpoint> getBreakpoints() {
        HashSet<Breakpoint> breakpoints = new HashSet<>();
        openScripts.keySet().forEach(tab ->
                breakpoints.addAll(tab.getBreakpoints())
        );
        return breakpoints;
    }

    public List<ProofScript> getCombinedAST() {
        ArrayList<ProofScript> all = new ArrayList<>();
        for (ScriptArea area : openScripts.keySet()) {
            //absolute path important to find area later by token

            all.addAll(Facade.getAST(area.getStream()));
        }
        return all;
    }

    /**
     * Open a new script with a random file name and load it into scriptarea
     *
     * @return reference to ScriptArea for new script
     */
    public ScriptArea newScript() {
        ScriptArea area = new ScriptArea();
        area.setFilePath(new File(Utils.getRandomName()));
        openScripts.put(area, createDockNode(area));
        return area;
    }

    public ASTNodeHighlighter getDebugPositionHighlighter() {
        return postMortemHighlighter;
    }

    /**
     * Save the current content of the script area
     */
    public void saveCurrentScript() throws IOException {
        for (ScriptArea scriptArea : openScripts.keySet()) {
            if (scriptArea.isFocused() || openScripts.size() == 1) {
                saveCurrentScriptAs(scriptArea.getFilePath());
            }
        }

        // throw new NotImplementedException();
    }

    /**
     * Save the script currently in focus to the specified file
     *
     * @param scriptFile
     * @throws IOException
     */
    public void saveCurrentScriptAs(File scriptFile) throws IOException {
        for (ScriptArea area : openScripts.keySet()) {
            if (openScripts.size() == 1 || area.isFocused()) {
                FileUtils.write(scriptFile, area.getText(), Charset.defaultCharset());
                area.setFilePath(scriptFile);
                area.setDirty(false);
            }
        }
    }

    public MainScriptIdentifier getMainScript() {
        return mainScript.get();
    }

    public void setMainScript(ProofScript proofScript) {
        MainScriptIdentifier msi = new MainScriptIdentifier();
        msi.setLineNumber(proofScript.getStartPosition().getLineNumber());
        msi.setScriptName(proofScript.getName());
        msi.setSourceName(proofScript.getRuleContext().getStart().getInputStream().getSourceName());
        msi.setScriptArea(findEditor(new File(proofScript.getOrigin())));
        setMainScript(msi);
    }

    public void setMainScript(MainScriptIdentifier mainScript) {
        this.mainScript.set(mainScript);
    }

    public ObjectProperty<MainScriptIdentifier> mainScriptProperty() {
        return mainScript;
    }


    public class ASTNodeHighlighter {
        public final String clazzName;

        private ScriptArea.RegionStyle lastRegion;

        private ScriptArea lastScriptArea;

        private ASTNodeHighlighter(String clazzName) {
            this.clazzName = clazzName;
        }

        public void highlight(ASTNode node) {
            logger.debug("Highlight requested for {}", node);
            remove();

            ScriptArea.RegionStyle r = asRegion(node);
            logger.debug("Region for highlighting: {}", r);

            ScriptArea area = findEditor(node);
            if (area != null) {
                area.getMarkedRegions().add(r);

                getDockNode(area).focus();
                area.requestFocus();
                //area.scrollBy(new Point2D(0, scrollY));
                area.selectRange(0, r.start, 0, r.start);

                lastScriptArea = area;
                lastRegion = r;
            } else {
                logger.debug("Could not find editor for the node to highlight: " + node.getOrigin());
            }
        }

        public void remove() {
            logger.debug("remove highlight");
            if (lastScriptArea != null) {
                logger.debug("previous highlight on {} for {}", lastScriptArea, lastRegion);
                lastScriptArea.getMarkedRegions().remove(lastRegion);
            }
        }

        private ScriptArea.RegionStyle asRegion(ASTNode node) {
            assert node != null;
            if (node.getRuleContext() != null)
                return new ScriptArea.RegionStyle(node.getRuleContext().getStart().getStartIndex(),
                        node.getRuleContext().getStop().getStopIndex(), clazzName);
            else return new ScriptArea.RegionStyle(0, 1, "");
        }
    }

    public ObservableList<SavePoint> getMainScriptSavePoints() {
        return mainScriptSavePoints;
    }

    public void setMainScriptSavePoints(ObservableList<SavePoint> mainScriptSavePoints) {
        this.mainScriptSavePoints = mainScriptSavePoints;
    }
}