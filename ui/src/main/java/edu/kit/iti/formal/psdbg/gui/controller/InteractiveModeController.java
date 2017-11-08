package edu.kit.iti.formal.psdbg.gui.controller;

import com.google.common.eventbus.Subscribe;
import de.uka.ilkd.key.logic.SequentFormula;
import de.uka.ilkd.key.proof.Node;
import edu.kit.iti.formal.psdbg.LabelFactory;
import edu.kit.iti.formal.psdbg.parser.PrettyPrinter;
import edu.kit.iti.formal.psdbg.parser.ast.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class InteractiveModeController {
    private static final Logger LOGGER = LogManager.getLogger(InteractiveModeController.class);
    private final Map<Node, Statements> cases = new HashMap<>();
    private BooleanProperty activated = new SimpleBooleanProperty();


    public InteractiveModeController() {
    }

    public void start() {
        Events.register(this);
        cases.clear();
        activated.set(true);
    }

    public void stop() {
        Events.unregister(this);
        String c = getCasesAsString();
        Events.fire(new Events.InsertAtTheEndOfMainScript(c));
        activated.set(false);
    }

    @Subscribe
    public void handle(Events.TacletApplicationEvent tap) {
        LOGGER.debug("Handling {}", tap);
        String tapName = tap.getApp().taclet().displayName();

        SequentFormula seqForm = tap.getPio().sequentFormula();
        //transform term to parsable string representation
        String term = edu.kit.iti.formal.psdbg.termmatcher.Utils.toPrettyTerm(seqForm.formula());
        Parameters params = new Parameters();
        params.put(new Variable("formula"), new TermLiteral(term));
        CallStatement call = new CallStatement(tapName, params);


        // Insert into the right cases
        Node currentNode = null;
        cases.get(findRoot(currentNode)).add(call);

        // How to Play this on the Proof?
        // How to Build a new StatePointer? Is it still possible?
        //  or only at #stop()?
    }

    private Node findRoot(Node cur) {
        while (cur != null) {
            if (cases.keySet().contains(cur))
                return cur;
            cur = cur.parent();
        }
        return null;
    }

    public String getCasesAsString() {
        CasesStatement c = new CasesStatement();
        cases.forEach((k, v) -> {
            val gcs = new GuardedCaseStatement();
            val m = new MatchExpression();
            m.setPattern(new StringLiteral(
                    LabelFactory.getBranchingLabel(k)
            ));
            gcs.setGuard(m);
            gcs.setBody(v);
        });

        PrettyPrinter pp = new PrettyPrinter();
        c.accept(pp);
        return pp.toString();
    }

    public boolean isActivated() {
        return activated.get();
    }

    public void setActivated(boolean activated) {
        this.activated.set(activated);
    }

    public BooleanProperty activatedProperty() {
        return activated;
    }
}
