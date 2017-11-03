package edu.kit.iti.formal.psdbg.gui.controls;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import edu.kit.iti.formal.psdbg.gui.controller.Events;
import edu.kit.iti.formal.psdbg.gui.model.InterpreterThreadState;
import edu.kit.iti.formal.psdbg.gui.model.MainScriptIdentifier;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.StatusBar;

/**
 * Created on 09.06.2017
 *
 * @author Alexander Weigl
 */
public class DebuggerStatusBar extends StatusBar {
    private static final Logger LOGGER = LogManager.getLogger(DebuggerStatusBar.class);

    //private final Dialog<Void> loggerDialog = createDialog();
    //private final ContextMenu contextMenu = createContextMenu();
    //private final Appender loggerHandler = createAppender();
    //private LogCatchHandlerFX logCatchHandler = new LogCatchHandlerFX();
    private ObjectProperty<InterpreterThreadState> interpreterStatusModel = new SimpleObjectProperty<>();

    private ObjectProperty<MainScriptIdentifier> mainScriptIdentifier = new SimpleObjectProperty<>();

    private IntegerProperty numberOfGoals = new SimpleIntegerProperty(0);

    private Label lblCurrentNodes = new Label("#nodes: %s");

    private Label lblMainscript = new Label();

    private MaterialDesignIconView interpreterStatusView =
            new MaterialDesignIconView(MaterialDesignIcon.MATERIAL_UI, "2.3em");


    private ProgressIndicator progressIndicator = new ProgressIndicator();

    private EventHandler<MouseEvent> toolTipHandler = event ->
            publishMessage(((Control) event.getTarget()).getTooltip().getText());

    public DebuggerStatusBar() {
        //listenOnField("psdbg");

        getRightItems().addAll(
                lblMainscript,
                new Separator(),
                interpreterStatusView
                //lblCurrentNodes,
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
            lblCurrentNodes.setText("#nodes: " + newValue);
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
        final Animation animation = new Transition() {
            {
                setCycleDuration(Duration.millis(1000));
                setInterpolator(Interpolator.EASE_OUT);
            }

            @Override
            protected void interpolate(double frac) {
                Color vColor = new Color(1, 0, 0, 1 - frac);
                setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        };
        animation.play();
    }
}
