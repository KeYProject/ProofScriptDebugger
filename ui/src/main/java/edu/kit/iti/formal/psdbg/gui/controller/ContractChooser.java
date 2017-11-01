package edu.kit.iti.formal.psdbg.gui.controller;

import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.speclang.Contract;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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
        private final TextFlow text = new TextFlow();

        ContractListCell(ListView<Contract> contractListView) {
            itemProperty().addListener((observable, oldValue, newValue) -> render());
            selectedProperty().addListener((observable, oldValue, newValue) -> render());
            text.maxWidthProperty().bind(contractListView.widthProperty());
            setGraphic(text);
        }

        private void render() {
            if (getItem() != null) {
                text.getChildren().clear();
                String content = getItem().getPlainText(services);
                Text contract = new Text("Contract for method: ");
                Text name = new Text(getItem().getTarget().toString());
                Text tcontent = new Text(content);

                contract.setStyle("-fx-font-weight: bold; -fx-font-size: 120%");
                name.setStyle("-fx-font-family: monospace; -fx-font-size: 120%");
                tcontent.setStyle("-fx-font-family: monospace");

                text.getChildren().add(contract);
                text.getChildren().add(name);
                text.getChildren().add(new Text("\n"));
                //text.getChildren().add(tcontent);

                for (String line : content.split("\n")) {
                    if (line.contains(":")) {
                        String[] a = line.split(":", 2);
                        Text s = new Text(String.format("%-15s", a[0] + ":"));
                        Text t = new Text(a[1] + "\n");
                        s.setStyle("-fx-font-family: monospace;-fx-font-weight:bold;-fx-min-width: 10em");
                        t.setStyle("-fx-font-family: monospace");
                        text.getChildren().addAll(s, t);
                    } else {
                        Text t = new Text(line + "\n");
                        t.setStyle("-fx-font-family: monospace");
                        text.getChildren().addAll(t);
                    }
                }

                if (selectedProperty().get()) {
                    //   text.setStyle("-fx-background-color: lightblue");
                }
            }
        }

        private String beautifyContractHTML(String html, String name) {
            return "Contract for <b>" + name + "</b><br>" + html;
        }

    }


}
