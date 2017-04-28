package edu.kit.formatl.proofscriptparser;

import edu.kit.formal.proofscriptparser.ScriptLanguageLexer;
import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formatl.proofscriptparser.ast.ASTNode;
import edu.kit.formatl.proofscriptparser.ast.ProofScript;
import org.antlr.v4.runtime.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (27.04.17)
 */
public final class Facade {
    public static ScriptLanguageParser.StartContext parseStream(CharStream stream) {
        ScriptLanguageParser slp = new ScriptLanguageParser(new CommonTokenStream(new ScriptLanguageLexer(stream)));
        return slp.start();
    }

    public static List<ProofScript> getAST(CharStream stream) {
        TransformAst astt = new TransformAst();
        parseStream(stream).accept(astt);
        return astt.getScripts();
    }

    public static List<ProofScript> getAST(File inputfile) throws IOException {
        return getAST(CharStreams.fromPath(inputfile.toPath()));
    }

    public static String prettyPrint(ASTNode node) {
        PrettyPrinter prettyPrinter = new PrettyPrinter();
        node.accept(prettyPrinter);
        return prettyPrinter.toString();
    }
}
