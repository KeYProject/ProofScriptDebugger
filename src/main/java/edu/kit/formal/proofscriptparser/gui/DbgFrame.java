package edu.kit.formal.proofscriptparser.gui;

import edu.kit.formal.dbg.Debugger;
import edu.kit.formal.interpreter.GoalNode;
import edu.kit.formal.interpreter.Interpreter;
import edu.kit.formal.interpreter.InterpreterBuilder;
import edu.kit.formal.interpreter.funchdl.BuiltinCommands;
import edu.kit.formal.proofscriptparser.Facade;
import edu.kit.formal.proofscriptparser.ast.ProofScript;
import edu.kit.formal.proofscriptparser.lint.LintProblem;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Alexander Weigl
 * @version 1 (25.05.17)
 */
public class DbgFrame implements Initializable {
    @FXML
    private ScriptArea scriptArea;

    @FXML
    private SequentViewer sequentView;

    @FXML
    private ComboBox<GoalNode> listGoals;

    public ObservableList<GoalNode> goals;

    private SimpleStringProperty code = new SimpleStringProperty();
    //private ObservableMap<Token, LintProblem> problems = FXCollections.emptyObservableList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(scriptArea);
        goals = listGoals.getItems();
        code.addListener((a, b, c) -> scriptArea.setText(c));
        //code.bind(scriptArea.textProperty());
        //scriptArea.setProblems(problems);
        sequentView.goalNode.bind(listGoals.valueProperty());

    }


    public String getCode() {
        return code.get();
    }

    public SimpleStringProperty codeProperty() {
        return code;
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public void execute(ActionEvent actionEvent) {
        List<ProofScript> scripts = Facade.getAST(CharStreams.fromString(code.get()));
        InterpreterBuilder ib = new InterpreterBuilder();
        ib.addCommand(new BuiltinCommands.SplitCommand())
                .addCommand(new BuiltinCommands.PrintCommand())
                .matcher(new Debugger.PseudoMatcher());

        Interpreter interpreter = ib.build();
        interpreter.interpret(scripts, "");
        goals.clear();
        goals.addAll(interpreter.getCurrentGoals());
    }
}
