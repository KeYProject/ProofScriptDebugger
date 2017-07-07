package edu.kit.formal.gui.controls;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.uka.ilkd.key.logic.op.IProgramMethod;
import de.uka.ilkd.key.pp.ProgramPrinter;
import edu.kit.formal.gui.model.InspectionModel;
import edu.kit.formal.gui.model.RootModel;
import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.KeyData;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Right part of the splitpane that displays the different parts of a state
 *
 * @author S. Grebing
 */
public class InspectionViewTab extends BorderPane {
    public GoalOptionsMenu goalOptionsMenu = new GoalOptionsMenu();
    @FXML
    private SectionPane sectionPaneJavaCode;
    @FXML
    private SplitPane lowerSplitPane;
    @FXML
    private SequentView sequentView;
    @FXML
    private JavaArea javaSourceCode;
    @FXML
    private ListView goalView;

    private ObjectProperty<Mode> mode = new SimpleObjectProperty<>();

    private BooleanProperty showCode = new SimpleBooleanProperty(true);

    public InspectionViewTab() {
        super();
        Utils.createWithFXML(this);

        getGoalView().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.getData() != null) {
                getSequentView().setNode(newValue.getData().getNode());
            }
        });
        getGoalView().setCellFactory(GoalNodeListCell::new);

        mode.addListener(o -> {
            getStyleClass().removeAll(
                    Mode.DEAD.name(),
                    Mode.LIVING.name(),
                    Mode.POSTMORTEM.name()
            );
            getStyleClass().add(mode.get().name());

            if (mode.get() == Mode.LIVING) {
                MaterialDesignIconView icon = new MaterialDesignIconView(MaterialDesignIcon.RUN);
                setClosable(false);
                setGraphic(icon);
            } else {
                setGraphic(null);
                setClosable(true);
            }
        });


        showCode.addListener(o -> {
            if (showCode.get())
                lowerSplitPane.getItems().add(sectionPaneJavaCode);
            else
                lowerSplitPane.getItems().remove(sectionPaneJavaCode);
        });
        showCode.set(false);
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

    public Mode getMode() {
        return mode.get();
    }

    public void setMode(Mode mode) {
        this.mode.set(mode);
    }

    public ObjectProperty<Mode> modeProperty() {
        return mode;
    }

    public boolean isShowCode() {
        return showCode.get();
    }

    public void setShowCode(boolean showCode) {
        this.showCode.set(showCode);
    }

    public BooleanProperty showCodeProperty() {
        return showCode;
    }

    enum Mode {
        LIVING, DEAD, POSTMORTEM,
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
