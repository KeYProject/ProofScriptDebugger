package edu.kit.formal.proofscriptparser;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.AssumptionViolatedException;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alexander Weigl
 * @version 1 (27.04.17)
 */
public class TestHelper {

    public static Iterable<Object[]> getResourcesAsParameters(String folder) {
        File[] files = getResources(folder);
        return Arrays.stream(files).map(f -> new Object[] { f }).collect(Collectors.toList());
    }

    public static final File[] getResources(String folder) {
        URL f = TestHelper.class.getClassLoader().getResource(folder);
        if (f == null) {
            System.err.format("Could not find %s%n", folder);
            return new File[0];
        }
        File file = new File(f.getFile());
        return file.listFiles();
    }

    public static Iterable<Object[]> loadLines(String filename) throws IOException {
        List<Object[]> validExpression = new ArrayList<>(4096);
        InputStream ve = TestHelper.class.getResourceAsStream(filename);

        if (ve == null) {
            System.err.println("Could not find: " + filename);
            return validExpression;
        }

        BufferedReader stream = new BufferedReader(new InputStreamReader(ve));
        String tmp = "";
        while ((tmp = stream.readLine()) != null) {
            if (tmp.startsWith("#"))
                continue;
            validExpression.add(new Object[] { tmp });
        }

        System.out.println("Found: " + filename + " with " + validExpression.size() + " lines!");

        return validExpression;
    }

    public static Collection<Object[]> asParameters(String[] cases) {
        return Arrays.stream(cases).map(s -> new Object[] { s }).collect(Collectors.toList());
    }

    public static ScriptLanguageParser getParser(String input) {
        return new ScriptLanguageParser(new CommonTokenStream(new ScriptLanguageLexer(CharStreams.fromString(input))));
    }

    public static ScriptLanguageParser getParser(File f) throws IOException {
        org.antlr.v4.runtime.CharStream stream = CharStreams.fromFileName(f.getAbsolutePath());
        ScriptLanguageLexer lexer = new ScriptLanguageLexer(stream);
        return new ScriptLanguageParser(new CommonTokenStream(lexer));
    }
}
