package edu.kit.formal.gui.controls;

import de.uka.ilkd.key.logic.op.IProgramMethod;
import de.uka.ilkd.key.pp.ProgramPrinter;
import edu.kit.formal.gui.model.InspectionModel;
import edu.kit.formal.gui.model.RootModel;
import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.KeyData;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Right part of the splitpane that displays the different parts of a state
 */
public class InspectionViewTab extends Tab implements Initializable {

    public GoalOptionsMenu goalOptionsMenu = new GoalOptionsMenu();
    @FXML
    private SequentView sequentView;
    @FXML
    private JavaArea javaSourceCode;
    @FXML
    private ListView goalView;

    public InspectionViewTab() {
        super();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InspectionView.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public SequentView getSequentView() {
        return sequentView;
    }

    public JavaArea getJavaSourceCode() {
        return javaSourceCode;
    }

    public ListView<GoalNode<KeyData>> getGoalView() {
        return goalView;
    }

    public GoalOptionsMenu getGoalOptionsMenu() {
        return goalOptionsMenu;
    }

    public void showGoalOptions(MouseEvent actionEvent) {
        Node n = (Node) actionEvent.getTarget();
        goalOptionsMenu.show(n, actionEvent.getScreenX(), actionEvent.getScreenY());
    }

    public void initialize(InspectionModel model) {

        System.out.println("model");
    }

    public void refresh(RootModel model) {
        IProgramMethod method = (IProgramMethod) model.getChosenContract().getTarget();
        getJavaSourceCode().clear();
        getJavaSourceCode().getLineToClass().clear();
        //javaSourceCode.clear();
        //javaSourceCode.getLineToClass().clear();
        StringWriter writer = new StringWriter();
        ProgramPrinter pp = new ProgramPrinter(writer);
        try {
            pp.printFullMethodSignature(method);
            pp.printStatementBlock(method.getBody());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getJavaSourceCode().insertText(0, writer.toString());
        // javaSourceCode.insertText(0, writer.toString());

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getGoalView().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            getSequentView().setNode(newValue.getData().getNode());
        });
        getGoalView().setCellFactory(GoalNodeListCell::new);


    }

    /**
     * Cells for GoalView
     */
    private class GoalNodeListCell extends ListCell<GoalNode<KeyData>> {

        public GoalNodeListCell(ListView<GoalNode<KeyData>> goalNodeListView) {
            itemProperty().addListener(this::update);
            goalOptionsMenu.selectedViewOptionProperty().addListener(this::update);
        }

        private void update(Observable observable) {
            if (getItem() == null) {
                setText("");
                return;
            }
            KeyData item = getItem().getData();
            String text = "n/a";
            if (goalOptionsMenu.getSelectedViewOption() != null) {
                text = goalOptionsMenu.getSelectedViewOption().getText(item);
            }
            setText(text);
        }
    }

}
