package edu.kit.iti.formal.psdbg.examples.lulu;

import edu.kit.iti.formal.psdbg.examples.JavaExample;

public class LuLuQuickSort extends JavaExample {
    public LuLuQuickSort() {
        setName("LuLu QuickSort");
        setHelpText(null);
        setJavaFile(getClass().getResource("Quicksort.java"));
        setScriptFile(getClass().getResource("QuickSort.kps"));
    }
}
