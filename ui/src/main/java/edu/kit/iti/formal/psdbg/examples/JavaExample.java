package edu.kit.iti.formal.psdbg.examples;

import de.uka.ilkd.key.proof.Proof;
import edu.kit.iti.formal.psdbg.gui.controller.DebuggerMain;
import org.apache.commons.io.IOUtils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Under construction!!!!!!
 *
 * @author Alexander Weigl
 */
public class JavaExample extends Example {


    protected URL javaFile;
    protected URL settingsFile;
    //public void setProjectFile(URL projectFile){this.projectFile = projectFile;}
    private URL projectFile;

    public void setJavaFile(URL javaFile) {
        this.javaFile = javaFile;
    }

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
            //TODO debuggerMain.getWelcomePaneDock().close();
            //File key = newTempFile(keyFile, "project.key");
            File java = newTempFile(javaFile, getName() + ".java");
            //System.out.println(java.getAbsolutePath());
            //debuggerMain.openKeyFile(key);
            debuggerMain.openJavaFile(java);
            if (settingsFile != null) {
                File settings = newTempFile(settingsFile, getName() + ".props");
                ProofListener pl = new ProofListener(debuggerMain, settings);
                //TODO debuggerMain.getFacade().proofProperty().addListener(pl);
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

    public class ProofListener implements PropertyChangeListener {
        File settingsFile;
        DebuggerMain debuggerMain;

        public ProofListener(DebuggerMain debuggerMain, File settingsFile) {
            this.debuggerMain = debuggerMain;
            this.settingsFile = settingsFile;

        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Proof newValue = (Proof) evt.getNewValue();
            if (newValue != null) {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(settingsFile));
                    newValue.getSettings().loadSettingsFromStream(reader);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                //TODO debuggerMain.getFacade().proofProperty().removeListener(this);
            }
        }
    }
}

