package edu.kit.formal.gui.controls;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Controller for TabPane
 */
public class ScriptTabsController extends TabPane {
    /**
     * String is filepath; if string already exists if new tab should be created nothing happens
     * If wished for, instead of filepath an object with omre script information may be used
     */
    private HashMap<String, ScriptAreaTab> mappingOfTabs = new HashMap();


    private SimpleObjectProperty<Tab> activeTab = new SimpleObjectProperty<>();

    public ScriptTabsController() {
        super();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TabPaneScriptArea.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        activeTab.bind(this.getSelectionModel().selectedItemProperty());


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
            System.out.println("File already exists. Will not load it again");
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

}
