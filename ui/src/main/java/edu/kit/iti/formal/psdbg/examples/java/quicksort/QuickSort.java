package edu.kit.iti.formal.psdbg.examples.java.quicksort;

import edu.kit.iti.formal.psdbg.examples.JavaExample;

public class QuickSort extends JavaExample {

    public QuickSort() {


        setName("Quicksort example");

        setHelpText(null);

        setJavaFile(getClass().

                getResource("Quicksort.java"));

        setScriptFile(getClass().

                getResource("script.kps"));
    }

}
