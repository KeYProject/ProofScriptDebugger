package edu.kit.formal.psdb.gui.controller;

import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.speclang.Contract;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
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

    public ContractChooser(Services services,
                           SimpleListProperty<Contract> contracts) {
        this(services);
        listOfContractsView.itemsProperty().bind(contracts);
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
        ContractListCell(ListView<Contract> contractListView) {
            itemProperty().addListener((observable, oldValue, newValue) -> render());
            selectedProperty().addListener((observable, oldValue, newValue) -> render());
        }

        private void render() {
            /*
            FunctionalOperationContractImpl c = (FunctionalOperationContractImpl) getItem();
            IProgramMethod pm = c.getTarget();
            Operator originalSelfVar = c.get != null ? contractSelf.op() : null;
            Operator originalResultVar = resultTerm != null ? resultTerm.op() : null;
            ImmutableList<? extends SVSubstitute> originalParamVars;
            ProgramVariable originalExcVar;
            boolean hasMby = c.hasMby();
            Term originalMby = ;
            Map<LocationVariable, Term> originalMods;
            Map<LocationVariable, Boolean> hasRealModifiesClause;
            de.uka.ilkd.key.logic.Term globalDefs;
            Map<LocationVariable, Term> originalPres;
            Map<LocationVariable, Term> originalFreePres;
            Map<LocationVariable, Term> originalPosts;
            Map<LocationVariable, Term> originalFreePosts;
            Map<LocationVariable, Term> originalAxioms,
            Modality modality;
            boolean transaction;
            boolean includeHtmlMarkup;
            boolean usePrettyPrinting,
            boolean useUnicodeSymbols

            HeapLDT heapLDT = services.getTypeConverter().getHeapLDT();
            TermBuilder tb = services.getTermBuilder();
            LocationVariable baseHeap = heapLDT.getHeap();
            StringBuffer sig = new StringBuffer();
            if (originalResultVar != null) {
                sig.append(originalResultVar);
                sig.append(" = ");
            } else if (pm.isConstructor()) {
                sig.append(originalSelfVar);
                sig.append(" = new ");
            }

            if (!pm.isStatic() && !pm.isConstructor()) {
                sig.append(originalSelfVar);
                sig.append(".");
            }

            sig.append(pm.getName());
            sig.append("(");
            Iterator var25 = originalParamVars.iterator();

            while (var25.hasNext()) {
                SVSubstitute subst = (SVSubstitute) var25.next();
                if (subst instanceof Named) {
                    Named named = (Named) subst;
                    sig.append(named.name()).append(", ");
                } else if (subst instanceof Term) {
                    sig.append(LogicPrinter.quickPrintTerm((Term) subst, services, usePrettyPrinting, useUnicodeSymbols).trim()).append(", ");
                } else {
                    sig.append(subst).append(", ");
                }
            }

            if (!originalParamVars.isEmpty()) {
                sig.setLength(sig.length() - 2);
            }

            sig.append(")");
            if (!pm.isModel()) {
                sig.append(" catch(");
                sig.append(originalExcVar);
                sig.append(")");
            }

            String mby = hasMby ? LogicPrinter.quickPrintTerm(originalMby, services, usePrettyPrinting, useUnicodeSymbols) : null;
            String mods = "";
            Iterator var38 = heapLDT.getAllHeaps().iterator();

            String freePres;
            while (var38.hasNext()) {
                LocationVariable h = (LocationVariable) var38.next();
                if (originalMods.get(h) != null) {
                    freePres = LogicPrinter.quickPrintTerm((Term) originalMods.get(h), services, usePrettyPrinting, useUnicodeSymbols);
                    mods = mods + (includeHtmlMarkup ? "<br><b>" : "\n") + "mod" + (h == baseHeap ? "" : "[" + h + "]") + (includeHtmlMarkup ? "</b> " : ": ") + (includeHtmlMarkup ? LogicPrinter.escapeHTML(freePres, false) : freePres.trim());
                    if (!((Boolean) hasRealModifiesClause.get(h)).booleanValue()) {
                        mods = mods + (includeHtmlMarkup ? "<b>" : "") + ", creates no new objects" + (includeHtmlMarkup ? "</b>" : "");
                    }
                }
            }

            String globalUpdates = "";
            String pres;
            if (globalDefs != null) {
                pres = LogicPrinter.quickPrintTerm(globalDefs, services, usePrettyPrinting, useUnicodeSymbols);
                globalUpdates = (includeHtmlMarkup ? "<br><b>" : "\n") + "defs" + (includeHtmlMarkup ? "</b> " : ": ") + (includeHtmlMarkup ? LogicPrinter.escapeHTML(pres, false) : pres.trim());
            }

            pres = "";
            Iterator var41 = heapLDT.getAllHeaps().iterator();

            String freePosts;
            while (var41.hasNext()) {
                LocationVariable h = (LocationVariable) var41.next();
                if (originalPres.get(h) != null) {
                    freePosts = LogicPrinter.quickPrintTerm((Term) originalPres.get(h), services, usePrettyPrinting, useUnicodeSymbols);
                    pres = pres + (includeHtmlMarkup ? "<br><b>" : "\n") + "pre" + (h == baseHeap ? "" : "[" + h + "]") + (includeHtmlMarkup ? "</b> " : ": ") + (includeHtmlMarkup ? LogicPrinter.escapeHTML(freePosts, false) : freePosts.trim());
                }
            }

            freePres = "";
            Iterator var42 = heapLDT.getAllHeaps().iterator();

            String printPosts;
            while (var42.hasNext()) {
                LocationVariable h = (LocationVariable) var42.next();
                Term freePre = (Term) originalFreePres.get(h);
                if (freePre != null && !freePre.equals(tb.tt())) {
                    printPosts = LogicPrinter.quickPrintTerm(freePre, services, usePrettyPrinting, useUnicodeSymbols);
                    freePres = freePres + (includeHtmlMarkup ? "<br><b>" : "\n") + "free pre" + (h == baseHeap ? "" : "[" + h + "]") + (includeHtmlMarkup ? "</b> " : ": ") + (includeHtmlMarkup ? LogicPrinter.escapeHTML(printPosts, false) : printPosts.trim());
                }
            }

            String posts = "";
            Iterator var45 = heapLDT.getAllHeaps().iterator();

            while (var45.hasNext()) {
                LocationVariable h = (LocationVariable) var45.next();
                if (originalPosts.get(h) != null) {
                    printPosts = LogicPrinter.quickPrintTerm((Term) originalPosts.get(h), services, usePrettyPrinting, useUnicodeSymbols);
                    posts = posts + (includeHtmlMarkup ? "<br><b>" : "\n") + "post" + (h == baseHeap ? "" : "[" + h + "]") + (includeHtmlMarkup ? "</b> " : ": ") + (includeHtmlMarkup ? LogicPrinter.escapeHTML(printPosts, false) : printPosts.trim());
                }
            }

            freePosts = "";
            Iterator var47 = heapLDT.getAllHeaps().iterator();

            String printAxioms;
            while (var47.hasNext()) {
                LocationVariable h = (LocationVariable) var47.next();
                Term freePost = (Term) originalFreePosts.get(h);
                if (freePost != null && !freePost.equals(tb.tt())) {
                    printAxioms = LogicPrinter.quickPrintTerm(freePost, services, usePrettyPrinting, useUnicodeSymbols);
                    freePosts = freePosts + (includeHtmlMarkup ? "<br><b>" : "\n") + "free post" + (h == baseHeap ? "" : "[" + h + "]") + (includeHtmlMarkup ? "</b> " : ": ") + (includeHtmlMarkup ? LogicPrinter.escapeHTML(printAxioms, false) : printAxioms.trim());
                }
            }

            String axioms = "";
            if (originalAxioms != null) {
                Iterator var50 = heapLDT.getAllHeaps().iterator();

                while (var50.hasNext()) {
                    LocationVariable h = (LocationVariable) var50.next();
                    if (originalAxioms.get(h) != null) {
                        printAxioms = LogicPrinter.quickPrintTerm((Term) originalAxioms.get(h), services, usePrettyPrinting, useUnicodeSymbols);
                        posts = posts + (includeHtmlMarkup ? "<br><b>" : "\n") + "axiom" + (h == baseHeap ? "" : "[" + h + "]") + (includeHtmlMarkup ? "</b> " : ": ") + (includeHtmlMarkup ? LogicPrinter.escapeHTML(printAxioms, false) : printAxioms.trim());
                    }
                }
            }

            return includeHtmlMarkup ? "<html><i>" + LogicPrinter.escapeHTML(sig.toString(), false) + "</i>" + globalUpdates + pres + freePres + posts + freePosts + axioms + mods + (hasMby ? "<br><b>measured-by</b> " + LogicPrinter.escapeHTML(mby, false) : "") + "<br><b>termination</b> " + modality + (transaction ? "<br><b>transaction applicable</b>" : "") + "</html>" : sig.toString() + globalUpdates + pres + freePres + posts + freePosts + axioms + mods + (hasMby ? "\nmeasured-by: " + mby : "") + "\ntermination: " + modality + (transaction ? "\ntransaction applicable:" : "");
            */
            if (getItem() == null) {
                setGraphic(null);
                return;
            }

            VBox box = new VBox();
            Label head = new Label(getItem().getDisplayName() + "\n");
            head.getStyleClass().add("title");

            Label contract = new Label(getItem().getPlainText(services));
            contract.getStyleClass().add("contract");

            setGraphic(
                    new VBox(head, contract)
            );
        }
    }
}
