package edu.kit.formal.gui.controls;

import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.logic.NamespaceSet;
import de.uka.ilkd.key.logic.Sequent;
import de.uka.ilkd.key.pp.IdentitySequentPrintFilter;
import de.uka.ilkd.key.pp.LogicPrinter;
import de.uka.ilkd.key.pp.NotationInfo;
import de.uka.ilkd.key.pp.ProgramPrinter;
import de.uka.ilkd.key.proof.Node;
import de.uka.ilkd.key.settings.ProofIndependentSettings;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.MouseEvent;
import org.fxmisc.richtext.CodeArea;

import java.io.StringWriter;

/**
 * @author Alexander Weigl
 * @version 1 (03.06.17)
 */
public class SequentView extends CodeArea {

    private LogicPrinter lp;
    private IdentitySequentPrintFilter filter;
    private LogicPrinter.PosTableStringBackend backend;
    private SimpleObjectProperty<de.uka.ilkd.key.proof.Node> node = new SimpleObjectProperty<>();


    public SequentView() {
        setEditable(false);
        node.addListener(this::update);
        setOnMouseMoved(this::hightlight);
    }

    private void hightlight(MouseEvent mouseEvent) {
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();

        /*
        TextAreaSkin skin = (TextAreaSkin) getSkin();
        int insertionPoint = skin.getInsertionPoint(x, y);

        PosInSequent pis = backend.getInitialPositionTable().getPosInSequent(insertionPoint, filter);
        if (pis != null) {
            System.out.println(pis);
            Range r = backend.getPositionTable().rangeForIndex(insertionPoint, getLength());

            selectRange(r.start(), r.end());
        } else {
            selectRange(0, 0);
        }
        mouseEvent.consume();
        */
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
    }

    public Node getNode() {
        return node.get();
    }

    public SimpleObjectProperty<Node> nodeProperty() {
        return node;
    }

    public void setNode(Node node) {
        this.node.set(node);
    }
}
