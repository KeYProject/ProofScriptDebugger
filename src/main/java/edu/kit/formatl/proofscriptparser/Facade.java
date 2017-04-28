package edu.kit.formatl.proofscriptparser;

import edu.kit.formal.proofscriptparser.ScriptLanguageLexer;
import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formatl.proofscriptparser.ast.ProofScript;
import org.antlr.v4.runtime.*;

/**
 * @author Alexander Weigl
 * @version 1 (27.04.17)
 */
public class Facade {
    public ScriptLanguageParser.StartContext parseStream(CharStream stream) {
        ScriptLanguageParser slp = new ScriptLanguageParser(new CommonTokenStream(new ScriptLanguageLexer(stream)));
        return slp.start();
    }

}
