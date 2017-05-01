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
 * This class captures high-level functions of this package.
 *
 * @author Alexander Weigl
 * @version 1 (27.04.17)
 */
public final class Facade {
    /**
     * Parses the given {@link CharStream} and returns the {@link ParserRuleContext}.
     *
     * @param stream containing the proof script
     * @return
     */
    public static ScriptLanguageParser.StartContext parseStream(CharStream stream) {
        ScriptLanguageParser slp = new ScriptLanguageParser(new CommonTokenStream(new ScriptLanguageLexer(stream)));
        return slp.start();
    }

    /**
     * Parses the given proof script from <code>stream</code> into an AST.
     *
     * @param stream
     * @return
     */
    public static List<ProofScript> getAST(CharStream stream) {
        TransformAst astt = new TransformAst();
        parseStream(stream).accept(astt);
        return astt.getScripts();
    }

    /**
     * Parses an AST from the given <code>inputfile</code>
     *
     * @param inputfile
     * @return
     * @throws IOException
     */
    public static List<ProofScript> getAST(File inputfile) throws IOException {
        return getAST(CharStreams.fromPath(inputfile.toPath()));
    }

    /**
     * Returns a prettified output of the given AST.
     *
     * @param node
     * @return
     */
    public static String prettyPrint(ASTNode node) {
        PrettyPrinter prettyPrinter = new PrettyPrinter();
        node.accept(prettyPrinter);
        return prettyPrinter.toString();
    }
}
