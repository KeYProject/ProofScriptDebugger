package edu.kit.formal.gui.controls;

import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.logic.NamespaceSet;
import de.uka.ilkd.key.logic.Sequent;
import de.uka.ilkd.key.pp.*;
import de.uka.ilkd.key.proof.Node;
import de.uka.ilkd.key.settings.ProofIndependentSettings;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.fxmisc.richtext.CharacterHit;
import org.fxmisc.richtext.CodeArea;

import java.io.StringWriter;

/**
 * @author Alexander Weigl
 * @version 1 (03.06.17)
 */
public class SequentView extends CodeArea {
    private Services services;

    private LogicPrinter lp;
    private IdentitySequentPrintFilter filter;
    private LogicPrinter.PosTableStringBackend backend;
    private SimpleObjectProperty<de.uka.ilkd.key.proof.Node> node = new SimpleObjectProperty<>();


    public SequentView() {
        getStyleClass().add("sequent-view");
        setEditable(false);
        node.addListener(this::update);
        setOnMouseMoved(this::hightlight);
    }

    private void hightlight(MouseEvent mouseEvent) {
        if (backend == null) return;

        CharacterHit hit = hit(mouseEvent.getX(), mouseEvent.getY());
        int insertionPoint = hit.getInsertionIndex();
        PosInSequent pis = backend.getInitialPositionTable().getPosInSequent(insertionPoint, filter);
        if (pis != null) {
            Range r = backend.getPositionTable().rangeForIndex(insertionPoint, getLength());
            hightlightRange(r.start(), r.end());
        } else {
            hightlightRange(0, 0);
        }
        mouseEvent.consume();
    }

    public void mouseClick(MouseEvent e) {
        if (backend == null) {
            return;
        }
        CharacterHit hit = hit(e.getX(), e.getY());
        int insertionPoint = hit.getInsertionIndex();
        PosInSequent pis = backend.getInitialPositionTable().getPosInSequent(insertionPoint, filter);

/*
        Goal g = new Goal(node, null);
        ImmutableList<NoPosTacletApp> rules = g.ruleAppIndex().getFindTaclet(new TacletFilter() {
            @Override
            protected boolean filter(Taclet taclet) {
                return true;
            }
        }, pis.getPosInOccurrence(), services);


        RuleAppIndex index = g.ruleAppIndex();
*/

    }

    private void hightlightRange(int start, int end) {
        try {
            clearHighlight();
            setStyleClass(start, end, "sequent-highlight");
        } catch (IllegalStateException e) {
        }
    }

    private void clearHighlight() {
        clearStyle(0, getLength());
    }

    public void update(Observable o) {
        Services services = node.get().proof().getEnv().getServicesForEnvironment();
        NamespaceSet nss = services.getNamespaces();

        Sequent sequent = node.get().sequent();
        filter = new IdentitySequentPrintFilter(sequent);

        ProgramPrinter prgPrinter = new ProgramPrinter(new StringWriter());
        this.backend = new LogicPrinter.PosTableStringBackend(80);

        ProofIndependentSettings.DEFAULT_INSTANCE.getViewSettings().setUseUnicode(true);
        ProofIndependentSettings.DEFAULT_INSTANCE.getViewSettings().setUsePretty(true);
        NotationInfo.DEFAULT_UNICODE_ENABLED = true;
        NotationInfo.DEFAULT_PRETTY_SYNTAX = true;

        NotationInfo notation = new NotationInfo();
        notation.refresh(services, true, true);

        lp = new LogicPrinter(prgPrinter, notation, backend, services, false);
        lp.printSequent(sequent);

        clear();
        insertText(0, backend.getString());
        if (node.get().isClosed()) {
            this.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            this.getStyleClass().add("closed-sequent-view");
        }
    }

    public Node getNode() {
        return node.get();
    }

    public void setNode(Node node) {
        this.node.set(node);
    }

    public SimpleObjectProperty<Node> nodeProperty() {
        return node;
    }
}
