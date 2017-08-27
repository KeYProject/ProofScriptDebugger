package edu.kit.formal.psdb.gui.controls;

import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.logic.NamespaceSet;
import de.uka.ilkd.key.logic.Sequent;
import de.uka.ilkd.key.pp.*;
import de.uka.ilkd.key.proof.Goal;
import de.uka.ilkd.key.proof.Node;
import de.uka.ilkd.key.settings.ProofIndependentSettings;
import edu.kit.formal.psdb.interpreter.KeYProofFacade;
import edu.kit.formal.psdb.termmatcher.MatchPath;
import edu.kit.formal.psdb.termmatcher.MatcherFacade;
import edu.kit.formal.psdb.termmatcher.Matchings;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.fxmisc.richtext.CharacterHit;
import org.fxmisc.richtext.CodeArea;
import org.key_project.util.collection.ImmutableList;

import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;

/**
 * @author Alexander Weigl
 * @version 1 (03.06.17)
 */
public class SequentView extends CodeArea {
    private Services services;

    private LogicPrinter lp;
    private IdentitySequentPrintFilter filter;
    private LogicPrinter.PosTableStringBackend backend;
    private SimpleObjectProperty<de.uka.ilkd.key.proof.Goal> goal = new SimpleObjectProperty<>();
    private SimpleObjectProperty<de.uka.ilkd.key.proof.Node> node = new SimpleObjectProperty<>();
    private ObservableBooleanValue rulesAvailable = goal.isNotNull();
    private KeYProofFacade keYProofFacade;
    private TacletContextMenu menu;


    public SequentView() {
        getStyleClass().add("sequent-view");
        setEditable(false);
        node.addListener(this::update);
        setOnMouseMoved(this::hightlight);
        setOnMouseClicked(this::onMouseClick);
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

    public void onMouseClick(MouseEvent e) {
        if (menu != null && menu.isShowing()) {
            menu.hide();
        }

        if (backend == null) {
            return;
        }
        if (e.getButton() == MouseButton.SECONDARY && rulesAvailable.get()) {
            CharacterHit hit = hit(e.getX(), e.getY());
            int insertionPoint = hit.getInsertionIndex();
            PosInSequent pis = backend.getInitialPositionTable().getPosInSequent(insertionPoint, filter);
            if (pis == null) return;

            menu = new TacletContextMenu(keYProofFacade, pis, goal.get());
            menu.setAutoFix(true);
            menu.setAutoHide(true);
            menu.show(this, e.getScreenX(), e.getScreenY());
            e.consume();
        }
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
            System.out.println("Closed " + node.get().sequent());
            this.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            this.getStyleClass().add("closed-sequent-view");
        } else {
            this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            this.getStyleClass().remove("closed-sequent-view");
            this.getStyleClass().add("sequent-view");
        }
    }

    public Goal getGoal() {
        return goal.get();
    }

    public void setGoal(Goal goal) {
        this.goal.set(goal);
    }

    public SimpleObjectProperty<Goal> goalProperty() {
        return goal;
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

    public KeYProofFacade getKeYProofFacade() {
        return keYProofFacade;
    }

    public void setKeYProofFacade(KeYProofFacade keYProofFacade) {
        this.keYProofFacade = keYProofFacade;
    }


    public void removeSearchHighlights() {
        clearHighlight();
    }

    public boolean showSearchHighlights(String pattern) {
        clearHighlight();

        if (pattern.trim().isEmpty()) {
            return false;
        }

        if (node.get().sequent() != null) {
            Matchings m = MatcherFacade.matches(pattern, node.get().sequent(), true);
            if (m.size() == 0) return false;
            for (Map<String, MatchPath> va : m) {
                MatchPath<?> complete = va.get("?COMPLETE");
                highlightTerm(complete.getPos());
            }
        }
        return true;
    }

    private void highlightTerm(ImmutableList<Integer> complete) {
        if (backend == null) {
            return;
        }
        Range r = backend.getInitialPositionTable().rangeForPath(complete);
        setStyle(r.start(), r.end(), Collections.singleton("search-highlight"));
    }
}
