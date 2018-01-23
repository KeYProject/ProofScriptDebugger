package edu.kit.iti.formal.psdbg.examples.java.simple;

import edu.kit.iti.formal.psdbg.examples.JavaExample;

public class JavaSimpleExample extends JavaExample {

    public JavaSimpleExample() {
        setName("Simple Java Example");
        setJavaFile(this.getClass().getResource("Simple.java"));
        defaultInit(getClass());
    }
}
