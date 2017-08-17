package edu.kit.formal.psdb.gui.controls;

import edu.kit.formal.psdb.gui.controller.Events;
import edu.kit.formal.psdb.gui.model.MainScriptIdentifier;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.MouseEvent;
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

    private ObjectProperty<MainScriptIdentifier> mainScriptIdentifier = new SimpleObjectProperty<>();
    private Label lblCurrentNodes = new Label("#nodes: %s");
    private Label lblMainscript = new Label();
    private ProgressIndicator progressIndicator = new ProgressIndicator();
    private EventHandler<MouseEvent> toolTipHandler = event -> {
        publishMessage(((Control) event.getTarget()).getTooltip().getText());
    };

    public DebuggerStatusBar() {
        //listenOnField("psdbg");

        getRightItems().addAll(
                lblMainscript,
                lblCurrentNodes,
                progressIndicator
        );

        /*setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(this, event.getScreenX(), event.getScreenY());
            }
        });*/


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

    /*
    private Appender createAppender() {
        PatternLayout layout = PatternLayout.createDefaultLayout();
        return new AbstractAppender("", null, layout) {
            @Override
            public void append(LogEvent event) {
                publishMessage(event.getMessage().getFormattedMessage());
            }
        };
    }*/

    public void publishMessage(String format, Object... args) {
        String msg = String.format(format, args);
        setText(msg);
    }

    public EventHandler<MouseEvent> getTooltipHandler() {
        return toolTipHandler;
    }

    /*
    public void listenOnField(ObservableStringValue value) {
        value.addListener((observable, oldValue, newValue) -> {
            publishMessage(newValue);
        });
    }*/

    /*
    public void listenOnField(Logger logger) {
        org.apache.logging.log4j.core.Logger plogger = ((org.apache.logging.log4j.core.Logger) logger); // Bypassing the public API
        plogger.addAppender(loggerHandler);
        plogger.addAppender(logCatchHandler);
        logger.info("Listener added");
    }*/

    /*public void listenOnField(String loggerCategory) {
        listenOnField(LogManager.getLogger(loggerCategory));
    }*/

    public void indicateProgress() {
        progressIndicator.setProgress(-1);
    }

    public void stopProgress() {
        progressIndicator.setProgress(100);
    }

    /*public ContextMenu createContextMenu() {
        ContextMenu cm = new ContextMenu();
        Menu viewOptions = new Menu("View Options");
        MenuItem showLog = new MenuItem("Show Log", new MaterialDesignIconView(MaterialDesignIcon.FORMAT_LIST_BULLETED));
        showLog.setOnAction(this::showLog);
        cm.getItems().addAll(viewOptions, showLog);
        return cm;
    }*/

    public MainScriptIdentifier getMainScriptIdentifier() {
        return mainScriptIdentifier.get();
    }

    public void setMainScriptIdentifier(MainScriptIdentifier mainScriptIdentifier) {
        this.mainScriptIdentifier.set(mainScriptIdentifier);
    }

    public ObjectProperty<MainScriptIdentifier> mainScriptIdentifierProperty() {
        return mainScriptIdentifier;
    }
}

    /*
    private void showLog(ActionEvent actionEvent) {
        loggerDialog.show();
    }*/

    /*public Dialog<Void> createDialog() {
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
}
        );


                Dialog<Void> dialog=new Dialog<>();
        dialog.setResizable(true);
        dialog.initModality(Modality.NONE);
        dialog.setHeaderText("Logger Records");
        DialogPane dpane=dialog.getDialogPane();
        dpane.setContent(new BorderPane(recordView));
        dpane.getButtonTypes().setAll(ButtonType.CLOSE);
        return dialog;
        }*/

/*
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

        public void setRecords(ObservableList<LogEvent> records) {
            this.records.set(records);
        }

        public ListProperty<LogEvent> recordsProperty() {
            return records;
        }

        public int getMaxRecords() {
            return maxRecords;
        }

        public void setMaxRecords(int maxRecords) {
            this.maxRecords = maxRecords;
        }
    }
    }*/


