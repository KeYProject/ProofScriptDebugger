package edu.kit.formal.psdb.gui.controls;

import com.sun.javafx.scene.control.skin.TabPaneSkin;
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;

/**
 * A way to display a custom tab when tab pane is empty
 * from https://stackoverflow.com/questions/35239420/display-label-if-tabpane-has-no-tabs
 */
public class CustomTabPaneSkin extends TabPaneSkin {
    private final PlaceHolderTab placeHolder;

    public CustomTabPaneSkin(TabPane tabPane) {
        super(tabPane);
        placeHolder = new PlaceHolderTab();
        placeHolder.prefWidthProperty().bind(getSkinnable().widthProperty());
        placeHolder.prefHeightProperty().bind(getSkinnable().heightProperty());


        for (Node node : getChildren()) {
            if (node.getStyleClass().contains("tab-header-area")) {

                Pane headerArea = (Pane) node;
                // Header area is hidden if there is no tabs, thus when the tabpane is "empty"
                headerArea.visibleProperty().addListener((observable, oldValue, newValue) ->
                        {
                            if (newValue) {
                                getChildren().remove(placeHolder);
                            } else {
                                getChildren().add(placeHolder);
                            }
                        }
                );

                break;
            }
        }
    }

    public PlaceHolderTab getPlaceHolder() {
        return placeHolder;
    }
}
