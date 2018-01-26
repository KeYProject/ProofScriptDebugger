package edu.kit.iti.formal.psdbg.examples;

import edu.kit.iti.formal.psdbg.gui.controller.DebuggerMain;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
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

            debuggerMain.showActiveInspector(null);
            if (helpText != null) {
                String content = IOUtils.toString(helpText, Charset.defaultCharset());
                debuggerMain.openNewHelpDock(getName() + " Example", content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
