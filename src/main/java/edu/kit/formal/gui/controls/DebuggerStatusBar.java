package edu.kit.formal.gui.controls;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.util.Callback;
import org.controlsfx.control.StatusBar;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.*;

/**
 * Created by weigl on 09.06.2017.
 */
public class DebuggerStatusBar extends StatusBar {
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

    private final Handler loggerHandler = new Handler() {
        @Override
        public void publish(LogRecord record) {
            publishMessage(record.getMessage());
        }

        @Override
        public void flush() {

        }

        @Override
        public void close() throws SecurityException {

        }
    };

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
        logger.addHandler(loggerHandler);
        logger.addHandler(logCatchHandler);

        logger.info("Listener added");
    }

    public void listenOnField(String loggerCategory) {
        listenOnField(Logger.getLogger(loggerCategory));
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
        final TableView<LogRecord> recordView = new TableView<>(logCatchHandler.recordsProperty());
        recordView.setEditable(false);
        recordView.setSortPolicy(param -> false);

        TableColumn<LogRecord, Date> dateColumn = new TableColumn<>("Date");
        TableColumn<LogRecord, Level> levelColumn = new TableColumn<>("Level");
        //TableColumn<LogRecord, String> classColumn = new TableColumn<>("Class");
        TableColumn<LogRecord, String> messageColumn = new TableColumn<>("Message");

        recordView.getColumns().setAll(dateColumn, levelColumn, messageColumn);


        dateColumn.setCellValueFactory(
                param -> new SimpleObjectProperty<>(new Date(param.getValue().getMillis())
                ));

        levelColumn.setCellValueFactory(
                param -> new SimpleObjectProperty<>(param.getValue().getLevel())
        );

        messageColumn.setCellValueFactory(
                param -> {
                    String s = formatter.formatMessage(param.getValue());
                    if (param.getValue().getThrown() != null) {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        pw.println();
                        param.getValue().getThrown().printStackTrace(pw);
                        pw.close();
                        s += "\n" + sw.toString();
                    }
                    return new SimpleStringProperty(s);
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

    public static class LogCatchHandlerFX extends Handler {
        private ListProperty<LogRecord> records = new SimpleListProperty<>(FXCollections.observableList(
                new LinkedList<>() //remove of head
        ));

        private int maxRecords = 5000;

        @Override
        public void publish(LogRecord record) {
            records.add(record);
            while (records.size() > maxRecords)
                records.remove(0);
        }

        @Override
        public void flush() {

        }

        @Override
        public void close() throws SecurityException {

        }

        public ObservableList<LogRecord> getRecords() {
            return records.get();
        }

        public ListProperty<LogRecord> recordsProperty() {
            return records;
        }

        public void setRecords(ObservableList<LogRecord> records) {
            this.records.set(records);
        }

        public int getMaxRecords() {
            return maxRecords;
        }

        public void setMaxRecords(int maxRecords) {
            this.maxRecords = maxRecords;
        }
    }

    static SimpleFormatter formatter = new SimpleFormatter();

    private static class LogRecordCellFactory extends ListCell<LogRecord> {
        public LogRecordCellFactory(ListView<LogRecord> view) {
        }

        @Override
        protected void updateItem(LogRecord item, boolean empty) {
            if (empty)
                super.updateItem(item, empty);
            else {
                Label lbl = new Label(formatter.format(item));
                lbl.setWrapText(true);
                lbl.getStyleClass().add(item.getLevel().getName());
                setGraphic(lbl);
            }
        }
    }
}


