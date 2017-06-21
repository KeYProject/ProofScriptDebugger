package edu.kit.formal.gui.controls;

import edu.kit.formal.gui.model.Breakpoint;
import edu.kit.formal.gui.model.MainScriptIdentifier;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Controller for TabPane
 *
 * @author Sarah Grebing
 */
public class ScriptTabPane extends TabPane {
    private static Logger logger = LogManager.getLogger(ScriptTabPane.class);
    private ObjectProperty<MainScriptIdentifier> mainScript = new SimpleObjectProperty<>();

    public ScriptTabPane() {
        Utils.createWithFXML(this);
    }

    /**
     * Create a new tab with the specified title
     *
     * @param
     */
    public ScriptArea createNewTab(String filePath) {
        return createNewTab(new File(filePath));
    }

    public ScriptArea createNewTab(File filePath) {
        filePath = filePath.getAbsoluteFile();
        if (findTabForFile(filePath) == null) {
            Tab newTab = new Tab(filePath.getName());
            ScriptArea area = new ScriptArea();
            newTab.setContent(area);
            area.mainScriptProperty().bindBidirectional(mainScript);
            area.setFilePath(filePath);
            this.getTabs().add(newTab);
            return area;
        } else {
            logger.info("File already exists. Will not load it again");
            return findScriptAreaForFile(filePath);
        }
    }

    public Set<Breakpoint> getBreakpoints() {
        HashSet<Breakpoint> breakpoints = new HashSet<>();
        getTabs().forEach(tab ->
                breakpoints.addAll(((ScriptArea) tab.getContent()).getBreakpoints())
        );
        return breakpoints;
    }

    public ScriptArea findScriptAreaForFile(File filePath) {
        Tab t = findTabForFile(filePath);
        if (t == null) return null;
        return ((ScriptArea) t.getContent());
    }

    public Tab findTabForFile(File filePath) {
        return getTabs().stream()
                .filter(tab -> {
                    File path = ((ScriptArea) tab.getContent()).getFilePath();
                    return filePath.equals(path);
                })
                .findAny()
                .orElse(null);
    }

    public ScriptArea getSelectedScriptArea() {
        return (ScriptArea) getSelectionModel().getSelectedItem().getContent();
    }
}
