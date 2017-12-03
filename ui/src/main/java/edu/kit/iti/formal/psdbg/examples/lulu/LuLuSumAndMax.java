package edu.kit.iti.formal.psdbg.examples.lulu;

import edu.kit.iti.formal.psdbg.examples.JavaExample;

public class LuLuSumAndMax extends JavaExample {
    public LuLuSumAndMax() {
        setName("LuLu SumAndMax");
        setHelpText(null);
        setJavaFile(getClass().getResource("SumAndMax.java"));
        setScriptFile(getClass().getResource("SumAndMax.kps"));
    }
}
