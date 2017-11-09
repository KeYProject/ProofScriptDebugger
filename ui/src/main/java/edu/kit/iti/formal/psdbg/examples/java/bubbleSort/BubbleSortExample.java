package edu.kit.iti.formal.psdbg.examples.java.bubbleSort;

import edu.kit.iti.formal.psdbg.examples.JavaExample;

public class BubbleSortExample extends JavaExample {
    public BubbleSortExample() {
        setName("Bubble Sort");
        setJavaFile(this.getClass().getResource("Bubblesort.java"));
        defaultInit(getClass());
        System.out.println(this);

    }
}
