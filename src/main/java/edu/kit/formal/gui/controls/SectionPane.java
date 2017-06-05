package edu.kit.formal.gui.controls;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

/**
 * @author Alexander Weigl
 * @version 1 (05.06.17)
 */
public class SectionPane extends BorderPane {
    private StringProperty title;

    @FXML
    private Label titleLabel;

    @FXML
    private HBox northBox;

    @FXML
    private HBox buttons;

    public SectionPane() {
        super();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SectionPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        title = titleLabel.textProperty();
    }

    public SectionPane(Node center) {
        this();
        setCenter(center);
    }


    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }


    public ObservableList<Node> getHeaderRight() {
        return buttons.getChildren();
    }
}
