package edu.kit.formal.gui.controls;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Paths;
import java.util.Iterator;

/**
 * Controller for TabPane
 *
 * @author Sarah Grebing
 */
public class ScriptTabPane extends TabPane {
    private static Logger logger = LogManager.getLogger(ScriptTabPane.class);

    /**
     * String is filepath; if string already exists if new tab should be created nothing happens
     * If wished for, instead of filepath an object with more script information may be used
     */
    private BiMap<String, ScriptAreaTab> mappingOfTabs = HashBiMap.create();

    private SimpleObjectProperty<Tab> activeTab = new SimpleObjectProperty<>();
    private StringProperty activeScriptPath = new SimpleStringProperty();

    public ScriptTabPane() {
        Utils.createWithFXML(this);
        activeTab.bind(this.getSelectionModel().selectedItemProperty());
        activeTab.addListener(o -> {
            activeScriptPath.set(mappingOfTabs.inverse().get(activeTab.get()));
        });
    }

    /**
     * Create a new tab with the specified title
     *
     * @param
     */
    public void createNewTab(String filePath) {
        if (!mappingOfTabs.containsKey(filePath)) {
            ScriptAreaTab newTab = new ScriptAreaTab(Paths.get(filePath).getFileName().toString());
            mappingOfTabs.put(filePath, newTab);
            this.getTabs().add(newTab);
            this.getSelectionModel().select(mappingOfTabs.get(filePath));

        } else {
            logger.info("File already exists. Will not load it again");
            this.getSelectionModel().select(mappingOfTabs.get(filePath));
        }
    }

    /**
     * Get the tab with the specified filename as title
     *
     * @param title
     * @return
     */
    public ScriptArea getTabContent(String title) {
        Iterator<Tab> iter = this.getTabs().iterator();
        while (iter.hasNext()) {
            ScriptAreaTab currentTab = (ScriptAreaTab) iter.next();
            if (currentTab.getText().equals(title)) {
                return currentTab.getScriptArea();
            }
        }
        return null;
    }

    public ScriptAreaTab getActiveScriptAreaTab() {
        return (ScriptAreaTab) activeTab.get();
    }


    public Tab getActiveTab() {
        return activeTab.get();
    }

    public SimpleObjectProperty<Tab> activeTabProperty() {
        return activeTab;
    }

    public void setActiveTab(Tab activeTab) {
        this.activeTab.set(activeTab);
    }

    public String getActiveScriptPath() {
        return activeScriptPath.get();
    }

    public StringProperty activeScriptPathProperty() {
        return activeScriptPath;
    }

    public void setActiveScriptPath(String activeScriptPath) {
        this.activeScriptPath.set(activeScriptPath);
    }
}
