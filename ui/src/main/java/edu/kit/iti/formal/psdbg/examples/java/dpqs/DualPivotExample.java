package edu.kit.iti.formal.psdbg.examples.java.dpqs;

import edu.kit.iti.formal.psdbg.examples.JavaExample;

public class DualPivotExample extends JavaExample {

    public DualPivotExample() {

        setName("Dual Pivot Quicksort");
        setJavaFile(this.getClass().getResource("DualPivotQuicksort.java"));


        defaultInit(getClass());

        System.out.println(this);
    }
}
