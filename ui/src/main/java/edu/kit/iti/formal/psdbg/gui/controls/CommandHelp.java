package edu.kit.iti.formal.psdbg.gui.controls;

import de.uka.ilkd.key.api.KeYApi;
import de.uka.ilkd.key.control.ProofControl;
import de.uka.ilkd.key.proof.Goal;
import de.uka.ilkd.key.rule.TacletApp;
import edu.kit.iti.formal.psdbg.gui.controller.DebuggerMain;
import edu.kit.iti.formal.psdbg.interpreter.funchdl.CommandLookup;
import edu.kit.iti.formal.psdbg.interpreter.funchdl.DefaultLookup;
import edu.kit.iti.formal.psdbg.interpreter.funchdl.MacroCommandHandler;
import edu.kit.iti.formal.psdbg.interpreter.funchdl.ProofScriptCommandBuilder;
import edu.kit.iti.formal.psdbg.parser.ast.CallStatement;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.controlsfx.control.textfield.TextFields;
import org.key_project.util.collection.ImmutableList;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CommandHelp extends JPanel {
    private WebView webView;
    private JComboBox<CommandEntry> comboBox;
    private CommandLookup commandLookup;
    private Goal goal;

    private List<CommandEntry> fixed = new ArrayList<>();

    public CommandHelp() {
        super(new BorderLayout());
        //create default lookup, should later be replaced by the interpreter lookup
        DefaultLookup dl = new DefaultLookup();
        dl.getBuilders().add(new MacroCommandHandler(KeYApi.getMacroApi().getMacros()));
        dl.getBuilders().add(new ProofScriptCommandBuilder(KeYApi.getScriptCommandApi().getScriptCommands()));
        setCommandLookup(dl);
        KeYApi.getMacroApi().getMacros().forEach(proofMacro -> {
            fixed.add(new CommandEntry(proofMacro.getScriptCommandName(), "macro"));
        });

        KeYApi.getScriptCommandApi().getScriptCommands().forEach(proofMacro -> {
            fixed.add(new CommandEntry(proofMacro.getName(), "command"));
        });

        /*comboBox.setConverter(new StringConverter<CommandEntry>() {
            @Override
            public String toString(CommandEntry object) {
                if(object==null) return "null";
                return object.toString();
            }

            @Override
            public CommandEntry fromString(String string) {
                return new CommandEntry(string.substring(0, string.indexOf(' ')), "");
            }
        });*/

        comboBox.setOnAction(event -> {
            this.openHelpFor(comboBox.getSelectionModel().getSelectedItem().name);
        });

        comboBox.getItems().setAll(fixed);

        comboBox.setPlaceholder(new Label("command name"));
        goal.addListener((p, o, n) -> {
            List<CommandEntry> l = new ArrayList<>(fixed);
            ProofControl c = DebuggerMain.FACADE.getEnvironment().getUi().getProofControl();
            ImmutableList<TacletApp> a = c.getNoFindTaclet(n);
            a.forEach(app ->
                    l.add(new CommandEntry(app.taclet().name().toString(), "taclet, no pos"))
            );

            /*
            final ImmutableList<BuiltInRule> builtInRules = c.getBuiltInRule(goal, occ);
            removeRewrites(c.getFindTaclet(goal, occ))
                            .prepend(c.getRewriteTaclet(goal, occ)),
                    c.getNoFindTaclet(goal), builtInRules);
            */
            // Is there a better for finding all taclets?????

            comboBox.getItems().setAll(l);
        });
    }

    /**
     * @param name
     */
    public void openHelpFor(String name) {
        CallStatement cs = new CallStatement();
        cs.setCommand(name);
        String html = getCommandLookup().getHelp(cs);
        webView.getEngine().loadContent(html);
    }

    public CommandLookup getCommandLookup() {
        return commandLookup.get();
    }

    public void setCommandLookup(CommandLookup commandLookup) {
        this.commandLookup.set(commandLookup);
    }

    public SimpleObjectProperty<CommandLookup> commandLookupProperty() {
        return commandLookup;
    }

    @Data
    @AllArgsConstructor
    class CommandEntry {
        String name, additionalInformation;


        @Override
        public String toString() {
            return beautifyName(name) + " (" + additionalInformation + ")";
        }

        private String beautifyName(String cname) {
            String ret;
            if (cname.contains("-")) {
                ret = cname.replace("-", "_");
                return ret;
            } else {
                return cname;
            }
        }
    }
}
