package edu.kit.iti.formal.psdbg.examples;

import de.uka.ilkd.key.proof.Proof;
import edu.kit.iti.formal.psdbg.gui.controller.DebuggerMain;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Alexander Weigl
 */
public class JavaExample extends Example {
    @Getter
    @Setter
    protected URL javaFile;
    @Getter
    @Setter
    protected URL settingsFile;
    @Getter
    @Setter
    private URL projectFile;

    @Getter
    @Setter
    private Path directoryPath;
    @Override
    public void open(DebuggerMain debuggerMain) {
        //TODO should be reworked if we have an example
        //     Loading via key file, or using an automatic contract selector?

        try {

            File script = newTempFile(scriptFile, getName() + ".kps");
            debuggerMain.openScript(script);
            debuggerMain.getWelcomePaneDock().close();
            //File key = newTempFile(keyFile, "project.key");
            File java = newTempFile(javaFile, getName() + ".java");
            //System.out.println(java.getAbsolutePath());
            //debuggerMain.openKeyFile(key);
            if(directoryPath != null){
                try (Stream<Path> paths = Files.walk(directoryPath)) {
                    List<Path> collect = paths.filter(Files::isRegularFile).collect(Collectors.toList());
                    List<String> files = new LinkedList<>();
                    collect.forEach(path -> {
                        File f = path.toFile();
                        if(f.getName().endsWith(".java") && !f.getName().equals(getName()+".java")){
                            try {
                                File temp = newTempFile(f, getName()+".java");

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }});
                    files.forEach(path -> System.out.println("path = " + path));
                }
            }
            debuggerMain.openJavaFile(java);
            if (settingsFile != null) {
                File settings = newTempFile(settingsFile, getName() + ".props");
                ProofListener pl = new ProofListener(debuggerMain, settings);
                debuggerMain.getFacade().proofProperty().addListener(pl);
            }
            debuggerMain.showActiveInspector(null);
            if (helpText != null) {
                String content = IOUtils.toString(helpText, Charset.defaultCharset());
                debuggerMain.openNewHelpDock(getName() + " Example", content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class ProofListener implements ChangeListener<Proof> {
        File settingsFile;
        DebuggerMain debuggerMain;

        public ProofListener(DebuggerMain debuggerMain, File settingsFile) {
            this.debuggerMain = debuggerMain;
            this.settingsFile = settingsFile;

        }

        @Override
        public void changed(ObservableValue<? extends Proof> observable, Proof oldValue, Proof newValue) {
            if (newValue != null) {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(settingsFile));
                    newValue.getSettings().loadSettingsFromStream(reader);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                debuggerMain.getFacade().proofProperty().removeListener(this);


            }
        }
    }
}

