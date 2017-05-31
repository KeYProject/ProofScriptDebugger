package edu.kit.formal.gui.controller;

import de.uka.ilkd.key.speclang.Contract;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ListView;
import org.controlsfx.dialog.WizardPane;

/**
 * WozardPane to dosplay all available contracts
 */
public class ContractChooser extends WizardPane {

    ListView listOfContractsView;
    SimpleObjectProperty<Contract> chosen;

    public ContractChooser(SimpleListProperty<Contract> contracts, Contract chosenContract) {
        initPane();
        listOfContractsView.itemsProperty().bind(contracts);
        this.chosen.bind(listOfContractsView.getSelectionModel().selectedItemProperty());

    }

    private void initPane() {
        this.setHeaderText("KeY Contractchooser");

        this.listOfContractsView = new ListView();
        this.listOfContractsView.setVisible(true);
        this.setContent(listOfContractsView);
        this.setVisible(true);

    }
}
