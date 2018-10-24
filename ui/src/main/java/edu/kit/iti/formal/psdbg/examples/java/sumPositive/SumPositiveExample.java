package edu.kit.iti.formal.psdbg.examples.java.sumPositive;

import edu.kit.iti.formal.psdbg.examples.JavaExample;

public class SumPositiveExample extends JavaExample {

    public SumPositiveExample () {

    setName("SumPositive example");

    setJavaFile(getClass().

    getResource("SumPositive.java"));

    setScriptFile(getClass().

    getResource("script.kps"));
    }
}

