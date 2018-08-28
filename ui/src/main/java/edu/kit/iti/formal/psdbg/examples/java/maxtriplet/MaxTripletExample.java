package edu.kit.iti.formal.psdbg.examples.java.maxtriplet;

import edu.kit.iti.formal.psdbg.examples.JavaExample;

public class MaxTripletExample extends JavaExample {

    public MaxTripletExample() {
        setName("MaxTriplet");
        setJavaFile(this.getClass().getResource("MaxTriplet.java"));
        defaultInit(getClass());
    }
}