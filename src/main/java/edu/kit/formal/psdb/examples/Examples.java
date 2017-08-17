package edu.kit.formal.psdb.examples;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * A facade for loading examples.
 * @author Alexander Weigl
 */
public class Examples {
    public static Collection<Example> loadExamples() {
        ServiceLoader<Example> sl = ServiceLoader.load(Example.class);
        return StreamSupport.stream(sl.spliterator(), false).collect(Collectors.toList());
    }
}
