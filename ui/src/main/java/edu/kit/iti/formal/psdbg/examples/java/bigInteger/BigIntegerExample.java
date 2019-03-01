package edu.kit.iti.formal.psdbg.examples.java.bigInteger;

import edu.kit.iti.formal.psdbg.examples.JavaExample;

import java.net.URISyntaxException;
import java.nio.file.Paths;


public class BigIntegerExample extends JavaExample {

    public BigIntegerExample() throws URISyntaxException {
        setName("BigInteger Example");

        setDirectoryPath(Paths.get(getClass().getResource("BigInteger.java").toURI()).getParent());
        setJavaFile(getClass().getResource("BigInteger.java"));
        setScriptFile(getClass().getResource("script.kps"));

       // setSettingsFile(getClass().getResource("proof-settings.props"));
       // setHelpText(getClass().getResource("help.html"));

    }
}
