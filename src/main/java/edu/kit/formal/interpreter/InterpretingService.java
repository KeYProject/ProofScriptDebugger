package edu.kit.formal.interpreter;

import edu.kit.formal.gui.controller.PuppetMaster;
import edu.kit.formal.gui.controls.Utils;
import edu.kit.formal.interpreter.data.KeyData;
import edu.kit.formal.interpreter.data.State;
import edu.kit.formal.proofscriptparser.ast.ProofScript;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Service handling interpreting the script in an own thread
 *
 * @author A. Weigl
 */
public class InterpretingService extends Service<State<KeyData>> {

    /**
     * The interpreter (with teh appropriate KeY state) that is used to traverse and execute the script
     */
    private final SimpleObjectProperty<Interpreter<KeyData>> interpreter = new SimpleObjectProperty<>();

    /**
     * The main script that is traversed
     */
    private final SimpleObjectProperty<ProofScript> mainScript = new SimpleObjectProperty<>();

    /**
     * The blocker, necessary for controlling how far the script should be interpreted
     */
    private PuppetMaster blocker;

    public InterpretingService(PuppetMaster blocker) {
        this.blocker = blocker;
    }


    @Override
    protected void succeeded() {
        System.out.println("Finished executing");
        updateView();
    }

    @Override
    protected void cancelled() {
        updateView();
    }

    @Override
    protected void failed() {
        getException().printStackTrace();
        Utils.showExceptionDialog("Execution failed", "", "", getException());
        updateView();
    }

    private void updateView() {
        //check proof
        //check state for empty/error goal nodes
        //currentGoals.set(state.getGoals());
        //currentSelectedGoal.set(state.getSelectedGoalNode());

        blocker.publishState();
    }

    @Override
    protected Task<edu.kit.formal.interpreter.data.State<KeyData>> createTask() {
        return new Task<edu.kit.formal.interpreter.data.State<KeyData>>() {
            final Interpreter<KeyData> i = interpreter.get();
            final ProofScript ast = mainScript.get();

            /**
             * Start the interpreter with the AST of the parsed mainscript and return the last state from the interpreter
             * after interpreting
             * @return top of the state stack of the interpreter
             * @throws Exception
             */
            @Override
            protected edu.kit.formal.interpreter.data.State<KeyData> call() throws Exception {
                i.interpret(ast);
                return i.peekState();
            }
        };
    }

    /**************************************************************************************************************
     *
     *                                              Getter and Setter
     *
     **************************************************************************************************************/
    public Interpreter<KeyData> getInterpreter() {
        return interpreter.get();
    }

    public void setInterpreter(Interpreter<KeyData> interpreter) {
        this.interpreter.set(interpreter);
    }

    public SimpleObjectProperty<Interpreter<KeyData>> interpreterProperty() {
        return interpreter;
    }

    public ProofScript getMainScript() {
        return mainScript.get();
    }

    public void setMainScript(ProofScript mainScript) {
        this.mainScript.set(mainScript);
    }

    public SimpleObjectProperty<ProofScript> mainScriptProperty() {
        return mainScript;
    }


}
