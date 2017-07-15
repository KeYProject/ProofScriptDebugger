package edu.kit.formal.gui.controls;

import com.google.common.eventbus.Subscribe;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import edu.kit.formal.gui.controller.Events;
import edu.kit.formal.gui.model.Breakpoint;
import edu.kit.formal.gui.model.MainScriptIdentifier;
import edu.kit.formal.proofscriptparser.Facade;
import edu.kit.formal.proofscriptparser.ast.ASTNode;
import edu.kit.formal.proofscriptparser.ast.ProofScript;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dockfx.DockNode;
import org.dockfx.DockPane;
import org.dockfx.DockPos;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

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


    public ScriptController(DockPane parent) {
        this.parent = parent;
        Events.register(this);
    }

    @Subscribe public void handle(Events.FocusScriptArea fsa) {
        logger.debug("FocusScriptArea handled!");
        openScripts.get(fsa.getScriptArea()).focus();
        fsa.getScriptArea().requestFocus();
    }

    public ObservableMap<ScriptArea, DockNode> getOpenScripts() {
        return openScripts;
    }

    public DockNode getDockNode(File filepath) {
        return getDockNode(findEditor(filepath));
    }

    private DockNode getDockNode(ScriptArea editor) {
        if (editor == null) {
            return null;
        }
        return openScripts.get(editor);
    }

    public ScriptArea createNewTab(File filePath) throws IOException {
        filePath = filePath.getAbsoluteFile();
        if (findEditor(filePath) == null) {
            ScriptArea area = new ScriptArea();
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

    private DockNode createDockNode(ScriptArea area) {
        DockNode dockNode = new DockNode(area, area.getFilePath().getName(), new MaterialDesignIconView(MaterialDesignIcon.FILE_DOCUMENT));
        dockNode.closableProperty().addListener(o -> {
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

    public Set<Breakpoint> getBreakpoints() {
        HashSet<Breakpoint> breakpoints = new HashSet<>();
        openScripts.keySet().forEach(tab ->
                breakpoints.addAll(tab.getBreakpoints())
        );
        return breakpoints;
    }

    public ScriptArea findEditor(File filePath) {
        return openScripts.keySet().stream()
                .filter(scriptArea ->
                        scriptArea.getFilePath().equals(filePath)
                )
                .findAny()
                .orElse(null);
    }

    public void saveCurrentScriptAs(File scriptFile) throws IOException {
        for (ScriptArea area : openScripts.keySet()) {
            if (area.isFocused()) {
                FileUtils.write(scriptFile, area.getText(), Charset.defaultCharset());
                area.setFilePath(scriptFile);
                area.setDirty(false);
            }
        }
    }

    public List<ProofScript> getCombinedAST() {
        ArrayList<ProofScript> all = new ArrayList<>();
        for (ScriptArea area : openScripts.keySet()) {
            //absolute path important to find area later by token
            CharStream stream = CharStreams.fromString(area.getText(), area.getFilePath().getAbsolutePath());
            all.addAll(Facade.getAST(stream));
        }
        return all;
    }

    public ScriptArea newScript() {
        ScriptArea area = new ScriptArea();
        area.setFilePath(new File(Utils.getRandomName()));
        openScripts.put(area, createDockNode(area));
        return area;
    }

    public ASTNodeHighlighter getDebugPositionHighlighter() {
        return postMortemHighlighter;
    }

    public void saveCurrentScript() {
        throw new NotImplementedException();
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

    private ScriptArea findEditor(ASTNode node) {
        File f = new File(node.getRuleContext().getStart().getInputStream().getSourceName());
        return findEditor(f);
    }

    public class ASTNodeHighlighter {
        public final String clazzName;
        private ScriptArea.RegionStyle lastRegion;
        private ScriptArea lastScriptArea;

        private ASTNodeHighlighter(String clazzName) {
            this.clazzName = clazzName;
        }

        public void remove() {
            logger.debug("remove highlight");
            if (lastScriptArea != null) {
                logger.debug("previous highlight on {} for {}", lastScriptArea, lastRegion);
                lastScriptArea.getMarkedRegions().remove(lastRegion);
            }
        }

        public void highlight(ASTNode node) {
            logger.debug("Highlight requested for {}", node);
            remove();

            ScriptArea.RegionStyle r = asRegion(node);
            logger.debug("Region for highlighting: {}", r);

            ScriptArea area = findEditor(node);
            area.getMarkedRegions().add(r);

            getDockNode(area).focus();
            area.requestFocus();

            lastScriptArea = area;
            lastRegion = r;
        }

        private ScriptArea.RegionStyle asRegion(ASTNode node) {
            assert node != null;
            return new ScriptArea.RegionStyle(node.getRuleContext().getStart().getStartIndex(),
                    node.getRuleContext().getStop().getStopIndex(), clazzName);

        }
    }
}
