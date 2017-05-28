package edu.kit.formal.proofscriptparser.gui;


import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.logic.Sequent;
import de.uka.ilkd.key.pp.LogicPrinter;
import de.uka.ilkd.key.pp.NotationInfo;
import de.uka.ilkd.key.pp.ProgramPrinter;
import edu.kit.formal.interpreter.GoalNode;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.BorderPane;
import org.fxmisc.richtext.CodeArea;


public class SequentViewer extends BorderPane {
    private CodeArea delegated = new CodeArea();
    private SimpleObjectProperty<Sequent> sequent = new SimpleObjectProperty<>();
    private SimpleObjectProperty<Services> services = new SimpleObjectProperty<>();
    public SimpleObjectProperty<GoalNode> goalNode = new SimpleObjectProperty<>();

    public SequentViewer() {
        delegated.setEditable(false);
        setCenter(delegated);
        //sequent.addListener((a, b, c) -> updateView());
        //services.addListener((a, b, c) -> updateView());

        goalNode.addListener(new ChangeListener<GoalNode>() {
            @Override
            public void changed(ObservableValue<? extends GoalNode> observable, GoalNode oldValue, GoalNode newValue) {
                delegated.clear();
                delegated.insertText(0, newValue.toString());
            }
        });
    }

    public void updateView() {
        if (services != null) {
            LogicPrinter printer = new LogicPrinter(
                    new ProgramPrinter(),
                    new NotationInfo(),
                    services.get());
            printer.printSequent(sequent.get());
            delegated.clear();
            delegated.insertText(0, printer.toString());
        }
    }

    public Sequent getSequent() {
        return sequent.get();
    }

    public SimpleObjectProperty<Sequent> sequentProperty() {
        return sequent;
    }

    public void setSequent(Sequent sequent) {
        this.sequent.set(sequent);
    }

    public Services getServices() {
        return services.get();
    }

    public SimpleObjectProperty<Services> servicesProperty() {
        return services;
    }

    public void setServices(Services services) {
        this.services.set(services);
    }
}