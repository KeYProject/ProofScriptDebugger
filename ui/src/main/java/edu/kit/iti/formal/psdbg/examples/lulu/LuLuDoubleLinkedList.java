package edu.kit.iti.formal.psdbg.examples.lulu;

import edu.kit.iti.formal.psdbg.examples.JavaExample;

public class LuLuDoubleLinkedList extends JavaExample {
    public LuLuDoubleLinkedList() {
        setName("LuLu DoubleLinkedList");
        setHelpText(null);
        setJavaFile(getClass().getResource("DoubleLinkedList.java"));
        setScriptFile(getClass().getResource("DoubleLinkedList.kps"));
    }
}
