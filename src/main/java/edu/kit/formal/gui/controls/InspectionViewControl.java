package edu.kit.formal.gui.controls;

import edu.kit.formal.gui.model.InspectionModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;

import java.io.IOException;

/**
 * Right part of the splitpane that displays the different parts of a state
 */
public class InspectionViewControl extends Tab {
    public final InspectionModel inspectionModel;

    public InspectionViewControl(InspectionModel inspectionModel) {
        super();
        this.inspectionModel = inspectionModel;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InspectionView.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
