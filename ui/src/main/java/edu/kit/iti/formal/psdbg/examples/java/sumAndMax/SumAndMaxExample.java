package edu.kit.iti.formal.psdbg.examples.java.sumAndMax;

import edu.kit.iti.formal.psdbg.examples.JavaExample;

public class SumAndMaxExample extends JavaExample {

    public SumAndMaxExample() {
        setName("SumAndMax Example");
        setJavaFile(this.getClass().getResource("SumAndMax.java"));
        defaultInit(getClass());
    }
}
