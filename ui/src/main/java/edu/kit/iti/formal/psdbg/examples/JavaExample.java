package edu.kit.iti.formal.psdbg.examples;

import de.uka.ilkd.key.proof.Proof;
import edu.kit.iti.formal.psdbg.gui.controller.DebuggerMain;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Under construction!!!!!!
 * @author Alexander Weigl
 */
public class JavaExample extends Example {


    private URL projectFile;

    public void setJavaFile(URL javaFile) {
        this.javaFile = javaFile;
    }
    //public void setProjectFile(URL projectFile){this.projectFile = projectFile;}

    protected URL javaFile;

    protected URL settingsFile;

    public void setSettingsFile(URL settingsFile) {
        this.settingsFile = settingsFile;
    }

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

