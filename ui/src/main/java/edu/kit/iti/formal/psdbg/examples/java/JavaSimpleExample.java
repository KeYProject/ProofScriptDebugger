package edu.kit.iti.formal.psdbg.examples.java;

import edu.kit.iti.formal.psdbg.examples.JavaExample;

public class JavaSimpleExample extends JavaExample {

    public JavaSimpleExample() {
        setName("Test");
        setJavaFile(this.getClass().getResource("Test.java"));


        defaultInit(getClass());

        System.out.println(this);
    }
}
