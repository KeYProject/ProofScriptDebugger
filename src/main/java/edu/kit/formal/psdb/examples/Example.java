package edu.kit.formal.psdb.examples;

import edu.kit.formal.psdb.gui.controller.DebuggerMain;
import edu.kit.formal.psdb.gui.model.MainScriptIdentifier;
import lombok.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public abstract class Example {
    protected String name;
    protected URL helpText;
    protected URL keyFile;
    protected URL scriptFile;
    protected MainScriptIdentifier mainScriptIdentifier;

    public static File newTempFile(URL url, String filename) throws IOException {
        File f = new File(FileUtils.getTempDirectoryPath(), filename);
        FileUtils.copyURLToFile(url, f);
        return f;
    }

    protected void defaultInit(Class aClass) {
        setHelpText(aClass.getResource("help.html"));
        setKeyFile(aClass.getResource("problem.key"));
        setMainScriptIdentifier(new MainScriptIdentifier("script.kps", -1, "main", null));
        setScriptFile(aClass.getResource("script.kps"));
    }

    public void open(DebuggerMain debuggerMain) {
        try {
            File script = newTempFile(scriptFile, getName() + ".kps");
            debuggerMain.openScript(script);
            File key = newTempFile(keyFile, "problem.key");
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
