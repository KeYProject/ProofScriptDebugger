package edu.kit.formal.gui.controls;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.controlsfx.control.StatusBar;

import java.util.Date;
import java.util.LinkedList;

/**
 * Created by weigl on 09.06.2017.
 */
public class DebuggerStatusBar extends StatusBar {
    private static final Logger LOGGER = LogManager.getLogger(DebuggerStatusBar.class);

    private Label lblCurrentNodes = new Label("#nodes: %s");
    private ProgressIndicator progressIndicator = new ProgressIndicator();
    private LogCatchHandlerFX logCatchHandler = new LogCatchHandlerFX();
    private EventHandler<MouseEvent> toolTipHandler = event -> {
        publishMessage(((Control) event.getTarget()).getTooltip().getText());
    };


    private final ContextMenu contextMenu = createContextMenu();
    private final Dialog<Void> loggerDialog = createDialog();

    public DebuggerStatusBar() {
        listenOnField("psdbg");

        getRightItems().addAll(
                lblCurrentNodes,
                progressIndicator
        );

        setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(this, event.getScreenX(), event.getScreenY());
            }
        });


    }

    private final Appender loggerHandler = createAppender();

    private Appender createAppender() {
        PatternLayout layout = PatternLayout.createDefaultLayout();
        return new AbstractAppender("", null, layout) {
            @Override
            public void append(LogEvent event) {
                publishMessage(event.getMessage().getFormattedMessage());
            }
        };
    }

    public void publishMessage(String format, Object... args) {
        String msg = String.format(format, args);
        setText(msg);
    }

    public EventHandler<MouseEvent> getTooltipHandler() {
        return toolTipHandler;
    }

    public void listenOnField(ObservableStringValue value) {
        value.addListener((observable, oldValue, newValue) -> {
            publishMessage(newValue);
        });
    }

    public void listenOnField(Logger logger) {
        org.apache.logging.log4j.core.Logger plogger = ((org.apache.logging.log4j.core.Logger) logger); // Bypassing the public API
        plogger.addAppender(loggerHandler);
        plogger.addAppender(logCatchHandler);
        logger.info("Listener added");
    }

    public void listenOnField(String loggerCategory) {
        listenOnField(LogManager.getLogger(loggerCategory));
    }

    public void indicateProgress() {
        progressIndicator.setProgress(-1);
    }

    public void stopProgress() {
        progressIndicator.setProgress(100);
    }

    public ContextMenu createContextMenu() {
        ContextMenu cm = new ContextMenu();
        Menu viewOptions = new Menu("View Options");
        MenuItem showLog = new MenuItem("Show Log", new MaterialDesignIconView(MaterialDesignIcon.FORMAT_LIST_BULLETED));
        showLog.setOnAction(this::showLog);
        cm.getItems().addAll(viewOptions, showLog);
        return cm;
    }

    private void showLog(ActionEvent actionEvent) {
        loggerDialog.show();
    }

    public Dialog<Void> createDialog() {
        final TableView<LogEvent> recordView = new TableView<>(logCatchHandler.recordsProperty());
        recordView.setEditable(false);
        recordView.setSortPolicy(param -> false);

        TableColumn<LogEvent, Date> dateColumn = new TableColumn<>("Date");
        TableColumn<LogEvent, Level> levelColumn = new TableColumn<>("Level");
        //TableColumn<LogRecord, String> classColumn = new TableColumn<>("Class");
        TableColumn<LogEvent, String> messageColumn = new TableColumn<>("Message");

        recordView.getColumns().setAll(dateColumn, levelColumn, messageColumn);


        dateColumn.setCellValueFactory(
                param -> new SimpleObjectProperty<>(new Date(param.getValue().getTimeMillis())
                ));

        levelColumn.setCellValueFactory(
                param -> new SimpleObjectProperty<>(param.getValue().getLevel())
        );

        messageColumn.setCellValueFactory(
                param -> {
                    return new SimpleStringProperty(param.getValue().getMessage().getFormattedMessage());
                    /*String s = formatter.formatMessage(param.getValue());
                    if (param.getValue().getThrown() != null) {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        pw.println();
                        param.getValue().getThrown().printStackTrace(pw);
                        pw.close();
                        s += "\n" + sw.toString();
                    }
                    return new SimpleStringProperty(s);*/
                }
        );


        Dialog<Void> dialog = new Dialog<>();
        dialog.setResizable(true);
        dialog.initModality(Modality.NONE);
        dialog.setHeaderText("Logger Records");
        DialogPane dpane = dialog.getDialogPane();
        dpane.setContent(new BorderPane(recordView));
        dpane.getButtonTypes().setAll(ButtonType.CLOSE);
        return dialog;
    }

    public static class LogCatchHandlerFX extends AbstractAppender {
        private ListProperty<LogEvent> records = new SimpleListProperty<>(FXCollections.observableList(
                new LinkedList<>() //remove of head
        ));

        private int maxRecords = 5000;

        protected LogCatchHandlerFX() {
            super("LogCatchHandlerFX", null, PatternLayout.createDefaultLayout());
        }

        @Override
        public void append(LogEvent event) {
            records.add(event);
            while (records.size() > maxRecords)
                records.remove(0);
        }


        public ObservableList<LogEvent> getRecords() {
            return records.get();
        }

        public ListProperty<LogEvent> recordsProperty() {
            return records;
        }

        public void setRecords(ObservableList<LogEvent> records) {
            this.records.set(records);
        }

        public int getMaxRecords() {
            return maxRecords;
        }

        public void setMaxRecords(int maxRecords) {
            this.maxRecords = maxRecords;
        }
    }
}


