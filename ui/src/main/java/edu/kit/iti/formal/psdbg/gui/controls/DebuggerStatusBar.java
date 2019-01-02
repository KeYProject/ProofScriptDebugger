package edu.kit.iti.formal.psdbg.gui.controls;

import edu.kit.iti.formal.psdbg.gui.controller.Events;
import edu.kit.iti.formal.psdbg.gui.model.InterpreterThreadState;
import edu.kit.iti.formal.psdbg.gui.model.MainScriptIdentifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

/**
 * Created on 09.06.2017
 *
 * @author Alexander Weigl
 */
public class DebuggerStatusBar extends JPanel {
    private static final Logger LOGGER = LogManager.getLogger(DebuggerStatusBar.class);
    private InterpreterThreadState interpreterStatusModel;
    private MainScriptIdentifier mainScriptIdentifier;
    private int numberOfGoals;
    private JLabel lblCurrentNodes = new JLabel("#open goals: " + numberOfGoals);
    private JLabel lblMainscript = new JLabel();
    private JLabel interpreterStatusView = new JLabel();

    //TODO swing: private ProgressIndicator progressIndicator = new ProgressIndicator();

    private EventHandler<MouseEvent> toolTipHandler = event ->
            publishMessage(((Control) event.getTarget()).getTooltip().getText());

    public DebuggerStatusBar() {
        //listenOnField("psdbg");

        getRightItems().addAll(
                lblMainscript,
                new Separator(),
                interpreterStatusView
                //lblCurrentNodes
                //progressIndicator
        );


        interpreterStatusModel.addListener((p, o, n) -> {
            interpreterStatusView.setIcon(n.getIcon());
            if (n == InterpreterThreadState.ERROR)
                interpreterStatusView.setFill(Color.web("orangered"));
            else
                interpreterStatusView.setFill(Color.web("cornflowerblue"));
        });

        numberOfGoals.addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > 0) {
                lblCurrentNodes.setText("#open Goals: " + newValue);
            } else {
                lblCurrentNodes.setText("No open goals");
            }
        });

        mainScriptIdentifier.addListener((ov, o, n) -> {
            if (n != null) {
                lblMainscript.setText("Main Script: " +
                        n.getScriptArea().getFilePath().getName() +
                        (n.getScriptName() != null
                                ? '\\' + n.getScriptName()
                                : " @ " + n.getLineNumber()));
                lblMainscript.setStyle("-fx-background-color: greenyellow;-fx-padding: 5pt;");
            } else {
                lblMainscript.setStyle("-fx-background-color: orangered;-fx-padding: 5pt;");
                lblMainscript.setText("No main script selected");
            }
        });

        lblMainscript.setStyle("-fx-background-color: orangered;-fx-padding: 5pt;");
        lblMainscript.setText("No main script selected");

        lblMainscript.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                event.consume();
                ScriptArea area = mainScriptIdentifier.get().getScriptArea();
                Events.fire(new Events.FocusScriptArea(area));
            }
        });
    }

    public int getNumberOfGoals() {
        return numberOfGoals.get();
    }

    public void setNumberOfGoals(int numberOfGoals) {
        this.numberOfGoals.set(numberOfGoals);
    }

    public IntegerProperty numberOfGoalsProperty() {
        return numberOfGoals;
    }

    public void publishMessage(String format, Object... args) {
        String msg = String.format(format, args);
        setText(msg);
    }

    public EventHandler<MouseEvent> getTooltipHandler() {
        return toolTipHandler;
    }

    public void indicateProgress() {
        progressIndicator.setProgress(-1);
    }

    public void stopProgress() {
        progressIndicator.setProgress(100);
    }

    public MainScriptIdentifier getMainScriptIdentifier() {
        return mainScriptIdentifier.get();
    }

    public void setMainScriptIdentifier(MainScriptIdentifier mainScriptIdentifier) {
        this.mainScriptIdentifier.set(mainScriptIdentifier);
    }

    public ObjectProperty<MainScriptIdentifier> mainScriptIdentifierProperty() {
        return mainScriptIdentifier;
    }

    public InterpreterThreadState getInterpreterStatusModel() {
        return interpreterStatusModel.get();
    }

    public void setInterpreterStatusModel(InterpreterThreadState interpreterStatusModel) {
        this.interpreterStatusModel.set(interpreterStatusModel);
    }

    public ObjectProperty<InterpreterThreadState> interpreterStatusModelProperty() {
        return interpreterStatusModel;
    }

    public void publishErrorMessage(String message) {
        publishMessage(message);
        flash((frac) -> new Color(1, 0, 0, 1 - frac));

    }

    public void publishSuccessMessage(String message) {
        publishMessage(message);
        flash((frac) -> new Color(0, 1, 0, 1 - frac));
    }

    private void flash(DoubleFunction<Color> color) {
        final Animation animation = new Transition() {
            {
                setCycleDuration(Duration.millis(1000));
                setInterpolator(Interpolator.EASE_OUT);
            }

            @Override
            protected void interpolate(double frac) {
                setBackground(new Background(new BackgroundFill(color.apply(frac), CornerRadii.EMPTY, Insets.EMPTY)));
            }
        };
        animation.play();
    }
}
