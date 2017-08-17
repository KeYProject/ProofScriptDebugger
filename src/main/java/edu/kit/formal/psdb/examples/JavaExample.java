package edu.kit.formal.psdb.examples;

import edu.kit.formal.psdb.gui.controller.DebuggerMain;
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
    protected URL javaFile;

    @Override
    public void open(DebuggerMain debuggerMain) {
        //TODO should be reworked if we have an example
        //     Loading via key file, or using an automatic contract selector?

        try {
            File script = newTempFile(scriptFile, getName() + ".kps");
            debuggerMain.openScript(script);
            File key = newTempFile(keyFile, "problem.key");
            File java = newTempFile(javaFile, getName() + ".java");
            debuggerMain.openKeyFile(key);
            if (helpText != null) {
                String content = IOUtils.toString(helpText, Charset.defaultCharset());
                debuggerMain.openNewHelpDock(getName() + " Example", content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
