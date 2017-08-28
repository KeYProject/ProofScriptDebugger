package edu.kit.iti.formal.psdbg.gui.controller;

import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.speclang.Contract;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import lombok.Getter;

import java.util.List;

/**
 * A Contract Chooser is a modal dialog, which shows a list of contracts and lets the user select one.
 * <p>
 * <p>
 * <p>
 * <p>
 * WizardPane to display all available contracts
 *
 * @author S. Grebing
 * @author A. Weigl
 */
public class ContractChooser extends Dialog<Contract> {
    @Getter
    private final MultipleSelectionModel<Contract> selectionModel;
    private final ObjectProperty<ObservableList<Contract>> items;
    private final ListView<Contract> listOfContractsView = new ListView<>();
    private final Services services;

    public ContractChooser(Services services,
                           SimpleListProperty<Contract> contracts) {
        this(services);
        listOfContractsView.itemsProperty().bind(contracts);
    }

    public ContractChooser(Services services) {
        super();
        this.services = services;

        setTitle("Key Contract Chooser");
        setHeaderText("Please select a contract");
        setResizable(true);

        initModality(Modality.APPLICATION_MODAL);

        selectionModel = listOfContractsView.getSelectionModel();
        items = listOfContractsView.itemsProperty();

        DialogPane dpane = new DialogPane();
        dpane.setContent(listOfContractsView);
        dpane.setPrefSize(680, 320);
        setDialogPane(dpane);

        listOfContractsView.setCellFactory(ContractListCell::new);

        setResultConverter((value) -> value == ButtonType.OK ? listOfContractsView.getSelectionModel().getSelectedItem() : null);
        dpane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        final Button okButton = (Button) dpane.lookupButton(ButtonType.OK);
        okButton.setDefaultButton(true);

        listOfContractsView.setOnKeyPressed(event -> okButton.fire());
    }

    public ContractChooser(Services service, List<Contract> contracts) {
        this(service);
        listOfContractsView.itemsProperty().get().setAll(contracts);
    }

    public MultipleSelectionModel<Contract> getSelectionModel() {
        return selectionModel;
    }

    public ObservableList<Contract> getItems() {
        return items.get();
    }

    public void setItems(ObservableList<Contract> items) {
        this.items.set(items);
    }

    public ObjectProperty<ObservableList<Contract>> itemsProperty() {
        return items;
    }

    private class ContractListCell extends ListCell<Contract> {
        private final WebView webView = new WebView();

        ContractListCell(ListView<Contract> contractListView) {
            itemProperty().addListener((observable, oldValue, newValue) -> render());
            selectedProperty().addListener((observable, oldValue, newValue) -> render());
            setGraphic(webView);
        }

        private void render() {
            if (getItem() != null) {
                String content = beautifyContractHTML(getItem().getHTMLText(services), getItem().getTarget().toString());
                webView.getEngine().loadContent(content);

                //webView.getEngine().loadContent(getItem().getHTMLText(services));
                //int val = (int) webView.getEngine().executeScript("document.height");
                webView.setPrefHeight(150);
                webView.setDisable(true);

                if (selectedProperty().get()) {
                    webView.setStyle("-fx-background-color: lightblue");
                }
            }
        }

        private String beautifyContractHTML(String html, String name) {
            return "Contract for <b>" + name + "</b><br>" + html;
        }
    }
}
