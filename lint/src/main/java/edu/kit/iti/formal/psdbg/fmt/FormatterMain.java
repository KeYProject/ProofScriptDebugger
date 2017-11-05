package edu.kit.iti.formal.psdbg.fmt;

import alice.tuprolog.InvalidLibraryException;
import alice.tuprolog.InvalidTheoryException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class FormatterMain {
    public static final void main(String[] args) throws InvalidLibraryException, IOException, InvalidTheoryException {
        DefaultFormatter df = new DefaultFormatter();
        String code = FileUtils.readFileToString(new File(args[0]), Charset.defaultCharset());
        String newCode = df.format(code);
        System.out.flush();
        System.err.flush();
        System.out.println(newCode);
    }
}
