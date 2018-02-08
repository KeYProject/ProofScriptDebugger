package edu.kit.iti.formal.psdbg.examples.java.quicksort;

import edu.kit.iti.formal.psdbg.examples.JavaExample;

public class QuickSort extends JavaExample {

    public QuickSort() {


        setName("Quicksort example");
        setSettingsFile(getClass().getResource("proof-settings.props"));
        setHelpText(getClass().getResource("help.html"));

        setJavaFile(getClass().
                getResource("Quicksort.java"));

        setScriptFile(getClass().getResource("script.kps"));
    }

}
