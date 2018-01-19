package edu.kit.iti.formal.psdbg.gui.controls;

import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.logic.NamespaceSet;
import de.uka.ilkd.key.logic.PosInOccurrence;
import de.uka.ilkd.key.logic.Sequent;
import de.uka.ilkd.key.pp.*;
import de.uka.ilkd.key.proof.Goal;
import de.uka.ilkd.key.proof.Node;
import de.uka.ilkd.key.settings.ProofIndependentSettings;
import edu.kit.iti.formal.psdbg.interpreter.KeYProofFacade;
import edu.kit.iti.formal.psdbg.termmatcher.MatcherFacade;
import edu.kit.iti.formal.psdbg.termmatcher.Matchings;
import edu.kit.iti.formal.psdbg.termmatcher.mp.MatchPath;
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
import org.key_project.util.collection.ImmutableSLList;

import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;

public class SequentViewForMatcher extends CodeArea {
    private Services services;

    private LogicPrinter lp;

    private IdentitySequentPrintFilter filter;

    private LogicPrinter.PosTableStringBackend backend;

    private SimpleObjectProperty<de.uka.ilkd.key.proof.Goal> goal = new SimpleObjectProperty<>();

    private SimpleObjectProperty<de.uka.ilkd.key.proof.Node> node = new SimpleObjectProperty<>();


    private KeYProofFacade keYProofFacade;



    public SequentViewForMatcher() {
        getStyleClass().add("sequent-view");
        setEditable(false);
        node.addListener(this::update);
    }





    public void clearHighlight() {
        clearStyle(0, getLength());
    }




    public void update(Observable o) {
        Services services = node.get().proof().getEnv().getServicesForEnvironment();
        NamespaceSet nss = services.getNamespaces();

        Sequent sequent = node.get().sequent();
        filter = new IdentitySequentPrintFilter();
        filter.setSequent(sequent);


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









    public LogicPrinter.PosTableStringBackend getBackend() {
        return backend;
    }
}
