package edu.kit.iti.formal.psdbg.examples.java.transitive;

import edu.kit.iti.formal.psdbg.examples.JavaExample;

public class PaperExample extends JavaExample {
    public PaperExample() {
        setName("Transitive Permutation");
        setJavaFile(this.getClass().getResource("Simple.java"));


        defaultInit(getClass());

        System.out.println(this);
    }

}
