package edu.kit.iti.formal.psdbg.gui.controls;

import javafx.application.Platform;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Alexander Weigl
 * @version 1 (24.05.18)
 */
public class FileReloadingService extends TimerTask {
    public static final Logger LOGGER = LogManager.getLogger(FileReloadingService.class);
    public static final Logger CONSOLE_LOGGER = LogManager.getLogger(ScriptArea.class);
    public Map<Path, FileChangedCallback> callbacks = new ConcurrentHashMap<Path, FileChangedCallback>();
    public Map<Path, WatchKey> watchKeys = new ConcurrentHashMap<>();
    public Map<WatchKey, Integer> watches = new ConcurrentHashMap<>();

    private Timer timer;

    @Nullable
    private WatchService wService;


    public FileReloadingService() {
        try {
            wService = FileSystems.getDefault().newWatchService();
            timer = new Timer("filereloading", true);
          //  timer.schedule(this, 500, 500);
        } catch (IOException e) {
            LOGGER.error(e);
            CONSOLE_LOGGER.error("Auto-reloading is not available. See log file for more details");
        }
    }

    public boolean addListener(File filePath, FileChangedCallback callback) {
        if (wService == null && filePath != null) return false;
        try {
            Path toWatch = filePath.toPath().toAbsolutePath();
            Path parent = toWatch.getParent();
            if (!Files.exists(parent)) return false;
            if (!watchKeys.containsKey(parent)) {
                WatchKey watchKey = parent.register(wService, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_CREATE);
                watchKeys.put(parent, watchKey);
            }
            //increment used resource counter
            watches.compute(watchKeys.get(parent), (k, v) -> (v == null) ? 1 : (v + 1));

            //register listener
            callbacks.put(toWatch, callback);
            return true;
        } catch (IOException e) {
            CONSOLE_LOGGER.catching(e);
            return false;
        }
    }

    public void removeListener(File filePath) {
        Path toWatch = filePath.toPath().toAbsolutePath();
        Path parent = toWatch.getParent();
        callbacks.remove(toWatch);
        WatchKey wk = watchKeys.get(parent);
        if (wk == null) return; // not registered before
        watches.compute(wk, (k, v) -> v == 1 ? null : v - 1);
        if (!watches.containsKey(wk)) {
            wk.cancel();
            watchKeys.remove(parent);
        }
    }

    public void run() {
        if (wService == null) return;
        CONSOLE_LOGGER.info("Waiting for file changed events");
        while (true) {
            // wait for key to be signaled
            WatchKey key;
            try {
                key = wService.take();
            } catch (InterruptedException x) {
                return;
            }
            Path dir = (Path) key.watchable();

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                if (kind == StandardWatchEventKinds.OVERFLOW) {
                    continue;
                }
                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                Path filename = ev.context();
                Path child = dir.resolve(filename).toAbsolutePath();

                try {
                    File file = child.toFile();
                    if(file != null) {
                        String content = FileUtils.readFileToString(file, Charset.defaultCharset());
                        CONSOLE_LOGGER.info("Auto-reload {}", child);
                        Platform.runLater(() -> {
                            if (callbacks.get(child) != null) {
                                callbacks.get(child).fileChanged(content);
                            }
                        });
                    }
                } catch (IOException e) {
                    CONSOLE_LOGGER.catching(e);
                } catch (NullPointerException npe){
                    CONSOLE_LOGGER.catching(npe);
                }
            }
            boolean valid = key.reset();
            //clean up dead keys
        }
    }

    public interface FileChangedCallback {
        void fileChanged(String newText);
    }
}
